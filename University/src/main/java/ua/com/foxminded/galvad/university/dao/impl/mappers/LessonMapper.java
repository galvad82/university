package ua.com.foxminded.galvad.university.dao.impl.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;

@Component
public class LessonMapper implements RowMapper<Lesson> {
	
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Group group = new Group(rs.getInt("group_id"));
		Course course = new Course(rs.getInt("course"));
		Classroom classroom = new Classroom(rs.getInt("classroom"));
		return new Lesson(rs.getInt("id"), group, course, classroom, rs.getLong("starttime"), rs.getLong("duration"));
	}

}