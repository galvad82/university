package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@DataJpaTest
@ComponentScan("ua.com.foxminded.galvad.university.dao")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class CourseDAOTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private CourseDAO courseDAO = new CourseDAO();

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Course course = createEntity("TestName");
		Course resultCourse = courseDAO.retrieve(1);
		assertEquals(course, resultCourse);
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Course course = new Course(1, "TestName");
		course.setTeacher(new Teacher(1));
		assertThrows(DataAreNotUpdatedException.class, () -> courseDAO.create(course));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Course course = createEntity("TestName");
		assertEquals(1, courseDAO.getId(course));
	}

	@Test
	void testGetIdWithNonexistentCourse_shouldThrowDataNotFoundException() {
		Course course = new Course();
		course.setName("NONE");
		course.setTeacher(new Teacher(999));
		assertThrows(DataNotFoundException.class, () -> courseDAO.getId(course));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Course course = new Course();
		course.setName("NONE");
		course.setTeacher(new Teacher(999));
		assertThrows(DataNotFoundException.class, () -> courseDAO.getId(course));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		Course course = createEntity("TestName");
		assertEquals(course, courseDAO.retrieve(1));
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundExceptionForNonExistedID() {
		assertThrows(DataNotFoundException.class, () -> courseDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> courseDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		Course course = createEntity("TestName");
		assertEquals(course, courseDAO.retrieve("TestName"));
	}

	@Test
	void testRetrieveByNameWithNonexistentName_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> courseDAO.retrieve("ABCDEF"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> courseDAO.retrieve("ABCDEF"));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Course initialCourse = createEntity("TestName");
		Course courseBeforeUpdate = courseDAO.retrieve(1);
		assertEquals(initialCourse, courseBeforeUpdate);
		courseBeforeUpdate.setName("NEW Name");
		Teacher newTeacher = new Teacher();
		newTeacher.setFirstName("NewFirstName");
		newTeacher.setLastName("NewLastName");
		entityManager.persist(newTeacher);
		courseBeforeUpdate.setTeacher(newTeacher);
		courseDAO.update(courseBeforeUpdate);
		Course courseAfterUpdate = courseDAO.retrieve(1);
		assertEquals(courseBeforeUpdate, courseAfterUpdate);
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Course course = new Course(1, "None");
		course.setTeacher(new Teacher(999));
		assertThrows(DataAreNotUpdatedException.class, () -> courseDAO.update(course));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Course course = new Course(100, "None");
		course.setTeacher(new Teacher(999));
		assertThrows(DataAreNotUpdatedException.class, () -> courseDAO.update(course));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestName");
		Course course2 = createEntity("TestName2");
		int numOfCoursesBeforeDelete = courseDAO.findAll().size();
		assertEquals(2, numOfCoursesBeforeDelete);
		courseDAO.delete(course2);
		int numOfCoursesAfterDelete = courseDAO.findAll().size();
		assertEquals(1, numOfCoursesBeforeDelete - numOfCoursesAfterDelete);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Course course = new Course(100, "None");
		assertThrows(DataAreNotUpdatedException.class, () -> courseDAO.delete(course));
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Course course = new Course(1, "None");
		assertThrows(DataAreNotUpdatedException.class, () -> courseDAO.delete(course));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestName");
		createEntity("TestName2");
		int numOfCoursesBeforeDelete = courseDAO.findAll().size();
		assertEquals(2, numOfCoursesBeforeDelete);
		courseDAO.delete(2);
		int numOfCoursesAfterDelete = courseDAO.findAll().size();
		assertEquals(1, numOfCoursesBeforeDelete - numOfCoursesAfterDelete);
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		Course course1 = createEntity("TestName");
		Course course2 = createEntity("TestName2");
		List<Course> retrievedList = courseDAO.findAll();
		List<Course> expectedList = new ArrayList<>();
		expectedList.add(course1);
		expectedList.add(course2);
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> courseDAO.findAll());
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		Course course1 = createEntity("TestName");
		Course course2 = createEntity("TestName2");
		List<Course> retrievedList = courseDAO.findAll();
		List<Course> expectedList = new ArrayList<>();
		expectedList.add(course1);
		expectedList.add(course2);
		assertEquals(expectedList, retrievedList);
		entityManager.createNativeQuery("DELETE FROM courses").executeUpdate();
		expectedList = new ArrayList<>();
		retrievedList = courseDAO.findAll();
		assertEquals(expectedList, retrievedList);
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}

	private Course createEntity(String name) {
		Course entity = new Course();
		entity.setName(name);
		Teacher teacher = new Teacher();
		teacher.setFirstName("John");
		teacher.setLastName("Doe");
		entityManager.persist(teacher);
		entity.setTeacher(teacher);
		courseDAO.create(entity);
		return entity;
	}
}
