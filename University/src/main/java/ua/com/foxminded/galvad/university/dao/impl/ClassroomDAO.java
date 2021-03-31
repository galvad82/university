package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.ClassroomMapper;
import ua.com.foxminded.galvad.university.model.Classroom;

@Repository
public class ClassroomDAO implements DAO<Integer, Classroom> {

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

	public void create(Classroom classroom) {
		jdbcTemplate.update(CREATE, classroom.getName());
	}

	public Classroom retrieve(Integer id) {
		try {
			return jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	public Integer getId(Classroom classroom) {
		try {
			return jdbcTemplate.query(FIND_BY_NAME, mapper, classroom.getName()).get(0).getId();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public void update(Classroom classroom) {
		jdbcTemplate.update(UPDATE, classroom.getName(), classroom.getId());
	}

	public void delete(Integer id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void delete(Classroom classroom) {
		delete(classroom.getId());
	}

	public List<Classroom> findAll() {
		return jdbcTemplate.query(FIND_ALL, mapper);
	}

}
