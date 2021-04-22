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
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.services.TeacherService;

@ExtendWith(MockitoExtension.class)
class TeachersControllerTest {

	@Mock
	TeacherService teacherServiceMock;

	@InjectMocks
	TeachersController teachersControllerUnderTest;

	@Mock
	CustomExceptionHandler customExceptionHandlerMock;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(teachersControllerUnderTest)
				.setControllerAdvice(customExceptionHandlerMock).build();
	}

	@Test
	void testListView_shouldReturnListView() throws Exception {
		mockMvc.perform(get("/teachers")).andExpect(view().name("teachers/list"));
	}

	@Test
	void testSingleView_shouldReturnSingleView() throws Exception {
		mockMvc.perform(get("/teachers/{id}", 1)).andExpect(view().name("teachers/single"));
	}

	@Test
	void testTeacherAttributeForSingleView() throws Exception {
		TeacherDTO expectedTeacherDTO = new TeacherDTO();
		expectedTeacherDTO.setFirstName("Jennie");
		expectedTeacherDTO.setLastName("Crigler");
		when(teacherServiceMock.retrieve(1)).thenReturn(expectedTeacherDTO);
		mockMvc.perform(get("/teachers/{id}", 1)).andExpect(matchAll(model().attribute("teacher", expectedTeacherDTO)));
	}

	@Test
	void testSingleView_shouldSetCorrectIdAttributeValue() throws Exception {
		mockMvc.perform(get("/teachers/{id}", 1)).andExpect(matchAll(model().attribute("id", 1)));
	}

	@Test
	void testSingleView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/teachers/{id}", "fff")).andExpect(status().is4xxClientError());
	}

	@Test
	void testSingleView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(teacherServiceMock.retrieve(99999)).thenThrow(expectedException);
		mockMvc.perform(get("/teachers/{id}", 99999))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testListView_shouldReturnExpectedList() throws Exception {
		List<TeacherDTO> expectedList = new ArrayList<>();
		TeacherDTO firstTeacherDTO = new TeacherDTO();
		firstTeacherDTO.setFirstName("Bradleigh");
		firstTeacherDTO.setLastName("Donaldson");
		TeacherDTO secondTeacherDTO = new TeacherDTO();
		secondTeacherDTO.setFirstName("Minahil");
		secondTeacherDTO.setLastName("Heath");
		expectedList.add(firstTeacherDTO);
		expectedList.add(secondTeacherDTO);

		when(teacherServiceMock.findAll()).thenReturn(expectedList);
		mockMvc.perform(get("/teachers")).andExpect(status().isOk()).andExpect(view().name("teachers/list"))
				.andExpect(model().attribute("teachers", expectedList));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(teacherServiceMock.findAll()).thenThrow(expectedException);
		mockMvc.perform(get("/teachers"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

}
