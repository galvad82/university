package ua.com.foxminded.galvad.university.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

	private static final String NAME = "NAME";
	private static final String NEWNAME = "NEWNAME";

	@Mock
	private CourseDAO mockCourseDAO;

	@Mock
	private TeacherDAO mockTeacherDAO;

	@Mock
	private LessonDAO mockLessonDAO;

	@Mock
	private ModelMapper mockModelMapper;

	@InjectMocks
	private CourseService courseService;

	@Test
	void testCreate() {
		CourseDTO courseDTO = createDTO(NAME);
		Course course = new Course(1, NAME);
		Teacher teacher = new Teacher(1, NAME, NAME);
		course.setTeacher(teacher);
		when(mockModelMapper.map(courseDTO, Course.class)).thenReturn(course);
		when(mockTeacherDAO.getId(teacher)).thenReturn(1);
		courseService.create(courseDTO);
		verify(mockCourseDAO, times(1)).create(any(Course.class));
	}

	@Test
	void testRetrieve() {
		CourseDTO courseDTO = createDTO(NAME);
		Course course = new Course(1, NAME);
		Teacher teacherWithIDOnly = new Teacher(1);
		course.setTeacher(teacherWithIDOnly);

		Teacher teacher = new Teacher(1, NAME, NAME);
		Course courseWithTeacherSet = course;
		courseWithTeacherSet.setTeacher(teacher);

		when(mockCourseDAO.retrieve(NAME)).thenReturn(course);
		when(mockTeacherDAO.retrieve(course.getTeacher().getId())).thenReturn(teacher);
		when(mockModelMapper.map(courseWithTeacherSet, CourseDTO.class)).thenReturn(courseDTO);
		courseService.retrieve(NAME);
		verify(mockCourseDAO, times(1)).retrieve(NAME);
		verify(mockTeacherDAO, times(2)).retrieve(course.getTeacher().getId());
	}

	@Test
	void testUpdate() {
		CourseDTO oldDTO = createDTO(NAME);
		CourseDTO newDTO = new CourseDTO();
		newDTO.setName(NEWNAME);
		newDTO.setTeacher(oldDTO.getTeacher());
		Course oldCourseEntity = new Course(1, NAME);
		Course newCourseEntity = new Course(1, NEWNAME);
		Teacher teacher = new Teacher(1, NAME, NAME);
		oldCourseEntity.setTeacher(teacher);
		newCourseEntity.setTeacher(teacher);

		when(mockModelMapper.map(newDTO, Course.class)).thenReturn(newCourseEntity);
		when(mockTeacherDAO.getId(newCourseEntity.getTeacher())).thenReturn(teacher.getId());
		when(mockModelMapper.map(oldDTO, Course.class)).thenReturn(oldCourseEntity);
		when(mockCourseDAO.getId(any(Course.class))).thenReturn(1);
		courseService.update(oldDTO, newDTO);
		verify(mockCourseDAO, times(1)).update(newCourseEntity);

	}

	@Test
	void testDelete() {
		CourseDTO DTO = createDTO(NAME);
		Course courseEntity = new Course(1, NEWNAME);
		Teacher teacherEntity = new Teacher(1, NAME, NAME);
		courseEntity.setTeacher(teacherEntity);
		when(mockModelMapper.map(DTO, Course.class)).thenReturn(courseEntity);
		when(mockTeacherDAO.getId(any(Teacher.class))).thenReturn(1);
		when(mockCourseDAO.getId(any(Course.class))).thenReturn(1);
		courseService.delete(DTO);
		verify(mockLessonDAO, times(1)).deleteByCourseID(1);
		verify(mockCourseDAO, times(1)).delete(courseEntity);
	}

	@Test
	void testFindAll() {
		CourseDTO DTO = createDTO(NAME);
		Course courseEntity = new Course(1, NEWNAME);
		Teacher teacherEntity = new Teacher(1, NAME, NAME);
		courseEntity.setTeacher(teacherEntity);
		List<Course> listOfCourses = new ArrayList<>();
		listOfCourses.add(courseEntity);
		when(mockCourseDAO.findAll()).thenReturn(listOfCourses);
		when(mockModelMapper.map(courseEntity, CourseDTO.class)).thenReturn(DTO);
		when(mockTeacherDAO.retrieve(1)).thenReturn(teacherEntity);
		when(mockModelMapper.map(teacherEntity, TeacherDTO.class)).thenReturn(DTO.getTeacher());
		courseService.findAll();
		verify(mockCourseDAO, times(1)).findAll();
	}

	private CourseDTO createDTO(String name) {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName(name);
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(name);
		teacherDTO.setLastName(name);
		courseDTO.setTeacher(teacherDTO);
		return courseDTO;
	}

}
