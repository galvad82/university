package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		String actualMessage = "";
		Group group = new Group(1, "TestName");
		try {
			groupDAO.create(group);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot add a group (name=TestName) to DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Group group = new Group();
		group.setName("AB-123");
		assertEquals(1, groupDAO.getId(group));
	}

	@Test
	void testGetIdWithNonexistentGroup_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		Group group = new Group(100, "TestName");
		try {
			groupDAO.getId(group);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A group with name=TestName is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Group group = new Group();
		group.setName("TestName");
		try {
			groupDAO.getId(group);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve an ID for a group with name=TestName";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, groupDAO.retrieve(1).getId());
		assertEquals("AB-123", groupDAO.retrieve(1).getName());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		try {
			groupDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A group with ID=100 is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			groupDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a group with ID=100";
		assertTrue(actualMessage.contains(expectedMessage));
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
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Group group = new Group(1, "TestName");
		try {
			groupDAO.update(group);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot update a group with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Group group = new Group(100, "TestName");
		try {
			groupDAO.update(group);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A group with ID=100 was not updated";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Group group = groupDAO.retrieve(1);
		groupDAO.delete(group);
		int numOfGroupsFromTestData = groupDAO.findAll().size();
		assertEquals(1, numOfGroupsFromTestData);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Group group = new Group(100, "TestName");
		try {
			groupDAO.delete(group);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A group with ID=100 was not deleted";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Group group = new Group(1, "TestName");
		try {
			groupDAO.delete(group);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot delete a group with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		groupDAO.delete(1);
		int numOfGroupsFromTestData = groupDAO.findAll().size();
		assertEquals(1, numOfGroupsFromTestData);
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> groupDAO.setDataSource(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> groupDAO.setMapper(null));
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

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			groupDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a list of groups from DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM groups";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			groupDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "None of groups was found in DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}
}
