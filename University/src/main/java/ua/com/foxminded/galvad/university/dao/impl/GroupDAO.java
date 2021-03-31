package ua.com.foxminded.galvad.university.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.galvad.university.dao.DAO;
import ua.com.foxminded.galvad.university.dao.impl.mappers.GroupMapper;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Repository
public class GroupDAO implements DAO<Integer, Group> {

	private JdbcTemplate jdbcTemplate;
	private GroupMapper mapper;

	private static final String CREATE = "INSERT INTO groups (name) VALUES (?)";
	private static final String RETRIEVE = "SELECT * FROM groups WHERE id=?";
	private static final String UPDATE = "UPDATE groups SET name=? WHERE id=?";
	private static final String DELETE = "DELETE FROM groups WHERE id=?";
	private static final String FIND_ALL = "SELECT * FROM groups";
	private static final String FIND_BY_NAME = "SELECT * FROM groups WHERE name=?";
	private static final String ADD_STUDENTS_TO_GROUP = "INSERT INTO groups_students (group_id,student_id) VALUES(?,?)";
	private static final String REMOVE_ALL_STUDENTS_FROM_GROUP = "DELETE FROM groups_students WHERE group_id=?";
	private static final String FIND_ALL_STUDENTS_FOR_GROUP = "SELECT students.id,students.firstname,students.lastname "
			+ "FROM students LEFT JOIN groups_students ON (groups_students.student_id=students.id) "
			+ "WHERE groups_students.group_id=?";

	@Autowired
	public void setMapper(GroupMapper mapper) {
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

	public void create(Group group) {
		jdbcTemplate.update(CREATE, group.getName());
		addStudentsToGroup(group.getListOfStudent(), getId(group));
	}

	public Group retrieve(Integer id) {
		try {
			Group resultGroup = jdbcTemplate.query(RETRIEVE, mapper, id).get(0);
			resultGroup.setListOfStudent(findAllStudentsForGroup(resultGroup.getId()));
			return resultGroup;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Integer getId(Group group) {
		try {
			return jdbcTemplate.query(FIND_BY_NAME, mapper, group.getName()).get(0).getId();
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public void update(Group group) {
		jdbcTemplate.update(UPDATE, group.getName(), group.getId());
		removeAllStudentsFromGroup(group.getId());
		addStudentsToGroup(group.getListOfStudent(), group.getId());
	}

	public void delete(Integer id) {
		jdbcTemplate.update(DELETE, id);
	}

	public void delete(Group group) {
		delete(group.getId());
	}

	public List<Group> findAll() {
		List<Group> resultList = jdbcTemplate.query(FIND_ALL, mapper);
		for (Group group : resultList) {
			List<Student> list = findAllStudentsForGroup(group.getId());
			group.setListOfStudent(list);
		}
		return resultList;
	}

	public void addStudentsToGroup(List<Student> listOfStudents, Integer groupID) {
		listOfStudents.stream()
				.forEach(student -> jdbcTemplate.update(ADD_STUDENTS_TO_GROUP, groupID, student.getId()));
	}

	public void removeAllStudentsFromGroup(Integer groupID) {
		jdbcTemplate.update(REMOVE_ALL_STUDENTS_FROM_GROUP, groupID);

	}

	public List<Student> findAllStudentsForGroup(Integer groupID) {
		return jdbcTemplate.query(FIND_ALL_STUDENTS_FOR_GROUP,
				(rs, rowNum) -> new Student(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname")),
				groupID);

	}
}
