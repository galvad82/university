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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.StudentMapper;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Repository
public class StudentDAO implements DAO<Integer, Student> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAO.class);
	private JdbcTemplate jdbcTemplate;
	private StudentMapper mapper;

	private static final String CREATE = "INSERT INTO students (firstname, lastname) VALUES (?, ?)";
	private static final String RETRIEVE = "SELECT * FROM students WHERE id=?";
	private static final String RETRIEVE_BY_NAME = "SELECT * FROM students WHERE firstname=? AND lastname=?";
	private static final String UPDATE = "UPDATE students SET firstname=?,lastname=? WHERE id=?";
	private static final String DELETE = "DELETE FROM students WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM students";
	private static final String ADD_STUDENT_TO_GROUP = "INSERT INTO groups_students (group_id,student_id) VALUES(?,?)";
	private static final String ADD_STUDENT_TO_ANOTHER_GROUP = "UPDATE groups_students SET group_id=? WHERE student_id=?";
	private static final String REMOVE_STUDENT_FROM_GROUPS = "DELETE FROM groups_students WHERE student_id=?";
	private static final String FIND_GROUP_ID_FOR_STUDENT = "SELECT group_id FROM groups_students WHERE student_id=?";

	@Autowired
	public void setMapper(StudentMapper mapper) {
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

	public void create(Student student) throws DataAreNotUpdatedException {
		try {
			LOGGER.trace("Going to add a student to DB. FirstName=\"{}\", LastName =\"{}\"", student.getFirstName(),
					student.getLastName());
			jdbcTemplate.update(CREATE, student.getFirstName(), student.getLastName());
			LOGGER.info("Student \"{}\" \"{}\" successfully added to DB.", student.getFirstName(),
					student.getLastName());
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot add a student \"%s\" \"%s\" to DB",
					student.getFirstName(), student.getLastName()), e);
		}
	}

	public Student retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a student from DB. ID={}", id);
			Student retrievedStudent = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a student with ID={} from DB", id);
			return retrievedStudent;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A student with ID=%d is not found", id));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a student with ID=%d", id), e);
		}
	}

	public Student retrieve(String firstName, String lastName) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a student entity (First_Name ={}, Last_Name ={})", firstName, lastName);
			Student result = jdbcTemplate.query(RETRIEVE_BY_NAME, mapper, firstName, lastName).get(0);
			LOGGER.info("A student retrieved from DB. First_Name ={}, Last_Name ={}, ID={}", result.getFirstName(),
					result.getLastName(), result.getId());
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String
					.format("A student with FirstName=\"%s\" and LastName=\"%s\" is not found", firstName, lastName));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format(
					"Cannot retrieve a student with FirstName=\"%s\" and LastName=\"%s\"", firstName, lastName), e);
		}
	}

	public Integer getGroupId(Student student) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a Group ID for a student from DB. First_Name ={}, Last_Name ={}",
					student.getFirstName(), student.getLastName());
			Integer result = (Integer) jdbcTemplate.queryForMap(FIND_GROUP_ID_FOR_STUDENT, student.getId())
					.get("GROUP_ID");
			LOGGER.info("Retrieved a Group ID for a student from DB. First_Name ={}, Last_Name ={}, ID={}",
					student.getFirstName(), student.getLastName(), result);
			return result;
		} catch (DataAccessException e) {
			LOGGER.info("A student (First_Name ={}, Last_Name ={}) is not assigned to any group!",
					student.getFirstName(), student.getLastName());
			return 0;
		}
	}

	public Integer getId(Student student) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a student from DB. First_Name ={}, Last_Name ={}",
					student.getFirstName(), student.getLastName());
			Integer result = jdbcTemplate.query(RETRIEVE_BY_NAME, mapper, student.getFirstName(), student.getLastName())
					.get(0).getId();
			LOGGER.info("Retrieved an ID for a student from DB. First_Name ={}, Last_Name ={}, ID={}",
					student.getFirstName(), student.getLastName(), result);
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A student with FirstName=%s and LastName=%s is not found",
					student.getFirstName(), student.getLastName()));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(
					String.format("Cannot retrieve an ID for a student with FirstName=%s and LastName=%s",
							student.getFirstName(), student.getLastName()),
					e);
		}
	}

	public void update(Student student) throws DataAreNotUpdatedException {
		try {

			Integer result = jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(),
					student.getId());
			if (result == 0) {
				throw new DataAreNotUpdatedException(
						String.format("A student with ID=%d was not updated", student.getId()));
			} else {
				LOGGER.info("A student with ID={} was updated, new FirstName={}, new LastName={}", student.getId(),
						student.getFirstName(), student.getLastName());
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot update a student with ID=%d", student.getId()),
					e);
		}
	}

	public void updateGroupForStudent(Student student, Group group) throws DataAreNotUpdatedException {
		try {
			Integer result = jdbcTemplate.update(ADD_STUDENT_TO_ANOTHER_GROUP, group.getId(), student.getId());
			if (result == 0) {
				throw new DataAreNotUpdatedException(
						String.format("A group for student with ID=%d was not updated to groupID=%d", student.getId(),
								group.getId()));
			} else {
				LOGGER.info("A student with ID={} was assigned to the group (ID={})", student.getId(), group.getId());
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot update a student with ID=%d", student.getId()),
					e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {

		LOGGER.trace("Going to delete a student (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("A student with ID=%d was not deleted", id));
			} else {
				LOGGER.info("A student with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a student with ID=%d", id), e);
		}
	}

	public void delete(Student student) throws DataAreNotUpdatedException {
		removeStudentFromGroups(student);
		delete(student.getId());
	}

	public List<Student> findAll() throws DataNotFoundException {
		List<Student> resultList = new ArrayList<>();

		LOGGER.trace("Going to retrieve a list of students from DB");
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			Collections.sort(resultList,
					Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName));
			if (resultList.isEmpty()) {
				throw new DataNotFoundException("None of students was found in DB");
			} else {
				LOGGER.info("Retrieved a list of students successfully. {} students were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve a list of students from DB", e);
		}
	}

	public void addStudentToGroup(Student student, Group group) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a student to the group (ID={})", group.getId());
		try {
			jdbcTemplate.update(ADD_STUDENT_TO_GROUP, group.getId(), student.getId());
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(
					String.format("Cannot add a student to the group (ID=%d)", group.getId()), e);
		}
	}

	public void removeStudentFromGroups(Student student) throws DataAreNotUpdatedException {
		Integer studentID = student.getId();
		LOGGER.trace("Going to delete a student (ID={}) from groups", studentID);
		try {
			if (jdbcTemplate.update(REMOVE_STUDENT_FROM_GROUPS, studentID) == 0) {
				LOGGER.info("A student (ID={}) wasn't found in any group", studentID);
			} else {
				LOGGER.info("A student (ID={}) was deleted from groups successfully", studentID);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(
					String.format("Cannot delete a student (ID=%d) from groups", studentID), e);
		}

	}

}