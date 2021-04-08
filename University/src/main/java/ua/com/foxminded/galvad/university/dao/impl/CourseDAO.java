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
			throw new DataAreNotUpdatedException(String.format("Cannot add a course (name=%s) to DB", course.getName()),
					e);
		}
	}

	public Course retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a course (ID={}) from DB", id);
			Course retrievedCourse = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a course (ID={}) from DB", retrievedCourse.getId());
			return retrievedCourse;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A course with ID=%d is not found", id));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a course with ID=%d", id), e);
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
			throw new DataNotFoundException(String.format("A course (name=%s, teacherID=%s) is not found",
					course.getName(), course.getTeacher().getId()));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(
					String.format("Cannot retrieve an ID for a course (name=%s, teacherID=%d) is not found",
							course.getName(), course.getTeacher().getId()),
					e);
		}
	}

	public void update(Course course) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a course (name={})", course.getName());
		try {
			Integer result = jdbcTemplate.update(UPDATE, course.getName(), course.getTeacher().getId(), course.getId());
			if (result == 0) {
				throw new DataAreNotUpdatedException(
						String.format("A course with ID=%d was not updated", course.getId()));
			} else {
				LOGGER.info("A course with ID={} was updated, new Name={}, new TeacherID={}", course.getId(),
						course.getName(), course.getTeacher().getId());
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot update a course with ID=%d", course.getId()), e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a course (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("A course with ID=%d was not deleted", id));
			} else {
				LOGGER.info("A course with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a course with ID=%d", id), e);
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
				throw new DataNotFoundException("None of courses was found in DB");
			} else {
				LOGGER.info("Retrieved a list of courses successfully. {} courses were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve a list of courses from DB", e);
		}
	}

}
