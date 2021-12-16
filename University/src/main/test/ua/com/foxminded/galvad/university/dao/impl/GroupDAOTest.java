package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@DataJpaTest
@ComponentScan("ua.com.foxminded.galvad.university.dao")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class GroupDAOTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private GroupDAO groupDAO = new GroupDAO();

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Group group = createEntity("TestGroup");
		Group resultGroup = groupDAO.retrieve(1);
		assertEquals(group, resultGroup);
	}

	@Test
	void testCreate_studentsShouldBeAssignedToGroupWithID1() {
		createEntity("TestGroup");
		Group resultGroup = groupDAO.retrieve(1);
		Set<Student> resultSet = resultGroup.getSetOfStudent();
		assertEquals(2, resultSet.size());
		resultSet.stream().forEach(s -> {
			assertEquals(1, s.getGroup().getId());
			assertEquals("TestGroup", s.getGroup().getName());
		});
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Group group = new Group();
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.create(group));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Group group = createEntity("TestGroup");
		assertEquals(1, groupDAO.getId(group));
	}

	@Test
	void testGetIdWithNonexistentGroup_shouldThrowDataNotFoundException() {
		Group group = new Group(100, "TestName");
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId(group));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Group group = new Group();
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId(group));
	}

	@Test
	void testGetIdByString_shouldReturnCorrectIdForEntity() {
		createEntity("TestName");
		assertEquals(1, groupDAO.getId("TestName"));
	}

	@Test
	void testGetIdByStringWithNonexistentGroup_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId("WrongName"));
	}

	@Test
	void testGetIdByString_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.getId("TestName"));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		Group group = createEntity("TestName");
		assertEquals(group, groupDAO.retrieve(1));
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		Group group = createEntity("TestName");
		assertEquals(group, groupDAO.retrieve("TestName"));
	}

	@Test
	void testRetrieveByNameWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve("WrongName"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.retrieve("ABCDEF"));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Group initialGroup = createEntity("TestName");
		Group groupBeforeUpdate = groupDAO.retrieve(1);
		assertEquals(initialGroup, groupBeforeUpdate);
		groupBeforeUpdate.setName("NEW Name");
		Student student = new Student();
		student.setFirstName("NewFirstName");
		student.setLastName("NewLastName");
		Set<Student> newSetOfStudents = new HashSet<>();
		newSetOfStudents.add(student);
		groupBeforeUpdate.setSetOfStudent(newSetOfStudents);
		groupDAO.update(groupBeforeUpdate);
		Group groupAfterUpdate = groupDAO.retrieve(1);
		assertEquals(groupBeforeUpdate, groupAfterUpdate);
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Group group = new Group(1, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.update(group));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestName");
		Group group2 = createEntity("TestName2");
		int numOfGroupsBeforeDelete = groupDAO.findAll().size();
		assertEquals(2, numOfGroupsBeforeDelete);
		groupDAO.delete(group2);
		int numOfGroupsAfterDelete = groupDAO.findAll().size();
		assertEquals(1, numOfGroupsBeforeDelete - numOfGroupsAfterDelete);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Group group = new Group(100, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.delete(group));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Group group = new Group(1, "TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.delete(group));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestName");
		createEntity("TestName2");
		int numOfGroupsBeforeDelete = groupDAO.findAll().size();
		assertEquals(2, numOfGroupsBeforeDelete);
		groupDAO.delete(2);
		int numOfGroupsAfterDelete = groupDAO.findAll().size();
		assertEquals(1, numOfGroupsBeforeDelete - numOfGroupsAfterDelete);
	}

	@Test
	void testDeleteByID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> groupDAO.delete(1));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		Group group1 = createEntity("TestName");
		Group group2 = createEntity("TestName2");
		List<Group> retrievedList = groupDAO.findAll();
		List<Group> expectedList = new ArrayList<>();
		expectedList.add(group1);
		expectedList.add(group2);
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> groupDAO.findAll());
	}

	@Test
	void testFindAll_shouldReturnEmptyListWhenNothingFound() {
		Group group1 = createEntity("TestName");
		Group group2 = createEntity("TestName2");
		List<Group> expectedList = new ArrayList<>();
		expectedList.add(group1);
		expectedList.add(group2);
		List<Group> retrievedList = groupDAO.findAll();
		assertEquals(expectedList, retrievedList);
		entityManager.createNativeQuery("DELETE FROM groups").executeUpdate();
		expectedList = new ArrayList<>();
		retrievedList = groupDAO.findAll();
		assertEquals(expectedList, retrievedList);
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
		Student studentB = new Student();
		studentB.setFirstName("FNameB");
		studentB.setLastName("LNameB");
		Set<Student> setOfStudent = new HashSet<>();
		setOfStudent.add(studentA);
		setOfStudent.add(studentB);
		entity.setSetOfStudent(setOfStudent);
		groupDAO.create(entity);
		return entity;
	}
}
