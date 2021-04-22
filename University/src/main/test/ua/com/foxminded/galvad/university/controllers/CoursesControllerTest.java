package ua.com.foxminded.galvad.university.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.services.CourseService;

@ExtendWith(MockitoExtension.class)
class CoursesControllerTest {

	@Mock
	CourseService courseServiceMock;

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
	void testListView_shouldReturnListView() throws Exception {
		mockMvc.perform(get("/courses")).andExpect(view().name("courses/list"));
	}

	@Test
	void testSingleView_shouldReturnSingleView() throws Exception {
		mockMvc.perform(get("/courses/{id}", 1)).andExpect(view().name("courses/single"));
	}

	@Test
	void testStudentAttributeForSingleView() throws Exception {
		CourseDTO expectedCourseDTO = new CourseDTO();
		expectedCourseDTO.setName("Advertising business");
		when(courseServiceMock.retrieve(1)).thenReturn(expectedCourseDTO);
		this.mockMvc = MockMvcBuilders.standaloneSetup(coursesControllerUnderTest).build();
		mockMvc.perform(get("/courses/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("courses/single"))
				.andExpect(model().attribute("course", expectedCourseDTO));
	}

	@Test
	void testSingleView_shouldSetCorrectIdAttributeValue() throws Exception {
		mockMvc.perform(get("/courses/{id}", 1)).andExpect(matchAll(model().attribute("id", 1)));
	}

	@Test
	void testSingleView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/courses/{id}", "fff")).andExpect(status().is4xxClientError());
	}

	@Test
	void testSingleView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(courseServiceMock.retrieve(99999)).thenThrow(expectedException);
		mockMvc.perform(get("/courses/{id}", 99999))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testListView_shouldReturnExpectedList() throws Exception {
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

}
