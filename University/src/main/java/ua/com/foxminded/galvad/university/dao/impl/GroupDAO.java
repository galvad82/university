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
import ua.com.foxminded.galvad.university.model.Group;

@Repository
public class GroupDAO implements DAO<Integer, Group> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupDAO.class);
	private static final String GENERAL_ERROR = "Cannot process data in the DB!";

	@Autowired
	private SessionFactory sessionFactory;

	private Session currentSession;
	private Transaction currentTransaction;

	public void create(Group group) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a group to DB. Name=\"{}\"", group.getName());
		openCurrentSessionWithTransaction();
		try {
			currentSession.persist(group);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("A group wasn't added to DB because of GENERAL ERROR. Name=\"{}\"", group.getName());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.info("Added a group to DB. Name=\"{}\"", group.getName());
		group.getSetOfStudent().stream().forEach(student -> student.setGroup(group));
	}

	public Group retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a group from DB. ID={}", id);
		openCurrentSession();
		try {
			return currentSession.get(Group.class, id);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A group with id={}) is not found", id);
			throw new DataNotFoundException(String.format("A group with ID=%d is not found", id));
		} catch (Exception e) {
			LOGGER.info("A group wasn't retrieved from DB because of GENERAL ERROR. ID={}", id);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			LOGGER.info("Retrieved a group with ID={} from DB", id);
			closeCurrentSession();
		}
	}

	public Group retrieve(String groupName) {
		LOGGER.trace("Going to retrieve a group from DB. Name={}", groupName);
		openCurrentSession();
		Group group = null;
		String hqlQuery = "from Group where name = :name";
		try {
			@SuppressWarnings("rawtypes")
			Query query = currentSession.createQuery(hqlQuery);
			query.setParameter("name", groupName);
			group = (Group) query.list().get(0);
		} catch (IndexOutOfBoundsException e) {
			LOGGER.trace("A group with name={}) is not found", groupName);
			throw new DataNotFoundException(String.format("A group with name=%s is not found", groupName));
		} catch (Exception e) {
			LOGGER.info("A group wasn't retrieved from DB because of GENERAL ERROR. Name={}", groupName);
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSession();
		}
		LOGGER.info("Retrieved a group with Name={} from DB", groupName);
		return group;
	}

	public Integer getId(Group group) throws DataNotFoundException {
		return retrieve(group.getName()).getId();
	}

	public Integer getId(String groupName) throws DataNotFoundException {
		return retrieve(groupName).getId();
	}

	public void update(Group group) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a group (ID={})", group.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.merge(group);
			closeCurrentSessionwithTransaction();
		} catch (Exception e) {
			LOGGER.info("A group wasn't updated because of GENERAL ERROR. ID={}", group.getId());
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
		LOGGER.trace("The group (ID={}) updated successfully", group.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a group by ID={}", id);
		LOGGER.trace("Going to retrieve an entity for a group (ID={})", id);
		openCurrentSession();
		Group group = currentSession.get(Group.class, id);
		LOGGER.trace("The entity retrieved for a group (ID={})", id);
		closeCurrentSession();
		if (group != null) {
			delete(group);
		} else {
			throw new DataAreNotUpdatedException(String.format("A group with ID=%d is not found", id));
		}
	}

	public void delete(Group group) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a group entity, ID={}", group.getId());
		openCurrentSessionWithTransaction();
		try {
			currentSession.remove(group);
			closeCurrentSessionwithTransaction();
			LOGGER.trace("The group entity deleted, ID={}", group.getId());
		} catch (Exception e) {
			throw new DataAreNotUpdatedException(GENERAL_ERROR);
		}
	}

	public List<Group> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of groups from DB");
		List<Group> resultList = new ArrayList<>();
		openCurrentSessionWithTransaction();
		try {
			resultList = currentSession.createQuery("from Group", Group.class).list();
		} catch (Exception e) {
			LOGGER.info("A list of groups wasn't retrieved because of GENERAL ERROR");
			throw new DataNotFoundException(GENERAL_ERROR);
		} finally {
			closeCurrentSessionwithTransaction();
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of groups");
		} else {
			LOGGER.info("Sorting the list by Name");
			Collections.sort(resultList, Comparator.comparing(Group::getName));
			LOGGER.info("Retrieved a list of groups successfully. {} groups were found", resultList.size());
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
