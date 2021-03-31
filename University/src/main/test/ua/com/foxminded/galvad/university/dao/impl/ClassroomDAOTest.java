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
	void testGetId_shouldReturnCorrectIdForEntity() {
		Classroom classroom = new Classroom();
		classroom.setName("ROOM-15");
		assertEquals(1,classroomDAO.getId(classroom));
	}
	
	@Test 
	void testGetId_shouldReturnNullIfEntityNotFound() {
		Classroom classroom = new Classroom();
		classroom.setName("NONE");
		assertNull(classroomDAO.getId(classroom));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, classroomDAO.retrieve(1).getId());
		assertEquals("ROOM-15", classroomDAO.retrieve(1).getName());
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
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Classroom classroom = classroomDAO.retrieve(1);
		classroomDAO.delete(classroom);
		int numOfClassroomsFromTestData = classroomDAO.findAll().size();
		assertEquals(1, numOfClassroomsFromTestData);
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {

		classroomDAO.delete(1);
		int numOfClassroomsFromTestData = classroomDAO.findAll().size();
		assertEquals(1, numOfClassroomsFromTestData);
	}

	@Test
	void shouldThrowExceptionAsTableDoesNotExist() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			classroomDAO.create(new Classroom(1, "Test Name"));
		} catch (BadSqlGrammarException e) {
			actualMessage = e.getMessage();
		}
		String expectedMessage = "Table \"CLASSROOMS\" not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> classroomDAO.setDataSource(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->classroomDAO.setMapper(null));
	}
	
	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Classroom> retrievedList = classroomDAO.findAll();
		List<Classroom> expectedList = new ArrayList<>();
		expectedList.add(new Classroom(1, "ROOM-15"));
		expectedList.add(new Classroom(2, "ROOM-20"));
		assertEquals(expectedList, retrievedList);
	}
}
