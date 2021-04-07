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
import ua.com.foxminded.galvad.university.dao.impl.mappers.ClassroomMapper;
import ua.com.foxminded.galvad.university.model.Classroom;

@Repository
public class ClassroomDAO implements DAO<Integer, Classroom> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomDAO.class);

	private JdbcTemplate jdbcTemplate;
	private ClassroomMapper mapper;

	private static final String CREATE = "INSERT INTO classrooms (name) VALUES (?)";
	private static final String RETRIEVE = "SELECT * FROM classrooms WHERE id=?";
	private static final String UPDATE = "UPDATE classrooms SET name=? WHERE id=?";
	private static final String DELETE = "DELETE FROM classrooms WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM classrooms";
	private static final String FIND_BY_NAME = "SELECT * FROM classrooms WHERE name=?";

	@Autowired
	public void setMapper(ClassroomMapper mapper) {
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

	public void create(Classroom classroom) throws DataAreNotUpdatedException {
		try {
			LOGGER.trace("Going to add a classroom to DB. Name={}", classroom.getName());
			jdbcTemplate.update(CREATE, classroom.getName());
			LOGGER.info("Classroom with name={} successfully added to DB.", classroom.getName());
		} catch (DataAccessException e) {
			String errorMessage = "Cannot add a classroom with name=\"" + classroom.getName() + "\" to DB";
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public Classroom retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a classroom from DB. ID={}", id);
			Classroom retrievedClassroom = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a classroom with ID={} from DB", id);
			return retrievedClassroom;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A classroom with ID=" + id + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a classroom with ID=" + id;
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public Integer getId(Classroom classroom) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a classroom (name={}) from DB. ", classroom.getName());
			Integer result = jdbcTemplate.query(FIND_BY_NAME, mapper, classroom.getName()).get(0).getId();
			LOGGER.info("Retrieved an ID for a classroom (name ={}, ID={})", classroom.getName(), result);
			return result;
		} catch (IndexOutOfBoundsException e) {
			String errorMessage = "A classroom with name=" + classroom.getName() + " is not found";
			throw new DataNotFoundException(errorMessage);
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve an ID for a classroom with name=" + classroom.getName();
			throw new DataNotFoundException(errorMessage, e);
		}
	}

	public void update(Classroom classroom) throws DataAreNotUpdatedException {
		try {
			Integer result = jdbcTemplate.update(UPDATE, classroom.getName(), classroom.getId());
			if (result == 0) {
				String errorMessage = "A classroom with ID=" + classroom.getId() + " was not updated";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A classroom with ID={} was updated, new Name={}", classroom.getId(), classroom.getName());
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot update a classroom with ID=" + classroom.getId();
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a classroom (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				String errorMessage = "A classroom with ID=" + id + " was not deleted";
				throw new DataAreNotUpdatedException(errorMessage);
			} else {
				LOGGER.info("A classroom with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot delete a classroom with ID=" + id;
			throw new DataAreNotUpdatedException(errorMessage, e);
		}
	}

	public void delete(Classroom classroom) throws DataAreNotUpdatedException {
		delete(classroom.getId());
	}

	public List<Classroom> findAll() throws DataNotFoundException {
		List<Classroom> resultList = new ArrayList<>();
		LOGGER.trace("Going to retrieve a list of classrooms from DB");
		try {
			resultList = jdbcTemplate.query(FIND_ALL, mapper);
			if (resultList.isEmpty()) {
				String errorMessage = "None of classrooms was found in DB";
				throw new DataNotFoundException(errorMessage);
			} else {
				LOGGER.info("Retrieved a list of classrooms successfully. {} classrooms were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			String errorMessage = "Cannot retrieve a list of classrooms from DB";
			throw new DataNotFoundException(errorMessage, e);
		}
	}

}
