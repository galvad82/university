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
import ua.com.foxminded.galvad.university.model.Teacher;

@DataJpaTest
@ComponentScan("ua.com.foxminded.galvad.university.dao")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TeacherDAOTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private TeacherDAO teacherDAO;

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		createEntity("TestFirstName", "TestLastName");
		Teacher resultTeacher = teacherDAO.retrieve(1);
		assertEquals(1, resultTeacher.getId());
		assertEquals("TestFirstName", resultTeacher.getFirstName());
		assertEquals("TestLastName", resultTeacher.getLastName());
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Teacher teacher = new Teacher();
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.create(teacher));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Teacher teacher = createEntity("TestFirstName", "TestLastName");
		assertEquals(1, teacherDAO.getId(teacher));
	}

	@Test
	void testGetIdWithNonexistentName_shouldThrowDataNotFoundException() {
		Teacher teacher = new Teacher();
		teacher.setFirstName("NONE");
		teacher.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> teacherDAO.getId(teacher));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Teacher teacher = new Teacher();
		teacher.setFirstName("NONE");
		teacher.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> teacherDAO.getId(teacher));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		createEntity("TestFirstName", "TestLastName");
		assertEquals(1, teacherDAO.retrieve(1).getId());
		assertEquals("TestFirstName", teacherDAO.retrieve(1).getFirstName());
		assertEquals("TestLastName", teacherDAO.retrieve(1).getLastName());
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		createEntity("TestFirstName", "TestLastName");
		assertEquals(1, teacherDAO.retrieve("TestFirstName", "TestLastName").getId());
		assertEquals("TestFirstName", teacherDAO.retrieve("TestFirstName", "TestLastName").getFirstName());
		assertEquals("TestLastName", teacherDAO.retrieve("TestFirstName", "TestLastName").getLastName());
	}

	@Test
	void testRetrieveByNameWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve("NONE", "NONE"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> teacherDAO.retrieve("NONE", "NONE"));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		createEntity("TestFirstName", "TestLastName");
		Teacher teacherBeforeUpdate = teacherDAO.retrieve(1);
		assertEquals(1, teacherBeforeUpdate.getId());
		assertEquals("TestFirstName", teacherBeforeUpdate.getFirstName());
		assertEquals("TestLastName", teacherBeforeUpdate.getLastName());
		teacherBeforeUpdate.setFirstName("NEW First Name");
		teacherBeforeUpdate.setLastName("NEW Last Name");
		teacherDAO.update(teacherBeforeUpdate);
		Teacher teacherAfterUpdate = teacherDAO.retrieve(1);
		assertEquals(1, teacherAfterUpdate.getId());
		assertEquals("NEW First Name", teacherAfterUpdate.getFirstName());
		assertEquals("NEW Last Name", teacherAfterUpdate.getLastName());
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Teacher teacher = new Teacher();
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.update(teacher));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestFirstName", "TestLastName");
		Teacher teacher2 = createEntity("TestFirstName2", "TestLastName2");
		int numOfTeachersBeforeDelete = teacherDAO.findAll().size();
		assertEquals(2, numOfTeachersBeforeDelete);
		teacherDAO.delete(teacher2);
		int numOfTeachersAfterDelete = teacherDAO.findAll().size();
		assertEquals(1, numOfTeachersBeforeDelete - numOfTeachersAfterDelete);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Teacher teacher = new Teacher();
		teacher.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.delete(teacher));
	}

	@Test
	void testDelete_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Teacher teacher = new Teacher();
		teacher.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> teacherDAO.delete(teacher));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestFirstName", "TestLastName");
		createEntity("TestFirstName2", "TestLastName2");
		int numOfTeachersBeforeDelete = teacherDAO.findAll().size();
		assertEquals(2, numOfTeachersBeforeDelete);
		teacherDAO.delete(2);
		int numOfTeachersAfterDelete = teacherDAO.findAll().size();
		assertEquals(1, numOfTeachersBeforeDelete - numOfTeachersAfterDelete);
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		createEntity("TestFirstName", "TestLastName");
		createEntity("TestFirstName2", "TestLastName2");
		List<Teacher> retrievedList = teacherDAO.findAll();
		List<Teacher> expectedList = new ArrayList<>();
		expectedList.add(new Teacher(1, "TestFirstName", "TestLastName"));
		expectedList.add(new Teacher(2, "TestFirstName2", "TestLastName2"));
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> teacherDAO.findAll());
	}

	@Test
	void testFindAll_shouldReturnEmptyListWhenNothingFound() {
		createEntity("TestFirstName", "TestLastName");
		createEntity("TestFirstName2", "TestLastName2");
		List<Teacher> retrievedList = teacherDAO.findAll();
		assertEquals(2, retrievedList.size());
		entityManager.createNativeQuery("DELETE FROM teachers").executeUpdate();
		List<Teacher> expectedList = new ArrayList<>();
		retrievedList = teacherDAO.findAll();
		assertEquals(expectedList, retrievedList);
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}

	private Teacher createEntity(String firstName, String lastName) {
		Teacher entity = new Teacher();
		entity.setFirstName(firstName);
		entity.setLastName(lastName);
		teacherDAO.create(entity);
		return entity;
	}
}
