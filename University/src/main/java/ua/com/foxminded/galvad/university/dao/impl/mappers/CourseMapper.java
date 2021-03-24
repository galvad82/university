package ua.com.foxminded.galvad.university.dao.impl.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@Component
public class CourseMapper implements RowMapper<Course> {

	public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
		Course resultCourse = new Course (rs.getInt("id"), rs.getString("name"));
		resultCourse.setTeacher(new Teacher(rs.getInt("teacher")));
		return resultCourse;
	}
}