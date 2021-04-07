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
import ua.com.foxminded.galvad.university.dao.impl.mappers.StudentMapper;
import ua.com.foxminded.galvad.university.model.Student;

@Repository
public class StudentDAO implements DAO<Integer, Student> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentDAO.class);
	private JdbcTemplate jdbcTemplate;
	private StudentMapper mapper;

	private static final String CREATE = "INSERT INTO students (firstname, lastname) VALUES (?, ?)";
	private static final String RETRIEVE = "SELECT * FROM students WHERE id=?";
	private static final String UPDATE = "UPDATE students SET firstname=?,lastname=? WHERE id=?";
	private static final String DELETE = "DELETE FROM students WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM students";
	private static final String REMOVE_STUDENT_FROM_GROUPS = "DELETE FROM groups_students WHERE student_id=?";
	private static final String FIND_BY_NAMES = "SELECT * FROM students WHERE firstname=? AND lastname=?";

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
			String errorMessage = "Cannot add a student \"" + student.getFirstName() + "\" \"" + student.getLastName()
					+ "\" to DB";
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public Student retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a student from DB. ID={}", id);
			Student retrievedStudent = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a student with ID={} from DB", id);
			return retrievedStudent;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A student with ID=" + id + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a student with ID=" + id;
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public Integer getId(Student student) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a student from DB. First_Name ={}, Last_Name ={}",
					student.getFirstName(), student.getLastName());
			Integer result = jdbcTemplate.query(FIND_BY_NAMES, mapper, student.getFirstName(), student.getLastName())
					.get(0).getId();
			LOGGER.info("Retrieved an ID for a student from DB. First_Name ={}, Last_Name ={}, ID={}",
					student.getFirstName(), student.getLastName(), result);
			return result;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A student with FirstName=" + student.getFirstName() + " and LastName="
					+ student.getLastName() + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve an ID for a student with FirstName=" + student.getFirstName()
					+ " and LastName=" + student.getLastName();
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public void update(Student student) throws DataAreNotUpdatedException {
		try {

			Integer result = jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(),
					student.getId());
			if (result == 0) {
				String errorMessage = "A student with ID=" + student.getId() + " was not updated";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A student with ID={} was updated, new FirstName={}, new LastName={}", student.getId(),
						student.getFirstName(), student.getLastName());
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot update a student with ID=" + student.getId();
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to remove a student (ID={}) from groups", id);
		try {
			removeStudentFromGroups(id);
			LOGGER.trace("A student (ID={}) was removed from groups", id);
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
		}
		LOGGER.trace("Going to delete a student (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				String errorMessage = "A student with ID=" + id + " was not deleted";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A student with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a student with ID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Student student) throws DataAreNotUpdatedException {
		delete(student.getId());
	}

	public List<Student> findAll() throws DataNotFoundException {
		List<Student> resultList = new ArrayList<>();
		LOGGER.trace("Going to retrieve a list of students from DB");
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			if (resultList.isEmpty()) {
				String errorMessage = "None of students was found in DB";
				throw new DataNotFoundException(errorMessage);
			} else {
				LOGGER.info("Retrieved a list of students successfully. {} students were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a list of students from DB";
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public void removeStudentFromGroups(Integer studentID) throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to delete a student (ID={}) from groups", studentID);
		try {
			if (jdbcTemplate.update(REMOVE_STUDENT_FROM_GROUPS, studentID) == 0) {
				String errorMessage = "A student with ID=" + studentID + " was not found in groups_students table";
				throw new DataNotFoundException(errorMessage);
			} else {
				LOGGER.info("A student (ID={}) was deleted from groups successfully", studentID);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a student (ID=" + studentID + ") from groups";
			throw new DataAreNotUpdatedException(errorMessage, e);
		}

	}

}