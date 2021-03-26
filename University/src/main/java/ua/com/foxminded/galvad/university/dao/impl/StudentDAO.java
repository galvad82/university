package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.StudentMapper;
import ua.com.foxminded.galvad.university.model.Student;

@Repository
public class StudentDAO implements DAO<Integer, Student> {

	private JdbcTemplate jdbcTemplate;
	private StudentMapper mapper;

	private static final String CREATE = "INSERT INTO students (firstname, lastname) VALUES (?, ?)";
	private static final String RETRIEVE = "SELECT * FROM students WHERE id=?";
	private static final String UPDATE = "UPDATE students SET firstname=?,lastname=? WHERE id=?";
	private static final String DELETE = "DELETE FROM students WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM students";
	private static final String REMOVE_STUDENT_FROM_GROUPS = "DELETE FROM groups_students WHERE student_id=?";

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
		jdbcTemplate.update(CREATE, student.getFirstName(), student.getLastName());
	}

	public Student retrieve(Integer id) {
		try {
			return jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public void update(Student student) {
		jdbcTemplate.update(UPDATE, student.getFirstName(), student.getLastName(), student.getId());
	}

	public void delete(Integer id) {
		removeStudentFromGroups(id);
		jdbcTemplate.update(DELETE, id);

	}

	public void delete(Student student) {
		delete(student.getId());
	}

	public List<Student> findAll() {
		return jdbcTemplate.query(FIND_ALL, mapper);
	}

	public void removeStudentFromGroups(Integer studentID) {
		jdbcTemplate.update(REMOVE_STUDENT_FROM_GROUPS, studentID);
	}

}