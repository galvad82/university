package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.ClassroomMapper;
import ua.com.foxminded.galvad.university.model.Classroom;

@Component
public class ClassroomDAO implements DAO<Integer, Classroom> {

	private JdbcTemplate jdbcTemplate;
	private ClassroomMapper mapper;
	
	@Autowired
	public void setMapper(ClassroomMapper mapper) {
		if (mapper!=null) {
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
		String query = "INSERT INTO classrooms (name) VALUES (?)";
		jdbcTemplate.update(query, classroom.getName());
	}

	public Classroom retrieve(Integer id) {
		String query = "SELECT * FROM classrooms WHERE id=" + id;
		List<Classroom> listOfClassrooms = jdbcTemplate.query(query, mapper);
		if (!listOfClassrooms.isEmpty()) {
			return listOfClassrooms.get(0);
		} else {
			return null;
		}
	}

	public void update(Classroom classroom) {
		String query = "UPDATE classrooms SET name=? WHERE id=?";
		jdbcTemplate.update(query, classroom.getName(), classroom.getId());
	}

	public void delete(Integer id) {
		String query = "DELETE FROM classrooms WHERE id=" + id;
		jdbcTemplate.execute(query);
	}

	public void delete(Classroom classroom) {
		delete(classroom.getId());
	}

	public List<Classroom> findAll() {
		String query = "SELECT * FROM classrooms";
		return jdbcTemplate.query(query, mapper);
	}
	
}
