package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.TeacherMapper;
import ua.com.foxminded.galvad.university.model.Teacher;

@Component
public class TeacherDAO implements DAO<Integer, Teacher> {

	private JdbcTemplate jdbcTemplate;
	private TeacherMapper mapper;

	@Autowired
	public void setDataSource(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	@Autowired
	public void setMapper(TeacherMapper mapper) {
		if (mapper!=null) {
			this.mapper = mapper;
		} else {
			throw new IllegalArgumentException("Mapper cannot be null!");
		}
	}

	public void create(Teacher teacher) {
		String query = "INSERT INTO teachers (firstname, lastname) VALUES (?, ?)";
		jdbcTemplate.update(query, teacher.getFirstName(), teacher.getLastName());
	}

	public Teacher retrieve(Integer id) {
		String query = "SELECT * FROM teachers WHERE id=" + id;
		List<Teacher> listOfTeachers = jdbcTemplate.query(query, mapper);
		if (!listOfTeachers.isEmpty()) {
			return listOfTeachers.get(0);
		} else {
			return null;
		}
	}

	public void update(Teacher teacher) {
		String query = "UPDATE teachers SET firstname=?,lastname=? WHERE id=?";
		jdbcTemplate.update(query, teacher.getFirstName(), teacher.getLastName(), teacher.getId());
	}

	public void delete(Integer id) {
		String query = "DELETE FROM teachers WHERE id=" + id;
		jdbcTemplate.execute(query);
	}

	public void delete(Teacher teacher) {
		delete(teacher.getId());
	}

	public List<Teacher> findAll() {
		String query = "SELECT * FROM teachers";
		return jdbcTemplate.query(query, mapper);
	}

}
