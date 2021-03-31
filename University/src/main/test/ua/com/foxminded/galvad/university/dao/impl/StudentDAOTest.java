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

import ua.com.foxminded.galvad.university.dao.impl.mappers.StudentMapper;
import ua.com.foxminded.galvad.university.model.Student;


class StudentDAOTest {
	private DataSource dataSource;
	private StudentDAO studentDAO = new StudentDAO();
	
	@BeforeEach
	void setDataSource() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
			      .addScript("classpath:schema.sql")
			      .addScript("classpath:test-data.sql")
			      .build();
		studentDAO.setDataSource(dataSource);
		studentDAO.setMapper(new StudentMapper());
	}
	
	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		studentDAO.create(new Student(6,"TestFirstName","TestLastName"));
		Student resultStudent = studentDAO.retrieve(6);
		assertEquals(6, resultStudent.getId());
		assertEquals("TestFirstName", resultStudent.getFirstName());
		assertEquals("TestLastName", resultStudent.getLastName());
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Davidson");
		assertEquals(1,studentDAO.getId(student));
	}
	
	@Test
	void testGetId_shouldReturnNullIfEntityNotFound() {
		Student student = new Student();
		student.setFirstName("None");
		student.setLastName("None");
		assertNull(studentDAO.getId(student));
	}
	
	@Test
	void testRetrieve_shouldReturnCorrectData() {
	    assertEquals(1, studentDAO.retrieve(1).getId());  
	    assertEquals("John", studentDAO.retrieve(1).getFirstName());  
	    assertEquals("Davidson", studentDAO.retrieve(1).getLastName());  
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
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Student student = studentDAO.retrieve(1);
		studentDAO.delete(student);
		int numOfStudentsFromTestData = studentDAO.findAll().size();
		assertEquals(4,numOfStudentsFromTestData);
	}
	
	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		studentDAO.delete(1);
		int numOfStudentsFromTestData = studentDAO.findAll().size();
		assertEquals(4,numOfStudentsFromTestData);
	}
	
	@Test
	void shouldThrowExceptionAsTableDoesNotExist() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query ="DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
		String actualMessage="";
		try {
			studentDAO.create(new Student(1,"TestFirstName","TestLastName"));
		} catch (BadSqlGrammarException e) {
			actualMessage = e.getMessage();
		}
		String expectedMessage = "Table \"STUDENTS\" not found";
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->studentDAO.setDataSource(null));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->studentDAO.setMapper(null));
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
	void testRemoveStudentFromGroups_shouldReturnOneRowBeforeAndZeroRowsAfterRemoving() {
		int studentID=1;
		String query = "SELECT COUNT (group_id) FROM groups_students WHERE student_id=" + studentID;
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int count = jdbcTemplate.queryForObject(query,Integer.class);
		assertEquals(1,count);
		studentDAO.removeStudentFromGroups(studentID);
		int countAfterRemoving = jdbcTemplate.queryForObject(query,Integer.class);
		assertEquals(0,countAfterRemoving);	
	}
	
}
