package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.GroupMapper;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Repository
public class GroupDAO implements DAO<Integer, Group> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupDAO.class);
	private static final String GROUP_NOT_FOUND = "A group with name=%s  is not found";
	private JdbcTemplate jdbcTemplate;
	private GroupMapper mapper;

	private static final String CREATE = "INSERT INTO groups (name) VALUES (?)";
	private static final String RETRIEVE = "SELECT * FROM groups WHERE id=?";
	private static final String UPDATE = "UPDATE groups SET name=? WHERE id=?";
	private static final String DELETE = "DELETE FROM groups WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM groups";
	private static final String FIND_BY_NAME = "SELECT * FROM groups WHERE name=?";
	private static final String ADD_STUDENTS_TO_GROUP = "INSERT INTO groups_students (group_id,student_id) VALUES(?,?)";
	private static final String REMOVE_ALL_STUDENTS_FROM_GROUP = "DELETE FROM groups_students WHERE group_id=?";
	private static final String FIND_ALL_STUDENTS_FOR_GROUP = "SELECT students.id,students.firstname,students.lastname "
			+ "FROM students LEFT JOIN groups_students ON (groups_students.student_id=students.id) "
			+ "WHERE groups_students.group_id=?";
	private static final String FIND_GROUP_ID_FOR_STUDENT = "SELECT group_id FROM groups_students WHERE student_id=?";

	@Autowired
	public void setMapper(GroupMapper mapper) {
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

	public void create(Group group) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a group (name={}) to DB", group.getName());
		try {
			jdbcTemplate.update(CREATE, group.getName());
			LOGGER.info("The group (name={}) was added to DB successfully", group.getName());
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot add a group (name=%s) to DB", group.getName()),
					e);
		}
		try {
			LOGGER.trace("Going to retrieve an ID the group (name={})", group.getName());
			Integer id = getId(group);
			LOGGER.trace("Retrieve an ID={} the group (name={})", id, group.getName());
			LOGGER.trace("Going to add students of the group (id={}) to DB", id);
			addStudentsToGroup(group.getListOfStudent(), id);
			LOGGER.trace("The students ({} in total) of the group (id={}) were added to DB.",
					group.getListOfStudent().size(), id);
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(
					String.format("Cannot add the students of the group (name=%s) to DB", group.getName()), e);
		}

	}

	public Group retrieve(Integer id) throws DataNotFoundException {
		Group resultGroup = new Group();
		try {
			LOGGER.trace("Going to retrieve a group (ID={}) from DB", id);
			resultGroup = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.trace("Retrieved a group with ID={} from DB. Need to retrieve a list of students.", id);
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A group with ID=%d is not found", id));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a group with ID=%d", id), e);
		}
		LOGGER.trace("Going to retrieve a list of students for the group (ID={}) from DB", resultGroup.getId());
		try {
			resultGroup.setListOfStudent(findAllStudentsForGroup(resultGroup));
			LOGGER.trace("Retrieved a list of students ({} in total) for the group (ID={}) from DB successfully",
					resultGroup.getListOfStudent().size(), resultGroup.getId());
		} catch (DataNotFoundException e) {
			LOGGER.trace("None of students was found for the group (ID={})", resultGroup.getId());
		}
		LOGGER.info("Retrieved a group with ID={} from DB", id);
		return resultGroup;
	}

	public Group retrieve(String groupName) {
		Group resultGroup = new Group();
		try {
			LOGGER.trace("Going to retrieve a group from DB by name={}", groupName);
			resultGroup = jdbcTemplate.query(FIND_BY_NAME, mapper, groupName).get(0);
			LOGGER.info("Retrieved a group (name={}) from DB", resultGroup.getName());
			return resultGroup;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format(GROUP_NOT_FOUND, groupName));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a group from DB by name=%s", groupName), e);
		}
	}

	public Group getGroupForStudent(Student student) {
		try {
			LOGGER.trace("Going to retrieve a group name for a student from DB. First_Name ={}, Last_Name ={}",
					student.getFirstName(), student.getLastName());
			Integer groupID = (Integer) jdbcTemplate.queryForMap(FIND_GROUP_ID_FOR_STUDENT, student.getId())
					.get("GROUP_ID");
			LOGGER.info("Retrieved a Group ID for a student from DB. First_Name ={}, Last_Name ={}, ID={}",
					student.getFirstName(), student.getLastName(), groupID);
			LOGGER.trace("Going to retrieve a group (ID={}) from DB", groupID);
			Group group = jdbcTemplate.query(RETRIEVE, mapper, groupID).get(0);
			LOGGER.trace("Retrieved a group with ID={} from DB", groupID);
			return group;
		} catch (EmptyResultDataAccessException e) {
			throw new DataNotFoundException(
					String.format("A student (First_Name =%s, Last_Name =%s) is not assigned to any group!",
							student.getFirstName(), student.getLastName()));
		}
	}

	public Integer getId(Group group) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a group (name={}) from DB", group.getName());
			Integer result = jdbcTemplate.query(FIND_BY_NAME, mapper, group.getName()).get(0).getId();
			LOGGER.info("Retrieved an ID for a group (name={}) from DB", group.getName());
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format(GROUP_NOT_FOUND, group.getName()));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(
					String.format("Cannot retrieve an ID for a group with name=%s", group.getName()), e);
		}
	}

	public Integer getId(String groupName) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a group (name={}) from DB", groupName);
			Integer result = jdbcTemplate.query(FIND_BY_NAME, mapper, groupName).get(0).getId();
			LOGGER.info("Retrieved an ID for a group (name={}) from DB", groupName);
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format(GROUP_NOT_FOUND, groupName));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve an ID for a group with name=%s", groupName),
					e);
		}
	}

	public void update(Group group) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update the group (ID={})", group.getId());
		try {
			LOGGER.trace("Updating a name of the group (ID={})", group.getId());
			Integer result = jdbcTemplate.update(UPDATE, group.getName(), group.getId());
			if (result == 0) {
				throw new DataAreNotUpdatedException(
						String.format("A group with ID=%d was not updated", group.getId()));
			} else {
				LOGGER.trace("A name of the group with ID={} was updated, new name={}", group.getId(), group.getName());
				LOGGER.trace("Deleting all the students of the group (ID={})", group.getId());
				removeAllStudentsFromGroup(group.getId());
				LOGGER.trace("Deleted all the students of the group (ID={})", group.getId());
				LOGGER.trace("Adding the students of the group (ID={})", group.getId());
				addStudentsToGroup(group.getListOfStudent(), group.getId());
				LOGGER.trace("Added the students of the group (ID={})", group.getId());
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot update a group with ID=%d", group.getId()), e);
		}

		LOGGER.info("The group with ID={} was updated successfully", group.getId());
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a group (ID={})", id);
		LOGGER.trace("Going to remove all the students from a group (ID={})", id);
		removeAllStudentsFromGroup(id);
		LOGGER.trace("All the students were removed from a group (ID={}) successfully", id);
		LOGGER.trace("Going to delete a group (ID={}) from DB", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("A group with ID=%d was not deleted", id));
			} else {
				LOGGER.info("A group with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a group with ID=%d", id), e);
		}
	}

	public void delete(Group group) throws DataAreNotUpdatedException {
		delete(group.getId());
	}

	public List<Group> findAll() throws DataNotFoundException {
		List<Group> resultList = new ArrayList<>();
		LOGGER.trace("Going to retrieve a list of groups from DB");
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			Collections.sort(resultList, Comparator.comparing(Group::getName));
			if (resultList.isEmpty()) {
				throw new DataNotFoundException("None of groups was found in DB");
			} else {
				LOGGER.info("Retrieved a list of groups successfully. {} groups were found", resultList.size());
			}
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve a list of groups from DB", e);
		}

		LOGGER.trace("Going to retrieve a list of students for each of the groups from DB");
		for (Group group : resultList) {
			try {
				group.setListOfStudent(findAllStudentsForGroup(group));
			} catch (DataNotFoundException e) {
				LOGGER.error("None of students was found for the group (ID={})", group.getId());
			} catch (DataAccessException e) {
				throw new DataNotFoundException(
						String.format("Cannot retrieve a list of students for the group(ID=%d)", group.getId()), e);
			}
		}
		return resultList;
	}

	public List<Group> findAllWithoutStudentList() throws DataNotFoundException {
		List<Group> resultList = new ArrayList<>();
		LOGGER.trace("Going to retrieve a list of groups from DB");
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			Collections.sort(resultList, Comparator.comparing(Group::getName));
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve a list of groups from DB", e);
		}
		return resultList;
	}

	public void addStudentsToGroup(List<Student> listOfStudents, Integer groupID) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add all students of the group (ID={} to groups_students table)", groupID);
		try {
			listOfStudents.stream()
					.forEach(student -> jdbcTemplate.update(ADD_STUDENTS_TO_GROUP, groupID, student.getId()));
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(
					String.format("Cannot add all students of the group (ID=%d) to groups_students table", groupID), e);
		}
	}

	public void removeAllStudentsFromGroup(Integer groupID) throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to delete all students from the group (ID={})", groupID);
		try {
			jdbcTemplate.update(REMOVE_ALL_STUDENTS_FROM_GROUP, groupID);
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(
					String.format("Cannot delete all students from the group (ID=%d", groupID), e);
		}

	}

	public List<Student> findAllStudentsForGroup(Group group) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve a list of students for the group (ID={}, name={}) from DB", group.getId(),
				group.getName());
		try {
			List<Student> resultList = jdbcTemplate.query(FIND_ALL_STUDENTS_FOR_GROUP,
					(rs, rowNum) -> new Student(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname")),
					group.getId());
			Collections.sort(resultList,
					Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName));
			if (resultList.isEmpty()) {
				throw new DataNotFoundException(
						String.format("The group (ID=%d) does not have any student", group.getId()));
			} else {
				LOGGER.info("Retrieved a list of students for the group (ID={}) successfully. {} students were found",
						group.getId(), resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			throw new DataNotFoundException(
					String.format("Cannot retrieve a list of students for the group (ID=%d, name=%s) from DB",
							group.getId(), group.getName()),
					e);
		}
	}
}
