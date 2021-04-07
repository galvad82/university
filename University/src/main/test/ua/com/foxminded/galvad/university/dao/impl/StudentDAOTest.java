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
		String actualMessage = "";
		try {
			studentDAO.create(new Student(1, "FirstName", "LastName"));
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot add a student \"FirstName\" \"LastName\" to DB";
		assertTrue(actualMessage.contains(expectedMessage));
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
		String actualMessage = "";
		Student student = new Student();
		student.setFirstName("NONE");
		student.setLastName("NONE");
		try {
			studentDAO.getId(student);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A student with FirstName=NONE and LastName=NONE is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Student student = new Student();
		student.setFirstName("NONE");
		student.setLastName("NONE");
		try {
			studentDAO.getId(student);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve an ID for a student with FirstName=NONE and LastName=NONE";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, studentDAO.retrieve(1).getId());
		assertEquals("John", studentDAO.retrieve(1).getFirstName());
		assertEquals("Davidson", studentDAO.retrieve(1).getLastName());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		try {
			studentDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A student with ID=100 is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			studentDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a student with ID=100";
		assertTrue(actualMessage.contains(expectedMessage));
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
		String actualMessage = "";
		Student student = new Student();
		student.setId(1);
		try {
			studentDAO.update(student);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot update a student with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Student student = new Student();
		student.setId(100);
		try {
			studentDAO.update(student);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A student with ID=100 was not updated";
		assertTrue(actualMessage.contains(expectedMessage));
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
		String actualMessage = "";
		Student student = new Student();
		student.setId(100);
		try {
			studentDAO.delete(student);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A student with ID=100 was not deleted";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Student student = new Student();
		student.setId(1);
		try {
			studentDAO.delete(student);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot delete a student (ID=1) from groups";
		assertTrue(actualMessage.contains(expectedMessage));
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
		String actualMessage = "";
		try {
			studentDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a list of students from DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM students";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			studentDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "None of students was found in DB";
		assertTrue(actualMessage.contains(expectedMessage));
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
