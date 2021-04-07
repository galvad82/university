package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.CourseMapper;
import ua.com.foxminded.galvad.university.model.Course;

@Repository
public class CourseDAO implements DAO<Integer, Course> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CourseDAO.class);

	private JdbcTemplate jdbcTemplate;
	private CourseMapper mapper;

	private static final String CREATE = "INSERT INTO courses (name, teacher) VALUES (?, ?)";
	private static final String RETRIEVE = "SELECT * FROM courses WHERE id=?";
	private static final String UPDATE = "UPDATE courses SET name=?,teacher=? WHERE id=?";
	private static final String DELETE = "DELETE FROM courses WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM courses";
	private static final String FIND_ID = "SELECT * FROM courses WHERE name=? AND teacher=?";

	@Autowired
	public void setMapper(CourseMapper mapper) {
		if (mapper != null) {
			this.mapper = mapper;
		} else {
			throw new IllegalArgumentException("Mapper cannot be null!");
		}
	}

	@Autowired
	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	public void create(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a course (name={}) to DB", course.getName());
		try {
			jdbcTemplate.update(CREATE, course.getName(), course.getTeacher().getId());
			LOGGER.info("Added a course (name={}) to DB", course.getName());
		} catch (DataAccessException e) {
			String errorMessage = "Cannot add a course (name=" + course.getName() + ") to DB";
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public Course retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a course (ID={}) from DB", id);
			Course retrievedCourse = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a course (ID={}) from DB", retrievedCourse.getId());
			return retrievedCourse;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A course with ID=" + id + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a course with ID=" + id;
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public Integer getId(Course course) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a course (name={}, teacherID={}) from DB", course.getName(),
					course.getTeacher().getId());
			Integer result = jdbcTemplate.query(FIND_ID, mapper, course.getName(), course.getTeacher().getId()).get(0)
					.getId();
			LOGGER.info("Retrieved an ID={} for a course (name={}, teacherID={}) from DB", result, course.getName(),
					course.getTeacher().getId());
			return result;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A course (name=" + course.getName() + ", teacherID=" + course.getTeacher().getId()
					+ ") is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve an ID for a course (name=" + course.getName() + ", teacherID="
					+ course.getTeacher().getId() + ")";
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public void update(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a course (name={})", course.getName());
		try {
			Integer result = jdbcTemplate.update(UPDATE, course.getName(), course.getTeacher().getId(), course.getId());
			if (result == 0) {
				String errorMessage = "A course with ID=" + course.getId() + " was not updated";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A course with ID={} was updated, new Name={}, new TeacherID={}", course.getId(),
						course.getName(), course.getTeacher().getId());
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot update a course with ID=" + course.getId();
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a course (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				String errorMessage = "A course with ID=" + id + " was not deleted";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A course with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a course with ID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Course course) throws DataAreNotUpdatedException {
		delete(course.getId());
	}

	public List<Course> findAll() throws DataNotFoundException {
		List<Course> resultList = new ArrayList<>();
		LOGGER.trace("Going to retrieve a list of courses from DB");
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			if (resultList.isEmpty()) {
				String errorMessage = "None of courses was found in DB";
				throw new DataNotFoundException(errorMessage);
			} else {
				LOGGER.info("Retrieved a list of courses successfully. {} courses were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a list of courses from DB";
			throw new DataNotFoundException(errorMessage, e);
		}
	}

}
