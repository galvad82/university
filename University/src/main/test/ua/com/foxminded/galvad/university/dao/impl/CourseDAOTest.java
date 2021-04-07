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
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		String actualMessage = "";
		Course course = new Course(1, "TestName");
		course.setTeacher(new Teacher(1));
		try {
			courseDAO.create(course);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot add a course (name=TestName) to DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Course course = new Course();
		course.setName("Science");
		course.setTeacher(new Teacher(1));
		assertEquals(1, courseDAO.getId(course));
	}

	@Test
	void testGetIdWithNonexistentCourse_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		Course course = new Course();
		course.setName("NONE");
		course.setTeacher(new Teacher(999));
		try {
			courseDAO.getId(course);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A course (name=NONE, teacherID=999) is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Course course = new Course();
		course.setName("NONE");
		course.setTeacher(new Teacher(999));
		try {
			courseDAO.getId(course);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve an ID for a course (name=NONE, teacherID=999)";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		assertEquals(1, courseDAO.retrieve(1).getId());
		assertEquals("Science", courseDAO.retrieve(1).getName());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		String actualMessage = "";
		try {
			courseDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A course with ID=100 is not found";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			courseDAO.retrieve(100);
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a course with ID=100";
		assertTrue(actualMessage.contains(expectedMessage));
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
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Course course = new Course(1, "None");
		course.setTeacher(new Teacher(999));
		try {
			courseDAO.update(course);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot update a course with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Course course = new Course(100, "None");
		course.setTeacher(new Teacher(999));
		try {
			courseDAO.update(course);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A course with ID=100 was not updated";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		Course course = courseDAO.retrieve(1);
		courseDAO.delete(course);
		int numOfCoursesFromTestData = courseDAO.findAll().size();
		assertEquals(1, numOfCoursesFromTestData);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		String actualMessage = "";
		Course course = new Course(100, "None");
		try {
			courseDAO.delete(course);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "A course with ID=100 was not deleted";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		Course course = new Course(1, "None");
		try {
			courseDAO.delete(course);
		} catch (DataAreNotUpdatedException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot delete a course with ID=1";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		courseDAO.delete(1);
		int numOfCoursesFromTestData = courseDAO.findAll().size();
		assertEquals(1, numOfCoursesFromTestData);
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenDatasourceIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> courseDAO.setDataSource(null));
	}

	@Test
	void shouldThrowIllegalArgumentExceptionWhenMapperIsNull() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> courseDAO.setMapper(null));
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

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		String actualMessage = "";
		try {
			courseDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "Cannot retrieve a list of courses from DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DELETE FROM courses";
		jdbcTemplate.execute(query);
		String actualMessage = "";
		try {
			courseDAO.findAll();
		} catch (DataNotFoundException e) {
			actualMessage = e.getErrorMessage();
		}
		String expectedMessage = "None of courses was found in DB";
		assertTrue(actualMessage.contains(expectedMessage));
	}

	private void dropDB() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String query = "DROP ALL OBJECTS";
		jdbcTemplate.execute(query);
	}
}
