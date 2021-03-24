package ua.com.foxminded.galvad.university.dao.impl.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.model.Group;

@Component
public class GroupMapper implements RowMapper<Group> {
	
	public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Group(rs.getInt("id"), rs.getString("name"));
	}

}