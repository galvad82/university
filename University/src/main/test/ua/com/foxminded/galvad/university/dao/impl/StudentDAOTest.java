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

import ua.com.foxminded.galvad.university.dao.impl.mappers.StudentMapper;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

class StudentDAOTest {
	private DataSource dataSource;
	private StudentDAO studentDAO = new StudentDAO();

	@BeforeEach
	void setDataSource() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		studentDAO.setDataSource(dataSource);
		studentDAO.setMapper(new StudentMapper());
	}

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		studentDAO.create(new Student(7, "TestFirstName", "TestLastName"));
		Student resultStudent = studentDAO.retrieve(7);
		assertEquals(7, resultStudent.getId());
		assertEquals("TestFirstName", resultStudent.getFirstName());
		assertEquals("TestLastName", resultStudent.getLastName());
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Student student = new Student(1, "FirstName", "LastName");
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.create(student));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, studentDAO.retrieve(1).getId());
		assertEquals("John", studentDAO.retrieve(1).getFirstName());
		assertEquals("Davidson", studentDAO.retrieve(1).getLastName());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		assertEquals(1, studentDAO.retrieve("John", "Davidson").getId());
		assertEquals("John", studentDAO.retrieve("John", "Davidson").getFirstName());
		assertEquals("Davidson", studentDAO.retrieve("John", "Davidson").getLastName());
	}

	@Test
	void testRetrieveByNameWithNonexistentName_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve("NONE", "NONE"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve("NONE", "NONE"));
	}

	@Test
	void testGetGroupId_shouldReturnCorrectIdForEntity() {
		assertEquals(1, studentDAO.getGroupId(new Student(1, "John", "Davidson")));
	}

	@Test
	void testGetGroupIdWithNonexistentStudent_shouldReturnZero() {
		assertEquals(0, studentDAO.getGroupId(new Student(10, "NONE", "NONE")));
	}

	@Test
	void testGetGroupId__shouldReturnZero() {
		dropDB();
		assertEquals(0, studentDAO.getGroupId(new Student(10, "NONE", "NONE")));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Davidson");
		assertEquals(1, studentDAO.getId(student));
	}

	@Test
	void testGetIdWithNonexistentName_shouldThrowDataNotFoundException() {
		Student student = new Student();
		student.setFirstName("NONE");
		student.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> studentDAO.getId(student));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setFirstName("NONE");
		student.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> studentDAO.getId(student));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Student student = studentDAO.retrieve(1);
		student.setFirstName("First Name");
		student.setLastName("Last Name");
		studentDAO.update(student);
		Student retrievedStudent = studentDAO.retrieve(1);
		assertEquals(student, retrievedStudent);
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.update(student));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Student student = new Student();
		student.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.update(student));
	}

	@Test
	void testUpdateGroupForStudent_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setId(100);
		Group group = new Group();
		group.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.updateGroupForStudent(student, group));
	}

	@Test
	void testUpdateGroupForStudent_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Student student = new Student();
		student.setId(100);
		Group group = new Group();
		group.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.updateGroupForStudent(student, group));
	}

	@Test
	void testUpdateGroupForStudent_shouldReturnUpdatedEntity() {
		Student student = studentDAO.retrieve(1);
		assertEquals(1, studentDAO.getGroupId(student));
		Group group = new Group();
		group.setId(2);
		studentDAO.updateGroupForStudent(student, group);
		assertEquals(2, studentDAO.getGroupId(student));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Student student = studentDAO.retrieve(1);
		studentDAO.delete(student);
		int numOfStudentsFromTestData = studentDAO.findAll().size();
		assertEquals(5, numOfStudentsFromTestData);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Student student = new Student();
		student.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(student));
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(student));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		studentDAO.delete(1);
		int numOfStudentsFromTestData = studentDAO.findAll().size();
		assertEquals(5, numOfStudentsFromTestData);
	}

	@Test
	void testDeleteByID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(1));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> studentDAO.setDataSource(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> studentDAO.setMapper(null));
	}

	@Test
	void testFindAll_shouldFindSixSortedByLastNameEntities() {
		List<Student> retrievedList = studentDAO.findAll();
		List<Student> expectedList = new ArrayList<>();
		expectedList.add(new Student(1, "John", "Davidson"));
		expectedList.add(new Student(5, "Mike", "Dombrovsky"));
		expectedList.add(new Student(3, "Peter", "Eastwood"));
		expectedList.add(new Student(2, "Nick", "Johnson"));
		expectedList.add(new Student(4, "Michael", "Murray"));
		expectedList.add(new Student(6, "Student", "WithoutGroup"));
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> studentDAO.findAll());
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM students";
		jdbcTemplate.execute(query);
		assertThrows(DataNotFoundException.class, () -> studentDAO.findAll());
	}

	@Test
	void testaddStudentToGroup_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Student student = new Student(1, "John", "Davidson");
		Group group = new Group(1, "GroupName");
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.addStudentToGroup(student, group));
	}

	@Test
	void testRemoveStudentFromGroups_shouldReturnOneRowBeforeAndZeroRowsAfterRemoving() {
		Student student = new Student(1, "John", "Davidson");
		String query = "SELECT COUNT (group_id) FROM groups_students WHERE student_id=1";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int count = jdbcTemplate.queryForObject(query, Integer.class);
		assertEquals(1, count);
		studentDAO.removeStudentFromGroups(student);
		int countAfterRemoving = jdbcTemplate.queryForObject(query, Integer.class);
		assertEquals(0, countAfterRemoving);
	}

	@Test
	void shouldGetCorrectGroupIDForStudent() {
		assertEquals(1, studentDAO.getGroupId(studentDAO.retrieve(1)));
	}

	@Test
	void shouldReturnZeroAsGroupNumberForUnassignedStudent() {
		Student student = studentDAO.retrieve(6);
		assertEquals(0, studentDAO.getGroupId(student));
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}

}
