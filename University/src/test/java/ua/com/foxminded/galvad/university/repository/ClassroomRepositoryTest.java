package ua.com.foxminded.galvad.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.com.foxminded.galvad.university.model.Classroom;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ClassroomRepositoryTest {

	@Autowired
	private ClassroomRepository classroomRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void testSave_shouldAddNewRowToDatabaseAndRetrieveIt() {
		assertEquals(0, classroomRepository.findAll().size());
		Classroom expectedEntity = createEntity("TestName");
		Classroom resultClassroom = classroomRepository.findByName("TestName");
		assertEquals(expectedEntity, resultClassroom);
		assertEquals(1, classroomRepository.findAll().size());
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfEntityIsNull() {
		assertThrows(DataAccessException.class, () -> classroomRepository.save(null));
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfDBDropped() {
		dropDB();
		Classroom classroom = new Classroom(1, "Test");
		assertThrows(DataAccessException.class, () -> classroomRepository.save(classroom));
	}

	@Test
	void testFindByName_shouldReturnCorrectDataForExistingEntity() {
		Classroom expectedEntity = createEntity("TestName");
		Classroom retrievedEntity = classroomRepository.findByName(expectedEntity.getName());
		assertEquals(expectedEntity, retrievedEntity);
	}

	@Test
	void testFindByNameWithNonexistentName_shouldReturnNull() {
		assertNull(classroomRepository.findByName("NONE"));
	}

	@Test
	void testFindByName_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> classroomRepository.findByName("NONE"));
	}

	@Test
	void testDelete_shouldReturnCorrectDataForExistingEntity() {
		Classroom expectedEntity = createEntity("TestName");
		Classroom retrievedEntity = classroomRepository.findByName(expectedEntity.getName());
		assertEquals(expectedEntity, retrievedEntity);
		classroomRepository.delete(retrievedEntity);
		assertNull(classroomRepository.findByName("TestName"));
	}

	@Test
	void testDelete_shouldThrowExceptionIfDeleteNull() {
		assertThrows(DataAccessException.class, () -> classroomRepository.delete(null));
	}

	@Test
	void testDelete_shouldThrowExceptionAfterDropDB() {
		dropDB();
		Classroom entity = new Classroom(1, "NONE");
		assertThrows(DataAccessException.class, () -> classroomRepository.delete(entity));
	}

	@Test
	void testFindAll_ShouldReturnListWithTwoEntities() {
		assertEquals(0, classroomRepository.findAll().size());
		Classroom expectedEntity1 = createEntity("TestName");
		Classroom expectedEntity2 = createEntity("TestName");
		assertEquals(2, classroomRepository.findAll().size());
		assertEquals(expectedEntity1, classroomRepository.findAll().get(0));
		assertEquals(expectedEntity2, classroomRepository.findAll().get(1));
	}

	@Test
	void testFindAll_shouldThrowExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> classroomRepository.findAll());
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}

	private Classroom createEntity(String entityName) {
		Classroom entity = new Classroom();
		entity.setName(entityName);
		classroomRepository.save(entity);
		return entity;
	}

}
