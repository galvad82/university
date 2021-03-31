package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.TeacherMapper;
import ua.com.foxminded.galvad.university.model.Teacher;

@Repository
public class TeacherDAO implements DAO<Integer, Teacher> {

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

	public void create(Teacher teacher) {
		jdbcTemplate.update(CREATE, teacher.getFirstName(), teacher.getLastName());
	}

	public Teacher retrieve(Integer id) {
		try {
			return jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Integer getId (Teacher teacher) {
		try {
			return jdbcTemplate.query(FIND_BY_NAMES, mapper, teacher.getFirstName(),teacher.getLastName()).get(0).getId();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void update(Teacher teacher) {
		jdbcTemplate.update(UPDATE, teacher.getFirstName(), teacher.getLastName(), teacher.getId());
	}

	public void delete(Integer id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void delete(Teacher teacher) {
		delete(teacher.getId());
	}

	public List<Teacher> findAll() {
		return jdbcTemplate.query(FIND_ALL, mapper);
	}

}
