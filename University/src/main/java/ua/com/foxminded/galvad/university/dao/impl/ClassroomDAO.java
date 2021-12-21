package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.model.Classroom;

@Repository
public class ClassroomDAO implements DAO<Integer, Classroom> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void create(Classroom classroom) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a classroom to DB. Name={}", classroom.getName());
		try {
			entityManager.persist(classroom);
		} catch (Exception e) {
			LOGGER.info("Classroom with name={} wasn't added to DB.", classroom.getName());
			throw new DataAreNotUpdatedException(
					String.format("Classroom with name=%s wasn't added to DB.", classroom.getName()));
		}
		LOGGER.info("Classroom with name={} successfully added to DB.", classroom.getName());
	}

	public Classroom retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a classroom from DB. ID={}", id);
		Classroom classroom = null;
		try {
			classroom = entityManager.find(Classroom.class, id);
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a classroom from DB. ID={}", id);
			throw new DataNotFoundException(String.format("Can't retrieve a classroom from DB. ID=%d", id));
		}
		if (classroom == null) {
			LOGGER.info("A classroom with ID={} is not found", id);
			throw new DataNotFoundException(String.format("A classroom with ID=%d is not found", id));
		} else {
			LOGGER.trace("The classroom with id={} retrieved from DB successfully", id);
			return classroom;
		}
	}

	public Classroom retrieve(String classroomName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a classroom from DB. Name={}", classroomName);
		Classroom classroom = null;
		try {
			classroom = (Classroom) entityManager.createQuery("from Classroom where name=:name")
					.setParameter("name", classroomName).getSingleResult();
		} catch (NoResultException ex) {
			LOGGER.info("A classroom with Name={} is not found", classroomName);
			throw new DataNotFoundException(String.format("A classroom with Name=%s is not found", classroomName));
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a classroom from DB. Name={}", classroomName);
			throw new DataNotFoundException(
					String.format("Can't retrieve a classroom from DB. Name=%s", classroomName));
		}
		LOGGER.trace("The classroom with name={} retrieved from DB successfully", classroomName);
		return classroom;
	}

	public Integer getId(Classroom classroom) throws DataNotFoundException {
		return retrieve(classroom.getName()).getId();
	}

	public void update(Classroom classroom) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a classroom (ID={})", classroom.getId());
		try {
			entityManager.merge(classroom);
		} catch (Exception e) {
			LOGGER.info("Can't update a classroom. ID={}", classroom.getId());
			throw new DataAreNotUpdatedException(String.format("Can't update a classroom. ID=%d", classroom.getId()));
		}
		LOGGER.trace("The classroom (ID={}) updated successfully", classroom.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a classroom entity, ID={}", id);
		Integer isDeleted;
		try {
			isDeleted = entityManager.createQuery("delete from Classroom classroom where classroom.id=:id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			LOGGER.info("Can't delete a classroom. ID={}", id);
			throw new DataAreNotUpdatedException(String.format("Can't delete a classroom. ID=%d", id));
		}
		if (isDeleted != 0) {
			LOGGER.trace("The classroom entity deleted, ID={}", id);
		} else {
			throw new DataAreNotUpdatedException(String.format("A classroom with ID=%d is not found!", id));
		}
	}

	public void delete(Classroom classroom) throws DataAreNotUpdatedException {
		delete(classroom.getId());
	}

	@SuppressWarnings("unchecked")
	public List<Classroom> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of Classrooms from DB");
		List<Classroom> resultList = new ArrayList<>();
		try {
			resultList = entityManager.createQuery("from Classroom").getResultList();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a list of classrooms.");
			throw new DataNotFoundException("Can't retrieve a list of classrooms.");
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of Classrooms");
		} else {
			LOGGER.info("Sorting the list by Name");
			Collections.sort(resultList, Comparator.comparing(Classroom::getName));
			LOGGER.info("Retrieved a list of Classrooms successfully. {} Classrooms were found", resultList.size());
		}
		return resultList;
	}
}
