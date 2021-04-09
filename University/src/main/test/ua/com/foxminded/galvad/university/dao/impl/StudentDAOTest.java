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
		studentDAO.create(new Student(6, "TestFirstName", "TestLastName"));
		Student resultStudent = studentDAO.retrieve(6);
		assertEquals(6, resultStudent.getId());
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
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Student student = studentDAO.retrieve(1);
		studentDAO.delete(student);
		int numOfStudentsFromTestData = studentDAO.findAll().size();
		assertEquals(4, numOfStudentsFromTestData);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Student student = new Student();
		student.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(student));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(student));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		studentDAO.delete(1);
		int numOfStudentsFromTestData = studentDAO.findAll().size();
		assertEquals(4, numOfStudentsFromTestData);
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
	void testFindAll_shouldFindFiveEntities() {
		List<Student> retrievedList = studentDAO.findAll();
		List<Student> expectedList = new ArrayList<>();
		expectedList.add(new Student(1, "John", "Davidson"));
		expectedList.add(new Student(2, "Nick", "Johnson"));
		expectedList.add(new Student(3, "Peter", "Eastwood"));
		expectedList.add(new Student(4, "Michael", "Murray"));
		expectedList.add(new Student(5, "Mike", "Dombrovsky"));
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
	void testRemoveStudentFromGroups_shouldReturnOneRowBeforeAndZeroRowsAfterRemoving() {
		int studentID = 1;
		String query = "SELECT COUNT (group_id) FROM groups_students WHERE student_id=" + studentID;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int count = jdbcTemplate.queryForObject(query, Integer.class);
		assertEquals(1, count);
		studentDAO.removeStudentFromGroups(studentID);
		int countAfterRemoving = jdbcTemplate.queryForObject(query, Integer.class);
		assertEquals(0, countAfterRemoving);
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}

}
