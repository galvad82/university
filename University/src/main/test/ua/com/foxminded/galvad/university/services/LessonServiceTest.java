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
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Event;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@SpringJUnitWebConfig(SpringConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class LessonServiceTest {

	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private LessonService lessonService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void testCreate() {
		LessonDTO lessonDTO = createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName");
		LessonDTO retrievedDTO = lessonService.findAll().get(0);
		assertEquals(lessonDTO.getClassroom(), retrievedDTO.getClassroom());
		assertEquals(lessonDTO.getCourse(), retrievedDTO.getCourse());
		assertEquals(lessonDTO.getCourse().getTeacher(), retrievedDTO.getCourse().getTeacher());
		assertEquals(lessonDTO.getGroup().getName(), retrievedDTO.getGroup().getName());
		assertEquals(lessonDTO.getGroup().getListOfStudent(), retrievedDTO.getGroup().getListOfStudent());
		assertEquals(lessonDTO.getStartTime(), retrievedDTO.getStartTime());
		assertEquals(lessonDTO.getDuration(), retrievedDTO.getDuration());
	}

	@Test
	void testRetrieveByFindAll() {
		List<LessonDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName"));
		expectedList.add(createDTO("Group1", "Course1", "Class1", "StudentFirstNameA", "StudentLastNameA",
				"TeacherFirstNameA", "TeacherLastNameA"));
		assertEquals(expectedList, lessonService.findAll());
	}

	@Test
	void testUpdate() {
		Student newStudent = new Student();
		newStudent.setFirstName("NewFirstName");
		newStudent.setLastName("NewLastName");
		entityManager.persist(newStudent);
		Group newGroup = new Group();
		newGroup.setName("NewName");
		Set<Student> setOfStudents = new HashSet<>();
		setOfStudents.add(newStudent);
		newGroup.setSetOfStudent(setOfStudents);
		entityManager.persist(newGroup);
		Teacher newTeacher = new Teacher();
		newTeacher.setFirstName("NewFirstName");
		newTeacher.setLastName("NewLastName");
		entityManager.persist(newTeacher);
		Course newCourse = new Course();
		newCourse.setName("NewName");
		newCourse.setTeacher(newTeacher);
		entityManager.persist(newCourse);
		Classroom newClassroom = new Classroom();
		newClassroom.setName("NewName");
		entityManager.persist(newClassroom);

		createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName", "TeacherFirstName",
				"TeacherLastName");
		LessonDTO initialDTO = lessonService.findAll().get(0);
		LessonDTO newDTO = new LessonDTO();
		newDTO.setClassroom(classroomService.retrieve("NewName"));
		newDTO.setCourse(courseService.retrieve("NewName"));
		newDTO.setDuration(123456L);
		newDTO.setGroup(groupService.retrieve("NewName"));
		newDTO.setStartTime(9876543L);
		lessonService.update(initialDTO, newDTO);
		assertEquals(newDTO, lessonService.findAll().get(0));
	}

	@Test
	void testDelete() {
		List<LessonDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName"));
		expectedList.add(createDTO("Group1", "Course1", "Class1", "StudentFirstNameA", "StudentLastNameA",
				"TeacherFirstNameA", "TeacherLastNameA"));
		expectedList.add(createDTO("Group2", "Course2", "Class2", "StudentFirstNameB", "StudentLastNameB",
				"TeacherFirstNameB", "TeacherLastNameB"));
		List<LessonDTO> listBeforeDelete = lessonService.findAll();
		assertEquals(expectedList, listBeforeDelete);
		lessonService.delete(expectedList.get(0));
		List<LessonDTO> listAfterDelete = lessonService.findAll();
		expectedList.remove(0);
		assertEquals(expectedList, listAfterDelete);
		assertEquals(1, listBeforeDelete.size() - listAfterDelete.size());
	}

	@Test
	void testDeleteByClassroom() {
		List<LessonDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName"));
		expectedList.add(createDTO("Group1", "Course1", "Class1", "StudentFirstNameA", "StudentLastNameA",
				"TeacherFirstNameA", "TeacherLastNameA"));
		expectedList.add(createDTO("Group2", "Course2", "Class2", "StudentFirstNameB", "StudentLastNameB",
				"TeacherFirstNameB", "TeacherLastNameB"));
		List<LessonDTO> listBeforeDelete = lessonService.findAll();
		assertEquals(expectedList, listBeforeDelete);
		lessonService.deleteByClassroom(expectedList.get(0).getClassroom());
		expectedList.remove(0);
		List<LessonDTO> listAfterDelete = lessonService.findAll();
		assertEquals(expectedList, listAfterDelete);
		assertEquals(1, listBeforeDelete.size() - listAfterDelete.size());
	}

	@Test
	void testDeleteByCourse() {
		List<LessonDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName"));
		expectedList.add(createDTO("Group1", "Course1", "Class1", "StudentFirstNameA", "StudentLastNameA",
				"TeacherFirstNameA", "TeacherLastNameA"));
		expectedList.add(createDTO("Group2", "Course2", "Class2", "StudentFirstNameB", "StudentLastNameB",
				"TeacherFirstNameB", "TeacherLastNameB"));
		List<LessonDTO> listBeforeDelete = lessonService.findAll();
		assertEquals(expectedList, listBeforeDelete);
		lessonService.deleteByCourse(expectedList.get(0).getCourse());
		expectedList.remove(0);
		List<LessonDTO> listAfterDelete = lessonService.findAll();
		assertEquals(expectedList, listAfterDelete);
		assertEquals(1, listBeforeDelete.size() - listAfterDelete.size());
	}

	@Test
	void testDeleteByGroup() {
		List<LessonDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName"));
		expectedList.add(createDTO("Group1", "Course1", "Class1", "StudentFirstNameA", "StudentLastNameA",
				"TeacherFirstNameA", "TeacherLastNameA"));
		expectedList.add(createDTO("Group2", "Course2", "Class2", "StudentFirstNameB", "StudentLastNameB",
				"TeacherFirstNameB", "TeacherLastNameB"));
		List<LessonDTO> listBeforeDelete = lessonService.findAll();
		assertEquals(expectedList, listBeforeDelete);
		lessonService.deleteByGroup(expectedList.get(0).getGroup());
		expectedList.remove(0);
		List<LessonDTO> listAfterDelete = lessonService.findAll();
		assertEquals(expectedList, listAfterDelete);
		assertEquals(1, listBeforeDelete.size() - listAfterDelete.size());
	}

	@Test
	void testConvertDateToMil() {
		long expectedMillis = 1628011620000l;
		String date = "03-08-2021 20:27";
		long resultMillis = lessonService.convertDateToMil(date);
		assertEquals(expectedMillis, resultMillis);
		assertNotEquals(123456l, resultMillis);
	}

	@Test
	void testConvertTimeToMil() {
		long expectedMillis = 73620000l;
		String date = "20:27";
		long resultMillis = lessonService.convertTimeToMil(date);
		assertEquals(expectedMillis, resultMillis);
		assertNotEquals(123456l, resultMillis);
	}

	@Test
	void testEventListForCalendarCreator() {
		List<LessonDTO> listOfLessons = new ArrayList<>();
		listOfLessons.add(createDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName"));
		List<Event> expectedEventList = new ArrayList<>();
		expectedEventList.add(
				new Event("Group: Group, Course: Course, Classroom: Class, Teacher: TeacherFirstName TeacherLastName",
						111111L, 113333l));
		assertEquals(expectedEventList, lessonService.eventListForCalendarCreator(listOfLessons));
	}

	private LessonDTO createDTO(String groupName, String courseName, String classroomName, String sFirstName,
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
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setClassroom(classroomService.retrieve(classroomName));
		lessonDTO.setCourse(courseService.retrieve(courseName));
		lessonDTO.setGroup(groupService.retrieve(groupName));
		lessonDTO.setStartTime(111111L);
		lessonDTO.setDuration(2222L);
		lessonService.create(lessonDTO);
		return lessonDTO;
	}

}
