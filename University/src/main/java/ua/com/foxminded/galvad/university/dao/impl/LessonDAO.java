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
import ua.com.foxminded.galvad.university.dao.impl.mappers.LessonMapper;
import ua.com.foxminded.galvad.university.model.Lesson;

@Repository
public class LessonDAO implements DAO<Integer, Lesson> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAO.class);

	private JdbcTemplate jdbcTemplate;
	private LessonMapper mapper;

	private static final String CREATE = "INSERT INTO lessons (group_id,course,classroom,starttime,duration) VALUES (?,?,?,?,?)";
	private static final String RETRIEVE = "SELECT * FROM lessons WHERE id=?";
	private static final String UPDATE = "UPDATE lessons SET group_id=?, course=?, classroom=?, starttime=?, duration=? WHERE id=?";
	private static final String DELETE = "DELETE FROM lessons WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM lessons";
	private static final String FIND_ID = "SELECT * FROM lessons WHERE group_id=? AND course=? AND classroom=? AND starttime=? AND duration=?";
	private static final String DELETE_BY_CLASSROOM_ID = "DELETE FROM lessons WHERE classroom=?";
	private static final String DELETE_BY_COURSE_ID = "DELETE FROM lessons WHERE course=?";
	private static final String DELETE_BY_GROUP_ID = "DELETE FROM lessons WHERE group_id=?";

	@Autowired
	public void setMapper(LessonMapper mapper) {
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

	public void create(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) to DB",
				lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
				lesson.getStartTime(), lesson.getDuration());
		try {
			jdbcTemplate.update(CREATE, lesson.getGroup().getId(), lesson.getCourse().getId(),
					lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration());
			LOGGER.info("Added a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) to DB",
					lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
					lesson.getStartTime(), lesson.getDuration());
		} catch (DataAccessException e) {
			String errorMessage = "Cannot add a lesson to DB";
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public Lesson retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a lesson (ID={}) from DB", id);
			Lesson retrievedLesson = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a lesson (ID={}) from DB", retrievedLesson.getId());
			return retrievedLesson;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A lesson with ID=" + id + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a lesson with ID=" + id;
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public Integer getId(Lesson lesson) throws DataNotFoundException {
		try {
			LOGGER.trace(
					"Going to retrieve an ID for a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={})  from DB",
					lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
					lesson.getStartTime(), lesson.getDuration());
			Integer result = jdbcTemplate.query(FIND_ID, mapper, lesson.getGroup().getId(), lesson.getCourse().getId(),
					lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration()).get(0).getId();
			LOGGER.info(
					"Retrieved an ID={} for a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={})  from DB",
					result, lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
					lesson.getStartTime(), lesson.getDuration());
			return result;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A lesson is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve an ID for a lesson";
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public void update(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a lesson with ID={}", lesson.getId());
		try {
			Integer result = jdbcTemplate.update(UPDATE, lesson.getGroup().getId(), lesson.getCourse().getId(),
					lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration(), lesson.getId());
			if (result == 0) {
				String errorMessage = "A lesson with ID=" + lesson.getId() + " was not updated";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info(
						"A lesson (ID={}) was updated (groupID={}, courseID={},classroomID={}, startTime={}, duration={})  from DB",
						lesson.getId(), lesson.getGroup().getId(), lesson.getCourse().getId(),
						lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration());
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot update a lesson with ID=" + lesson.getId();
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				String errorMessage = "A lesson with ID=" + id + " was not deleted";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A lesson with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a lesson with ID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Lesson lesson) throws DataAreNotUpdatedException {
		delete(lesson.getId());
	}

	public List<Lesson> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of lessons from DB");
		List<Lesson> resultList = new ArrayList<>();
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			if (resultList.isEmpty()) {
				String errorMessage = "None of lessons was found in DB";
				throw new DataNotFoundException(errorMessage);
			} else {
				LOGGER.info("Retrieved a list of lessons successfully. {} lessons were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a list of lessons from DB";
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public void deleteByClassroomID(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (classroomID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE_BY_CLASSROOM_ID, id);
			if (result == 0) {
				String errorMessage = "Didn't find any lesson with classroomID=" + id;
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("All lessons with classroomID={} were deleted successfully ({} in total)", id, result);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a lesson with classroomID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void deleteByCourseID(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (courseID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE_BY_COURSE_ID, id);
			if (result == 0) {
				String errorMessage = "Didn't find any lesson with courseID=" + id;
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("All lessons with courseID={} were deleted successfully ({} in total)", id, result);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a lesson with courseID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void deleteByGroupID(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (groupID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE_BY_GROUP_ID, id);
			if (result == 0) {
				String errorMessage = "Didn't find any lesson with groupID=" + id;
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("All lessons with groupID={} were deleted successfully ({} in total)", id, result);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a lesson with groupID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

}
