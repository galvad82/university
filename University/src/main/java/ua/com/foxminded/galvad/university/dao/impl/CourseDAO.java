package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.CourseMapper;
import ua.com.foxminded.galvad.university.model.Course;

@Repository
public class CourseDAO implements DAO<Integer, Course> {

	private JdbcTemplate jdbcTemplate;
	private CourseMapper mapper;

	private static final String CREATE = "INSERT INTO courses (name, teacher) VALUES (?, ?)";
	private static final String RETRIEVE = "SELECT * FROM courses WHERE id=?";
	private static final String UPDATE = "UPDATE courses SET name=?,teacher=? WHERE id=?";
	private static final String DELETE = "DELETE FROM courses WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM courses";
	private static final String FIND_ID = "SELECT * FROM courses WHERE name=? AND teacher=?";
	
	@Autowired
	public void setMapper(CourseMapper mapper) {
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

	public void create(Course course) {
		jdbcTemplate.update(CREATE, course.getName(), course.getTeacher().getId());
	}

	public Course retrieve(Integer id) {
		try {
			return jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Integer getId (Course course) {
		try {
			return jdbcTemplate.query(FIND_ID, mapper, course.getName(),course.getTeacher().getId()).get(0).getId();
		} catch (Exception e) {
			return null;
		}
	}
	
	public void update(Course course) {
		jdbcTemplate.update(UPDATE, course.getName(), course.getTeacher().getId(), course.getId());
	}

	public void delete(Integer id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void delete(Course course) {
		delete(course.getId());
	}

	public List<Course> findAll() {
		return jdbcTemplate.query(FIND_ALL, mapper);
	}

}
