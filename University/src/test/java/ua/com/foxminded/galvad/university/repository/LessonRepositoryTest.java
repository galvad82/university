package ua.com.foxminded.galvad.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class LessonRepositoryTest {

	@Autowired
	private LessonRepository lessonRepository;

	@Autowired
	private EntityManager entityManager;

	@Test
	void testSave_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Lesson entity = createEntity("Name", "FirstName", "LastName");
		assertEquals(entity, lessonRepository.findAll().get(0));
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfEntityIsNull() {
		assertThrows(DataAccessException.class, () -> lessonRepository.save(null));
	}

	@Test
	void testSave_shouldThrowDataAccessExceptionIfDBDropped() {
		dropDB();
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAccessException.class, () -> lessonRepository.save(lesson));
	}

	@Test
	void testGetID_ShouldReturnIDForEntity() {
		Lesson lesson = createEntity("Name", "FirstName", "LastName");
		assertEquals(1, lessonRepository.getId(lesson));
	}

	@Test
	void testGetIdWithNonexistentLesson_shouldReturnNull() {
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertNull(lessonRepository.getId(lesson));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAccessException.class, () -> lessonRepository.getId(lesson));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		createEntity("Name", "FirstName", "LastName");
		Lesson lesson2 = createEntity("Name2", "FirstName2", "LastName2");
		int numOfLessonsBeforeDelete = lessonRepository.findAll().size();
		assertEquals(2, numOfLessonsBeforeDelete);
		lessonRepository.delete(lesson2);
		int numOfLessonsAfterDelete = lessonRepository.findAll().size();
		assertEquals(1, numOfLessonsBeforeDelete - numOfLessonsAfterDelete);
	}

	@Test
	void testDeleteByEntity_shouldThrowExceptionIfDeleteNull() {
		assertThrows(DataAccessException.class, () -> lessonRepository.delete(null));
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAccessException.class, () -> lessonRepository.delete(lesson));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonRepository.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAccessException.class, () -> lessonRepository.findAll());
	}

	@Test
	void testFindAll_shouldReturnEmptyListWhenNothingFound() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonRepository.findAll();
		assertEquals(expectedList, retrievedList);
		entityManager.createNativeQuery("DELETE FROM lessons").executeUpdate();
		expectedList = new ArrayList<>();
		retrievedList = lessonRepository.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testDeleteByClassroom() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonRepository.findAll();
		assertEquals(expectedList, retrievedList);
		lessonRepository.deleteByClassroom(expectedList.get(0).getClassroom());
		expectedList.remove(0);
		List<Lesson> listAfterDelete = lessonRepository.findAll();
		assertEquals(expectedList, listAfterDelete);
	}

	@Test
	void testDeleteByClassroom_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		Classroom entity = new Classroom(1, "Test");
		assertThrows(DataAccessException.class, () -> lessonRepository.deleteByClassroom(entity));
	}

	@Test
	void testDeleteByCourse() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonRepository.findAll();
		assertEquals(expectedList, retrievedList);
		lessonRepository.deleteByCourse(expectedList.get(0).getCourse());
		expectedList.remove(0);
		List<Lesson> listAfterDelete = lessonRepository.findAll();
		assertEquals(expectedList, listAfterDelete);
	}

	@Test
	void testDeleteByCourse_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		Course entity = new Course(1, "Test");
		assertThrows(DataAccessException.class, () -> lessonRepository.deleteByCourse(entity));
	}

	@Test
	void testDeleteByGroup() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonRepository.findAll();
		assertEquals(expectedList, retrievedList);
		lessonRepository.deleteByGroup(expectedList.get(0).getGroup());
		expectedList.remove(0);
		List<Lesson> listAfterDelete = lessonRepository.findAll();
		assertEquals(expectedList, listAfterDelete);
	}

	@Test
	void testDeleteByGroup_shouldThrowDataAccessExceptionAfterDropDB() {
		dropDB();
		Group entity = new Group(1, "Test");
		assertThrows(DataAccessException.class, () -> lessonRepository.deleteByGroup(entity));
	}

	private void dropDB() {
		entityManager.createNativeQuery("DROP ALL OBJECTS").executeUpdate();
	}

	private Lesson createEntity(String name, String firstName, String lastName) {
		Group group = new Group();
		group.setName(name);
		entityManager.persist(group);
		Student student = new Student();
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setGroup(group);
		entityManager.persist(student);
		Teacher teacher = new Teacher();
		teacher.setFirstName(firstName);
		teacher.setLastName(lastName);
		entityManager.persist(teacher);
		Course course = new Course();
		course.setName(name);
		course.setTeacher(teacher);
		entityManager.persist(course);
		Classroom classroom = new Classroom();
		classroom.setName(name);
		entityManager.persist(classroom);
		Lesson entity = new Lesson();
		entity.setClassroom(classroom);
		entity.setCourse(course);
		entity.setGroup(group);
		entity.setStartTime(111111L);
		entity.setDuration(2222L);
		lessonRepository.save(entity);
		return entity;
	}

}
