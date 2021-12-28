package ua.com.foxminded.galvad.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class StudentRepositoryTest {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void testSave_shouldAddNewRowToDatabaseAndRetrieveIt() {
		assertEquals(0, studentRepository.findAll().size());
		Student expectedEntity = createEntity("TestFirstName", "TestLastName");
		Student retrievedEntity = studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertEquals(expectedEntity, retrievedEntity);
		assertEquals(1, studentRepository.findAll().size());
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfEntityIsNull() {
		assertThrows(DataAccessException.class, () -> studentRepository.save(null));
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfDBDropped() {
		dropDB();
		Student entity = new Student(1,"TestFirstName", "TestLastName");
		assertThrows(DataAccessException.class, () -> studentRepository.save(entity));
	}

	@Test
	void testFindByName_shouldReturnCorrectDataForExistingEntity() {
		Student expectedEntity = createEntity("TestFirstName", "TestLastName");
		Student retrievedEntity = studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertEquals(expectedEntity, retrievedEntity);
	}

	@Test
	void testFindByNameWithNonexistentName_shouldReturnNull() {
		assertNull(studentRepository.findByFirstNameAndLastName("NONE", "NONE"));
	}

	@Test
	void testFindByName_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName"));
	}

	@Test
	void testDelete_shouldDeleteExistingEntity() {
		Student expectedEntity = createEntity("TestFirstName", "TestLastName");
		Student retrievedEntity = studentRepository.findByFirstNameAndLastName(expectedEntity.getFirstName(), expectedEntity.getLastName());
		assertEquals(expectedEntity, retrievedEntity);
		studentRepository.delete(retrievedEntity);
		assertNull(studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName"));
	}

	@Test
	void testDelete_shouldThrowExceptionIfDeleteNull() {
		assertThrows(DataAccessException.class, () -> studentRepository.delete(null));
	}

	@Test
	void testDelete_shouldThrowExceptionAfterDropDB() {
		dropDB();
		Student entity = new Student(1, "TestFirstName", "TestLastName");
		assertThrows(DataAccessException.class, () -> studentRepository.delete(entity));
	}

	@Test
	void testFindAll_ShouldReturnListWithTwoEntities() {
		assertEquals(0, studentRepository.findAll().size());
		Student expectedEntity1 = createEntity("TestFirstNameA", "TestLastNameA");
		Student expectedEntity2 = createEntity("TestFirstNameB", "TestLastNameB");
		assertEquals(2, studentRepository.findAll().size());
		assertEquals(expectedEntity1, studentRepository.findAll().get(0));
		assertEquals(expectedEntity2, studentRepository.findAll().get(1));
	}

	@Test
	void testFindAll_shouldThrowExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> studentRepository.findAll());
	}
	
	@Test
	void addStudentToGroup() {
		Student entity = new Student();
		entity.setFirstName("TestFirstName");
		entity.setLastName("TestLastName");
		studentRepository.save(entity);
		Student retrievedStudent = studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertNull(retrievedStudent.getGroup());
		
		Group group = new Group();
		group.setName("Group");
		entityManager.persist(group);
		Group retrievedGroup = entityManager.find(Group.class,1);
		assertTrue(retrievedGroup.getSetOfStudent().isEmpty());
		
		studentRepository.addStudentToGroup(retrievedStudent.getId(), retrievedGroup);
		retrievedStudent = studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertEquals(entityManager.find(Group.class,1), retrievedStudent.getGroup());
		assertTrue(entityManager.find(Group.class,1).getSetOfStudent().contains(retrievedStudent));
	}
	
	
	@Test
	void testRemoveFromGroups() {
		Student entity = createEntity("TestFirstName", "TestLastName");
		Student retrievedStudentBeforeRemovingFromGroups = studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertEquals(entity.getGroup(), retrievedStudentBeforeRemovingFromGroups.getGroup());
		studentRepository.removeStudentFromGroups(retrievedStudentBeforeRemovingFromGroups.getId());
		Student retrievedStudentAfterRemovingFromGroups = studentRepository.findByFirstNameAndLastName("TestFirstName", "TestLastName");
		assertNull(retrievedStudentAfterRemovingFromGroups.getGroup());		
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}
	
	private Student createEntity(String firstName, String lastName) {
		Student entity = new Student();
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		Group group = new Group();
		group.setName("TestGroup");
		entityManager.persist(group);
		entity.setGroup(group);
		studentRepository.save(entity);
		return entity;
	}

}
