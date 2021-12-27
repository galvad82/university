package ua.com.foxminded.galvad.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class CourseRepositoryTest {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void testSave_shouldAddNewRowToDatabaseAndRetrieveIt() {
		assertEquals(0, courseRepository.findAll().size());
		Course expectedEntity = createEntity("TestName");
		Course retrievedEntity = courseRepository.findByName("TestName");
		assertEquals(expectedEntity, retrievedEntity);
		assertEquals(1, courseRepository.findAll().size());
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfEntityIsNull() {
		assertThrows(DataAccessException.class, () -> courseRepository.save(null));
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfDBDropped() {
		dropDB();
		Course entity = new Course(1, "Test");
		assertThrows(DataAccessException.class, () -> courseRepository.save(entity));
	}

	@Test
	void testFindByName_shouldReturnCorrectDataForExistingEntity() {
		Course expectedEntity = createEntity("TestName");
		Course retrievedEntity = courseRepository.findByName("TestName");
		assertEquals(expectedEntity, retrievedEntity);
	}

	@Test
	void testFindByNameWithNonexistentName_shouldReturnNull() {
		assertNull(courseRepository.findByName("NONE"));
	}

	@Test
	void testFindByName_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> courseRepository.findByName("NONE"));
	}

	@Test
	void testDelete_shouldReturnCorrectDataForExistingEntity() {
		Course expectedEntity = createEntity("TestName");
		Course retrievedEntity = courseRepository.findByName(expectedEntity.getName());
		assertEquals(expectedEntity, retrievedEntity);
		courseRepository.delete(retrievedEntity);
		assertNull(courseRepository.findByName("TestName"));
	}

	@Test
	void testDelete_shouldThrowExceptionIfDeleteNull() {
		assertThrows(DataAccessException.class, () -> courseRepository.delete(null));
	}

	@Test
	void testDelete_shouldThrowExceptionAfterDropDB() {
		dropDB();
		Course entity = new Course(1, "NONE");
		assertThrows(DataAccessException.class, () -> courseRepository.delete(entity));
	}

	@Test
	void testFindAll_ShouldReturnListWithTwoEntities() {
		assertEquals(0, courseRepository.findAll().size());
		Course expectedEntity1 = createEntity("TestName");
		Course expectedEntity2 = createEntity("TestName");
		assertEquals(2, courseRepository.findAll().size());
		assertEquals(expectedEntity1, courseRepository.findAll().get(0));
		assertEquals(expectedEntity2, courseRepository.findAll().get(1));
	}

	@Test
	void testFindAll_shouldThrowExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> courseRepository.findAll());
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
		courseRepository.save(entity);
		return entity;
	}
	
}
