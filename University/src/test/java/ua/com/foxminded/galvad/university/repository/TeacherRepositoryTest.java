package ua.com.foxminded.galvad.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TeacherRepositoryTest {

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void testSave_shouldAddNewRowToDatabaseAndRetrieveIt() {
		assertEquals(0, teacherRepository.findAll().size());
		Teacher expectedEntity = createEntity("TestFirstName", "TestLastName");
		Teacher retrievedEntity = teacherRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertEquals(expectedEntity, retrievedEntity);
		assertEquals(1, teacherRepository.findAll().size());
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfEntityIsNull() {
		assertThrows(DataAccessException.class, () -> teacherRepository.save(null));
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfDBDropped() {
		dropDB();
		Teacher entity = new Teacher(1, "TestFirstName", "TestLastName");
		assertThrows(DataAccessException.class, () -> teacherRepository.save(entity));
	}

	@Test
	void testFindByName_shouldReturnCorrectDataForExistingEntity() {
		Teacher expectedEntity = createEntity("TestFirstName", "TestLastName");
		Teacher retrievedEntity = teacherRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertEquals(expectedEntity, retrievedEntity);
	}

	@Test
	void testFindByNameWithNonexistentName_shouldReturnNull() {
		assertNull(teacherRepository.findByFirstNameAndLastName("NONE", "NONE"));
	}

	@Test
	void testFindByName_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class,
				() -> teacherRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName"));
	}

	@Test
	void testDelete_shouldThrowDataIntegrityViolationExceptionIfCourseIsSetToTeacher() {
		Teacher expectedEntity = createEntity("TestFirstName", "TestLastName");
		Course course = new Course();
		course.setName("Math");
		course.setTeacher(expectedEntity);
		entityManager.persist(course);
		assertTrue(teacherRepository.findAll().contains(expectedEntity));
		teacherRepository.delete(expectedEntity);
		assertThrows(DataIntegrityViolationException.class, () -> teacherRepository.flush());
	}

	@Test
	void testDelete_shouldDeleteTeacherIfThereIsNoCourseConnected() {
		Teacher entity = createEntity("TestFirstName", "TestLastName");
		Course course = new Course();
		course.setName("Math");
		course.setTeacher(entity);
		entityManager.persist(course);

		Course retrievedCourse = entityManager.find(Course.class, 1);
		assertEquals(course.getName(), retrievedCourse.getName());
		assertEquals(course.getTeacher(), retrievedCourse.getTeacher());
		retrievedCourse.setTeacher(null);
		entityManager.merge(retrievedCourse);
		retrievedCourse = entityManager.find(Course.class, 1);
		assertEquals(course.getName(), retrievedCourse.getName());
		assertNull(retrievedCourse.getTeacher());

		assertTrue(teacherRepository.findAll().contains(entity));
		teacherRepository.delete(entity);
		assertFalse(teacherRepository.findAll().contains(entity));
	}

	@Test
	void testDelete_shouldThrowExceptionIfDeleteNull() {
		assertThrows(DataAccessException.class, () -> teacherRepository.delete(null));
	}

	@Test
	void testDelete_shouldThrowExceptionAfterDropDB() {
		dropDB();
		Teacher entity = new Teacher(1, "TestFirstName", "TestLastName");
		assertThrows(DataAccessException.class, () -> teacherRepository.delete(entity));
	}

	@Test
	void testFindAll_ShouldReturnListWithTwoEntities() {
		assertEquals(0, teacherRepository.findAll().size());
		Teacher expectedEntity1 = createEntity("TestFirstNameA", "TestLastNameA");
		Teacher expectedEntity2 = createEntity("TestFirstNameB", "TestLastNameB");
		assertEquals(2, teacherRepository.findAll().size());
		assertEquals(expectedEntity1, teacherRepository.findAll().get(0));
		assertEquals(expectedEntity2, teacherRepository.findAll().get(1));
	}

	@Test
	void testFindAll_shouldThrowExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> teacherRepository.findAll());
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}

	private Teacher createEntity(String firstName, String lastName) {
		Teacher entity = new Teacher();
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		teacherRepository.save(entity);
		return entity;
	}
}
