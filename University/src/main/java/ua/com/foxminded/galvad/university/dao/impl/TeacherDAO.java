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
import ua.com.foxminded.galvad.university.dao.impl.mappers.TeacherMapper;
import ua.com.foxminded.galvad.university.model.Teacher;

@Repository
public class TeacherDAO implements DAO<Integer, Teacher> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAO.class);

	private JdbcTemplate jdbcTemplate;
	private TeacherMapper mapper;

	private static final String CREATE = "INSERT INTO teachers (firstname, lastname) VALUES (?, ?)";
	private static final String RETRIEVE = "SELECT * FROM teachers WHERE id=?";
	private static final String RETRIEVE_BY_NAME = "SELECT * FROM teachers WHERE firstname=? AND lastname=?";
	private static final String UPDATE = "UPDATE teachers SET firstname=?,lastname=? WHERE id=?";
	private static final String DELETE = "DELETE FROM teachers WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM teachers";

	@Autowired
	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	@Autowired
	public void setMapper(TeacherMapper mapper) {
		if (mapper != null) {
			this.mapper = mapper;
		} else {
			throw new IllegalArgumentException("Mapper cannot be null!");
		}
	}

	public void create(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to add a teacher to DB. FirstName=\"{}\", LastName =\"{}\"", teacher.getFirstName(),
				teacher.getLastName());
		try {
			jdbcTemplate.update(CREATE, teacher.getFirstName(), teacher.getLastName());
			LOGGER.info("Added a teacher to DB. FirstName=\"{}\", LastName =\"{}\"", teacher.getFirstName(),
					teacher.getLastName());
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot add a teacher \"%s\" \"%s\" to DB",
					teacher.getFirstName(), teacher.getLastName()), e);
		}

	}

	public Teacher retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a teacher from DB. ID={}", id);
			Teacher retrievedTeacher = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a teacher with ID={} from DB", id);
			return retrievedTeacher;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A teacher with ID=%d is not found", id));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a teacher with ID=%d", id), e);
		}
	}

	public Teacher retrieve(String firstName, String lastName) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a teacher entity (First_Name ={}, Last_Name ={})", firstName, lastName);
			Teacher result = jdbcTemplate.query(RETRIEVE_BY_NAME, mapper, firstName, lastName).get(0);
			LOGGER.info("A teacher retrieved from DB. First_Name ={}, Last_Name ={}, ID={}", result.getFirstName(),
					result.getLastName(), result.getId());
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String
					.format("A teacher with FirstName=\"%s\" and LastName=\"%s\" is not found", firstName, lastName));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(
					String.format("Cannot retrieve an ID for a teacher with FirstName=\"%s\" and LastName=\"%s\"",
							firstName, lastName),
					e);
		}
	}

	public Integer getId(Teacher teacher) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a teacher from DB. First_Name ={}, Last_Name ={}",
					teacher.getFirstName(), teacher.getLastName());
			Integer result = jdbcTemplate.query(RETRIEVE_BY_NAME, mapper, teacher.getFirstName(), teacher.getLastName())
					.get(0).getId();
			LOGGER.info("Retrieved an ID for a teacher from DB. First_Name ={}, Last_Name ={}, ID={}",
					teacher.getFirstName(), teacher.getLastName(), result);
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(
					String.format("A teacher with FirstName=\"%s\" and LastName=\"%s\" is not found",
							teacher.getFirstName(), teacher.getLastName()));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(
					String.format("Cannot retrieve an ID for a teacher with FirstName=\"%s\" and LastName=\"%s\"",
							teacher.getFirstName(), teacher.getLastName()),
					e);
		}
	}

	public void update(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a teacher (ID={})", teacher.getId());
		try {
			Integer result = jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(),
					teacher.getId());
			if (result == 0) {
				throw new DataAreNotUpdatedException(
						String.format("A teacher with ID=%d was not updated", teacher.getId()));
			} else {
				LOGGER.info("A teacher with ID={} was updated, new FirstName={}, new LastName={}", teacher.getId(),
						teacher.getFirstName(), teacher.getLastName());
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot update a teacher with ID=%d", teacher.getId()),
					e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a teacher (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("A teacher with ID=%d was not deleted", id));
			} else {
				LOGGER.info("A teacher with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a teacher with ID=%d", id), e);
		}
	}

	public void delete(Teacher teacher) throws DataAreNotUpdatedException {
		delete(teacher.getId());
	}

	public List<Teacher> findAll() throws DataNotFoundException {
		List<Teacher> resultList = new ArrayList<>();
		LOGGER.trace("Going to retrieve a list of teachers from DB");
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			Collections.sort(resultList,
					Comparator.comparing(Teacher::getLastName).thenComparing(Teacher::getFirstName));
			if (resultList.isEmpty()) {
				throw new DataNotFoundException("None of teachers was found in DB");
			} else {
				LOGGER.info("Retrieved a list of teachers successfully. {} teachers were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve a list of teachers from DB", e);
		}
	}

}
