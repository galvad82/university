package ua.com.foxminded.galvad.university.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;

import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.galvad.university.config.SpringConfig;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.services.StudentService;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(SpringConfig.class)
class StudentsControllerTest {

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Mock
	StudentService studentServiceMock;

	@InjectMocks
	StudentsController studentsControllerUnderTest;
	
	@Autowired
	CustomExceptionHandler customExceptionHandler;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	StudentsController studentsController;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
	}

	@Test
	void testListView_shouldReturnListView() throws Exception {
		mockMvc.perform(get("/students")).andExpect(view().name("students/list"));
	}

	@Test
	void testSingleView_shouldReturnSingleView() throws Exception {
		mockMvc.perform(get("/students/{id}", 1)).andExpect(view().name("students/single"));
	}

	@Test
	void testIDView_shouldReturnIDView() throws Exception {
		mockMvc.perform(get("/students/id")).andExpect(view().name("students/id"));
	}

	@Test
	void testStudentAttributeForSingleView() throws Exception {
		StudentDTO expectedStudentDTO = new StudentDTO();
		expectedStudentDTO.setFirstName("Abbey");
		expectedStudentDTO.setLastName("Wilkes");
		mockMvc.perform(get("/students/{id}", 1)).andExpect(matchAll(model().attribute("student", expectedStudentDTO)));
	}

	@Test
	void testSingleView_shouldSetCorrectIdAttributeValue() throws Exception {
		mockMvc.perform(get("/students/{id}", 1)).andExpect(matchAll(model().attribute("id", 1)));
	}

	@Test
	void testSingleView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/students/{id}", "fff")).andExpect(status().is4xxClientError());
	}

	@Test
	void testSingleView_shouldForwardToExceptionViewAndReturnCorrectErrorMessage() throws Exception {
		mockMvc.perform(get("/students/{id}", 99999)).andExpect(view().name("/exception"));
		mockMvc.perform(get("/students/{id}", 99999))
				.andExpect(matchAll(model().attribute("error", "A student with ID=99999 is not found")));
	}

	@Test
	void testListView_shouldReturnExpectedList() throws Exception {
		List<StudentDTO> expectedList = new ArrayList<>();
		StudentDTO firstStudentDTO = new StudentDTO();
		firstStudentDTO.setFirstName("Abbey");
		firstStudentDTO.setLastName("Wilkes");
		StudentDTO secondStudentDTO = new StudentDTO();
		secondStudentDTO.setFirstName("Bernice");
		secondStudentDTO.setLastName("Bone");
		expectedList.add(firstStudentDTO);
		expectedList.add(secondStudentDTO);
		
		when(studentServiceMock.findAll()).thenReturn(expectedList);
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentsControllerUnderTest).build();
		mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(view().name("students/list"))
				.andExpect(model().attribute("students", expectedList));
	}
	
	@Test
	void testListView_shouldForwardToExceptionView() throws Exception {
		DataNotFoundException exception = new DataNotFoundException("Error Message");
		when(studentServiceMock.findAll()).thenThrow(exception);
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentsControllerUnderTest)
				.setControllerAdvice(customExceptionHandler).build();
		mockMvc.perform(get("/students")).andExpect(view().name("/exception"))
				.andExpect(matchAll(model().attribute("error", "Error Message")));
	}
	
}
