package ua.com.foxminded.galvad.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

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
class GroupRepositoryTest {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void testSave_shouldAddNewRowToDatabaseAndRetrieveIt() {
		assertEquals(0, groupRepository.findAll().size());
		Group expectedEntity = createEntity("TestName");
		Group retrievedEntity = groupRepository.findByName("TestName");
		assertEquals(expectedEntity, retrievedEntity);
		assertEquals(1, groupRepository.findAll().size());
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfEntityIsNull() {
		assertThrows(DataAccessException.class, () -> groupRepository.save(null));
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfDBDropped() {
		dropDB();
		Group entity = new Group(1, "Test");
		assertThrows(DataAccessException.class, () -> groupRepository.save(entity));
	}

	@Test
	void testFindByName_shouldReturnCorrectDataForExistingEntity() {
		Group expectedEntity = createEntity("TestName");
		Group retrievedEntity = groupRepository.findByName("TestName");
		assertEquals(expectedEntity, retrievedEntity);
	}

	@Test
	void testFindByNameWithNonexistentName_shouldReturnNull() {
		assertNull(groupRepository.findByName("NONE"));
	}

	@Test
	void testFindByName_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> groupRepository.findByName("NONE"));
	}

	@Test
	void testDelete_shouldNotDeleteGroupIfItHasAssignedStudents() {
		Group entity = createEntity("TestName");
		assertEquals(entity.getId(), entityManager.find(Student.class, 1).getGroup().getId());
		assertEquals(entity.getId(), entityManager.find(Student.class, 2).getGroup().getId());
		groupRepository.delete(entity);
		assertEquals(entity, groupRepository.findByName("TestName"));
	}

	@Test
	void testDelete_shouldDeleteGroupIfItHasNoAssignedStudents() {
		Group entity = createEntity("TestName");
		entity.getSetOfStudent().stream().forEach(student -> {
			Student retrievedStudent = entityManager.find(Student.class, student.getId());
			assertEquals(entity.getId(), retrievedStudent.getGroup().getId());
			retrievedStudent.setGroup(null);
			entityManager.merge(retrievedStudent);
			assertNull(entityManager.find(Student.class, retrievedStudent.getId()).getGroup());
		});
		groupRepository.delete(entity);
		assertNull(groupRepository.findByName("TestName"));
	}

	@Test
	void testDelete_shouldThrowExceptionIfDeleteNull() {
		assertThrows(DataAccessException.class, () -> groupRepository.delete(null));
	}

	@Test
	void testDelete_shouldThrowExceptionAfterDropDB() {
		dropDB();
		Group entity = new Group(1, "NONE");
		assertThrows(DataAccessException.class, () -> groupRepository.delete(entity));
	}

	@Test
	void testFindAll_ShouldReturnListWithTwoEntities() {
		assertEquals(0, groupRepository.findAll().size());
		Group expectedEntity1 = createEntity("TestName1");
		Group expectedEntity2 = createEntity("TestName2");
		assertEquals(2, groupRepository.findAll().size());
		assertEquals(expectedEntity1, groupRepository.findAll().get(0));
		assertEquals(expectedEntity2, groupRepository.findAll().get(1));
	}

	@Test
	void testFindAll_shouldThrowExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> groupRepository.findAll());
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}

	private Group createEntity(String name) {
		Group entity = new Group();
		entity.setName(name);
		Student studentA = new Student();
		studentA.setFirstName("FNameA");
		studentA.setLastName("LNameA");
		entityManager.persist(studentA);
		Student studentB = new Student();
		studentB.setFirstName("FNameB");
		studentB.setLastName("LNameB");
		entityManager.persist(studentB);
		Set<Student> setOfStudent = new HashSet<>();
		setOfStudent.add(studentA);
		setOfStudent.add(studentB);
		entity.setSetOfStudent(setOfStudent);
		groupRepository.save(entity);
		return entity;
	}

}
