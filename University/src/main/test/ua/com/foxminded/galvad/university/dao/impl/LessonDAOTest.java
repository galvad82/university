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
		Lesson lesson = new Lesson(3,new Group(1),new Course(1),new Classroom(1),161651000L, 3600000L);
		lessonDAO.create(lesson);
		Lesson retrievedLesson = lessonDAO.retrieve(3);
		assertEquals(lesson, retrievedLesson);
	}
	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Group group = new Group(1);
		Course course = new Course(1);
		Classroom classroom = new Classroom(1);
		Lesson lesson = new Lesson(group,course,classroom,1616510000000L,2700000L);
		assertEquals(1, lessonDAO.getId(lesson));		
	}
	
	@Test
	void testGetId_shouldReturnNullIfEntityNotFound() {
		Group group = new Group(5);
		Course course = new Course(5);
		Classroom classroom = new Classroom(5);
		Lesson lesson = new Lesson(group,course,classroom,0L,0L);
		assertNull(lessonDAO.getId(lesson));
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
	void testUpdate_shouldReturnUpdatedEntity() {
		Lesson lesson = lessonDAO.retrieve(1);
		lesson.setStartTime(999);
		lessonDAO.update(lesson);
		Lesson retrievedLesson = lessonDAO.retrieve(1);;
		assertEquals(lesson, retrievedLesson);  
	}
	
	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Lesson lesson = lessonDAO.retrieve(1);
		lessonDAO.delete(lesson);
		int numOfLessonsFromTestData = lessonDAO.findAll().size();
		assertEquals(1,numOfLessonsFromTestData);
	}
	
	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		lessonDAO.delete(1);
		int numOfLessonsFromTestData = lessonDAO.findAll().size();
		assertEquals(1,numOfLessonsFromTestData);
	}
	
	@Test
	void shouldThrowExceptionAsTableDoesNotExist() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query ="DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
		String actualMessage="";
		try {
			lessonDAO.create(new Lesson(3,new Group(1),new Course(1),new Classroom(1),161651000L, 3600000L));
		} catch (BadSqlGrammarException e) {
			actualMessage = e.getMessage();
		}
		String expectedMessage = "Table \"LESSONS\" not found";
	    assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->lessonDAO.setDataSource(null));
	}
	
	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->lessonDAO.setMapper(null));
	}
	
	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Lesson> retrievedList = lessonDAO.findAll();
		List<Lesson> expectedList = new ArrayList<>(); 
		expectedList.add(new Lesson(1,new Group(1),new Course(1),new Classroom(1),1616510000000L, 2700000L));
		expectedList.add(new Lesson(2,new Group(2),new Course(2),new Classroom(2),1616510000000L, 3600000L));
		assertEquals(expectedList, retrievedList);
	}
	
}
