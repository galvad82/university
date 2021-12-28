package ua.com.foxminded.galvad.university.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;
import ua.com.foxminded.galvad.university.repository.CourseRepository;
import ua.com.foxminded.galvad.university.repository.LessonRepository;
import ua.com.foxminded.galvad.university.repository.TeacherRepository;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

	private static final String NAME = "NAME";
	private static final String NEWNAME = "NEWNAME";

	@Mock
	private CourseRepository mockCourseRepository;

	@Mock
	private TeacherRepository mockTeacherRepository;

	@Mock
	private LessonRepository mockLessonRepository;

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
		when(mockTeacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()))
				.thenReturn(teacher);
		courseService.create(courseDTO);
		verify(mockCourseRepository, times(1)).save(any(Course.class));
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

		when(mockCourseRepository.findByName(NAME)).thenReturn(course);
		when(mockTeacherRepository.findById(course.getTeacher().getId())).thenReturn(Optional.of(teacher));
		when(mockModelMapper.map(courseWithTeacherSet, CourseDTO.class)).thenReturn(courseDTO);
		courseService.retrieve(NAME);
		verify(mockCourseRepository, times(1)).findByName(NAME);
		verify(mockTeacherRepository, times(2)).findById(course.getTeacher().getId());
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
		when(mockTeacherRepository.findByFirstNameAndLastName(newCourseEntity.getTeacher().getFirstName(),
				newCourseEntity.getTeacher().getLastName())).thenReturn(teacher);
		when(mockModelMapper.map(oldDTO, Course.class)).thenReturn(oldCourseEntity);
		when(mockCourseRepository.findByName(NAME)).thenReturn(oldCourseEntity);
		courseService.update(oldDTO, newDTO);
		verify(mockCourseRepository, times(1)).save(newCourseEntity);

	}

	@Test
	void testDelete() {
		CourseDTO DTO = createDTO(NAME);
		Course courseEntity = new Course(1, NAME);
		Teacher teacherEntity = new Teacher(1, NAME, NAME);
		courseEntity.setTeacher(teacherEntity);
		when(mockModelMapper.map(DTO, Course.class)).thenReturn(courseEntity);
		when(mockTeacherRepository.findByFirstNameAndLastName(NAME, NAME)).thenReturn(teacherEntity);
		when(mockCourseRepository.findByName(NAME)).thenReturn(courseEntity);
		courseService.delete(DTO);
		verify(mockLessonRepository, times(1)).deleteByCourse(courseEntity);
		verify(mockCourseRepository, times(1)).delete(courseEntity);
	}

	@Test
	void testFindAll() {
		CourseDTO DTO = createDTO(NAME);
		Course courseEntity = new Course(1, NEWNAME);
		Teacher teacherEntity = new Teacher(1, NAME, NAME);
		courseEntity.setTeacher(teacherEntity);
		List<Course> listOfCourses = new ArrayList<>();
		listOfCourses.add(courseEntity);
		when(mockCourseRepository.findAll()).thenReturn(listOfCourses);
		when(mockModelMapper.map(courseEntity, CourseDTO.class)).thenReturn(DTO);
		when(mockTeacherRepository.findById(1)).thenReturn(Optional.of(teacherEntity));
		when(mockModelMapper.map(teacherEntity, TeacherDTO.class)).thenReturn(DTO.getTeacher());
		courseService.findAll();
		verify(mockCourseRepository, times(1)).findAll();
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
