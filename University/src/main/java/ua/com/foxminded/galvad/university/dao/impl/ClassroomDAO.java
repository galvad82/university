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
import ua.com.foxminded.galvad.university.model.Classroom;

@Repository
public class ClassroomDAO implements DAO<Integer, Classroom> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomDAO.class);
	private static final String GENERAL_ERROR = "Cannot process data in the DB!";

	@Autowired
	private SessionFactory sessionFactory;

	private Session currentSession;
	private Transaction currentTransaction;

	public void create(Classroom classroom) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a classroom to DB. Name={}", classroom.getName());
		openCurrentSessionWithTransaction();
		try {
			currentSession.persist(classroom);
		} catch (Exception e) {
			LOGGER.info("A classroom wasn't added to DB because of GENERAL ERROR. Name=\"{}\"", classroom.getName());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		} finally {
			closeCurrentSessionwithTransaction();
		}
		LOGGER.info("Classroom with name={} successfully added to DB.", classroom.getName());
	}

	public Classroom retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a classroom from DB. ID={}", id);
		openCurrentSession();
		Classroom classroom = null;
		String hqlQuery = "from Classroom where id = :id";
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createQuery(hqlQuery);
		query.setParameter("id", id);
		try {
			classroom = (Classroom) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.info("Classroom with id={} is not found", id);
			throw new DataNotFoundException(String.format("Classroom with id=%d is not found", id));
		} catch (Exception e) {
			LOGGER.info("A classroom wasn't retrieved from DB because of GENERAL ERROR. ID={}", id);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
		LOGGER.trace("The classroom with id={} retrieved from DB successfully", id);
		return classroom;
	}

	public Classroom retrieve(String classroomName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a classroom from DB. Name={}", classroomName);
		openCurrentSession();
		Classroom classroom = null;
		String hqlQuery = "from Classroom where name=:name";
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createQuery(hqlQuery);
		query.setParameter("name", classroomName);
		try {
			classroom = (Classroom) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.info("Classroom with name={} is not found", classroomName);
			throw new DataNotFoundException(String.format("Classroom with name=%s is not found", classroomName));
		} catch (Exception e) {
			LOGGER.info("A classroom wasn't retrieved from DB because of GENERAL ERROR. Name={}", classroomName);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
		LOGGER.trace("The classroom with name={} retrieved from DB successfully", classroomName);
		return classroom;
	}

	public Integer getId(Classroom classroom) throws DataNotFoundException {
		return retrieve(classroom.getName()).getId();
	}

	public void update(Classroom classroom) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a classroom (ID={})", classroom.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.merge(classroom);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A classroom wasn't updated because of GENERAL ERROR. ID={}", classroom.getId());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.trace("The teacher (ID={}) updated successfully", classroom.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a classroom by ID={}", id);
		LOGGER.trace("Going to retrieve an entity for a classroom (ID={})", id);
		openCurrentSession();
		Classroom classroom = currentSession.get(Classroom.class, id);
		LOGGER.trace("The entity retrieved for a classroom (ID={})", id);
		closeCurrentSession();
		if (classroom != null) {
			delete(classroom);
		} else {
			throw new DataAreNotUpdatedException(String.format("A classroom with ID=%d is not found", id));
		}
	}

	public void delete(Classroom classroom) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a classroom entity, ID={}", classroom.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.remove(classroom);
			closeCurrentSessionwithTransaction();
			LOGGER.trace("The classroom entity deleted, ID={}", classroom.getId());
		} catch (Exception e) {
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
	}

	public List<Classroom> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of Classrooms from DB");
		List<Classroom> resultList = new ArrayList<>();
		openCurrentSession();
		try {
			resultList = currentSession.createQuery("from Classroom", Classroom.class).list();
		} catch (Exception e) {
			LOGGER.info("A list of Classrooms wasn't retrieved because of GENERAL ERROR");
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}

		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of Classrooms");
		} else {
			LOGGER.info("Sorting the list by ID");
			Collections.sort(resultList, Comparator.comparing(Classroom::getId));
			LOGGER.info("Retrieved a list of Classrooms successfully. {} Classrooms were found", resultList.size());
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
