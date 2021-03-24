package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.GroupMapper;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Component
public class GroupDAO implements DAO<Integer, Group> {

	private JdbcTemplate jdbcTemplate;
	private GroupMapper mapper;

	@Autowired
	public void setMapper(GroupMapper mapper) {
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

	public void create(Group group) {
		String query = "INSERT INTO groups (name) VALUES (?)";
		jdbcTemplate.update(query, group.getName());
		addStudentsToGroup(group.getListOfStudent(), group.getId());
	}

	public Group retrieve(Integer id) {
		String query = "SELECT * FROM groups WHERE id=" + id;
		List<Group> listOfGroups = jdbcTemplate.query(query, mapper);
		if (!listOfGroups.isEmpty()) {
			Group resultGroup = listOfGroups.get(0);
			resultGroup.setListOfStudent(findAllStudentsForGroup(resultGroup.getId()));
			return resultGroup;
		} else {
			return null;
		}
	}

	public void update(Group group) {
		String query = "UPDATE groups SET name=? WHERE id=?";
		jdbcTemplate.update(query, group.getName(), group.getId());
		removeAllStudentsFromGroup(group.getId());
		addStudentsToGroup(group.getListOfStudent(), group.getId());
	}

	public void delete(Integer id) {
		String query = "DELETE FROM groups WHERE id=" + id;
		jdbcTemplate.execute(query);
	}

	public void delete(Group group) {
		delete(group.getId());
	}

	public List<Group> findAll() {
		String query = "SELECT * FROM groups";
		List<Group> resultList = jdbcTemplate.query(query, mapper);
		for (Group group : resultList) {
			List<Student> list = findAllStudentsForGroup(group.getId());
			group.setListOfStudent(list);
		}
		return resultList;
	}

	public void addStudentsToGroup(List<Student> listOfStudents, Integer groupID) {
		listOfStudents.stream().forEach(student -> {
			String query = "INSERT INTO groups_students (group_id,student_id) VALUES(?,?)";
			jdbcTemplate.update(query, groupID, student.getId());
		});
	}

	public void removeAllStudentsFromGroup(Integer groupID) {
		String query = "DELETE FROM groups_students WHERE group_id=?";
		jdbcTemplate.update(query, groupID);

	}

	public List<Student> findAllStudentsForGroup(Integer groupID) {
		String query = "SELECT students.id,students.firstname,students.lastname FROM students "
				+ "LEFT JOIN groups_students ON (groups_students.student_id=students.id) "
				+ "WHERE groups_students.group_id=" + groupID;
		return jdbcTemplate.query(query,
				(rs, rowNum) -> new Student(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname")));

	}
}
