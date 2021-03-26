package ua.com.foxminded.galvad.university.dao.impl.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.model.Teacher;
@Component
public class TeacherMapper implements RowMapper<Teacher> {

	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {

		int id = rs.getInt("id");
		String firstName = rs.getString("firstname");
		String lastName = rs.getString("lastname");
		return new Teacher(id, firstName, lastName);
	}

}