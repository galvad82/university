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

import ua.com.foxminded.galvad.university.dao.impl.mappers.CourseMapper;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

class CourseDAOTest {

	private CourseDAO courseDAO = new CourseDAO();

	DataSource dataSource;

	@BeforeEach
	void setDataSource() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		courseDAO.setDataSource(dataSource);
		CourseMapper mapper = new CourseMapper();
		courseDAO.setMapper(mapper);
	}

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Course course = new Course(3, "TestName");
		course.setTeacher(new Teacher(1));
		courseDAO.create(course);
		Course resultCourse = courseDAO.retrieve(3);
		assertEquals(course, resultCourse);
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, courseDAO.retrieve(1).getId());
		assertEquals("Science", courseDAO.retrieve(1).getName());
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Course course = courseDAO.retrieve(1);
		course.setName("New Name");
		courseDAO.update(course);
		Course retrievedCourse = courseDAO.retrieve(1);
		assertEquals(course, retrievedCourse);
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Course course = courseDAO.retrieve(1);
		courseDAO.delete(course);
		int numOfCoursesFromTestData = courseDAO.findAll().size();
		assertEquals(1, numOfCoursesFromTestData);
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		courseDAO.delete(1);
		int numOfCoursesFromTestData = courseDAO.findAll().size();
		assertEquals(1, numOfCoursesFromTestData);
	}

	@Test
	void shouldThrowExceptionAsTableDoesNotExist() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			Course course = new Course(1, "Test Name");
			course.setTeacher(new Teacher(1));
			courseDAO.create(course);
		} catch (BadSqlGrammarException e) {
			actualMessage = e.getMessage();
		}
		String expectedMessage = "Table \"COURSES\" not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> courseDAO.setDataSource(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, ()->courseDAO.setMapper(null));
	}
	
	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Course> retrievedList = courseDAO.findAll();
		List<Course> expectedList = new ArrayList<>();
		Course course = new Course(1, "Science");
		course.setTeacher(new Teacher(1));
		expectedList.add(course);
		course = new Course(2, "Math");
		course.setTeacher(new Teacher(2));
		expectedList.add(course);
		assertEquals(expectedList, retrievedList);
	}
}
