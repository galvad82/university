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
		Lesson lesson = new Lesson(new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.create(lesson));
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
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataNotFoundException.class, () -> lessonDAO.getId(lesson));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataNotFoundException.class, () -> lessonDAO.getId(lesson));
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
		assertThrows(DataNotFoundException.class, () -> lessonDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> lessonDAO.retrieve(100));
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
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.update(lesson));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.update(lesson));
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
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.delete(lesson));
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.delete(lesson));
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
		expectedList.add(new Lesson(2, new Group(2), new Course(2), new Classroom(2), 1616520000000L, 3600000L));
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> lessonDAO.findAll());
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM lessons";
		jdbcTemplate.execute(query);
		assertThrows(DataNotFoundException.class, () -> lessonDAO.findAll());
	}

	@Test
	void testDeleteByClassroomID() {
		List<Lesson> listBeforeDelete = lessonDAO.findAll();
		assertEquals(2, listBeforeDelete.size());
		assertEquals(1, listBeforeDelete.get(0).getClassroom().getId());
		lessonDAO.deleteByClassroomID(1);
		List<Lesson> listAfterDelete = lessonDAO.findAll();
		assertEquals(1, listAfterDelete.size());
		assertNotEquals(1, listAfterDelete.get(0).getClassroom().getId());
	}
	
	@Test
	void testDeleteByClassroomID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByClassroomID(1));
	}
	
	@Test
	void testDeleteByWrongClassroomID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByClassroomID(100));
	}

	@Test
	void testDeleteByCourseID() {
		List<Lesson> listBeforeDelete = lessonDAO.findAll();
		assertEquals(2, listBeforeDelete.size());
		assertEquals(1, listBeforeDelete.get(0).getCourse().getId());
		lessonDAO.deleteByCourseID(1);
		List<Lesson> listAfterDelete = lessonDAO.findAll();
		assertEquals(1, listAfterDelete.size());
		assertNotEquals(1, listAfterDelete.get(0).getClassroom().getId());
	}

	@Test
	void testDeleteByCourseID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByCourseID(1));
	}
	
	@Test
	void testDeleteByWrongCourseID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByCourseID(100));
	}
	
	@Test
	void testDeleteByGroupID() {
		List<Lesson> listBeforeDelete = lessonDAO.findAll();
		assertEquals(2, listBeforeDelete.size());
		assertEquals(1, listBeforeDelete.get(0).getGroup().getId());
		lessonDAO.deleteByGroupID(1);
		List<Lesson> listAfterDelete = lessonDAO.findAll();
		assertEquals(1, listAfterDelete.size());
		assertNotEquals(1, listAfterDelete.get(0).getClassroom().getId());
	}
	
	@Test
	void testDeleteByGroupID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByGroupID(1));
	}
	
	@Test
	void testDeleteByWrongGroupID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByGroupID(100));
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}
}
