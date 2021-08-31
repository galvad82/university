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
import ua.com.foxminded.galvad.university.model.Course;

@Repository
public class CourseDAO implements DAO<Integer, Course> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CourseDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void create(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a course to DB. Name={}", course.getName());
		try {
			entityManager.persist(course);
		} catch (Exception e) {
			LOGGER.info("Course with name={} wasn't added to DB.", course.getName());
			throw new DataAreNotUpdatedException(
					String.format("Course with name=%s wasn't added to DB.", course.getName()));
		}
		LOGGER.info("Course with name={} successfully added to DB.", course.getName());
	}

	public Course retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a course from DB. ID={}", id);
		Course course = null;
		try {
			course = entityManager.find(Course.class, id);
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a course from DB. ID={}", id);
			throw new DataAreNotUpdatedException(String.format("Can't retrieve a course from DB. ID=%d", id));
		}
		LOGGER.trace("The course with id={} retrieved from DB successfully", id);
		return course;
	}

	public Course retrieve(String courseName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a course from DB. Name={}", courseName);
		Course course = null;
		try {
			course = (Course) entityManager.createQuery("from Course where name=:name").setParameter("name", courseName)
					.getSingleResult();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a course from DB. Name={}", courseName);
			throw new DataAreNotUpdatedException(String.format("Can't retrieve a course from DB. Name=%s", courseName));
		}
		LOGGER.trace("The course with name={} retrieved from DB successfully", courseName);
		return course;
	}

	public Integer getId(Course course) throws DataNotFoundException {
		return retrieve(course.getName()).getId();
	}

	public void update(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a course (ID={})", course.getId());
		try {
			entityManager.merge(course);
		} catch (Exception e) {
			LOGGER.info("Can't update a course. ID={}", course.getId());
			throw new DataAreNotUpdatedException(String.format("Can't update a course. ID=%d", course.getId()));
		}
		LOGGER.trace("The course (ID={}) updated successfully", course.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a course entity, ID={}", id);
		Integer isDeleted;
		try {
			isDeleted = entityManager.createQuery("delete from Course course where course.id=:id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			LOGGER.info("Can't delete a course. ID={}", id);
			throw new DataAreNotUpdatedException(String.format("Can't delete a course. ID=%d", id));
		}
		if (isDeleted != 0) {
			LOGGER.trace("The course entity deleted, ID={}", id);
		} else {
			throw new DataAreNotUpdatedException(String.format("A course with ID=%d is not found!", id));
		}
	}

	public void delete(Course course) throws DataAreNotUpdatedException {
		delete(course.getId());
	}

	@SuppressWarnings("unchecked")
	public List<Course> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of Courses from DB");
		List<Course> resultList = new ArrayList<>();
		try {
			resultList = entityManager.createQuery("from Course").getResultList();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a list of courses.");
			throw new DataAreNotUpdatedException("Can't retrieve a list of courses.");
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of Courses");
		} else {
			LOGGER.info("Sorting the list by Name");
			Collections.sort(resultList, Comparator.comparing(Course::getName));
			LOGGER.info("Retrieved a list of Courses successfully. {} Courses were found", resultList.size());
		}
		return resultList;
	}
}
