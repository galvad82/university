package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
class CoursesControllerTest {

	@Mock
	CourseService courseServiceMock;

	@Mock
	TeacherService teacherServiceMock;

	@InjectMocks
	CoursesController coursesControllerUnderTest;

	@Mock
	CustomExceptionHandler customExceptionHandlerMock;

	@Autowired
	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(coursesControllerUnderTest)
				.setControllerAdvice(customExceptionHandlerMock).build();
	}

	@Test
	void testListView() throws Exception {
		List<CourseDTO> expectedList = new ArrayList<>();
		CourseDTO firstCourseDTO = new CourseDTO();
		firstCourseDTO.setName("Advertising business");
		CourseDTO secondCourseDTO = new CourseDTO();
		secondCourseDTO.setName("Management");
		expectedList.add(firstCourseDTO);
		expectedList.add(secondCourseDTO);

		when(courseServiceMock.findAll()).thenReturn(expectedList);
		mockMvc.perform(get("/courses")).andExpect(status().isOk()).andExpect(view().name("courses/list"))
				.andExpect(model().attribute("courses", expectedList));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(courseServiceMock.findAll()).thenThrow(expectedException);
		mockMvc.perform(get("/courses"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testAddViewGet() throws Exception {
		List<TeacherDTO> listOfTeachers = new ArrayList<>();
		TeacherDTO emptyDTO = new TeacherDTO();
		emptyDTO.setFirstName("TEST");
		emptyDTO.setLastName("TEST");
		listOfTeachers.add(emptyDTO);
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("");
		courseDTO.setTeacher(emptyDTO);
		when(teacherServiceMock.findAll()).thenReturn(listOfTeachers);
		mockMvc.perform(get("/courses/add")).andExpect(view().name("courses/add")).andExpectAll(
				model().attribute("listOfTeachers", listOfTeachers), model().attribute("courseDTO", courseDTO));
	}

	@Test
	void testAddViewPostWithNewDTO() throws Exception {
		CourseDTO expectedCourseDTO = new CourseDTO();
		expectedCourseDTO.setName("TEST");
		when(courseServiceMock.checkIfExists(expectedCourseDTO)).thenReturn(false);
		RequestBuilder request = post("/courses/add").flashAttr("courseDTO", expectedCourseDTO);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("courseDTO", expectedCourseDTO),
						model().attribute("result", "A course was successfully added."))
				.andExpect(result -> assertEquals("courses/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testAddViewPostWithExistentDTO() throws Exception {
		CourseDTO expectedCourseDTO = new CourseDTO();
		expectedCourseDTO.setName("TEST");
		when(courseServiceMock.checkIfExists(expectedCourseDTO)).thenReturn(true);
		RequestBuilder request = post("/courses/add").flashAttr("courseDTO", expectedCourseDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.courseDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The course with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(expectedCourseDTO, mockResult.getModel().get("courseDTO"));
		assertEquals("courses/add", mockResult.getViewName());
	}

	@Test
	void testAddViewPostWithBlankDTO_shouldReturnAddView() throws Exception {
		CourseDTO expectedCourseDTO = new CourseDTO();
		expectedCourseDTO.setName(" ");
		when(courseServiceMock.checkIfExists(expectedCourseDTO)).thenReturn(false);
		RequestBuilder request = post("/courses/add").flashAttr("courseDTO", expectedCourseDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.courseDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Course name cannot be empty", bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(expectedCourseDTO, mockResult.getModel().get("courseDTO"));
		assertEquals("courses/add", mockResult.getViewName());
	}

	@Test
	void testEditViewPost() throws Exception {
		List<TeacherDTO> listOfTeachers = new ArrayList<>();
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("TEST");
		teacherDTO.setLastName("TEST");
		listOfTeachers.add(teacherDTO);
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("TEST");
		courseDTO.setTeacher(teacherDTO);
		when(courseServiceMock.retrieve("TEST")).thenReturn(courseDTO);
		when(teacherServiceMock.findAll()).thenReturn(listOfTeachers);
		mockMvc.perform(post("/courses/edit").param("name", "TEST"))
				.andExpectAll(model().attribute("courseDTO", courseDTO),
						model().attribute("listOfTeachers", listOfTeachers))
				.andExpect(result -> assertEquals("courses/edit", result.getModelAndView().getViewName()));
	}

	@Test
	void testEditResultViewPostWithNewDTO_shouldReturnResultView() throws Exception {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("NewName");
		String initialName = "OldName";
		CourseDTO initialCourseDTO = new CourseDTO();
		initialCourseDTO.setName(initialName);
		when(courseServiceMock.checkIfExists(courseDTO)).thenReturn(false);
		when(courseServiceMock.retrieve(initialName)).thenReturn(initialCourseDTO);
		RequestBuilder request = post("/courses/edit/result").flashAttr("courseDTO", courseDTO).flashAttr("initialName",
				initialName);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "Course was successfully updated"),
						model().attribute("courseDTO", courseDTO))
				.andExpect(result -> assertEquals("courses/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testEditResultViewPostWithExistentDTO_shouldReturnEditView() throws Exception {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("NewName");
		when(courseServiceMock.checkIfExists(courseDTO)).thenReturn(true);
		RequestBuilder request = post("/courses/edit/result").flashAttr("courseDTO", courseDTO).flashAttr("initialName",
				"OldName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.courseDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The course with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(courseDTO, mockResult.getModel().get("courseDTO"));
		assertEquals("courses/edit", mockResult.getViewName());
	}

	@Test
	void testEditResultViewPostWithBlankCourseName_shouldReturnEditView() throws Exception {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("  ");
		when(courseServiceMock.checkIfExists(courseDTO)).thenReturn(false);
		RequestBuilder request = post("/courses/edit/result").flashAttr("courseDTO", courseDTO).flashAttr("initialName",
				"OldName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.courseDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Course name cannot be empty", bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(courseDTO, mockResult.getModel().get("courseDTO"));
		assertEquals("courses/edit", mockResult.getViewName());
	}

	@Test
	void testDeleteViewPost() throws Exception {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("TEST");
		when(courseServiceMock.retrieve(courseDTO.getName())).thenReturn(courseDTO);
		mockMvc.perform(post("/courses/delete").param("name", "TEST"))
				.andExpectAll(model().attribute("courseDTO", courseDTO))
				.andExpect(result -> assertEquals("courses/delete", result.getModelAndView().getViewName()));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("TEST");
		RequestBuilder request = post("/courses/delete/result").flashAttr("courseDTO", courseDTO);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "A course was successfully deleted."),
						model().attribute("courseDTO", courseDTO))
				.andExpect(result -> assertEquals("courses/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testDeleteResultViewPostWithBlankCourseName_ShouldReturnClassroomListView() throws Exception {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName(" ");
		RequestBuilder request = post("/courses/delete/result").flashAttr("courseDTO", courseDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.courseDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Course name cannot be empty", bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(courseDTO, mockResult.getModel().get("courseDTO"));
		assertEquals("courses/list", mockResult.getViewName());
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/courses/wrong")).andExpect(status().is4xxClientError());
	}

}
