package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.model.Teacher;

@Repository
public class TeacherDAO implements DAO<Integer, Teacher> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void create(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a teacher to DB. FirstName=\"{}\", LastName =\"{}\"", teacher.getFirstName(),
				teacher.getLastName());
		try {
			entityManager.persist(teacher);
		} catch (Exception e) {
			LOGGER.info("Teacher with firstName={}, lastName={} wasn't added to DB.", teacher.getFirstName(),
					teacher.getLastName());
			throw new DataAreNotUpdatedException(
					String.format("Teacher with firstName=%s, lastName=%s wasn't added to DB.", teacher.getFirstName(),
							teacher.getLastName()));
		}
		LOGGER.info("Teacher with firstName={}, lastName={} successfully added to DB.", teacher.getFirstName(),
				teacher.getLastName());
	}

	public Teacher retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a teacher from DB. ID={}", id);
		Teacher teacher = null;
		try {
			teacher = entityManager.find(Teacher.class, id);
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a teacher from DB. ID={}", id);
			throw new DataAreNotUpdatedException(String.format("Can't retrieve a teacher from DB. ID=%d", id));
		}
		LOGGER.trace("The teacher with id={} retrieved from DB successfully", id);
		return teacher;
	}

	public Teacher retrieve(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a teacher entity (First_Name={}, Last_Name={})", firstName, lastName);
		Teacher teacher = null;
		try {
			teacher = (Teacher) entityManager
					.createQuery("from Teacher where firstName=:firstName and lastName=:lastName")
					.setParameter("firstName", firstName).setParameter("lastName", lastName).getSingleResult();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a teacher from DB. First_Name={}, Last_Name={}", firstName, lastName);
			throw new DataAreNotUpdatedException(String
					.format("Can't retrieve a teacher from DB. First_Name=%s, Last_Name=%s", firstName, lastName));
		}
		LOGGER.trace("The teacher with First_Name={}, Last_Name={} retrieved from DB successfully", firstName,
				lastName);
		return teacher;
	}

	public Integer getId(Teacher teacher) throws DataNotFoundException {
		return retrieve(teacher.getFirstName(), teacher.getLastName()).getId();
	}

	public void update(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a teacher (ID={})", teacher.getId());
		try {
			entityManager.merge(teacher);
		} catch (Exception e) {
			LOGGER.info("Can't update a teacher. ID={}", teacher.getId());
			throw new DataAreNotUpdatedException(String.format("Can't update a teacher. ID=%d", teacher.getId()));
		}
		LOGGER.trace("The teacher (ID={}) updated successfully", teacher.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a teacher entity, ID={}", id);
		Integer isDeleted;
		try {
			isDeleted = entityManager.createQuery("delete from Teacher teacher where teacher.id=:id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			if (e.getCause() instanceof ConstraintViolationException) {
				LOGGER.info("Can't delete a teacher as the entity is connected to a Course. ID={}", id);
				throw new DataAreNotUpdatedException(
						String.format("Can't delete a teacher (ID=%d) because of Course-Teacher connection", id));
			} else {
				LOGGER.info("Can't delete a teacher. ID={}", id);
				throw new DataAreNotUpdatedException(String.format("Can't delete a teacher. ID=%d", id));
			}
		}
		if (isDeleted != 0) {
			LOGGER.trace("The teacher entity deleted, ID={}", id);
		} else {
			throw new DataAreNotUpdatedException(String.format("A teacher with ID=%d is not found!", id));
		}
	}

	public void delete(Teacher teacher) throws DataAreNotUpdatedException {
		delete(teacher.getId());
	}

	@SuppressWarnings("unchecked")
	public List<Teacher> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of Teachers from DB");
		List<Teacher> resultList = new ArrayList<>();
		try {
			resultList = entityManager.createQuery("from Teacher").getResultList();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a list of teachers.");
			throw new DataAreNotUpdatedException("Can't retrieve a list of teachers.");
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of Teachers");
		} else {
			LOGGER.info("Sorting the list by Name");
			Collections.sort(resultList,
					Comparator.comparing(Teacher::getLastName).thenComparing(Teacher::getFirstName));
			LOGGER.info("Retrieved a list of Teachers successfully. {} Teachers were found", resultList.size());
		}
		return resultList;
	}
}
