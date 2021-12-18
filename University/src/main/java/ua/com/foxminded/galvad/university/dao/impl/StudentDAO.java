package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Repository
public class StudentDAO implements DAO<Integer, Student> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void create(Student student) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a student to DB. FirstName=\"{}\", LastName =\"{}\"", student.getFirstName(),
				student.getLastName());
		try {
			entityManager.persist(student);
		} catch (Exception e) {
			LOGGER.info("Student with firstName={}, lastName={} wasn't added to DB.", student.getFirstName(),
					student.getLastName());
			throw new DataAreNotUpdatedException(
					String.format("Student with firstName=%s, lastName=%s wasn't added to DB.", student.getFirstName(),
							student.getLastName()));
		}
		LOGGER.info("Student with firstName={}, lastName={} successfully added to DB.", student.getFirstName(),
				student.getLastName());
	}

	public Student retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a student from DB. ID={}", id);
		Student student = null;
		try {
			student = entityManager.find(Student.class, id);
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a student from DB. ID={}", id);
			throw new DataNotFoundException(String.format("Can't retrieve a student from DB. ID=%d", id));
		}
		if (student == null) {
			LOGGER.info("A student with ID={} is not found", id);
			throw new DataNotFoundException(String.format("A student with ID=%d is not found", id));
		}
		LOGGER.trace("The student with id={} retrieved from DB successfully", id);
		return student;
	}

	public Student retrieve(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a student entity (First_Name={}, Last_Name={})", firstName, lastName);
		Student student = null;
		try {
			student = (Student) entityManager.createQuery("from Student where firstName=:firstName and lastName=:lastName")
					.setParameter("firstName", firstName).setParameter("lastName", lastName).getSingleResult();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a student from DB. First_Name={}, Last_Name={}", firstName, lastName);
			throw new DataNotFoundException(
					String.format("Can't retrieve a student from DB. First_Name=%s, Last_Name=%s", firstName, lastName));
		}
		if (student == null) {
			LOGGER.info("A student with First_Name={}, Last_Name={} is not found", firstName, lastName);
			throw new DataNotFoundException(String.format("A student with First_Name=%s, Last_Name=%s is not found", firstName, lastName));
		}
		LOGGER.trace("The student with First_Name={}, Last_Name={} retrieved from DB successfully", firstName, lastName);
		return student;
	}

	public Integer getId(Student student) throws DataNotFoundException {
		return retrieve(student.getFirstName(), student.getLastName()).getId();
	}

	public void update(Student student) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a student (ID={})", student.getId());
		try {
			entityManager.merge(student);
		} catch (Exception e) {
			LOGGER.info("Can't update a student. ID={}", student.getId());
			throw new DataAreNotUpdatedException(String.format("Can't update a student. ID=%d", student.getId()));
		}
		LOGGER.trace("The student (ID={}) updated successfully", student.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a student entity, ID={}", id);
		Integer isDeleted;
		try {
			isDeleted = entityManager.createQuery("DELETE FROM Student student WHERE student.id=:id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			LOGGER.info("Can't delete a student. ID={}", id);
			throw new DataAreNotUpdatedException(String.format("Can't delete a student. ID=%d", id));
		}
		if (isDeleted != 0) {
			LOGGER.trace("The student entity deleted, ID={}", id);
		} else {
			throw new DataAreNotUpdatedException(String.format("A student with ID=%d is not found!", id));
		}
	}

	public void delete(Student student) throws DataAreNotUpdatedException {
		delete(student.getId());
	}

	@SuppressWarnings("unchecked")
	public List<Student> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of Students from DB");
		List<Student> resultList = new ArrayList<>();
		try {
			resultList = entityManager.createQuery("from Student").getResultList();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a list of students.");
			throw new DataNotFoundException("Can't retrieve a list of students.");
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of Students");
		} else {
			LOGGER.info("Sorting the list by Name");
			Collections.sort(resultList, Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName));
			LOGGER.info("Retrieved a list of Students successfully. {} Students were found", resultList.size());
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
		entityManager.flush();
	}
}
