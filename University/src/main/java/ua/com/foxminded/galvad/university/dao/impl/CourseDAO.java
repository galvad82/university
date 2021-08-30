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
import ua.com.foxminded.galvad.university.model.Course;

@Repository
public class CourseDAO implements DAO<Integer, Course> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CourseDAO.class);
	private static final String GENERAL_ERROR = "Cannot process data in the DB!";

	@Autowired
	private SessionFactory sessionFactory;

	private Session currentSession;
	private Transaction currentTransaction;

	public void create(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a course (name={}) to DB", course.getName());
		openCurrentSessionWithTransaction();
		try {
			currentSession.persist(course);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A course wasn't added to DB because of GENERAL ERROR. Name=\"{}\"", course.getName());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.info("Added a course to DB. Name=\"{}\"", course.getName());
	}

	public Course retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a course from DB. ID={}", id);
		openCurrentSession();
		try {
			return currentSession.get(Course.class, id);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A course with id={}) is not found", id);
			throw new DataNotFoundException(String.format("A course with ID=%d is not found", id));
		} catch (Exception e) {
			LOGGER.info("A course wasn't retrieved from DB because of GENERAL ERROR. ID={}", id);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			LOGGER.info("Retrieved a course with ID={} from DB", id);
			closeCurrentSession();
		}
	}

	public Course retrieve(String courseName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a course (name={}) from DB", courseName);
		openCurrentSession();
		String hqlQuery = "from Course where name=:name";
		@SuppressWarnings("rawtypes")
		Query query = currentSession.createQuery(hqlQuery);
		query.setParameter("name", courseName);
		try {
			Course course = (Course) query.list().get(0);
			LOGGER.trace("A course entity (name ={}) was retrieved successfully", courseName);
			return course;
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A course with name={} is not found", courseName);
			throw new DataNotFoundException(String.format("A course with name=\"%s\" is not found", courseName));
		} catch (Exception e) {
			LOGGER.info("A course wasn't retrieved from DB because of GENERAL ERROR. Name ={}", courseName);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
	}

	public Integer getId(Course course) throws DataNotFoundException {
		return retrieve(course.getName()).getId();
	}

	public void update(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a course (ID={})", course.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.merge(course);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A course wasn't updated because of GENERAL ERROR. ID={}", course.getId());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.trace("The course (ID={}) updated successfully", course.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a course by ID={}", id);
		LOGGER.trace("Going to retrieve an entity for a course (ID={})", id);
		openCurrentSession();
		Course course = currentSession.get(Course.class, id);
		LOGGER.trace("The entity retrieved for a course (ID={})", id);
		closeCurrentSession();
		if (course != null) {
			delete(course);
		} else {
			throw new DataAreNotUpdatedException(String.format("A course with ID=%d is not found", id));
		}
	}

	public void delete(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a course entity, ID={}", course.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.remove(course);
			closeCurrentSessionwithTransaction();
			LOGGER.trace("The course entity deleted, ID={}", course.getId());
		} catch (Exception e) {
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
	}

	public List<Course> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of courses from DB");
		List<Course> resultList = new ArrayList<>();
		openCurrentSession();
		try {
			resultList = currentSession.createQuery("from Course", Course.class).list();
		} catch (Exception e) {
			LOGGER.info("A list of courses wasn't retrieved because of GENERAL ERROR");
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}

		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of courses");
		} else {
			LOGGER.info("Sorting the list by name");
			Collections.sort(resultList, Comparator.comparing(Course::getName));
			LOGGER.info("Retrieved a list of courses successfully. {} courses were found", resultList.size());
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
