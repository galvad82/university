package ua.com.foxminded.galvad.university.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
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
			throw new DataAreNotUpdatedException(
					String.format("Cannot add a classroom with name=%s to DB", classroom.getName()), e);
		}
	}

	public Classroom retrieve(Integer id) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a classroom from DB. ID={}", id);
			Classroom retrievedClassroom = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			LOGGER.info("Retrieved a classroom with ID={} from DB", id);
			return retrievedClassroom;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A classroom with ID=%d is not found", id));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a classroom with ID=%d", id), e);
		}
	}

	public Classroom retrieve(String classroomName) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve a classroom from DB. Name={}", classroomName);
			Classroom retrievedClassroom = jdbcTemplate.query(FIND_BY_NAME, mapper, classroomName).get(0);
			LOGGER.info("Retrieved a classroom with name={} from DB", classroomName);
			return retrievedClassroom;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(String.format("A classroom with name=%s is not found", classroomName));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(String.format("Cannot retrieve a classroom with name=%s", classroomName),
					e);
		}
	}

	public Integer getId(Classroom classroom) throws DataNotFoundException {
		try {
			LOGGER.trace("Going to retrieve an ID for a classroom (name={}) from DB. ", classroom.getName());
			Integer result = jdbcTemplate.query(FIND_BY_NAME, mapper, classroom.getName()).get(0).getId();
			LOGGER.info("Retrieved an ID for a classroom (name ={}, ID={})", classroom.getName(), result);
			return result;
		} catch (IndexOutOfBoundsException e) {
			throw new DataNotFoundException(
					String.format("A classroom with name=%s is not found", classroom.getName()));
		} catch (DataAccessException e) {
			throw new DataNotFoundException(
					String.format("Cannot retrieve an ID for a classroom with name=%s", classroom.getName()), e);
		}
	}

	public void update(Classroom classroom) throws DataAreNotUpdatedException {
		try {
			Integer result = jdbcTemplate.update(UPDATE, classroom.getName(), classroom.getId());
			if (result == 0) {
				throw new DataAreNotUpdatedException(
						String.format("A classroom with ID=%d was not updated", classroom.getId()));
			} else {
				LOGGER.info("A classroom with ID={} was updated, new Name={}", classroom.getId(), classroom.getName());
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(
					String.format("Cannot update a classroom with ID=%d", classroom.getId()), e);
		}
	}

	public void delete(Integer id) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete a classroom (ID={})", id);
		try {
			Integer result = jdbcTemplate.update(DELETE, id);
			if (result == 0) {
				throw new DataAreNotUpdatedException(String.format("A classroom with ID=%d was not deleted", id));
			} else {
				LOGGER.info("A classroom with ID={} was deleted successfully", id);
			}
		} catch (DataAccessException e) {
			throw new DataAreNotUpdatedException(String.format("Cannot delete a classroom with ID=%d", id), e);
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
			Collections.sort(resultList, (o1, o2) -> o1.getId().compareTo(o2.getId()));
			if (resultList.isEmpty()) {
				throw new DataNotFoundException("None of classrooms was found in DB");
			} else {
				LOGGER.info("Retrieved a list of classrooms successfully. {} classrooms were found", resultList.size());
				return resultList;
			}
		} catch (DataAccessException e) {
			throw new DataNotFoundException("Cannot retrieve a list of classrooms from DB", e);
		}
	}

}
