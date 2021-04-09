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
			throw new DataAreNotUpdatedException("Cannot add a lesson to DB", e);
		}
	}

	public Lesson retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a lesson (ID={}) from DB", id);
			Lesson retrievedLesson = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a lesson (ID={}) from DB", retrievedLesson.getId());
			return retrievedLesson;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A lesson with ID=%d is not found", id));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a lesson with ID=%d", id), e);
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
			throw new DataNotFoundException("A lesson is not found");
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve an ID for a lesson", e);
		}
	}

	public void update(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a lesson with ID={}", lesson.getId());
		try {
			Integer result = jdbcTemplate.update(UPDATE, lesson.getGroup().getId(), lesson.getCourse().getId(),
					lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration(), lesson.getId());
			if (result == 0) {
				throw new DataAreNotUpdatedException(
						String.format("A lesson with ID=%d was not updated", lesson.getId()));
			} else {
				LOGGER.info(
						"A lesson (ID={}) was updated (groupID={}, courseID={},classroomID={}, startTime={}, duration={})  from DB",
						lesson.getId(), lesson.getGroup().getId(), lesson.getCourse().getId(),
						lesson.getClassroom().getId(), lesson.getStartTime(), lesson.getDuration());
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot update a lesson with ID=%d", lesson.getId()), e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("A lesson with ID=%d was not deleted", id));
			} else {
				LOGGER.info("A lesson with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a lesson with ID=%d", id), e);
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
				throw new DataNotFoundException("None of lessons was found in DB");
			} else {
				LOGGER.info("Retrieved a list of lessons successfully. {} lessons were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve a list of lessons from DB", e);
		}
	}

	public void deleteByClassroomID(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (classroomID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE_BY_CLASSROOM_ID, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("Didn't find any lesson with classroomID=%d", id));
			} else {
				LOGGER.info("All lessons with classroomID={} were deleted successfully ({} in total)", id, result);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a lesson with classroomID=%d", id), e);
		}
	}

	public void deleteByCourseID(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (courseID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE_BY_COURSE_ID, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("Didn't find any lesson with courseID=%d", id));
			} else {
				LOGGER.info("All lessons with courseID={} were deleted successfully ({} in total)", id, result);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a lesson with courseID=%d", id), e);
		}
	}

	public void deleteByGroupID(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson (groupID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE_BY_GROUP_ID, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("Didn't find any lesson with groupID=%d", id));
			} else {
				LOGGER.info("All lessons with groupID={} were deleted successfully ({} in total)", id, result);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a lesson with groupID=%d", id), e);
		}
	}

}
