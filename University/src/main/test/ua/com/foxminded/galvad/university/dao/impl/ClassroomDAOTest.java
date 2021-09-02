package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigDAOTest;
import ua.com.foxminded.galvad.university.model.Classroom;

@SpringJUnitConfig(SpringConfigDAOTest.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class ClassroomDAOTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ClassroomDAO classroomDAO;

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		createEntity("TestName");
		Classroom resultClassroom = classroomDAO.retrieve(1);
		assertEquals(1, resultClassroom.getId());
		assertEquals("TestName", resultClassroom.getName());
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Classroom classroom = new Classroom();
		classroom.setName("TestName");
		assertThrows(DataAreNotUpdatedException.class, () -> classroomDAO.create(classroom));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		createEntity("TestName");
		Classroom resultClassroom = classroomDAO.retrieve(1);
		assertEquals(1, resultClassroom.getId());
	}

	@Test
	void testGetIdWithNonexistentName_shouldThrowDataNotFoundException() {
		Classroom classroom = new Classroom();
		classroom.setName("NONE");
		assertThrows(DataNotFoundException.class, () -> classroomDAO.getId(classroom));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Classroom classroom = new Classroom();
		classroom.setName("NONE");
		assertThrows(DataNotFoundException.class, () -> classroomDAO.getId(classroom));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		createEntity("TestName");
		Classroom resultClassroom = classroomDAO.retrieve(1);
		assertEquals(1, resultClassroom.getId());
		assertEquals("TestName", resultClassroom.getName());
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> classroomDAO.retrieve(1));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionForNonexistentID() {
		assertThrows(DataNotFoundException.class, () -> classroomDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		createEntity("TestName");
		assertEquals("TestName", classroomDAO.retrieve("TestName").getName());
		assertEquals(1, classroomDAO.retrieve("TestName").getId());
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> classroomDAO.retrieve("TestName"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionForNonexistentID() {
		assertThrows(DataNotFoundException.class, () -> classroomDAO.retrieve("ROOM-77"));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		createEntity("TestName");
		Classroom classroomBeforeUpdate = classroomDAO.retrieve(1);
		assertEquals("TestName", classroomBeforeUpdate.getName());
		assertEquals(1, classroomBeforeUpdate.getId());
		classroomBeforeUpdate.setName("New Name");
		classroomDAO.update(classroomBeforeUpdate);
		Classroom classroomAfterUpdate = classroomDAO.retrieve(1);
		assertEquals("New Name", classroomAfterUpdate.getName());
		assertEquals(1, classroomAfterUpdate.getId());
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Classroom classroom = new Classroom();
		assertThrows(DataAreNotUpdatedException.class, () -> classroomDAO.update(classroom));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestName");
		Classroom classroom2 = createEntity("TestName2");
		int numOfClassroomsBeforeDelete = classroomDAO.findAll().size();
		assertEquals(2, numOfClassroomsBeforeDelete);
		classroomDAO.delete(classroom2);
		int numOfClassroomsAfterDelete = classroomDAO.findAll().size();
		assertEquals(1, numOfClassroomsBeforeDelete - numOfClassroomsAfterDelete);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Classroom classroom = new Classroom();
		classroom.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> classroomDAO.delete(classroom));
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Classroom classroom = new Classroom();
		classroom.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> classroomDAO.delete(classroom));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestName");
		createEntity("TestName2");
		int numOfClassroomsBeforeDelete = classroomDAO.findAll().size();
		assertEquals(2, numOfClassroomsBeforeDelete);
		classroomDAO.delete(2);
		int numOfClassroomsAfterDelete = classroomDAO.findAll().size();
		assertEquals(1, numOfClassroomsBeforeDelete - numOfClassroomsAfterDelete);
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		createEntity("TestName");
		createEntity("TestName2");
		List<Classroom> retrievedList = classroomDAO.findAll();
		List<Classroom> expectedList = new ArrayList<>();
		expectedList.add(new Classroom(1, "TestName"));
		expectedList.add(new Classroom(2, "TestName2"));
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> classroomDAO.findAll());
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAsNothingFound() {
		createEntity("TestName");
		createEntity("TestName2");
		List<Classroom> retrievedList = classroomDAO.findAll();
		assertEquals(2, retrievedList.size());
		entityManager.createNativeQuery("DELETE FROM classrooms").executeUpdate();
		List<Classroom> expectedList = new ArrayList<>();
		retrievedList = classroomDAO.findAll();
		assertEquals(expectedList, retrievedList);
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}

	private Classroom createEntity(String entityName) {
		Classroom entity = new Classroom();
		entity.setName(entityName);
		classroomDAO.create(entity);
		return entity;
	}

}
