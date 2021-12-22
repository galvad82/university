package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.model.Group;

@Repository
public class GroupDAO implements DAO<Integer, Group> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void create(Group group) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a group to DB. Name={}", group.getName());
		try {
			entityManager.persist(group);
		} catch (Exception e) {
			LOGGER.info("Group with name={} wasn't added to DB.", group.getName());
			throw new DataAreNotUpdatedException(
					String.format("Group with name=%s wasn't added to DB.", group.getName()));
		}
		group.getSetOfStudent().stream().forEach(student -> student.setGroup(group));
		LOGGER.info("Group with name={} successfully added to DB.", group.getName());
	}

	public Group retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a group from DB. ID={}", id);
		Group group = null;
		try {
			group = entityManager.find(Group.class, id);
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a group from DB. ID={}", id);
			throw new DataNotFoundException(String.format("Can't retrieve a group from DB. ID=%d", id));
		}
		if (group == null) {
			LOGGER.info("A group with ID={} is not found", id);
			throw new DataNotFoundException(String.format("A group with ID=%d is not found", id));
		} else {
			LOGGER.trace("The group with id={} retrieved from DB successfully", id);
			return group;
		}
	}

	public Group retrieve(String groupName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a group from DB. Name={}", groupName);
		Group group = null;
		try {
			group = (Group) entityManager.createQuery("from Group where name=:name").setParameter("name", groupName)
					.getSingleResult();
		} catch (NoResultException ex) {
			LOGGER.info("A group with Name={} is not found", groupName);
			throw new DataNotFoundException(String.format("A group with Name=%s is not found", groupName));
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a group from DB. Name={}", groupName);
			throw new DataNotFoundException(String.format("Can't retrieve a group from DB. Name=%s", groupName));
		}
		LOGGER.trace("The group with name={} retrieved from DB successfully", groupName);
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
		try {
			entityManager.merge(group);
		} catch (Exception e) {
			LOGGER.info("Can't update a group. ID={}", group.getId());
			throw new DataAreNotUpdatedException(String.format("Can't update a group. ID=%d", group.getId()));
		}
		LOGGER.trace("The group (ID={}) updated successfully", group.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a group entity, ID={}", id);
		Integer isDeleted;
		try {
			isDeleted = entityManager.createQuery("delete from Group gr where gr.id=:id").setParameter("id", id)
					.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Can't delete a group. ID={}", id);
			throw new DataAreNotUpdatedException(String.format("Can't delete a group. ID=%d", id));
		}
		if (isDeleted != 0) {
			LOGGER.trace("The group entity deleted, ID={}", id);
		} else {
			throw new DataAreNotUpdatedException(String.format("A group with ID=%d is not found!", id));
		}
	}

	public void delete(Group group) throws DataAreNotUpdatedException {
		delete(group.getId());
	}

	@SuppressWarnings("unchecked")
	public List<Group> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of Groups from DB");
		List<Group> resultList = new ArrayList<>();
		try {
			resultList = entityManager.createQuery("from Group").getResultList();
		} catch (Exception e) {
			LOGGER.info("Can't retrieve a list of groups.");
			throw new DataNotFoundException("Can't retrieve a list of groups.");
		}
		if (resultList.isEmpty()) {
			LOGGER.info("Retrieved an EMPTY list of Groups");
		} else {
			LOGGER.info("Sorting the list by Name");
			Collections.sort(resultList, Comparator.comparing(Group::getName));
			LOGGER.info("Retrieved a list of Groups successfully. {} Groups were found", resultList.size());
		}
		return resultList;
	}
}
