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
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@DataJpaTest
@ComponentScan("ua.com.foxminded.galvad.university.dao")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class StudentDAOTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private StudentDAO studentDAO = new StudentDAO();

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Student student = createEntity("TestFirstName", "TestLastName");
		assertEquals(student, studentDAO.retrieve(1));
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Student student = new Student(1, "FirstName", "LastName");
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.create(student));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		Student student = createEntity("TestFirstName", "TestLastName");
		assertEquals(student, studentDAO.retrieve(1));
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve(100));
	}

	@Test
	void testRetrieveByName_shouldReturnCorrectData() {
		Student student = createEntity("TestFirstName", "TestLastName");
		assertEquals(student, studentDAO.retrieve("TestFirstName", "TestLastName"));
	}

	@Test
	void testRetrieveByNameWithNonexistentName_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve("NONE", "NONE"));
	}

	@Test
	void testRetrieveByName_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> studentDAO.retrieve("NONE", "NONE"));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Student student = createEntity("TestFirstName", "TestLastName");
		assertEquals(1, studentDAO.getId(student));
	}

	@Test
	void testGetIdWithNonexistentName_shouldThrowDataNotFoundException() {
		Student student = new Student();
		student.setFirstName("NONE");
		student.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> studentDAO.getId(student));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setFirstName("NONE");
		student.setLastName("NONE");
		assertThrows(DataNotFoundException.class, () -> studentDAO.getId(student));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Student student = createEntity("TestFirstName", "TestLastName");
		Student studentBeforeUpdate = studentDAO.retrieve(1);
		assertEquals(student, studentBeforeUpdate);
		studentBeforeUpdate.setFirstName("NEW First Name");
		studentBeforeUpdate.setLastName("NEW Last Name");
		Group newGroup = new Group();
		newGroup.setName("NewGroup");
		entityManager.persist(newGroup);
		studentBeforeUpdate.setGroup(newGroup);
		studentDAO.update(studentBeforeUpdate);
		Student studentAfterUpdate = studentDAO.retrieve(1);
		assertEquals(studentBeforeUpdate, studentAfterUpdate);
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.update(student));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestFirstName", "TestLastName");
		Student student2 = createEntity("TestFirstName2", "TestLastName2");
		int numOfStudentsBeforeDelete = studentDAO.findAll().size();
		assertEquals(2, numOfStudentsBeforeDelete);
		studentDAO.delete(student2);
		int numOfStudentsAfterDelete = studentDAO.findAll().size();
		assertEquals(1, numOfStudentsBeforeDelete - numOfStudentsAfterDelete);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Student student = new Student();
		student.setId(100);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(student));
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student();
		student.setId(1);
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(student));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		createEntity("TestFirstName", "TestLastName");
		createEntity("TestFirstName2", "TestLastName2");
		int numOfStudentsBeforeDelete = studentDAO.findAll().size();
		assertEquals(2, numOfStudentsBeforeDelete);
		studentDAO.delete(2);
		int numOfStudentsAfterDelete = studentDAO.findAll().size();
		assertEquals(1, numOfStudentsBeforeDelete - numOfStudentsAfterDelete);
	}

	@Test
	void testDeleteByID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.delete(1));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Student> expectedList = new ArrayList<>();
		expectedList.add(createEntity("TestFirstName", "TestLastName"));
		expectedList.add(createEntity("TestFirstName2", "TestLastName2"));
		List<Student> retrievedList = studentDAO.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> studentDAO.findAll());
	}

	@Test
	void testFindAll_shouldReturnEmptyListWhenNothingFound() {
		createEntity("TestFirstName", "TestLastName");
		createEntity("TestFirstName2", "TestLastName2");
		List<Student> retrievedList = studentDAO.findAll();
		assertEquals(2, retrievedList.size());
		entityManager.createNativeQuery("DELETE FROM students").executeUpdate();
		List<Student> expectedList = new ArrayList<>();
		retrievedList = studentDAO.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testAddStudentToGroup_shouldSetProperGroupForStudentEntity() {
		Student student = createEntity("TestFirstName", "TestLastName");
		assertEquals(student, studentDAO.retrieve(1));
		Group newGroup = new Group();
		newGroup.setName("NewGroup");
		entityManager.persist(newGroup);
		studentDAO.addStudentToGroup(student, newGroup);
		student.setGroup(newGroup);
		assertEquals(student, studentDAO.retrieve(1));
	}

	@Test
	void testAddStudentToGroup_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student(1, "John", "Davidson");
		Group group = new Group(1, "GroupName");
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.addStudentToGroup(student, group));
	}

	@Test
	void testRemoveStudentFromGroups_shouldSetNullAsGroupForStudent() {
		Student student = createEntity("TestFirstName", "TestLastName");
		assertEquals(student, studentDAO.retrieve(1));
		studentDAO.removeStudentFromGroups(student);
		student.setGroup(null);
		assertEquals(student, studentDAO.retrieve(1));
	}

	@Test
	void testRemoveStudentFromGroups_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Student student = new Student(1, "John", "Davidson");
		assertThrows(DataAreNotUpdatedException.class, () -> studentDAO.removeStudentFromGroups(student));
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
		studentDAO.create(entity);
		return entity;
	}
}
