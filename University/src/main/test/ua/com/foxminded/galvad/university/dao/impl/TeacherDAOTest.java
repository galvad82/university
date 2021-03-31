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

import ua.com.foxminded.galvad.university.dao.impl.mappers.TeacherMapper;
import ua.com.foxminded.galvad.university.model.Teacher;

class TeacherDAOTest {

	private DataSource dataSource;
	private TeacherDAO teacherDAO = new TeacherDAO();
	
	
	@BeforeEach
	void setDataSource() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
			      .addScript("classpath:schema.sql")
			      .addScript("classpath:test-data.sql")
			      .build();
		teacherDAO.setDataSource(dataSource);
		teacherDAO.setMapper(new TeacherMapper());
	}
	
	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		teacherDAO.create(new Teacher(3,"TestFirstName","TestLastName"));
		Teacher resultTeacher = teacherDAO.retrieve(3);
		assertEquals(3, resultTeacher.getId());
		assertEquals("TestFirstName", resultTeacher.getFirstName());
		assertEquals("TestLastName", resultTeacher.getLastName());
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Jennie");
		teacher.setLastName("Crigler");
		assertEquals(1,teacherDAO.getId(teacher));
	}
	
	@Test
	void testGetId_shouldReturnNullIfEntityNotFound() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("None");
		teacher.setLastName("None");
		assertNull(teacherDAO.getId(teacher));	
	}
	
	@Test
	void testRetrieve_shouldReturnCorrectData() {
	    assertEquals(1, teacherDAO.retrieve(1).getId());  
	    assertEquals("Jennie", teacherDAO.retrieve(1).getFirstName());  
	    assertEquals("Crigler", teacherDAO.retrieve(1).getLastName());  
	}
	
	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Teacher teacher = teacherDAO.retrieve(1);
		teacher.setFirstName("First Name");
		teacher.setLastName("Last Name");
		teacherDAO.update(teacher);
		Teacher retrievedTeacher = teacherDAO.retrieve(1);
		assertEquals(teacher, retrievedTeacher);  
	}
	
	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Teacher teacher = teacherDAO.retrieve(1);
		teacherDAO.delete(teacher);
		int numOfTeachersFromTestData = teacherDAO.findAll().size();
		assertEquals(1,numOfTeachersFromTestData);
	}
	
	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		teacherDAO.delete(1);
		int numOfTeachersFromTestData = teacherDAO.findAll().size();
		assertEquals(1,numOfTeachersFromTestData);
	}
	
	@Test
	void shouldThrowExceptionAsTableDoesNotExist() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query ="DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
		String actualMessage="";
		try {
			teacherDAO.create(new Teacher(1,"TestFirstName","TestLastName"));
		} catch (BadSqlGrammarException e) {
			actualMessage = e.getMessage();
		}
		String expectedMessage = "Table \"TEACHERS\" not found";
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->teacherDAO.setMapper(null));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->teacherDAO.setDataSource(null));
	}
	
	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Teacher> retrievedList = teacherDAO.findAll();
		List<Teacher> expectedList = new ArrayList<>(); 
		expectedList.add(new Teacher(1, "Jennie", "Crigler"));
		expectedList.add(new Teacher(2, "Gladys", "Swon"));
		assertEquals(expectedList, retrievedList);
	}
}
