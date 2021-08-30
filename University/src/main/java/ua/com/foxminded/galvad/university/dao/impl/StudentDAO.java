package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Repository
public class StudentDAO implements DAO<Integer, Student> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAO.class);
	private static final String GENERAL_ERROR = "Cannot process data in the DB!";

	@Autowired
	private SessionFactory sessionFactory;

	private Session currentSession;
	private Transaction currentTransaction;

	public void create(Student student) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a student to DB. FirstName=\"{}\", LastName =\"{}\"", student.getFirstName(),
				student.getLastName());
		openCurrentSessionWithTransaction();
		try {
			currentSession.persist(student);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A student wasn't added to DB because of GENERAL ERROR. FirstName=\"{}\", LastName =\"{}\"",
					student.getFirstName(), student.getLastName());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.info("Added a student to DB. FirstName=\"{}\", LastName =\"{}\"", student.getFirstName(),
				student.getLastName());
	}

	public Student retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a student from DB. ID={}", id);
		openCurrentSession();
		try {
			return currentSession.get(Student.class, id);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A student with id={}) is not found", id);
			throw new DataNotFoundException(String.format("A student with ID=%d is not found", id));
		} catch (Exception e) {
			LOGGER.info("A student wasn't retrieved from DB because of GENERAL ERROR. ID={}", id);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			LOGGER.info("Retrieved a student with ID={} from DB", id);
			closeCurrentSession();
		}
	}

	public Student retrieve(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a student entity (First_Name ={}, Last_Name ={})", firstName, lastName);
		openCurrentSession();
		String hqlQuery = "from Student where firstName=:firstName AND lastName=:lastName";
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createQuery(hqlQuery);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		try {
			Student student = (Student) query.list().get(0);
			LOGGER.trace("A student entity (First_Name ={}, Last_Name ={}) was retrieved successfully", firstName,
					lastName);
			return student;
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A student with FirstName={}, LastName ={}) is not found", firstName, lastName);
			throw new DataNotFoundException(String
					.format("A student with FirstName=\"%s\" and LastName=\"%s\" is not found", firstName, lastName));
		} catch (Exception e) {
			LOGGER.info("A student wasn't retrieved from DB because of GENERAL ERROR. First_Name ={}, Last_Name ={}",
					firstName, lastName);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
	}

	public Integer getId(Student student) throws DataNotFoundException {
		return retrieve(student.getFirstName(), student.getLastName()).getId();
	}

	public void update(Student student) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a student (ID={})", student.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.merge(student);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A student wasn't updated because of GENERAL ERROR. ID={}", student.getId());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.trace("The student (ID={}) updated successfully", student.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a student by ID={}", id);
		LOGGER.trace("Going to retrieve an entity for a student (ID={})", id);
		openCurrentSession();
		Student student = currentSession.get(Student.class, id);
		LOGGER.trace("The entity retrieved for a student (ID={})", id);
		closeCurrentSession();
		if (student != null) {
			delete(student);
		} else {
			throw new DataAreNotUpdatedException(String.format("A student with ID=%d is not found", id));
		}
	}

	public void delete(Student student) throws DataAreNotUpdatedException {
		removeStudentFromGroups(student);
		LOGGER.trace("Going to delete a student entity, ID={}", student.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.remove(student);
			closeCurrentSessionwithTransaction();
			LOGGER.trace("The student entity deleted, ID={}", student.getId());
		} catch (Exception e) {
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
	}

	public List<Student> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of students from DB");
		List<Student> resultList = new ArrayList<>();
		openCurrentSession();
		try {
			resultList = currentSession.createQuery("from Student", Student.class).list();
		} catch (Exception e) {
			LOGGER.info("A list of students wasn't retrieved because of GENERAL ERROR");
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}

		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of students");
		} else {
			LOGGER.info("Sorting the list by Last Name and then by First Name");
			Collections.sort(resultList,
					Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName));
			LOGGER.info("Retrieved a list of students successfully. {} students were found", resultList.size());
		}
		return resultList;
	}

	public void addStudentToGroup(Student student, Group group) throws DataAreNotUpdatedException {
		student.setGroup(group);
		update(student);
	}

	public void removeStudentFromGroups(Student student) throws DataAreNotUpdatedException {
		student.setGroup(null);
		update(student);
	}

	private void openCurrentSession() {
		currentSession = sessionFactory.openSession();
	}

	private void closeCurrentSession() {
		currentSession.close();
		currentSession = null;
	}

	private void openCurrentSessionWithTransaction() {
		if (currentSession == null) {
			currentSession = sessionFactory.openSession();
			currentTransaction = currentSession.getTransaction();
			currentTransaction.begin();
		}
	}

	private void closeCurrentSessionwithTransaction() {
		currentTransaction.commit();
		currentSession.close();
		currentTransaction = null;
		currentSession = null;
	}

}
