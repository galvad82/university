package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import ua.com.foxminded.galvad.university.dao.impl.mappers.GroupMapper;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

class GroupDAOTest {

	private GroupDAO groupDAO = new GroupDAO();

	DataSource dataSource;

	@BeforeEach
	void setDataSource() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		groupDAO.setDataSource(dataSource);
		groupDAO.setMapper(new GroupMapper());
	}

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Group group = new Group(3, "TestName");
		List<Student> listOfStudents = new ArrayList<>();
		listOfStudents.add(new Student(1, "John", "Davidson"));
		group.setListOfStudent(listOfStudents);
		groupDAO.create(group);
		Group resultGroup = groupDAO.retrieve(3);
		assertEquals(group, resultGroup);
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, groupDAO.retrieve(1).getId());
		assertEquals("AB-123", groupDAO.retrieve(1).getName());
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Group group = groupDAO.retrieve(1);
		group.setName("New Name");
		groupDAO.update(group);
		Group retrievedGroup = groupDAO.retrieve(1);
		assertEquals(group, retrievedGroup);
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Group group = groupDAO.retrieve(1);
		groupDAO.delete(group);
		int numOfGroupsFromTestData = groupDAO.findAll().size();
		assertEquals(1, numOfGroupsFromTestData);
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		groupDAO.delete(1);
		int numOfGroupsFromTestData = groupDAO.findAll().size();
		assertEquals(1, numOfGroupsFromTestData);
	}

	@Test
	void shouldThrowExceptionAsTableDoesNotExist() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			Group group = new Group(1, "Test Name");
			List<Student> listOfStudents = new ArrayList<>();
			listOfStudents.add(new Student(1));
			group.setListOfStudent(listOfStudents);
			groupDAO.create(group);
		} catch (BadSqlGrammarException e) {
			actualMessage = e.getMessage();
		}
		String expectedMessage = "Table \"GROUPS\" not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> groupDAO.setDataSource(null));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->groupDAO.setMapper(null));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Group> retrievedList = groupDAO.findAll();
		List<Group> expectedList = new ArrayList<>();
		Group group1 = new Group(1, "AB-123");
		List<Student> listOfStudents = new ArrayList<>();
		listOfStudents.add(new Student(1, "John", "Davidson"));
		listOfStudents.add(new Student(2, "Nick", "Johnson"));
		group1.setListOfStudent(listOfStudents);
		expectedList.add(group1);
		Group group2 = new Group(2, "CD-456");
		listOfStudents = new ArrayList<>();
		listOfStudents.add(new Student(3, "Peter", "Eastwood"));
		listOfStudents.add(new Student(4, "Michael", "Murray"));
		listOfStudents.add(new Student(5, "Mike", "Dombrovsky"));
		group2.setListOfStudent(listOfStudents);
		expectedList.add(group2);
		assertEquals(expectedList, retrievedList);
	}
}
