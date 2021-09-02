package ua.com.foxminded.galvad.university.services;

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
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@SpringJUnitWebConfig(SpringConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class CourseServiceTest {

	@Autowired
	private CourseService courseService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void testCreate() {
		createDTO("TestName");
		CourseDTO retrievedDTO = courseService.findAll().get(0);
		assertEquals("TestName", retrievedDTO.getName());
		assertEquals("TestName", retrievedDTO.getTeacher().getFirstName());
		assertEquals("LastName", retrievedDTO.getTeacher().getLastName());
	}

	@Test
	void testRetrieve() {
		createDTO("TestName");
		CourseDTO retrievedDTO = courseService.retrieve("TestName");
		assertEquals("TestName", retrievedDTO.getName());
		assertEquals("TestName", retrievedDTO.getTeacher().getFirstName());
		assertEquals("LastName", retrievedDTO.getTeacher().getLastName());
	}

	@Test
	void testUpdate() {
		CourseDTO initialDTO = createDTO("TestName");
		CourseDTO newDTO = new CourseDTO();
		newDTO.setName("NewName");
		newDTO.setTeacher(initialDTO.getTeacher());
		courseService.update(initialDTO, newDTO);
		CourseDTO updatedCourseDTO = courseService.findAll().get(0);
		assertEquals(newDTO, updatedCourseDTO);
	}

	@Test
	void testDelete() {
		List<CourseDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("TestName"));
		expectedList.add(createDTO("TestName2"));
		List<CourseDTO> listBeforeDel = courseService.findAll();
		assertEquals(expectedList, listBeforeDel);
		courseService.delete(expectedList.get(1));
		List<CourseDTO> listAfterDel = courseService.findAll();
		expectedList.remove(1);
		assertEquals(expectedList, listAfterDel);
	}

	@Test
	void testFindAll() {
		CourseDTO course = createDTO("TestName");
		CourseDTO course1 = createDTO("TestName1");
		CourseDTO course2 = createDTO("TestName2");
		List<CourseDTO> retrievedList = courseService.findAll();
		assertEquals(course, retrievedList.get(0));
		assertEquals(course1, retrievedList.get(1));
		assertEquals(course2, retrievedList.get(2));
		assertEquals(3, retrievedList.size());
	}

	@Test
	void testFindAllLessonsForCourse() {
		createLesson("Name", "FirstName", "LastName");
		createLesson("Name2", "FirstName2", "LastName2");
		createLesson("Name3", "FirstName3", "LastName3");
		List<LessonDTO> listOfLessons = courseService.findAllLessonsForCourse("Name3");
		assertEquals("Name3", listOfLessons.get(0).getClassroom().getName());
		assertEquals("Name3", listOfLessons.get(0).getCourse().getName());
		assertEquals("LastName3", listOfLessons.get(0).getCourse().getTeacher().getLastName());
		assertEquals("Name3", listOfLessons.get(0).getGroup().getName());
		assertEquals(2222L, listOfLessons.get(0).getDuration());
		assertEquals(111111L, listOfLessons.get(0).getStartTime());
		assertEquals(1, listOfLessons.size());
	}
	
	private CourseDTO createDTO(String name) {
		Course course = new Course();
		course.setName(name);
		Teacher teacher = new Teacher();
		teacher.setFirstName(name);
		teacher.setLastName("LastName");
		entityManager.persist(teacher);
		course.setTeacher(teacher);
		entityManager.persist(course);
		return courseService.retrieve(name);
	}
	
	private Lesson createLesson(String name, String firstName, String lastName) {
		Student student = new Student();
		student.setFirstName(firstName);
		student.setLastName(lastName);
		entityManager.persist(student);
		Group group = new Group();
		group.setName(name);
		Set<Student> setOfStudents = new HashSet<>();
		setOfStudents.add(student);
		group.setSetOfStudent(setOfStudents);
		entityManager.persist(group);		
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
