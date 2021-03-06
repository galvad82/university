package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.model.Event;
import ua.com.foxminded.galvad.university.services.ClassroomService;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.LessonService;
import ua.com.foxminded.galvad.university.services.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
class ScheduleControllerTest {

	private static final String SCHEDULE_RESULT = "schedule/result";
	private static final String FIRST = "First";
	private static final String SECOND = "Second";
	private static final String FIRST_NAME = "FirstName";
	private static final String LAST_NAME = "LastName";

	@Mock
	ClassroomService classroomServiceMock;
	@Mock
	TeacherService teacherServiceMock;
	@Mock
	CourseService courseServiceMock;
	@Mock
	GroupService groupServiceMock;
	@Mock
	LessonService lessonServiceMock;

	@InjectMocks
	ScheduleController scheduleControllerUnderTest;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(scheduleControllerUnderTest).build();
	}

	@Test
	void testTeacherView() throws Exception {
		List<TeacherDTO> listOfTeachers = createListOfTeachers();
		when(teacherServiceMock.findAll()).thenReturn(listOfTeachers);
		mockMvc.perform(get("/schedule/teacher")).andExpect(status().isOk()).andExpect(view().name("schedule/teacher"))
				.andExpect(model().attribute("firstName", "")).andExpect(model().attribute("lastName", ""))
				.andExpect(model().attribute("listOfTeachers", listOfTeachers));
	}

	@Test
	void testTeacherResultView() throws Exception {
		List<Event> eventList = createEventList(createListOfLessons());
		when(lessonServiceMock
				.eventListForCalendarCreator(teacherServiceMock.findAllLessonsForTeacher(FIRST_NAME, LAST_NAME)))
						.thenReturn(eventList);
		mockMvc.perform(post("/schedule/teacher/result").param("firstName", FIRST_NAME).param("lastName", LAST_NAME))
				.andExpect(status().isOk()).andExpect(view().name(SCHEDULE_RESULT))
				.andExpect(model().attribute("lessons", eventList));
	}

	@Test
	void testGroupView() throws Exception {
		List<GroupDTO> listOfGroups = createListOfGroups();
		when(groupServiceMock.findAll()).thenReturn(listOfGroups);
		mockMvc.perform(get("/schedule/group")).andExpect(status().isOk()).andExpect(view().name("schedule/group"))
				.andExpect(model().attribute("listOfGroups", listOfGroups));
	}

	@Test
	void testGroupResultView() throws Exception {
		List<Event> eventList = createEventList(createListOfLessons());
		when(lessonServiceMock.eventListForCalendarCreator(lessonServiceMock.findAllLessonsForGroup(FIRST)))
				.thenReturn(eventList);
		mockMvc.perform(post("/schedule/group/result").param("group", FIRST)).andExpect(status().isOk())
				.andExpect(view().name(SCHEDULE_RESULT)).andExpect(model().attribute("lessons", eventList));
	}

	@Test
	void testGroupResultViewWithDataAreNotUpdatedException() throws Exception {
		DataAreNotUpdatedException expectedException = new DataAreNotUpdatedException("Error Message");
		when(lessonServiceMock.eventListForCalendarCreator(lessonServiceMock.findAllLessonsForGroup(FIRST)))
				.thenThrow(expectedException);
		mockMvc.perform(post("/schedule/group/result").param("group", FIRST))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testGroupResultViewWithDataNotFoundException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(lessonServiceMock.eventListForCalendarCreator(lessonServiceMock.findAllLessonsForGroup(FIRST)))
				.thenThrow(expectedException);
		mockMvc.perform(post("/schedule/group/result").param("group", FIRST))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testClassroomView() throws Exception {
		List<ClassroomDTO> listOfClassrooms = createListOfClassrooms();
		when(classroomServiceMock.findAll()).thenReturn(listOfClassrooms);
		mockMvc.perform(get("/schedule/classroom")).andExpect(status().isOk())
				.andExpect(view().name("schedule/classroom"))
				.andExpect(model().attribute("listOfClassrooms", listOfClassrooms));
	}

	@Test
	void testClassroomResultView() throws Exception {
		List<Event> eventList = createEventList(createListOfLessons());
		when(lessonServiceMock.eventListForCalendarCreator(lessonServiceMock.findAllLessonsForClassroom(FIRST)))
				.thenReturn(eventList);
		mockMvc.perform(post("/schedule/classroom/result").param("classroom", FIRST)).andExpect(status().isOk())
				.andExpect(view().name(SCHEDULE_RESULT)).andExpect(model().attribute("lessons", eventList));
	}

	@Test
	void testCourseView() throws Exception {
		List<CourseDTO> listOfCourses = createListOfCourses();
		when(courseServiceMock.findAll()).thenReturn(listOfCourses);
		mockMvc.perform(get("/schedule/course")).andExpect(status().isOk()).andExpect(view().name("schedule/course"))
				.andExpect(model().attribute("listOfCourses", listOfCourses));
	}

	@Test
	void testCourseResultView() throws Exception {
		List<Event> eventList = createEventList(createListOfLessons());
		when(lessonServiceMock.eventListForCalendarCreator(lessonServiceMock.findAllLessonsForCourse(FIRST)))
				.thenReturn(eventList);
		mockMvc.perform(post("/schedule/course/result").param("course", FIRST)).andExpect(status().isOk())
				.andExpect(view().name(SCHEDULE_RESULT)).andExpect(model().attribute("lessons", eventList));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/schedule/wrong")).andExpect(status().is4xxClientError());
	}

	private List<Event> createEventList(List<LessonDTO> listOfLessons) {
		List<Event> eventList = new ArrayList<>();
		listOfLessons.stream().forEach(lesson -> {
			String title = String.format("Group: %s, Course: %s, Classroom: %s, Teacher: %s %s",
					lesson.getGroup().getName(), lesson.getCourse().getName(), lesson.getClassroom().getName(),
					lesson.getCourse().getTeacher().getFirstName(), lesson.getCourse().getTeacher().getLastName());
			eventList.add(new Event(title, lesson.getStartTime(), lesson.getStartTime() + lesson.getDuration()));
		});
		return eventList;
	}

	private List<LessonDTO> createListOfLessons() {
		List<LessonDTO> listOfLessons = new ArrayList<>();
		LessonDTO firstLessonDTO = new LessonDTO();
		firstLessonDTO.setClassroom(createClassroom(FIRST));
		firstLessonDTO.setCourse(createCourse(FIRST));
		firstLessonDTO.setGroup(createGroup(FIRST));
		firstLessonDTO.setDuration(1l);
		firstLessonDTO.setStartTime(1l);

		LessonDTO secondLessonDTO = new LessonDTO();
		secondLessonDTO.setClassroom(createClassroom(SECOND));
		secondLessonDTO.setCourse(createCourse(SECOND));
		secondLessonDTO.setGroup(createGroup(SECOND));
		secondLessonDTO.setDuration(2l);
		secondLessonDTO.setStartTime(2l);
		listOfLessons.add(firstLessonDTO);
		listOfLessons.add(secondLessonDTO);
		return listOfLessons;
	}

	private List<TeacherDTO> createListOfTeachers() {
		List<TeacherDTO> listOfTeachers = new ArrayList<>();
		listOfTeachers.add(createTeacher(FIRST));
		listOfTeachers.add(createTeacher(SECOND));
		return listOfTeachers;
	}

	private List<GroupDTO> createListOfGroups() {
		List<GroupDTO> listOfGroups = new ArrayList<>();
		listOfGroups.add(createGroup(FIRST));
		listOfGroups.add(createGroup(SECOND));
		return listOfGroups;
	}

	private List<ClassroomDTO> createListOfClassrooms() {
		List<ClassroomDTO> listOfClassrooms = new ArrayList<>();
		listOfClassrooms.add(createClassroom(FIRST));
		listOfClassrooms.add(createClassroom(SECOND));
		return listOfClassrooms;
	}

	private List<CourseDTO> createListOfCourses() {
		List<CourseDTO> listOfCourses = new ArrayList<>();
		listOfCourses.add(createCourse(FIRST));
		listOfCourses.add(createCourse(SECOND));
		return listOfCourses;
	}

	private TeacherDTO createTeacher(String name) {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(FIRST_NAME);
		teacherDTO.setLastName(LAST_NAME);
		return teacherDTO;
	}

	private GroupDTO createGroup(String name) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(name);
		return groupDTO;
	}

	private CourseDTO createCourse(String name) {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName(name);
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("FirstName");
		teacherDTO.setLastName("LastName");
		courseDTO.setTeacher(teacherDTO);
		return courseDTO;
	}

	private ClassroomDTO createClassroom(String name) {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName(name);
		return classroomDTO;
	}
}
