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
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.TeacherService;

@SpringBootTest
@AutoConfigureMockMvc
class TeachersControllerTest {

	@Mock
	TeacherService teacherServiceMock;

	@InjectMocks
	TeachersController teachersControllerUnderTest;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(teachersControllerUnderTest).build();
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
	void testAddViewPostWithNewDTO_shouldReturnResultView() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("Bradleigh");
		teacherDTO.setLastName("Donaldson");
		when(teacherServiceMock.checkIfExists(teacherDTO)).thenReturn(false);
		RequestBuilder request = post("/teachers/add").flashAttr("teacherDTO", teacherDTO);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("teacherDTO", teacherDTO),
						model().attribute("result", "A teacher was successfully added."))
				.andExpect(view().name("teachers/result"));
	}

	@Test
	void testAddViewPostWithExistentDTO_shouldReturnAddView() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("Bradleigh");
		teacherDTO.setLastName("Donaldson");
		when(teacherServiceMock.checkIfExists(teacherDTO)).thenReturn(true);
		RequestBuilder request = post("/teachers/add").flashAttr("teacherDTO", teacherDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.teacherDTO");
		assertEquals("firstName", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The teacher with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(teacherDTO, mockResult.getModel().get("teacherDTO"));
		assertEquals("teachers/add", mockResult.getViewName());
	}

	@Test
	void testAddViewPostWithBlankNames_shouldReturnAddView() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(" ");
		teacherDTO.setLastName(" ");
		when(teacherServiceMock.checkIfExists(teacherDTO)).thenReturn(false);
		RequestBuilder request = post("/teachers/add").flashAttr("teacherDTO", teacherDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.teacherDTO");
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("firstName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("lastName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("First name cannot be empty"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("Last name cannot be empty"));
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(1).getCode());
		assertEquals(teacherDTO, mockResult.getModel().get("teacherDTO"));
		assertEquals("teachers/add", mockResult.getViewName());
	}

	@Test
	void testEditViewPost_shouldReturnEditView() throws Exception {
		String firstName = "FirstName";
		String lastName = "LastName";
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(firstName);
		teacherDTO.setLastName(lastName);

		when(teacherServiceMock.retrieve(firstName, lastName)).thenReturn(teacherDTO);
		RequestBuilder request = post("/teachers/edit").flashAttr("firstName", firstName).flashAttr("lastName",
				lastName);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("initialFirstName", firstName),
						model().attribute("initialLastName", lastName), model().attribute("teacherDTO", teacherDTO))
				.andExpect(view().name("teachers/edit"));
	}

	@Test
	void testEditResultViewPostWithNewDTO_shouldReturnResultView() throws Exception {
		TeacherDTO updatedTeacherDTO = new TeacherDTO();
		updatedTeacherDTO.setFirstName("updatedFirstName");
		updatedTeacherDTO.setLastName("updatedLastName");
		TeacherDTO initialTeacherDTO = new TeacherDTO();
		initialTeacherDTO.setFirstName("initialFirstName");
		initialTeacherDTO.setLastName("initialLastName");

		when(teacherServiceMock.checkIfExists(updatedTeacherDTO)).thenReturn(false);
		when(teacherServiceMock.retrieve("initialFirstName", "initialLastName")).thenReturn(initialTeacherDTO);
		RequestBuilder request = post("/teachers/edit/result").flashAttr("teacherDTO", updatedTeacherDTO)
				.flashAttr("initialFirstName", "initialFirstName").flashAttr("initialLastName", "initialLastName");
		mockMvc.perform(request).andExpectAll(model().attribute("result", "Teacher was successfully updated"),
				model().attribute("teacherDTO", updatedTeacherDTO)).andExpect(view().name("teachers/result"));
	}

	@Test
	void testEditResultViewPostWithExistentDTO_shouldReturnEditView() throws Exception {
		TeacherDTO updatedTeacherDTO = new TeacherDTO();
		updatedTeacherDTO.setFirstName("updatedFirstName");
		updatedTeacherDTO.setLastName("updatedLastName");
		TeacherDTO initialTeacherDTO = new TeacherDTO();
		initialTeacherDTO.setFirstName("initialFirstName");
		initialTeacherDTO.setLastName("initialLastName");

		when(teacherServiceMock.checkIfExists(updatedTeacherDTO)).thenReturn(true);
		when(teacherServiceMock.retrieve("initialFirstName", "initialLastName")).thenReturn(initialTeacherDTO);
		RequestBuilder request = post("/teachers/edit/result").flashAttr("teacherDTO", updatedTeacherDTO)
				.flashAttr("initialFirstName", "initialFirstName").flashAttr("initialLastName", "initialLastName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.teacherDTO");
		assertEquals("firstName", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The teacher with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(updatedTeacherDTO, mockResult.getModel().get("teacherDTO"));
		assertEquals("initialFirstName", mockResult.getModel().get("initialFirstName"));
		assertEquals("initialLastName", mockResult.getModel().get("initialLastName"));
		assertEquals("teachers/edit", mockResult.getViewName());
	}

	@Test
	void testEditResultViewPostWithBlankNames_shouldReturnEditView() throws Exception {
		TeacherDTO updatedTeacherDTO = new TeacherDTO();
		updatedTeacherDTO.setFirstName(" ");
		updatedTeacherDTO.setLastName(" ");

		when(teacherServiceMock.checkIfExists(updatedTeacherDTO)).thenReturn(false);
		RequestBuilder request = post("/teachers/edit/result").flashAttr("teacherDTO", updatedTeacherDTO)
				.flashAttr("initialFirstName", "initialFirstName").flashAttr("initialLastName", "initialLastName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.teacherDTO");
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("firstName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("lastName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("First name cannot be empty"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("Last name cannot be empty"));
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(1).getCode());
		assertEquals(updatedTeacherDTO, mockResult.getModel().get("teacherDTO"));
		assertEquals("initialFirstName", mockResult.getModel().get("initialFirstName"));
		assertEquals("initialLastName", mockResult.getModel().get("initialLastName"));
		assertEquals("teachers/edit", mockResult.getViewName());
	}

	@Test
	void testDeleteViewPost() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("firstName");
		teacherDTO.setLastName("lastName");

		when(teacherServiceMock.retrieve("firstName", "lastName")).thenReturn(teacherDTO);
		RequestBuilder request = post("/teachers/delete").flashAttr("firstName", "firstName").flashAttr("lastName",
				"lastName");
		mockMvc.perform(request).andExpect(model().attribute("teacherDTO", teacherDTO))
				.andExpect(view().name("teachers/delete"));
	}

	@Test
	void testDeleteResultViewPost_shouldReturnResultView() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("firstName");
		teacherDTO.setLastName("lastName");
		RequestBuilder request = post("/teachers/delete/result").flashAttr("teacherDTO", teacherDTO);
		mockMvc.perform(request).andExpectAll(model().attribute("result", "A teacher was successfully deleted."),
				model().attribute("teacherDTO", teacherDTO)).andExpect(view().name("teachers/result"));
	}

	@Test
	void testDeleteResultViewPostWithBlankNames_shouldReturnListView() throws Exception {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(" ");
		teacherDTO.setLastName(" ");
		RequestBuilder request = post("/teachers/delete/result").flashAttr("teacherDTO", teacherDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.teacherDTO");
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("firstName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("lastName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("First name cannot be empty"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("Last name cannot be empty"));
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(1).getCode());
		assertEquals(teacherDTO, mockResult.getModel().get("teacherDTO"));
		assertEquals("teachers/list", mockResult.getViewName());
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/teachers/wrong")).andExpect(status().is4xxClientError());
	}
}
