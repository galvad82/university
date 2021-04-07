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

import ua.com.foxminded.galvad.university.dao.impl.mappers.ClassroomMapper;
import ua.com.foxminded.galvad.university.model.Classroom;

class ClassroomDAOTest {

	private DataSource dataSource;
	private ClassroomDAO classroomDAO = new ClassroomDAO();

	@BeforeEach
	void setDataSource() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		classroomDAO.setDataSource(dataSource);
		classroomDAO.setMapper(new ClassroomMapper());
	}

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		classroomDAO.create(new Classroom(3, "TestName"));
		Classroom resultClassroom = classroomDAO.retrieve(3);
		assertEquals(3, resultClassroom.getId());
		assertEquals("TestName", resultClassroom.getName());
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		String actualMessage = "";
		try {
			classroomDAO.create(new Classroom(1, "Test Name"));
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot add a classroom with name=\"Test Name\" to DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Classroom classroom = new Classroom();
		classroom.setName("ROOM-15");
		assertEquals(1, classroomDAO.getId(classroom));
	}

	@Test
	void testGetIdWithNonexistentName_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		Classroom classroom = new Classroom();
		classroom.setName("NONE");
		try {
			classroomDAO.getId(classroom);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A classroom with name=NONE is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Classroom classroom = new Classroom();
		classroom.setName("NONE");
		String actualMessage = "";
		try {
			classroomDAO.getId(classroom);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve an ID for a classroom with name=NONE";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, classroomDAO.retrieve(1).getId());
		assertEquals("ROOM-15", classroomDAO.retrieve(1).getName());
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			classroomDAO.retrieve(1);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a classroom with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionForNonexistentID() {
		String actualMessage = "";
		try {
			classroomDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A classroom with ID=100 is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Classroom classroom = classroomDAO.retrieve(1);
		classroom.setName("New Name");
		classroomDAO.update(classroom);
		Classroom retrievedClassroom = classroomDAO.retrieve(1);
		assertEquals(classroom, retrievedClassroom);
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Classroom classroom = new Classroom();
		classroom.setId(1);
		try {
			classroomDAO.update(classroom);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot update a classroom with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Classroom classroom = new Classroom();
		classroom.setId(100);
		try {
			classroomDAO.update(classroom);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A classroom with ID=100 was not updated";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Classroom classroom = classroomDAO.retrieve(1);
		classroomDAO.delete(classroom);
		int numOfClassroomsFromTestData = classroomDAO.findAll().size();
		assertEquals(1, numOfClassroomsFromTestData);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Classroom classroom = new Classroom();
		classroom.setId(100);
		try {
			classroomDAO.delete(classroom);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A classroom with ID=100 was not deleted";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Classroom classroom = new Classroom();
		classroom.setId(1);
		try {
			classroomDAO.delete(classroom);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot delete a classroom with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {

		classroomDAO.delete(1);
		int numOfClassroomsFromTestData = classroomDAO.findAll().size();
		assertEquals(1, numOfClassroomsFromTestData);
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> classroomDAO.setDataSource(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> classroomDAO.setMapper(null));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Classroom> retrievedList = classroomDAO.findAll();
		List<Classroom> expectedList = new ArrayList<>();
		expectedList.add(new Classroom(1, "ROOM-15"));
		expectedList.add(new Classroom(2, "ROOM-20"));
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			classroomDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a list of classrooms from DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM classrooms";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			classroomDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "None of classrooms was found in DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}
}
