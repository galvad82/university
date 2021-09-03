package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@SpringJUnitConfig(SpringConfigDAOTest.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class LessonDAOTest {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private LessonDAO lessonDAO = new LessonDAO();

	@Test
	void testCreate_shouldAddNewRowToDatabaseAndRetrieveIt() {
		Lesson lesson = createEntity("Name", "FirstName", "LastName");
		assertEquals(lesson, lessonDAO.retrieve(1));
	}

	@Test
	void testCreate_shouldThrowDataAreNotUpdatedException() {
		dropDB();
		Lesson lesson = new Lesson(new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.create(lesson));
	}

	@Test
	void testGetId_shouldReturnCorrectIdForEntity() {
		Lesson lesson = createEntity("Name", "FirstName", "LastName");
		assertEquals(lesson, lessonDAO.retrieve(1));
	}

	@Test
	void testGetIdWithNonexistentLesson_shouldThrowDataNotFoundException() {
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataNotFoundException.class, () -> lessonDAO.getId(lesson));
	}

	@Test
	void testGetId_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataNotFoundException.class, () -> lessonDAO.getId(lesson));
	}

	@Test
	void testRetrieve_shouldReturnCorrectData() {
		Lesson lesson = createEntity("Name", "FirstName", "LastName");
		assertEquals(lesson, lessonDAO.retrieve(1));
	}

	@Test
	void testRetrieveWithNonexistentID_shouldThrowDataNotFoundException() {
		assertThrows(DataNotFoundException.class, () -> lessonDAO.retrieve(100));
	}

	@Test
	void testRetrieve_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> lessonDAO.retrieve(100));
	}

	@Test
	void testUpdate_shouldReturnUpdatedEntity() {
		Lesson lesson = createEntity("Name", "FirstName", "LastName");
		Group newGroup = new Group();
		newGroup.setName("NEW Name");
		Student newStudent = new Student();
		newStudent.setFirstName("NewFirstName");
		newStudent.setLastName("NewLastName");
		entityManager.persist(newStudent);
		Set<Student> newSetOfStudents = new HashSet<>();
		newSetOfStudents.add(newStudent);
		newGroup.setSetOfStudent(newSetOfStudents);
		entityManager.persist(newGroup);
		Classroom newClassroom = new Classroom();
		newClassroom.setName("NewName");
		entityManager.persist(newClassroom);
		Course newCourse = new Course();
		newCourse.setName("NewName");
		Teacher newTeacher = new Teacher();
		newTeacher.setFirstName("John");
		newTeacher.setLastName("Doe");
		entityManager.persist(newTeacher);
		newCourse.setTeacher(newTeacher);
		entityManager.persist(newCourse);
		lesson.setClassroom(newClassroom);
		lesson.setCourse(newCourse);
		lesson.setGroup(newGroup);
		lesson.setDuration(999L);
		lesson.setStartTime(999999L);
		lessonDAO.update(lesson);
		Lesson retrievedLesson = lessonDAO.retrieve(1);
		assertEquals(lesson, retrievedLesson);
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.update(lesson));
	}

	@Test
	void testUpdate_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.update(lesson));
	}

	@Test
	void testDeleteByEntity_shouldReturnCorrectNumberOfEntities() {
		createEntity("Name", "FirstName", "LastName");
		Lesson lesson2 = createEntity("Name2", "FirstName2", "LastName2");
		int numOfLessonsBeforeDelete = lessonDAO.findAll().size();
		assertEquals(2, numOfLessonsBeforeDelete);
		lessonDAO.delete(lesson2);
		int numOfLessonsAfterDelete = lessonDAO.findAll().size();
		assertEquals(1, numOfLessonsBeforeDelete - numOfLessonsAfterDelete);
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionForNonexistentEntity() {
		Lesson lesson = new Lesson(100, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.delete(lesson));
	}

	@Test
	void testDeleteByEntity_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		Lesson lesson = new Lesson(1, new Group(1), new Course(1), new Classroom(1), 161651000L, 3600000L);
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.delete(lesson));
	}

	@Test
	void testDeleteByID_shouldReturnCorrectNumberOfEntities() {
		createEntity("Name", "FirstName", "LastName");
		createEntity("Name2", "FirstName2", "LastName2");
		int numOfLessonsBeforeDelete = lessonDAO.findAll().size();
		assertEquals(2, numOfLessonsBeforeDelete);
		lessonDAO.delete(2);
		int numOfLessonsAfterDelete = lessonDAO.findAll().size();
		assertEquals(1, numOfLessonsBeforeDelete - numOfLessonsAfterDelete);
	}

	@Test
	void testDeleteById_shouldThrowDataAreNotUpdatedExceptionForNonexistentId() {
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.delete(999));
	}

	@Test
	void testDeleteById_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.delete(999));
	}

	@Test
	void testFindAll_shouldFindTwoEntities() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonDAO.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll_shouldThrowDataNotFoundExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataNotFoundException.class, () -> lessonDAO.findAll());
	}

	@Test
	void testFindAll_shouldReturnEmptyListWhenNothingFound() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonDAO.findAll();
		assertEquals(expectedList, retrievedList);
		entityManager.createNativeQuery("DELETE FROM lessons").executeUpdate();
		expectedList = new ArrayList<>();
		retrievedList = lessonDAO.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testDeleteByClassroomID() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonDAO.findAll();
		assertEquals(expectedList, retrievedList);
		lessonDAO.deleteByClassroomID(1);
		expectedList.remove(0);
		List<Lesson> listAfterDelete = lessonDAO.findAll();
		assertEquals(expectedList, listAfterDelete);
	}

	@Test
	void testDeleteByClassroomID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByClassroomID(1));
	}

	@Test
	void testDeleteByCourseID() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonDAO.findAll();
		assertEquals(expectedList, retrievedList);
		lessonDAO.deleteByCourseID(1);
		expectedList.remove(0);
		List<Lesson> listAfterDelete = lessonDAO.findAll();
		assertEquals(expectedList, listAfterDelete);
	}

	@Test
	void testDeleteByCourseID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByCourseID(1));
	}

	@Test
	void testDeleteByGroupID() {
		List<Lesson> expectedList = new ArrayList<>();
		expectedList.add(createEntity("Name", "FirstName", "LastName"));
		expectedList.add(createEntity("Name2", "FirstName2", "LastName2"));
		List<Lesson> retrievedList = lessonDAO.findAll();
		assertEquals(expectedList, retrievedList);
		lessonDAO.deleteByGroupID(1);
		expectedList.remove(0);
		List<Lesson> listAfterDelete = lessonDAO.findAll();
		assertEquals(expectedList, listAfterDelete);
	}

	@Test
	void testDeleteByGroupID_shouldThrowDataAreNotUpdatedExceptionAfterDropDB() {
		dropDB();
		assertThrows(DataAreNotUpdatedException.class, () -> lessonDAO.deleteByGroupID(1));
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
		lessonDAO.create(entity);
		return entity;
	}
}
