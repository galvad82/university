package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.StudentMapper;
import ua.com.foxminded.galvad.university.model.Student;

@Component
public class StudentDAO implements DAO<Integer, Student> {

	private JdbcTemplate jdbcTemplate;
	private StudentMapper mapper;

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

	public void create(Student student) {
		String query = "INSERT INTO students (firstname, lastname) VALUES (?, ?)";
		jdbcTemplate.update(query, student.getFirstName(), student.getLastName());
	}

	public Student retrieve(Integer id) {
		String query = "SELECT * FROM students WHERE id=" + id;
		if (!jdbcTemplate.query(query, new StudentMapper()).isEmpty()) {
			return jdbcTemplate.query(query, mapper).get(0);
		} else {
			return null;
		}
	}

	public void update(Student student) {
		String query = "UPDATE students SET firstname=?,lastname=? WHERE id=?";
		jdbcTemplate.update(query, student.getFirstName(), student.getLastName(), student.getId());
	}

	public void delete(Integer id) {
		removeStudentFromGroups(id);
		String query = "DELETE FROM students WHERE id=" + id;
		jdbcTemplate.execute(query);

	}

	public void delete(Student student) {
		delete(student.getId());
	}

	public List<Student> findAll() {
		String query = "SELECT * FROM students";
		return jdbcTemplate.query(query, mapper);
	}

	public void removeStudentFromGroups(Integer studentID) {
		String query = "DELETE FROM groups_students WHERE student_id=?";
		jdbcTemplate.update(query, studentID);
	}

}