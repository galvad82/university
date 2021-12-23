package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

	private static final String FIRST_NAME = "FirstName";
	private static final String LAST_NAME = "LastName";

	@Mock
	private ModelMapper mockModelMapper;

	@Mock
	private TeacherDAO mockTeacherDAO;

	@Mock
	private LessonService mockLessonService;

	@InjectMocks
	private TeacherService teacherService;

	@Test
	void testCreate() {
		TeacherDTO teacherDTO = createDTO(FIRST_NAME, LAST_NAME);
		Teacher teacherEntity = new Teacher(1, FIRST_NAME, LAST_NAME);
		when(mockModelMapper.map(teacherDTO, Teacher.class)).thenReturn(teacherEntity);
		teacherService.create(teacherDTO);
		verify(mockTeacherDAO, times(1)).create(teacherEntity);
	}

	@Test
	void testRetrieve() {
		TeacherDTO teacherDTO = createDTO(FIRST_NAME, LAST_NAME);
		Teacher teacherEntity = new Teacher(1, FIRST_NAME, LAST_NAME);
		when(mockTeacherDAO.retrieve(FIRST_NAME, LAST_NAME)).thenReturn(teacherEntity);
		when(mockModelMapper.map(teacherEntity, TeacherDTO.class)).thenReturn(teacherDTO);
		teacherService.retrieve(FIRST_NAME, LAST_NAME);
		verify(mockTeacherDAO, times(1)).retrieve(FIRST_NAME, LAST_NAME);
	}

	@Test
	void testUpdate() {
		TeacherDTO oldDTO = createDTO(FIRST_NAME, LAST_NAME);
		TeacherDTO newDTO = createDTO("NewFName", "NewLName");
		Teacher oldEntity = new Teacher(1, FIRST_NAME, LAST_NAME);
		Teacher newEntity = new Teacher(1, "NewFName", "NewLName");
		when(mockModelMapper.map(newDTO, Teacher.class)).thenReturn(newEntity);
		when(mockModelMapper.map(oldDTO, Teacher.class)).thenReturn(oldEntity);
		when(mockTeacherDAO.getId(oldEntity)).thenReturn(1);
		teacherService.update(oldDTO, newDTO);
		verify(mockTeacherDAO, times(1)).update(newEntity);
	}

	@Test
	void testDelete() {
		TeacherDTO teacherDTO = createDTO(FIRST_NAME, LAST_NAME);
		Teacher teacherEntity = new Teacher(1, FIRST_NAME, LAST_NAME);
		when(mockModelMapper.map(teacherDTO, Teacher.class)).thenReturn(teacherEntity);
		when(mockTeacherDAO.getId(teacherEntity)).thenReturn(teacherEntity.getId());
		teacherService.delete(teacherDTO);
		verify(mockTeacherDAO, times(1)).delete(teacherEntity);
	}

	@Test
	void testFindAll() {
		TeacherDTO teacherDTO = createDTO(FIRST_NAME, LAST_NAME);
		Teacher teacherEntity = new Teacher(1, FIRST_NAME, LAST_NAME);
		List<Teacher> list = new ArrayList<>();
		list.add(teacherEntity);
		when(mockTeacherDAO.findAll()).thenReturn(list);
		when(mockModelMapper.map(teacherEntity, TeacherDTO.class)).thenReturn(teacherDTO);
		teacherService.findAll();
		verify(mockTeacherDAO, times(1)).findAll();
	}

	@Test
	void testFindAllLessonsForTeacher() {
		TeacherDTO teacherDTO = createDTO(FIRST_NAME, LAST_NAME);
		Teacher teacherEntity = new Teacher(1, FIRST_NAME, LAST_NAME);
		when(mockTeacherDAO.retrieve(FIRST_NAME, LAST_NAME)).thenReturn(teacherEntity);
		when(mockModelMapper.map(teacherEntity, TeacherDTO.class)).thenReturn(teacherDTO);
		Lesson lessonEntity = createLesson("G1", "Marketing", "A-101", FIRST_NAME, LAST_NAME, FIRST_NAME, LAST_NAME);
		List<Lesson> list = new ArrayList<>();
		list.add(lessonEntity);
		LessonDTO lessonDTO = createLessonDTO("G1", "Marketing", "A-101", FIRST_NAME, LAST_NAME, FIRST_NAME, LAST_NAME);
		List<LessonDTO> listOfLessonDTO = new ArrayList<>();
		listOfLessonDTO.add(lessonDTO);
		when(mockLessonService.findAll()).thenReturn(listOfLessonDTO);
		List<LessonDTO> retrievedList = teacherService.findAllLessonsForTeacher(FIRST_NAME, LAST_NAME);
		verify(mockTeacherDAO, times(1)).retrieve(FIRST_NAME, LAST_NAME);
		assertEquals(retrievedList, listOfLessonDTO);
	}

	private TeacherDTO createDTO(String firstName, String LastName) {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(firstName);
		teacherDTO.setLastName(LastName);
		return teacherDTO;
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
