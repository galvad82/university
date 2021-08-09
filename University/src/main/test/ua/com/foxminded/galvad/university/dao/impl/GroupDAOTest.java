package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	void testCreate_shouldhrowDataAreNotUpdatedException_forAddingStudents() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP TABLE groups_students";
		jdbcTemplate.execute(query);
		
		Group group = new Group(3, "TestName");
		List<Student> listOfStudents = new ArrayList<>();
		listOfStudents.add(new Student(1, "John", "Davidson"));
		group.setListOfStudent(listOfStudents);
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.create(group));
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Group group = new Group(1, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.create(group));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Group group = new Group();
		group.setName("AB-123");
		assertEquals(1, groupDAO.getId(group));
	}

	@Test
	void testGetIdWithNonexistentGroup_shouldThrowDataNotFoundException() {
		Group group = new Group(100, "TestName");
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId(group));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Group group = new Group();
		group.setName("TestName");
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId(group));
	}
	
	@Test
	void testGetIdByString_shouldReturnCorrectIdForEntity() {
		assertEquals(1, groupDAO.getId("AB-123"));
	}
	
	@Test
	void testGetIdByStringWithNonexistentGroup_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId("TestName"));
	}

	@Test
	void testGetIdByString_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId("TestName"));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, groupDAO.retrieve(1).getId());
		assertEquals("AB-123", groupDAO.retrieve(1).getName());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		assertEquals(1, groupDAO.retrieve("AB-123").getId());
		assertEquals("AB-123", groupDAO.retrieve("AB-123").getName());
	}

	@Test
	void testRetrieveByNameWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve("ABCDEF"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve("ABCDEF"));
	}

	@Test
	void testgetGroupForStudent() {
		Student student = new Student(1, "John", "Davidson");
		Group expectedGroup = new Group(1, "AB-123");
		assertEquals(expectedGroup, groupDAO.getGroupForStudent(student));
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
		Group group = new Group(1, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.update(group));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Group group = new Group(100, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.update(group));
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
		Group group = new Group(100, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.delete(group));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Group group = new Group(1, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.delete(group));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		groupDAO.delete(1);
		int numOfGroupsFromTestData = groupDAO.findAll().size();
		assertEquals(1, numOfGroupsFromTestData);
	}
	
	@Test
	void testDeleteByID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.delete(1));
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
		listOfStudents.add(new Student(5, "Mike", "Dombrovsky"));
		listOfStudents.add(new Student(3, "Peter", "Eastwood"));
		listOfStudents.add(new Student(4, "Michael", "Murray"));
		group2.setListOfStudent(listOfStudents);
		expectedList.add(group2);
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void findAllWithoutStudentList_shouldFindTwoEntities() {
		List<Group> retrievedList = groupDAO.findAllWithoutStudentList();
		List<Group> expectedList = new ArrayList<>();
		Group group1 = new Group(1, "AB-123");
		expectedList.add(group1);
		Group group2 = new Group(2, "CD-456");
		expectedList.add(group2);
		assertEquals(expectedList, retrievedList);
	}
	
	@Test
	void testFindAllWithoutStudentList_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.findAllWithoutStudentList());
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.findAll());
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM groups";
		jdbcTemplate.execute(query);
		assertThrows(DataNotFoundException.class, () -> groupDAO.findAll());
	}

	@Test
	void testAddStudentsToGroup() {
		List<Student> listOfStudentsToAdd = new ArrayList<>();
		listOfStudentsToAdd.add(new Student(5, "Mike", "Dombrovsky"));
		listOfStudentsToAdd.add(new Student(3, "Peter", "Eastwood"));
		listOfStudentsToAdd.add(new Student(4, "Michael", "Murray"));
		groupDAO.addStudentsToGroup(listOfStudentsToAdd, 1);
		List<Student> listAfterAdding = groupDAO.findAll().get(0).getListOfStudent();
		assertEquals(5, listAfterAdding.size());
		listOfStudentsToAdd.add(new Student(1, "John", "Davidson"));
		listOfStudentsToAdd.add(new Student(2, "Nick", "Johnson"));
		Collections.sort(listOfStudentsToAdd,
				Comparator.comparing(Student::getLastName).thenComparing(Student::getFirstName));
		assertEquals(listOfStudentsToAdd, listAfterAdding);
	}

	@Test
	void testRemoveAllStudentsFromGroup() {
		List<Student> listBeforeRemoving = groupDAO.findAll().get(0).getListOfStudent();
		assertEquals(2, listBeforeRemoving.size());
		groupDAO.removeAllStudentsFromGroup(1);
		List<Student> listAfterRemoving = groupDAO.findAll().get(0).getListOfStudent();
		assertEquals(0, listAfterRemoving.size());
	}

	@Test
	void testFindAllStudentsForGroup() {
		Group group = new Group(1, "AB-123");
		List<Student> listOfStudents = groupDAO.findAllStudentsForGroup(group);
		assertEquals(2, listOfStudents.size());
		assertEquals("Davidson", listOfStudents.get(0).getLastName());
		assertEquals("Johnson", listOfStudents.get(1).getLastName());
	}
	
	@Test
	void testFindAllStudentsForGroup_shouldThrowDataNotFoundException() {
	JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	String query = "DROP TABLE groups_students";
	jdbcTemplate.execute(query);
	
	Group group = new Group(1, "AB-123");	
	assertThrows(DataNotFoundException.class, () -> groupDAO.findAllStudentsForGroup(group));
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}
}
