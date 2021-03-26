package ua.com.foxminded.galvad.university.dao.impl.mappers;

import java.sql.ResultSet;

import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.model.Classroom;

@Component
public class ClassroomMapper implements RowMapper<Classroom> {

	public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Classroom(rs.getInt("id"), rs.getString("name"));
	}

}