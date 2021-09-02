package ua.com.foxminded.galvad.university.services;

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
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@SpringJUnitWebConfig(SpringConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ClassroomServiceTest {

	@Autowired
	private ClassroomService classroomService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void testCreate() {
		ClassroomDTO classroomDTO = createDTO("Name");
		ClassroomDTO retrievedDTO = classroomService.retrieve("Name");
		assertEquals(classroomDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		ClassroomDTO classroomDTO = createDTO("Name");
		ClassroomDTO retrievedDTO = classroomService.retrieve("Name");
		assertEquals(classroomDTO, retrievedDTO);
	}

	@Test
	void testRetrieveByFindAll() {
		ClassroomDTO classroomDTO = createDTO("Name");
		ClassroomDTO retrievedDTO = classroomService.findAll().get(0);
		assertEquals(classroomDTO, retrievedDTO);
	}

	@Test
	void testUpdate() {
		createDTO("Name");
		ClassroomDTO initialDTO = classroomService.findAll().get(0);
		ClassroomDTO newDTO = new ClassroomDTO();
		newDTO.setName("NewName");
		classroomService.update(initialDTO, newDTO);
		assertEquals(newDTO, classroomService.findAll().get(0));
	}

	@Test
	void testDelete() {
		List<ClassroomDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("Name"));
		expectedList.add(createDTO("Name2"));
		List<ClassroomDTO> retrievedList =classroomService.findAll();
		assertEquals(expectedList, retrievedList);
		classroomService.delete(expectedList.get(1));
		expectedList.remove(1);
		retrievedList =classroomService.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	void testFindAll() {
		List<ClassroomDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("Name"));
		expectedList.add(createDTO("Name2"));
		List<ClassroomDTO> retrievedList =classroomService.findAll();
		assertEquals(expectedList, retrievedList);
	}

	@Test
	@Transactional
	void testFindAllLessonsForClassroom() {
		createLesson("Name", "FirstName", "LastName");
		createLesson("Name2", "FirstName2", "LastName2");
		createLesson("Name3", "FirstName3", "LastName3");
		List<LessonDTO> listOfLessons = classroomService.findAllLessonsForClassroom("Name3");
		assertEquals("Name3", listOfLessons.get(0).getClassroom().getName());
		assertEquals("Name3", listOfLessons.get(0).getCourse().getName());
		assertEquals("LastName3", listOfLessons.get(0).getCourse().getTeacher().getLastName());
		assertEquals("Name3", listOfLessons.get(0).getGroup().getName());
		assertEquals(2222L, listOfLessons.get(0).getDuration());
		assertEquals(111111L, listOfLessons.get(0).getStartTime());
		assertEquals(1, listOfLessons.size());
	}
	
	private ClassroomDTO createDTO(String name) {
		ClassroomDTO DTO = new ClassroomDTO();
		DTO.setName(name);
		classroomService.create(DTO);
		return DTO;
	}
	
	private Lesson createLesson(String name, String firstName, String lastName) {
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
		entityManager.persist(entity);
		return entity;
	}
}
