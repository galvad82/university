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
import ua.com.foxminded.galvad.university.dao.impl.mappers.LessonMapper;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;

class LessonDAOTest {

	private LessonDAO lessonDAO = new LessonDAO();
	private DataSource dataSource;

	@BeforeEach
	void setDataSource() {

		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		lessonDAO.setDataSource(dataSource);
		lessonDAO.setMapper(new LessonMapper());
	}

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Lesson lesson = new Lesson(3, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		lessonDAO.create(lesson);
		Lesson retrievedLesson = lessonDAO.retrieve(3);
		assertEquals(lesson, retrievedLesson);
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		String actualMessage = "";
		Lesson lesson = new Lesson(new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		try {
			lessonDAO.create(lesson);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot add a lesson to DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Group group = new Group(1);
		Course course = new Course(1);
		Classroom classroom = new Classroom(1);
		Lesson lesson = new Lesson(group, course, classroom, 1616510000000L, 2700000L);
		assertEquals(1, lessonDAO.getId(lesson));
	}

	@Test
	void testGetIdWithNonexistentLesson_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		try {
			lessonDAO.getId(lesson);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A lesson is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		try {
			lessonDAO.getId(lesson);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
			System.out.println(actualMessage);
		}
		String expectedMessage = "Cannot retrieve an ID for a lesson";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, lessonDAO.retrieve(1).getId());
		assertEquals(1, lessonDAO.retrieve(1).getGroup().getId());
		assertEquals(1, lessonDAO.retrieve(1).getCourse().getId());
		assertEquals(1616510000000L, lessonDAO.retrieve(1).getStartTime());
		assertEquals(2700000L, lessonDAO.retrieve(1).getDuration());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		try {
			lessonDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A lesson with ID=100 is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			lessonDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a lesson with ID=100";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Lesson lesson = lessonDAO.retrieve(1);
		lesson.setStartTime(999);
		lessonDAO.update(lesson);
		Lesson retrievedLesson = lessonDAO.retrieve(1);
		;
		assertEquals(lesson, retrievedLesson);
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		try {
			lessonDAO.update(lesson);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot update a lesson with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		try {
			lessonDAO.update(lesson);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A lesson with ID=100 was not updated";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Lesson lesson = lessonDAO.retrieve(1);
		lessonDAO.delete(lesson);
		int numOfLessonsFromTestData = lessonDAO.findAll().size();
		assertEquals(1, numOfLessonsFromTestData);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		try {
			lessonDAO.delete(lesson);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A lesson with ID=100 was not deleted";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		try {
			lessonDAO.delete(lesson);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot delete a lesson with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		lessonDAO.delete(1);
		int numOfLessonsFromTestData = lessonDAO.findAll().size();
		assertEquals(1, numOfLessonsFromTestData);
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> lessonDAO.setDataSource(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> lessonDAO.setMapper(null));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Lesson> retrievedList = lessonDAO.findAll();
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(new Lesson(1, new Group(1), new Course(1), new Classroom(1), 1616510000000L, 2700000L));
		expectedList.add(new Lesson(2, new Group(2), new Course(2), new Classroom(2), 1616510000000L, 3600000L));
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			lessonDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a list of lessons from DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM lessons";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			lessonDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "None of lessons was found in DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}
}
