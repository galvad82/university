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
import ua.com.foxminded.galvad.university.model.Teacher;

@Repository
public class TeacherDAO implements DAO<Integer, Teacher> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAO.class);
	private static final String GENERAL_ERROR = "Cannot process data in the DB!";

	@Autowired
	private SessionFactory sessionFactory;

	private Session currentSession;
	private Transaction currentTransaction;

	public void create(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a teacher to DB. FirstName=\"{}\", LastName =\"{}\"", teacher.getFirstName(),
				teacher.getLastName());
		openCurrentSessionWithTransaction();
		try {
			currentSession.persist(teacher);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A teacher wasn't added to DB because of GENERAL ERROR. FirstName=\"{}\", LastName =\"{}\"",
					teacher.getFirstName(), teacher.getLastName());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.info("Added a teacher to DB. FirstName=\"{}\", LastName =\"{}\"", teacher.getFirstName(),
				teacher.getLastName());
	}

	public Teacher retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a teacher from DB. ID={}", id);
		openCurrentSession();
		Teacher teacher = null;
		try {
			teacher = currentSession.get(Teacher.class, id);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A teacher with id={}) is not found", id);
			throw new DataNotFoundException(String.format("A teacher with ID=%d is not found", id));
		} catch (Exception e) {
			LOGGER.info("A teacher wasn't retrieved from DB because of GENERAL ERROR. ID={}", id);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
		LOGGER.info("Retrieved a teacher with ID={} from DB", id);
		return teacher;
	}

	public Teacher retrieve(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a teacher entity (First_Name ={}, Last_Name ={})", firstName, lastName);
		openCurrentSession();
		String hqlQuery = "from Teacher where firstName=:firstName AND lastName=:lastName";
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createQuery(hqlQuery);
		query.setParameter("firstName", firstName);
		query.setParameter("lastName", lastName);
		try {
			Teacher teacher = (Teacher) query.list().get(0);
			LOGGER.trace("A teacher entity (First_Name ={}, Last_Name ={}) was retrieved successfully", firstName,
					lastName);
			return teacher;
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A teacher with FirstName={}, LastName ={}) is not found", firstName, lastName);
			throw new DataNotFoundException(String
					.format("A teacher with FirstName=\"%s\" and LastName=\"%s\" is not found", firstName, lastName));
		} catch (Exception e) {
			LOGGER.info("A teacher wasn't retrieved from DB because of GENERAL ERROR. First_Name ={}, Last_Name ={}",
					firstName, lastName);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
	}

	public Integer getId(Teacher teacher) throws DataNotFoundException {
		return retrieve(teacher.getFirstName(), teacher.getLastName()).getId();
	}

	public void update(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a teacher (ID={})", teacher.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.merge(teacher);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A teacher wasn't updated because of GENERAL ERROR. ID={}", teacher.getId());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.trace("The teacher (ID={}) updated successfully", teacher.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a teacher by ID={}", id);
		LOGGER.trace("Going to retrieve an entity for a teacher (ID={})", id);
		openCurrentSession();
		Teacher teacher = currentSession.get(Teacher.class, id);
		LOGGER.trace("The entity retrieved for a teacher (ID={})", id);
		closeCurrentSession();
		if (teacher != null) {
			delete(teacher);
		} else {
			throw new DataAreNotUpdatedException(String.format("A teacher with ID=%d is not found", id));
		}
	}

	public void delete(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a teacher entity, ID={}", teacher.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.remove(teacher);
			closeCurrentSessionwithTransaction();
			LOGGER.trace("The teacher entity deleted, ID={}", teacher.getId());
		} catch (Exception e) {
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
	}

	public List<Teacher> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of teachers from DB");
		List<Teacher> resultList = new ArrayList<>();
		openCurrentSession();
		try {
			resultList = currentSession.createQuery("from Teacher", Teacher.class).list();
		} catch (Exception e) {
			LOGGER.info("A list of teachers wasn't retrieved because of GENERAL ERROR");
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}

		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of teachers");
			throw new DataNotFoundException("None of teachers was found in DB");
		} else {
			LOGGER.info("Sorting the list by Last Name and then by First Name");
			Collections.sort(resultList,
					Comparator.comparing(Teacher::getLastName).thenComparing(Teacher::getFirstName));
			LOGGER.info("Retrieved a list of teachers successfully. {} teachers were found", resultList.size());
		}
		return resultList;
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
