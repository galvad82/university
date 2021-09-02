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
import ua.com.foxminded.galvad.university.model.Lesson;

@Repository
public class LessonDAO implements DAO<Integer, Lesson> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void create(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) to DB",
				lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
				lesson.getStartTime(), lesson.getDuration());
		try {
			entityManager.persist(lesson);
		} catch (Exception e) {
			LOGGER.info(
					"A lesson wasn't added to DB. (groupID={}, courseID={},classroomID={}, startTime={}, duration={})",
					lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
					lesson.getStartTime(), lesson.getDuration());
			throw new DataAreNotUpdatedException(String.format(
					"A lesson wasn't added to DB. (groupID=%d, courseID=%d,classroomID=%d, startTime=%d, duration=%d)",
					lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
					lesson.getStartTime(), lesson.getDuration()));
		}
		LOGGER.info("Added a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) to DB",
				lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
				lesson.getStartTime(), lesson.getDuration());
	}

	public Lesson retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a lesson from DB. ID={}", id);
		Lesson lesson = null;
		try {
			lesson = entityManager.find(Lesson.class, id);
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a lesson from DB. ID={}", id);
			throw new DataNotFoundException(String.format("Can't retrieve a lesson from DB. ID=%d", id));
		}
		if (lesson == null) {
			LOGGER.info("A lesson with ID={} was not found", id);
			throw new DataNotFoundException(String.format("A lesson with ID=%d was not found", id));
		}
		LOGGER.trace("The lesson with id={} retrieved from DB successfully", id);
		return lesson;
	}

	public Integer getId(Lesson lesson) throws DataNotFoundException {
		Integer groupID = lesson.getGroup().getId();
		Integer courseID = lesson.getCourse().getId();
		Integer classroomID = lesson.getClassroom().getId();
		Long startTime = lesson.getStartTime();
		Long duration = lesson.getDuration();
		LOGGER.trace(
				"Going to retrieve an ID for a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={})  from DB",
				groupID, courseID, classroomID, startTime, duration);
		Lesson resultLesson = null;
		try {
			resultLesson = (Lesson) entityManager.createQuery(
					"from Lesson where group_id = :groupID and course_id=:courseID and classroom_id=:classroomID and starttime=:startTime and duration=:duration")
					.setParameter("groupID", groupID).setParameter("courseID", courseID)
					.setParameter("classroomID", classroomID).setParameter("startTime", startTime)
					.setParameter("duration", duration).getSingleResult();
		} catch (Exception e) {
			LOGGER.trace("A lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) is not found!",
					groupID, courseID, classroomID, startTime, duration);
			throw new DataNotFoundException(String.format(
					"A lesson (groupID=%d, courseID=%d,classroomID=%d, startTime=%d, duration=%d) is not found!",
					groupID, courseID, classroomID, startTime, duration));
		}
		LOGGER.trace("The lesson retrieved from DB (groupID={}, courseID={},classroomID={}, startTime={}, duration={})",
				groupID, courseID, classroomID, startTime, duration);
		return resultLesson.getId();
	}

	public void update(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a lesson (ID={})", lesson.getId());
		try {
			entityManager.merge(lesson);
		} catch (Exception e) {
			LOGGER.info("Can't update a lesson. ID={}", lesson.getId());
			throw new DataAreNotUpdatedException(String.format("Can't update a lesson. ID=%d", lesson.getId()));
		}
		LOGGER.trace("The lesson (ID={}) updated successfully", lesson.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson entity, ID={}", id);
		Integer isDeleted;
		try {
			isDeleted = entityManager.createQuery("delete from Lesson lesson where lesson.id=:id")
					.setParameter("id", id).executeUpdate();
		} catch (Exception e) {
			LOGGER.info("Can't delete a lesson. ID={}", id);
			throw new DataAreNotUpdatedException(String.format("Can't delete a lesson. ID=%d", id));
		}
		if (isDeleted != 0) {
			LOGGER.trace("The lesson entity deleted, ID={}", id);
		} else {
			throw new DataAreNotUpdatedException(String.format("A lesson with ID=%d is not found!", id));
		}
	}

	public void delete(Lesson lesson) throws DataAreNotUpdatedException {
		delete(lesson.getId());
	}

	@SuppressWarnings("unchecked")
	public List<Lesson> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of Lessons from DB");
		List<Lesson> resultList = new ArrayList<>();
		try {
			resultList = entityManager.createQuery("from Lesson").getResultList();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a list of lessons.");
			throw new DataNotFoundException("Can't retrieve a list of lessons.");
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of Lessons");
		} else {
			LOGGER.info("Sorting the list by Id");
			Collections.sort(resultList, Comparator.comparing(Lesson::getId));
			LOGGER.info("Retrieved a list of Lessons successfully. {} Lessons were found", resultList.size());
		}
		return resultList;
	}

	public void deleteByClassroomID(Integer id) throws DataAreNotUpdatedException {
		deleteByEntityID("classroom", id);
	}

	public void deleteByCourseID(Integer id) throws DataAreNotUpdatedException {
		deleteByEntityID("course", id);
	}

	public void deleteByGroupID(Integer id) throws DataAreNotUpdatedException {
		deleteByEntityID("group", id);
	}

	private void deleteByEntityID(String entityClassName, Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete lessons with {}ID={}", entityClassName, id);
		try {
			entityManager.createQuery(String.format("DELETE FROM Lesson WHERE %s_id= :id", entityClassName))
					.setParameter("id", id).executeUpdate();
			LOGGER.trace("The lessons with {}ID={} were deleted", entityClassName, id);
		} catch (Exception e) {
			LOGGER.info("Cannot delete lessons with {}ID={}", entityClassName, id);
			throw new DataAreNotUpdatedException(
					String.format("Cannot delete lessons with %sID=%d", entityClassName, id));
		}
	}
}
