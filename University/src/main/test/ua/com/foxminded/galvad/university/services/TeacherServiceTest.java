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
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@SpringJUnitWebConfig(SpringConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class TeacherServiceTest {

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private LessonService lessonService;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void testCreate() {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("First Name");
		teacherDTO.setLastName("Last Name");
		teacherService.create(teacherDTO);
		TeacherDTO retrievedDTO = teacherService.findAll().get(0);
		assertEquals(teacherDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		TeacherDTO expectedDTO = createDTO("First Name", "Last Name");
		TeacherDTO retrievedDTO = teacherService.retrieve("First Name", "Last Name");
		assertEquals(expectedDTO, retrievedDTO);
	}

	@Test
	void testRetrieveByFindAll() {
		TeacherDTO expectedDTO = createDTO("First Name", "Last Name");
		TeacherDTO retrievedDTO = teacherService.findAll().get(0);
		assertEquals(expectedDTO, retrievedDTO);
	}

	@Test
	void testUpdate() {
		createDTO("First Name", "Last Name");
		TeacherDTO initialDTO = teacherService.findAll().get(0);
		TeacherDTO newDTO = new TeacherDTO();
		newDTO.setFirstName("NewName");
		newDTO.setLastName("NewLastName");
		teacherService.update(initialDTO, newDTO);
		TeacherDTO updatedTeacherDTO = teacherService.findAll().get(0);
		assertEquals(newDTO, updatedTeacherDTO);
	}

	@Test
	void testDelete() {
		List<TeacherDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("First Name", "Last Name"));
		expectedList.add(createDTO("First NameB", "Last NameB"));
		expectedList.add(createDTO("First NameC", "Last NameC"));
		List<TeacherDTO> listBeforeDelete = teacherService.findAll();
		assertEquals(expectedList,listBeforeDelete);
		teacherService.delete(expectedList.get(1));
		List<TeacherDTO> listAfterDelete = teacherService.findAll();
		expectedList.remove(1);
		assertEquals(expectedList,listAfterDelete);
		assertEquals(1,listBeforeDelete.size()-listAfterDelete.size());
	}

	@Test
	void testFindAll() {
		List<TeacherDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("First Name", "Last Name"));
		expectedList.add(createDTO("First NameB", "Last NameB"));
		expectedList.add(createDTO("First NameC", "Last NameC"));
		List<TeacherDTO> retrievedList = teacherService.findAll();
		assertEquals(expectedList,retrievedList);
	}

	@Test
	void testFindAllLessonsForTeacher() {
		assertTrue(lessonService.findAll().isEmpty());
		createLesson("Group", "Course","Class","StudentFName", "StudentLName", "TeacherFName", "TeacherLName");
		createLesson("Group1", "Course1","Class1","StudentFNameA", "StudentLNameA", "TeacherFNameA", "TeacherLNameA");
		createLesson("Group", "Course","Class","StudentFNameA", "StudentLNameA", "TeacherFNameB", "TeacherLNameB");
		List<LessonDTO> retrievedList = teacherService.findAllLessonsForTeacher("TeacherFNameA","TeacherLNameA");
		List<LessonDTO> expectedList = new ArrayList<>(); 
		expectedList.add(lessonService.findAll().get(1));
		assertEquals(1, retrievedList.size());
		assertEquals(expectedList,retrievedList);
	}
	
	private TeacherDTO createDTO(String firstName, String LastName) {
		Teacher teacher = new Teacher();
		teacher.setFirstName(firstName);
		teacher.setLastName(LastName);
		entityManager.persist(teacher);
		return teacherService.retrieve(firstName, LastName);
	}

	private Lesson createLesson(String groupName, String courseName, String classroomName, String sFirstName,
			String sLastName, String tFirstName, String tLastName) {
		Student student = new Student();
		student.setFirstName(sFirstName);
		student.setLastName(sLastName);
		entityManager.persist(student);
		Group group = new Group();
		group.setName(groupName);
		Set<Student> setOfStudents = new HashSet<>();
		setOfStudents.add(student);
		group.setSetOfStudent(setOfStudents);
		entityManager.persist(group);
		Teacher teacher = new Teacher();
		teacher.setFirstName(tFirstName);
		teacher.setLastName(tLastName);
		entityManager.persist(teacher);
		Course course = new Course();
		course.setName(courseName);
		course.setTeacher(teacher);
		entityManager.persist(course);
		Classroom classroom = new Classroom();
		classroom.setName(classroomName);
		entityManager.persist(classroom);
		Lesson lesson = new Lesson();
		lesson.setClassroom(classroom);
		lesson.setCourse(course);
		lesson.setGroup(group);
		lesson.setStartTime(111111L);
		lesson.setDuration(2222L);
		entityManager.persist(lesson);
		return lesson;
	}

}
