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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.services.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
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
	void testListView() throws Exception {
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
		mockMvc.perform(get("/teachers")).andExpectAll(status().isOk(), model().attribute("teachers", expectedList))
				.andExpect(view().name("teachers/list"));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(teacherServiceMock.findAll()).thenThrow(expectedException);
		mockMvc.perform(get("/teachers"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testAddViewGet() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		mockMvc.perform(get("/teachers/add")).andExpect(model().attribute("teacherDTO", teacherDTO))
				.andExpect(view().name("teachers/add"));
	}

	@Test
	void testAddViewPost() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("Bradleigh");
		teacherDTO.setLastName("Donaldson");
		RequestBuilder request = post("/teachers/add").flashAttr("teacherDTO", teacherDTO);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("teacher", teacherDTO),
						model().attribute("result", "A teacher was successfully added."))
				.andExpect(view().name("teachers/result"));
	}

	@Test
	void testEditViewPost_shouldReturnEditView() throws Exception {
		String firstName = "FirstName";
		String lastName = "LastName";
		RequestBuilder request = post("/teachers/edit").flashAttr("firstName", firstName).flashAttr("lastName",
				lastName);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("firstName", firstName), model().attribute("lastName", lastName))
				.andExpect(view().name("teachers/edit"));
	}

	@Test
	void testEditResultViewPost() throws Exception {
		TeacherDTO initialTeacherDTO = new TeacherDTO();
		initialTeacherDTO.setFirstName("initialFirstName");
		initialTeacherDTO.setLastName("initialLastName");

		TeacherDTO updatedTeacherDTO = new TeacherDTO();
		updatedTeacherDTO.setFirstName("updatedFirstName");
		updatedTeacherDTO.setLastName("updatedLastName");

		RequestBuilder request = post("/teachers/edit/result").flashAttr("firstName", updatedTeacherDTO.getFirstName())
				.flashAttr("lastName", updatedTeacherDTO.getLastName())
				.flashAttr("initialFirstName", initialTeacherDTO.getFirstName())
				.flashAttr("initialLastName", initialTeacherDTO.getLastName());

		mockMvc.perform(request).andExpectAll(model().attribute("result", "Teacher was successfully updated"),
				model().attribute("teacher", updatedTeacherDTO)).andExpect(view().name("teachers/result"));
	}

	@Test
	void testDeleteViewPost() throws Exception {
		RequestBuilder request = post("/teachers/delete").flashAttr("firstName", "firstName").flashAttr("lastName",
				"lastName");
		mockMvc.perform(request)
				.andExpectAll(model().attribute("firstName", "firstName"), model().attribute("lastName", "lastName"))
				.andExpect(view().name("teachers/delete"));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("firstName");
		teacherDTO.setLastName("lastName");
		RequestBuilder request = post("/teachers/delete/result").flashAttr("firstName", "firstName")
				.flashAttr("lastName", "lastName");
		mockMvc.perform(request).andExpectAll(model().attribute("result", "A teacher was successfully deleted."),
				model().attribute("teacher", teacherDTO)).andExpect(view().name("teachers/result"));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/teachers/wrong")).andExpect(status().is4xxClientError());
	}
}
