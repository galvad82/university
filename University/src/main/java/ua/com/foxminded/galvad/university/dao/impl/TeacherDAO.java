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
import ua.com.foxminded.galvad.university.dao.impl.mappers.TeacherMapper;
import ua.com.foxminded.galvad.university.model.Teacher;

@Repository
public class TeacherDAO implements DAO<Integer, Teacher> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDAO.class);

	private JdbcTemplate jdbcTemplate;
	private TeacherMapper mapper;

	private static final String CREATE = "INSERT INTO teachers (firstname, lastname) VALUES (?, ?)";
	private static final String RETRIEVE = "SELECT * FROM teachers WHERE id=?";
	private static final String UPDATE = "UPDATE teachers SET firstname=?,lastname=? WHERE id=?";
	private static final String DELETE = "DELETE FROM teachers WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM teachers";
	private static final String FIND_BY_NAMES = "SELECT * FROM teachers WHERE firstname=? AND lastname=?";

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
			String errorMessage = "Cannot add a teacher \"" + teacher.getFirstName() + "\" \"" + teacher.getLastName()
					+ "\" to DB";
			throw new DataAreNotUpdatedException(errorMessage, e);
		}

	}

	public Teacher retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a teacher from DB. ID={}", id);
			Teacher retrievedTeacher = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a teacher with ID={} from DB", id);
			return retrievedTeacher;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A teacher with ID=" + id + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a teacher with ID=" + id;
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public Integer getId(Teacher teacher) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a teacher from DB. First_Name ={}, Last_Name ={}",
					teacher.getFirstName(), teacher.getLastName());
			Integer result = jdbcTemplate.query(FIND_BY_NAMES, mapper, teacher.getFirstName(), teacher.getLastName())
					.get(0).getId();
			LOGGER.info("Retrieved an ID for a teacher from DB. First_Name ={}, Last_Name ={}, ID={}",
					teacher.getFirstName(), teacher.getLastName(), result);
			return result;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A teacher with FirstName=" + teacher.getFirstName() + " and LastName="
					+ teacher.getLastName() + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve an ID for a teacher with FirstName=" + teacher.getFirstName()
					+ " and LastName=" + teacher.getLastName();
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public void update(Teacher teacher) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update a teacher (ID={})", teacher.getId());
		try {
			Integer result = jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(),
					teacher.getId());
			if (result == 0) {
				String errorMessage = "A teacher with ID=" + teacher.getId() + " was not updated";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A teacher with ID={} was updated, new FirstName={}, new LastName={}", teacher.getId(),
						teacher.getFirstName(), teacher.getLastName());
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot update a teacher with ID=" + teacher.getId();
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a teacher (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				String errorMessage = "A teacher with ID=" + id + " was not deleted";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A teacher with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a teacher with ID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
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
			if (resultList.isEmpty()) {
				String errorMessage = "None of teachers was found in DB";
				throw new DataNotFoundException(errorMessage);
			} else {
				LOGGER.info("Retrieved a list of teachers successfully. {} teachers were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a list of teachers from DB";
			throw new DataNotFoundException(errorMessage, e);
		}
	}

}
