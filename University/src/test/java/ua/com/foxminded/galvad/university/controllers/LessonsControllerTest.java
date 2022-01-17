package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.ClassroomService;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.LessonService;

@SpringBootTest
@AutoConfigureMockMvc
class LessonsControllerTest {

	@Mock
	LessonService lessonServiceMock;

	@Mock
	GroupService groupServiceMock;

	@Mock
	CourseService courseServiceMock;

	@Mock
	ClassroomService classroomServiceMock;

	@InjectMocks
	LessonsController lessonsControllerUnderTest;

	MockMvc mockMvc;

	private static final String GROUP_NAME = "group";
	private static final String COURSE_NAME = "course";
	private static final String CLASSROOM_NAME = "classroom";
	private static final long START_TIME = 1628485200000l;
	private static final long DURATION = 3600000l;
	private static final long START_TIME_NEW = 1633845600000l;
	private static final long DURATION_NEW = 7200000l;
	private static final String START_TIME_STRING = "09-08-2021 08:00";
	private static final String DURATION_STRING = "01:00";
	private static final String START_TIME_STRING_NEW = "10-10-2021 09:00";
	private static final String DURATION_STRING_NEW = "02:00";
	private static final String FIRST = "First";
	private static final String SECOND = "Second";

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(lessonsControllerUnderTest).build();
	}

	@Test
	void testListView_shouldReturnExpectedList() throws Exception {
		GroupDTO groupDTO = createGroup(FIRST);
		CourseDTO courseDTO = createCourse(FIRST);
		ClassroomDTO classroomDTO = createClassroom(FIRST);
		LessonDTO firstLessonDTO = new LessonDTO();
		firstLessonDTO.setGroup(groupDTO);
		firstLessonDTO.setCourse(courseDTO);
		firstLessonDTO.setClassroom(classroomDTO);
		firstLessonDTO.setDuration(DURATION);
		firstLessonDTO.setStartTime(START_TIME);
		LessonDTO secondLessonDTO = new LessonDTO();
		secondLessonDTO.setGroup(groupDTO);
		secondLessonDTO.setCourse(courseDTO);
		secondLessonDTO.setClassroom(classroomDTO);
		secondLessonDTO.setDuration(DURATION);
		secondLessonDTO.setStartTime(START_TIME);
		List<LessonDTO> expectedList = new ArrayList<>();
		expectedList.add(firstLessonDTO);
		expectedList.add(secondLessonDTO);

		when(lessonServiceMock.findAll()).thenReturn(expectedList);
		mockMvc.perform(get("/lessons")).andExpect(status().isOk()).andExpect(view().name("lessons/list"))
				.andExpect(model().attribute("lessons", expectedList));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(lessonServiceMock.findAll()).thenThrow(expectedException);
		mockMvc.perform(get("/lessons"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testAddViewGet() throws Exception {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setStartTime(0);
		lessonDTO.setDuration(0);

		List<String> listOfGroupNames = new ArrayList<>();
		listOfGroupNames.add(FIRST);
		listOfGroupNames.add(SECOND);
		when(groupServiceMock.findAll()).thenReturn(createGroupList());

		List<String> listOfCourseNames = new ArrayList<>();
		listOfCourseNames.add(FIRST);
		listOfCourseNames.add(SECOND);
		when(courseServiceMock.findAll()).thenReturn(createCourseList());

		List<String> listOfClassroomNames = new ArrayList<>();
		listOfClassroomNames.add(FIRST);
		listOfClassroomNames.add(SECOND);
		when(classroomServiceMock.findAll()).thenReturn(createClassroomList());

		mockMvc.perform(get("/lessons/add")).andExpectAll(model().attribute("groups", listOfGroupNames),
				model().attribute("courses", listOfCourseNames), model().attribute("classrooms", listOfClassroomNames),
				model().attribute("startTime", "01-01-2021 02:00"), model().attribute("duration", "00:30"))
				.andExpect(view().name("lessons/add"));
	}

	@Test
	void testAddViewPostWithNewDTO_shouldReturnResultView() throws Exception {

		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setCourse(createCourse(COURSE_NAME));
		lessonDTO.setGroup(createGroup(GROUP_NAME));
		lessonDTO.setClassroom(createClassroom(CLASSROOM_NAME));
		lessonDTO.setStartTime(START_TIME);
		lessonDTO.setDuration(DURATION);

		when(courseServiceMock.retrieve(COURSE_NAME)).thenReturn(createCourse(COURSE_NAME));
		when(groupServiceMock.retrieve(GROUP_NAME)).thenReturn(createGroup(GROUP_NAME));
		when(classroomServiceMock.retrieve(CLASSROOM_NAME)).thenReturn(createClassroom(CLASSROOM_NAME));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING)).thenReturn(START_TIME);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING)).thenReturn(DURATION);
		when(lessonServiceMock.checkIfExists(lessonDTO)).thenReturn(false);
		RequestBuilder request = post("/lessons/add").flashAttr("group", GROUP_NAME).flashAttr("course", COURSE_NAME)
				.flashAttr("classroom", CLASSROOM_NAME).flashAttr("startTime", START_TIME_STRING)
				.flashAttr("duration", DURATION_STRING);

		mockMvc.perform(request)
				.andExpectAll(model().attribute("lessonDTO", lessonDTO),
						model().attribute("result", "A lesson was successfully added."))
				.andExpect(view().name("lessons/result"));
	}

	@Test
	void testAddViewPostWithExistentDTO_shouldReturnAddView() throws Exception {

		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setCourse(createCourse(COURSE_NAME));
		lessonDTO.setGroup(createGroup(GROUP_NAME));
		lessonDTO.setClassroom(createClassroom(CLASSROOM_NAME));
		lessonDTO.setStartTime(START_TIME);
		lessonDTO.setDuration(DURATION);
		List<String> expectedNameList = Arrays.asList("First", "Second");

		when(courseServiceMock.retrieve(COURSE_NAME)).thenReturn(createCourse(COURSE_NAME));
		when(groupServiceMock.retrieve(GROUP_NAME)).thenReturn(createGroup(GROUP_NAME));
		when(classroomServiceMock.retrieve(CLASSROOM_NAME)).thenReturn(createClassroom(CLASSROOM_NAME));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING)).thenReturn(START_TIME);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING)).thenReturn(DURATION);
		when(groupServiceMock.findAll()).thenReturn(createGroupList());
		when(courseServiceMock.findAll()).thenReturn(createCourseList());
		when(classroomServiceMock.findAll()).thenReturn(createClassroomList());
		when(lessonServiceMock.checkIfExists(lessonDTO)).thenReturn(true);

		RequestBuilder request = post("/lessons/add").flashAttr("group", GROUP_NAME).flashAttr("course", COURSE_NAME)
				.flashAttr("classroom", CLASSROOM_NAME).flashAttr("startTime", START_TIME_STRING)
				.flashAttr("duration", DURATION_STRING);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("error", "The same lesson is already added to the database!"),
						model().attribute("group", GROUP_NAME), model().attribute("course", COURSE_NAME),
						model().attribute("classroom", CLASSROOM_NAME),
						model().attribute("startTime", START_TIME_STRING),
						model().attribute("duration", DURATION_STRING), model().attribute("groups", expectedNameList),
						model().attribute("courses", expectedNameList),
						model().attribute("classrooms", expectedNameList))
				.andExpect(view().name("lessons/add"));
	}

	@Test
	void testAddViewPostWithNonValidDTO_shouldReturnAddView() throws Exception {

		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setCourse(createCourse(COURSE_NAME));
		lessonDTO.setGroup(createGroup(GROUP_NAME));
		lessonDTO.setClassroom(createClassroom(CLASSROOM_NAME));
		lessonDTO.setStartTime(0);
		lessonDTO.setDuration(0);
		List<String> expectedNameList = Arrays.asList("First", "Second");

		when(courseServiceMock.retrieve(COURSE_NAME)).thenReturn(createCourse(COURSE_NAME));
		when(groupServiceMock.retrieve(GROUP_NAME)).thenReturn(createGroup(GROUP_NAME));
		when(classroomServiceMock.retrieve(CLASSROOM_NAME)).thenReturn(createClassroom(CLASSROOM_NAME));
		when(lessonServiceMock.convertDateToMil("0")).thenReturn(0l);
		when(lessonServiceMock.convertTimeToMil("0")).thenReturn(0l);
		when(groupServiceMock.findAll()).thenReturn(createGroupList());
		when(courseServiceMock.findAll()).thenReturn(createCourseList());
		when(classroomServiceMock.findAll()).thenReturn(createClassroomList());

		RequestBuilder request = post("/lessons/add").flashAttr("group", GROUP_NAME).flashAttr("course", COURSE_NAME)
				.flashAttr("classroom", CLASSROOM_NAME).flashAttr("startTime", "0").flashAttr("duration", "0");
		mockMvc.perform(request).andExpectAll(model().attribute("error",
				"The startTime field cannot be blank or earlier than UTC Jan-01-2021 00:00:01!<br>The duration field"
						+ " cannot be blank or less than 30 minutes!"),
				model().attribute("group", GROUP_NAME), model().attribute("course", COURSE_NAME),
				model().attribute("classroom", CLASSROOM_NAME), model().attribute("startTime", "0"),
				model().attribute("duration", "0"), model().attribute("groups", expectedNameList),
				model().attribute("courses", expectedNameList), model().attribute("classrooms", expectedNameList))
				.andExpect(view().name("lessons/add"));
	}

	@Test
	void testEditViewPost() throws Exception {
		List<String> expectedNameList = Arrays.asList("First", "Second");

		when(groupServiceMock.findAll()).thenReturn(createGroupList());
		when(courseServiceMock.findAll()).thenReturn(createCourseList());
		when(classroomServiceMock.findAll()).thenReturn(createClassroomList());
		when(courseServiceMock.retrieve(COURSE_NAME)).thenReturn(createCourse(COURSE_NAME));
		when(groupServiceMock.retrieve(GROUP_NAME)).thenReturn(createGroup(GROUP_NAME));
		when(classroomServiceMock.retrieve(CLASSROOM_NAME)).thenReturn(createClassroom(CLASSROOM_NAME));

		RequestBuilder request = post("/lessons/edit").flashAttr("groupName", GROUP_NAME)
				.flashAttr("courseName", COURSE_NAME).flashAttr("classroomName", CLASSROOM_NAME)
				.flashAttr("startTime", START_TIME_STRING).flashAttr("duration", DURATION_STRING);
		mockMvc.perform(request).andExpectAll(model().attribute("groups", expectedNameList),
				model().attribute("courses", expectedNameList), model().attribute("classrooms", expectedNameList),
				model().attribute("initialGroup", GROUP_NAME), model().attribute("initialCourse", COURSE_NAME),
				model().attribute("initialClassroom", CLASSROOM_NAME),
				model().attribute("initialStartTime", START_TIME_STRING),
				model().attribute("initialDuration", DURATION_STRING)).andExpect(view().name("lessons/edit"));
	}

	@Test
	void testEditResultViewPostWithNewDTO_shouldReturnResultView() throws Exception {
		when(courseServiceMock.retrieve(FIRST)).thenReturn(createCourse(FIRST));
		when(groupServiceMock.retrieve(FIRST)).thenReturn(createGroup(FIRST));
		when(classroomServiceMock.retrieve(FIRST)).thenReturn(createClassroom(FIRST));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING)).thenReturn(START_TIME);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING)).thenReturn(DURATION);

		LessonDTO updatedLessonDTO = new LessonDTO();
		updatedLessonDTO.setGroup(createGroup(SECOND));
		updatedLessonDTO.setClassroom(createClassroom(SECOND));
		updatedLessonDTO.setCourse(createCourse(SECOND));
		updatedLessonDTO.setStartTime(START_TIME_NEW);
		updatedLessonDTO.setDuration(DURATION_NEW);
		when(courseServiceMock.retrieve(SECOND)).thenReturn(createCourse(SECOND));
		when(groupServiceMock.retrieve(SECOND)).thenReturn(createGroup(SECOND));
		when(classroomServiceMock.retrieve(SECOND)).thenReturn(createClassroom(SECOND));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING_NEW)).thenReturn(START_TIME_NEW);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING_NEW)).thenReturn(DURATION_NEW);
		when(lessonServiceMock.checkIfExists(updatedLessonDTO)).thenReturn(false);

		RequestBuilder request = post("/lessons/edit/result").flashAttr("initialGroup", FIRST)
				.flashAttr("initialCourse", FIRST).flashAttr("initialClassroom", FIRST)
				.flashAttr("initialStartTime", START_TIME_STRING).flashAttr("initialDuration", DURATION_STRING)
				.flashAttr("group", SECOND).flashAttr("course", SECOND).flashAttr("classroom", SECOND)
				.flashAttr("startTime", START_TIME_STRING_NEW).flashAttr("duration", DURATION_STRING_NEW);
		mockMvc.perform(request).andExpectAll(model().attribute("result", "Lesson was successfully updated"),
				model().attribute("lessonDTO", updatedLessonDTO)).andExpect(view().name("lessons/result"));
	}

	@Test
	void testEditResultViewPostWithExistedDTO_shouldReturnEditView() throws Exception {
		List<String> expectedNameList = Arrays.asList("First", "Second");
		when(courseServiceMock.retrieve(FIRST)).thenReturn(createCourse(FIRST));
		when(groupServiceMock.retrieve(FIRST)).thenReturn(createGroup(FIRST));
		when(classroomServiceMock.retrieve(FIRST)).thenReturn(createClassroom(FIRST));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING)).thenReturn(START_TIME);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING)).thenReturn(DURATION);

		LessonDTO updatedLessonDTO = new LessonDTO();
		updatedLessonDTO.setGroup(createGroup(SECOND));
		updatedLessonDTO.setClassroom(createClassroom(SECOND));
		updatedLessonDTO.setCourse(createCourse(SECOND));
		updatedLessonDTO.setStartTime(START_TIME_NEW);
		updatedLessonDTO.setDuration(DURATION_NEW);
		when(courseServiceMock.retrieve(SECOND)).thenReturn(createCourse(SECOND));
		when(groupServiceMock.retrieve(SECOND)).thenReturn(createGroup(SECOND));
		when(classroomServiceMock.retrieve(SECOND)).thenReturn(createClassroom(SECOND));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING_NEW)).thenReturn(START_TIME_NEW);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING_NEW)).thenReturn(DURATION_NEW);
		when(lessonServiceMock.checkIfExists(updatedLessonDTO)).thenReturn(true);
		when(groupServiceMock.findAll()).thenReturn(createGroupList());
		when(courseServiceMock.findAll()).thenReturn(createCourseList());
		when(classroomServiceMock.findAll()).thenReturn(createClassroomList());

		RequestBuilder request = post("/lessons/edit/result").flashAttr("initialGroup", FIRST)
				.flashAttr("initialCourse", FIRST).flashAttr("initialClassroom", FIRST)
				.flashAttr("initialStartTime", START_TIME_STRING).flashAttr("initialDuration", DURATION_STRING)
				.flashAttr("group", SECOND).flashAttr("course", SECOND).flashAttr("classroom", SECOND)
				.flashAttr("startTime", START_TIME_STRING_NEW).flashAttr("duration", DURATION_STRING_NEW);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("error", "The same lesson is already added to the database!"),
						model().attribute("groups", expectedNameList), model().attribute("courses", expectedNameList),
						model().attribute("classrooms", expectedNameList), model().attribute("group", SECOND),
						model().attribute("course", SECOND), model().attribute("classroom", SECOND),
						model().attribute("startTime", START_TIME_STRING_NEW),
						model().attribute("duration", DURATION_STRING_NEW), model().attribute("initialGroup", FIRST),
						model().attribute("initialCourse", FIRST), model().attribute("initialClassroom", FIRST),
						model().attribute("initialStartTime", START_TIME_STRING),
						model().attribute("initialDuration", DURATION_STRING))
				.andExpect(view().name("lessons/edit"));
	}

	@Test
	void testEditResultViewPostWithNonValidDTO_shouldReturnEditView() throws Exception {
		List<String> expectedNameList = Arrays.asList("First", "Second");
		LessonDTO initialLessonDTO = new LessonDTO();
		initialLessonDTO.setGroup(createGroup(FIRST));
		initialLessonDTO.setClassroom(createClassroom(FIRST));
		initialLessonDTO.setCourse(createCourse(FIRST));
		initialLessonDTO.setStartTime(START_TIME);
		initialLessonDTO.setDuration(DURATION);
		when(courseServiceMock.retrieve(FIRST)).thenReturn(createCourse(FIRST));
		when(groupServiceMock.retrieve(FIRST)).thenReturn(createGroup(FIRST));
		when(classroomServiceMock.retrieve(FIRST)).thenReturn(createClassroom(FIRST));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING)).thenReturn(START_TIME);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING)).thenReturn(DURATION);

		when(courseServiceMock.retrieve(SECOND)).thenReturn(createCourse(SECOND));
		when(groupServiceMock.retrieve(SECOND)).thenReturn(createGroup(SECOND));
		when(classroomServiceMock.retrieve(SECOND)).thenReturn(createClassroom(SECOND));
		when(lessonServiceMock.convertDateToMil("0")).thenReturn(0l);
		when(lessonServiceMock.convertTimeToMil("0")).thenReturn(0l);
		when(groupServiceMock.findAll()).thenReturn(createGroupList());
		when(courseServiceMock.findAll()).thenReturn(createCourseList());
		when(classroomServiceMock.findAll()).thenReturn(createClassroomList());

		RequestBuilder request = post("/lessons/edit/result").flashAttr("initialGroup", FIRST)
				.flashAttr("initialCourse", FIRST).flashAttr("initialClassroom", FIRST)
				.flashAttr("initialStartTime", START_TIME_STRING).flashAttr("initialDuration", DURATION_STRING)
				.flashAttr("group", SECOND).flashAttr("course", SECOND).flashAttr("classroom", SECOND)
				.flashAttr("startTime", "0").flashAttr("duration", "0");
		mockMvc.perform(request).andExpectAll(
				model().attribute("error", "The startTime field cannot be blank or earlier than"
						+ " UTC Jan-01-2021 00:00:01!<br>The duration field cannot be blank or less than 30 minutes!"),
				model().attribute("groups", expectedNameList), model().attribute("courses", expectedNameList),
				model().attribute("classrooms", expectedNameList), model().attribute("group", SECOND),
				model().attribute("course", SECOND), model().attribute("classroom", SECOND),
				model().attribute("startTime", "0"), model().attribute("duration", "0"),
				model().attribute("initialGroup", FIRST), model().attribute("initialCourse", FIRST),
				model().attribute("initialClassroom", FIRST), model().attribute("initialStartTime", START_TIME_STRING),
				model().attribute("initialDuration", DURATION_STRING)).andExpect(view().name("lessons/edit"));
	}

	@Test
	void testDeleteViewPost() throws Exception {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setGroup(createGroup(FIRST));
		lessonDTO.setClassroom(createClassroom(FIRST));
		lessonDTO.setCourse(createCourse(FIRST));
		lessonDTO.setStartTime(START_TIME);
		lessonDTO.setDuration(DURATION);

		when(courseServiceMock.retrieve(FIRST)).thenReturn(createCourse(FIRST));
		when(groupServiceMock.retrieve(FIRST)).thenReturn(createGroup(FIRST));
		when(classroomServiceMock.retrieve(FIRST)).thenReturn(createClassroom(FIRST));
		when(lessonServiceMock.convertDateToMil(START_TIME_STRING)).thenReturn(START_TIME);
		when(lessonServiceMock.convertTimeToMil(DURATION_STRING)).thenReturn(DURATION);

		RequestBuilder request = post("/lessons/delete").flashAttr("groupName", FIRST).flashAttr("courseName", FIRST)
				.flashAttr("classroomName", FIRST).flashAttr("startTime", START_TIME_STRING)
				.flashAttr("duration", DURATION_STRING);

		mockMvc.perform(request).andExpect(model().attribute("lessonDTO", lessonDTO))
				.andExpect(view().name("lessons/delete"));
	}

	@Test
	void testDeleteResultViewPostWithValidDTO_shouldReturnResultView() throws Exception {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setGroup(createGroup(FIRST));
		lessonDTO.setClassroom(createClassroom(FIRST));
		lessonDTO.setCourse(createCourse(FIRST));
		lessonDTO.setStartTime(START_TIME);
		lessonDTO.setDuration(DURATION);
		when(courseServiceMock.retrieve(FIRST)).thenReturn(createCourse(FIRST));
		RequestBuilder request = post("/lessons/delete/result").flashAttr("lesson", lessonDTO);
		mockMvc.perform(request).andExpectAll(model().attribute("result", "A lesson was successfully deleted."),
				model().attribute("lessonDTO", lessonDTO)).andExpect(view().name("lessons/result"));
	}

	@Test
	void testDeleteResultViewPostWithNonValidDTO_shouldReturnListView() throws Exception {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setGroup(createGroup(FIRST));
		lessonDTO.setClassroom(createClassroom(FIRST));
		lessonDTO.setCourse(createCourse(FIRST));
		lessonDTO.setStartTime(0);
		lessonDTO.setDuration(0);
		when(courseServiceMock.retrieve(FIRST)).thenReturn(createCourse(FIRST));
		RequestBuilder request = post("/lessons/delete/result").flashAttr("lesson", lessonDTO);
		mockMvc.perform(request).andExpect(view().name("lessons/list"));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/lessons/wrong")).andExpect(status().is4xxClientError());
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

	private List<GroupDTO> createGroupList() {
		List<GroupDTO> listOfGroups = new ArrayList<>();
		listOfGroups.add(createGroup("First"));
		listOfGroups.add(createGroup("Second"));
		return listOfGroups;
	}

	private List<CourseDTO> createCourseList() {
		List<CourseDTO> listOfCourses = new ArrayList<>();
		listOfCourses.add(createCourse("First"));
		listOfCourses.add(createCourse("Second"));
		return listOfCourses;
	}

	private List<ClassroomDTO> createClassroomList() {
		List<ClassroomDTO> listOfClassrooms = new ArrayList<>();
		listOfClassrooms.add(createClassroom("First"));
		listOfClassrooms.add(createClassroom("Second"));
		return listOfClassrooms;
	}

}
