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

import ua.com.foxminded.galvad.university.dao.impl.mappers.TeacherMapper;
import ua.com.foxminded.galvad.university.model.Teacher;

class TeacherDAOTest {

	private DataSource dataSource;
	private TeacherDAO teacherDAO = new TeacherDAO();

	@BeforeEach
	void setDataSource() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		teacherDAO.setDataSource(dataSource);
		teacherDAO.setMapper(new TeacherMapper());
	}

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		teacherDAO.create(new Teacher(3, "TestFirstName", "TestLastName"));
		Teacher resultTeacher = teacherDAO.retrieve(3);
		assertEquals(3, resultTeacher.getId());
		assertEquals("TestFirstName", resultTeacher.getFirstName());
		assertEquals("TestLastName", resultTeacher.getLastName());
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Teacher teacher = new Teacher(1, "FirstName", "LastName");
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.create(teacher));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("Jennie");
		teacher.setLastName("Crigler");
		assertEquals(1, teacherDAO.getId(teacher));
	}

	@Test
	void testGetIdWithNonexistentName_shouldThrowDataNotFoundException() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("NONE");
		teacher.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> teacherDAO.getId(teacher));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Teacher teacher = new Teacher();
		teacher.setFirstName("NONE");
		teacher.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> teacherDAO.getId(teacher));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, teacherDAO.retrieve(1).getId());
		assertEquals("Jennie", teacherDAO.retrieve(1).getFirstName());
		assertEquals("Crigler", teacherDAO.retrieve(1).getLastName());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		assertEquals(1, teacherDAO.retrieve("Jennie", "Crigler").getId());
		assertEquals("Jennie", teacherDAO.retrieve("Jennie", "Crigler").getFirstName());
		assertEquals("Crigler", teacherDAO.retrieve("Jennie", "Crigler").getLastName());
	}

	@Test
	void testRetrieveByNameWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve("NONE", "NONE"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve("NONE", "NONE"));
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
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.update(teacher));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Teacher teacher = new Teacher();
		teacher.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.update(teacher));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Teacher teacher = teacherDAO.retrieve(1);
		teacherDAO.delete(teacher);
		int numOfTeachersFromTestData = teacherDAO.findAll().size();
		assertEquals(1, numOfTeachersFromTestData);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Teacher teacher = new Teacher();
		teacher.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.delete(teacher));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.delete(teacher));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		teacherDAO.delete(1);
		int numOfTeachersFromTestData = teacherDAO.findAll().size();
		assertEquals(1, numOfTeachersFromTestData);
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> teacherDAO.setMapper(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> teacherDAO.setDataSource(null));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Teacher> retrievedList = teacherDAO.findAll();
		List<Teacher> expectedList = new ArrayList<>();
		expectedList.add(new Teacher(1, "Jennie", "Crigler"));
		expectedList.add(new Teacher(2, "Gladys", "Swon"));
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> teacherDAO.findAll());
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM teachers";
		jdbcTemplate.execute(query);
		assertThrows(DataNotFoundException.class, () -> teacherDAO.findAll());
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}
}
