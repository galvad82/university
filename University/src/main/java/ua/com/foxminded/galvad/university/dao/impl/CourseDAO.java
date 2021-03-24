package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.CourseMapper;
import ua.com.foxminded.galvad.university.model.Course;

@Component
public class CourseDAO implements DAO<Integer, Course> {

	private JdbcTemplate jdbcTemplate;
	private CourseMapper mapper;

	@Autowired
	public void setMapper(CourseMapper mapper) {
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

	public void create(Course course) {
		String query = "INSERT INTO courses (name, teacher) VALUES (?, ?)";
		jdbcTemplate.update(query, course.getName(), course.getTeacher().getId());
	}

	public Course retrieve(Integer id) {
		String query = "SELECT * FROM courses WHERE id=" + id;
		
		List<Course> listOfCourses = jdbcTemplate.query(query, mapper);
		if (!listOfCourses.isEmpty()) {		
			return listOfCourses.get(0);
		} else {
			return null;
		}
	}

	public void update(Course course) {
		String query = "UPDATE courses SET name=?,teacher=? WHERE id=?";
		jdbcTemplate.update(query, course.getName(), course.getTeacher().getId(), course.getId());
	}

	public void delete(Integer id) {
		String query = "DELETE FROM courses WHERE id=" + id;
		jdbcTemplate.execute(query);
	}

	public void delete(Course course) {
		delete(course.getId());
	}

	public List<Course> findAll() {
		String query = "SELECT * FROM courses";
		return jdbcTemplate.query(query, mapper);
	}

}
