package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.TeacherService;

@ExtendWith(MockitoExtension.class)
class CoursesControllerTest {

	@Mock
	CourseService courseServiceMock;

	@Mock
	TeacherService teacherServiceMock;

	@InjectMocks
	CoursesController coursesControllerUnderTest;

	@Mock
	CustomExceptionHandler customExceptionHandlerMock;

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
		when(teacherServiceMock.findAll()).thenReturn(listOfTeachers);
		mockMvc.perform(get("/courses/add")).andExpect(view().name("courses/add"))
				.andExpect(matchAll(model().attribute("listOfTeachers", listOfTeachers)));
	}

	@Test
	void testAddViewPost() throws Exception {
		CourseDTO expectedCourseDTO = new CourseDTO();
		expectedCourseDTO.setName("TEST");
		RequestBuilder request = post("/courses/add").flashAttr("courseDTO", expectedCourseDTO);
		mockMvc.perform(request).andExpect(matchAll(model().attribute("course", expectedCourseDTO)))
				.andExpect(matchAll(model().attribute("result", "A course was successfully added.")))
				.andExpect(view().name("courses/result"));
	}

	@Test
	void testEditViewPost() throws Exception {
		mockMvc.perform(post("/courses/edit").param("name", "TEST"))
				.andExpect(matchAll(model().attribute("name", "TEST"))).andExpect(view().name("courses/edit"));
	}

	@Test
	void testEditResultViewPost() throws Exception {
		CourseDTO updatedCourseDTO = new CourseDTO();
		updatedCourseDTO.setName("NewName");
		String initialName = "OldName";
		RequestBuilder request = post("/courses/edit/result").flashAttr("courseDTO", updatedCourseDTO)
				.flashAttr("initialName", initialName);
		mockMvc.perform(request).andExpect(matchAll(model().attribute("result", "Course was successfully updated")))
				.andExpect(matchAll(model().attribute("course", updatedCourseDTO)))
				.andExpect(view().name("courses/result"));
	}

	@Test
	void testDeleteViewPost() throws Exception {
		mockMvc.perform(post("/courses/delete").param("name", "TEST"))
				.andExpect(matchAll(model().attribute("name", "TEST"))).andExpect(view().name("courses/delete"));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("TEST");
		RequestBuilder request = post("/courses/delete/result").flashAttr("course", courseDTO);
		mockMvc.perform(request).andExpect(matchAll(model().attribute("result", "A course was successfully deleted.")))
				.andExpect(matchAll(model().attribute("course", courseDTO))).andExpect(view().name("courses/result"));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/courses/wrong")).andExpect(status().is4xxClientError());
	}

}
