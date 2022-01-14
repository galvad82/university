package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Event;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;
import ua.com.foxminded.galvad.university.repository.LessonRepository;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

	private static final String GROUP_NAME = "G1";
	private static final String COURSE_NAME = "Marketing";
	private static final String CLASSROOM_NAME = "A-101";
	private static final String FIRST_NAME = "FirstName";
	private static final String LAST_NAME = "LastName";

	@Mock
	private ModelMapper mockModelMapper;

	@Mock
	private ClassroomService mockClassroomService;

	@Mock
	private CourseService mockCourseService;

	@Mock
	private GroupService mockGroupService;

	@Mock
	private LessonRepository mockLessonRepository;

	@InjectMocks
	private LessonService lessonService;

	@Test
	void testCreate() {
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		lessonEntity.setId(null);
		when(mockGroupService.convertToEntity(lessonDTO.getGroup())).thenReturn(lessonEntity.getGroup());
		when(mockCourseService.convertToEntity(lessonDTO.getCourse())).thenReturn(lessonEntity.getCourse());
		when(mockClassroomService.convertToEntity(lessonDTO.getClassroom())).thenReturn(lessonEntity.getClassroom());
		when(mockLessonRepository.save(lessonEntity)).thenReturn(lessonEntity);
		when(mockModelMapper.map(lessonEntity, LessonDTO.class)).thenReturn(lessonDTO);
		lessonService.create(lessonDTO);
		verify(mockLessonRepository, times(1)).save(any(Lesson.class));
	}

	@Test
	void testRetrieveByFindAll() {
		List<LessonDTO> listOfLessonDTO = new ArrayList<>();
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		listOfLessonDTO.add(lessonDTO);
		List<Lesson> listOfLessonEntities = new ArrayList<>();
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		listOfLessonEntities.add(lessonEntity);
		when(mockLessonRepository.findAll()).thenReturn(listOfLessonEntities);
		when(mockModelMapper.map(lessonEntity, LessonDTO.class)).thenReturn(lessonDTO);
		List<LessonDTO> retrievedListDTO = lessonService.findAll();
		verify(mockLessonRepository, times(1)).findAll();
		assertEquals(retrievedListDTO, listOfLessonDTO);
	}

	@Test
	void testUpdate() {
		LessonDTO oldDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		Lesson oldEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		LessonDTO newDTO = createLessonDTO("NewName", "NewName", "NewName", "NewFirstName", "NewLastName",
				"NewFirstName", "NewLastName");
		Lesson newEntity = createLesson("NewName", "NewName", "NewName", "NewFirstName", "NewLastName", "NewFirstName",
				"NewLastName");
		when(mockGroupService.convertToEntity(newDTO.getGroup())).thenReturn(newEntity.getGroup());
		when(mockCourseService.convertToEntity(newDTO.getCourse())).thenReturn(newEntity.getCourse());
		when(mockClassroomService.convertToEntity(newDTO.getClassroom())).thenReturn(newEntity.getClassroom());
		when(mockGroupService.convertToEntity(oldDTO.getGroup())).thenReturn(oldEntity.getGroup());
		when(mockCourseService.convertToEntity(oldDTO.getCourse())).thenReturn(oldEntity.getCourse());
		when(mockClassroomService.convertToEntity(oldDTO.getClassroom())).thenReturn(oldEntity.getClassroom());
		when(mockLessonRepository.getId(any(Lesson.class))).thenReturn(1);
		when(mockLessonRepository.save(newEntity)).thenReturn(newEntity);
		when(mockModelMapper.map(newEntity, LessonDTO.class)).thenReturn(newDTO);
		lessonService.update(oldDTO, newDTO);
		verify(mockLessonRepository, times(1)).save(any(Lesson.class));
	}

	@Test
	void testDelete() {
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		when(mockGroupService.convertToEntity(lessonDTO.getGroup())).thenReturn(lessonEntity.getGroup());
		when(mockCourseService.convertToEntity(lessonDTO.getCourse())).thenReturn(lessonEntity.getCourse());
		when(mockClassroomService.convertToEntity(lessonDTO.getClassroom())).thenReturn(lessonEntity.getClassroom());
		when(mockLessonRepository.getId(any(Lesson.class))).thenReturn(1);
		lessonService.delete(lessonDTO);
		verify(mockLessonRepository, times(1)).delete(lessonEntity);
	}

	@Test
	void testFindAllLessonsForClassroom() {
		List<LessonDTO> listOfLessonDTO = new ArrayList<>();
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		listOfLessonDTO.add(lessonDTO);
		List<Lesson> listOfLessonEntities = new ArrayList<>();
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		listOfLessonEntities.add(lessonEntity);
		when(mockLessonRepository.findAll()).thenReturn(listOfLessonEntities);
		when(mockModelMapper.map(lessonEntity, LessonDTO.class)).thenReturn(lessonDTO);
		List<LessonDTO> retrievedListDTO = lessonService.findAllLessonsForClassroom(CLASSROOM_NAME);
		assertEquals(retrievedListDTO, listOfLessonDTO);
	}

	@Test
	void testFindAllLessonsForCourse() {
		List<LessonDTO> listOfLessonDTO = new ArrayList<>();
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		listOfLessonDTO.add(lessonDTO);
		List<Lesson> listOfLessonEntities = new ArrayList<>();
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		listOfLessonEntities.add(lessonEntity);
		when(mockLessonRepository.findAll()).thenReturn(listOfLessonEntities);
		when(mockModelMapper.map(lessonEntity, LessonDTO.class)).thenReturn(lessonDTO);
		List<LessonDTO> retrievedListDTO = lessonService.findAllLessonsForCourse(COURSE_NAME);
		assertEquals(retrievedListDTO, listOfLessonDTO);
	}

	@Test
	void testFindAllLessonsForGroup() {
		List<LessonDTO> listOfLessonDTO = new ArrayList<>();
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		listOfLessonDTO.add(lessonDTO);
		List<Lesson> listOfLessonEntities = new ArrayList<>();
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		listOfLessonEntities.add(lessonEntity);
		when(mockLessonRepository.findAll()).thenReturn(listOfLessonEntities);
		when(mockModelMapper.map(lessonEntity, LessonDTO.class)).thenReturn(lessonDTO);
		List<LessonDTO> retrievedListDTO = lessonService.findAllLessonsForGroup(GROUP_NAME);
		assertEquals(retrievedListDTO, listOfLessonDTO);
	}

	@Test
	void testDeleteByClassroom() {
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		when(mockClassroomService.convertToEntity(lessonDTO.getClassroom())).thenReturn(lessonEntity.getClassroom());
		lessonService.deleteByClassroom(lessonDTO.getClassroom());
		verify(mockLessonRepository, times(1)).deleteByClassroom(lessonEntity.getClassroom());
	}

	@Test
	void testDeleteByCourse() {
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		when(mockCourseService.convertToEntity(lessonDTO.getCourse())).thenReturn(lessonEntity.getCourse());
		lessonService.deleteByCourse(lessonDTO.getCourse());
		verify(mockLessonRepository, times(1)).deleteByCourse(lessonEntity.getCourse());
	}

	@Test
	void testDeleteByGroup() {
		LessonDTO lessonDTO = createLessonDTO(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME,
				FIRST_NAME, LAST_NAME);
		Lesson lessonEntity = createLesson(GROUP_NAME, COURSE_NAME, CLASSROOM_NAME, FIRST_NAME, LAST_NAME, FIRST_NAME,
				LAST_NAME);
		when(mockGroupService.convertToEntity(lessonDTO.getGroup())).thenReturn(lessonEntity.getGroup());
		lessonService.deleteByGroup(lessonDTO.getGroup());
		verify(mockLessonRepository, times(1)).deleteByGroup(lessonEntity.getGroup());
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
		listOfLessons.add(createLessonDTO("Group", "Course", "Class", "StudentFirstName", "StudentLastName",
				"TeacherFirstName", "TeacherLastName"));
		List<Event> expectedEventList = new ArrayList<>();
		expectedEventList.add(
				new Event("Group: Group, Course: Course, Classroom: Class, Teacher: TeacherFirstName TeacherLastName",
						111111L, 113333l));
		assertEquals(expectedEventList, lessonService.eventListForCalendarCreator(listOfLessons));
	}

	private LessonDTO createLessonDTO(String groupName, String courseName, String classroomName, String sFirstName,
			String sLastName, String tFirstName, String tLastName) {
		StudentDTO student = new StudentDTO();
		student.setFirstName(sFirstName);
		student.setLastName(sLastName);
		GroupDTO group = new GroupDTO();
		group.setName(groupName);
		List<StudentDTO> listOfStudents = new ArrayList<>();
		listOfStudents.add(student);
		group.setListOfStudent(listOfStudents);
		TeacherDTO teacher = new TeacherDTO();
		teacher.setFirstName(tFirstName);
		teacher.setLastName(tLastName);
		CourseDTO course = new CourseDTO();
		course.setName(courseName);
		course.setTeacher(teacher);
		ClassroomDTO classroom = new ClassroomDTO();
		classroom.setName(classroomName);
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setClassroom(classroom);
		lessonDTO.setCourse(course);
		lessonDTO.setGroup(group);
		lessonDTO.setStartTime(111111L);
		lessonDTO.setDuration(2222L);
		return lessonDTO;
	}

	private Lesson createLesson(String groupName, String courseName, String classroomName, String sFirstName,
			String sLastName, String tFirstName, String tLastName) {
		Student student = new Student(1, sFirstName, sLastName);
		Group group = new Group(1, groupName);
		Set<Student> setOfStudents = new HashSet<>();
		setOfStudents.add(student);
		group.setSetOfStudent(setOfStudents);
		Teacher teacher = new Teacher(1, tFirstName, tLastName);
		Course course = new Course(1, courseName);
		course.setTeacher(teacher);
		Classroom classroom = new Classroom(1, classroomName);
		return new Lesson(1, group, course, classroom, 111111L, 2222L);
	}

}
