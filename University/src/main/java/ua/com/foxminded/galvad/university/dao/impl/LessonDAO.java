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
import ua.com.foxminded.galvad.university.model.Lesson;

@Repository
public class LessonDAO implements DAO<Integer, Lesson> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonDAO.class);
	private static final String GENERAL_ERROR = "Cannot process data in the DB!";

	@Autowired
	private SessionFactory sessionFactory;

	private Session currentSession;
	private Transaction currentTransaction;

	public void create(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) to DB",
				lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
				lesson.getStartTime(), lesson.getDuration());
		openCurrentSessionWithTransaction();
		try {
			currentSession.persist(lesson);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(
					"A lesson wasn't added to DB because of GENERAL ERROR. (groupID={}, courseID={},classroomID={}, startTime={}, duration={})",
					lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
					lesson.getStartTime(), lesson.getDuration());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.info("Added a lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) to DB",
				lesson.getGroup().getId(), lesson.getCourse().getId(), lesson.getClassroom().getId(),
				lesson.getStartTime(), lesson.getDuration());
	}

	public Lesson retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a lesson from DB. ID={}", id);
		openCurrentSession();
		try {
			return currentSession.get(Lesson.class, id);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A lesson with id={}) is not found", id);
			throw new DataNotFoundException(String.format("A lesson with ID=%d is not found", id));
		} catch (Exception e) {
			LOGGER.info("A lesson wasn't retrieved from DB because of GENERAL ERROR. ID={}", id);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			LOGGER.info("Retrieved a lesson with ID={} from DB", id);
			closeCurrentSession();
		}
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
		openCurrentSession();
		Integer id = null;
		String hqlQuery = "from Lesson where group_id = :groupID and course_id=:courseID and classroom_id=:classroomID"
				+ " and starttime=:startTime and duration=:duration";
		try {
			@SuppressWarnings("rawtypes")
			Query query = currentSession.createQuery(hqlQuery);
			query.setParameter("groupID", groupID);
			query.setParameter("courseID", courseID);
			query.setParameter("classroomID", classroomID);
			query.setParameter("startTime", startTime);
			query.setParameter("duration", duration);
			Lesson resultLesson = (Lesson) query.list().get(0);
			id = resultLesson.getId();
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A lesson (groupID={}, courseID={},classroomID={}, startTime={}, duration={}) is not found!",
					groupID, courseID, classroomID, startTime, duration);
			throw new DataNotFoundException(
					String.format("A (groupID=%d, courseID=%d,classroomID=%d, startTime=%d, duration=%d) is not found!",
							groupID, courseID, classroomID, startTime, duration));
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info(
					"A lesson wasn't retrieved from DB because of GENERAL ERROR. (groupID={},"
							+ " courseID={},classroomID={}, startTime={}, duration={})",
					groupID, courseID, classroomID, startTime, duration);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
		LOGGER.trace("The lesson retrieved from DB (groupID={}, courseID={},classroomID={}, startTime={}, duration={})",
				groupID, courseID, classroomID, startTime, duration);
		return id;
	}

	public void update(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a lesson (ID={})", lesson.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.merge(lesson);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A lesson wasn't updated because of GENERAL ERROR. ID={}", lesson.getId());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.trace("The lesson (ID={}) updated successfully", lesson.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson by ID={}", id);
		LOGGER.trace("Going to retrieve an entity for a lesson (ID={})", id);
		openCurrentSession();
		Lesson lesson = currentSession.get(Lesson.class, id);
		LOGGER.trace("The entity retrieved for a lesson (ID={})", id);
		closeCurrentSession();
		if (lesson != null) {
			delete(lesson);
		} else {
			throw new DataAreNotUpdatedException(String.format("A lesson with ID=%d is not found", id));
		}
	}

	public void delete(Lesson lesson) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a lesson entity, ID={}", lesson.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.remove(lesson);
			closeCurrentSessionwithTransaction();
			LOGGER.trace("The lesson entity deleted, ID={}", lesson.getId());
		} catch (Exception e) {
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
	}

	public List<Lesson> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of lessons from DB");
		List<Lesson> resultList = new ArrayList<>();
		openCurrentSessionWithTransaction();
		try {
			resultList = currentSession.createQuery("from Lesson", Lesson.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("A list of lessons wasn't retrieved because of GENERAL ERROR");
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSessionwithTransaction();
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of lessons");
		} else {
			LOGGER.info("Sorting the list by ID");
			Collections.sort(resultList, Comparator.comparing(Lesson::getId));
			LOGGER.info("Retrieved a list of lessons successfully. {} lessons were found", resultList.size());
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
		openCurrentSessionWithTransaction();
		try {
			@SuppressWarnings("rawtypes")
			Query query = currentSession
					.createQuery(String.format("DELETE FROM Lesson WHERE %s_id= :id", entityClassName));
			query.setParameter("id", id);
			query.executeUpdate();
			LOGGER.trace("The lessons with {}ID={} were deleted", entityClassName, id);
		} catch (Exception e) {
			LOGGER.info("Cannot delete lessons with {}ID={}", entityClassName, id);
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		} finally {
			closeCurrentSessionwithTransaction();
		}
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
