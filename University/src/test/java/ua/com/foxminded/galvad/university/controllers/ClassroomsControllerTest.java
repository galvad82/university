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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.ClassroomService;

@SpringBootTest
@AutoConfigureMockMvc
class ClassroomsControllerTest {

	@Mock
	ClassroomService classroomServiceMock;

	@InjectMocks
	ClassroomsController classroomsControllerUnderTest;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(classroomsControllerUnderTest).build();
	}

	@Test
	void testListView() throws Exception {
		List<ClassroomDTO> expectedList = new ArrayList<>();
		ClassroomDTO firstClassroomDTO = new ClassroomDTO();
		firstClassroomDTO.setName("ROOM-1");
		ClassroomDTO secondClassroomDTO = new ClassroomDTO();
		secondClassroomDTO.setName("ROOM-2");
		expectedList.add(firstClassroomDTO);
		expectedList.add(secondClassroomDTO);

		when(classroomServiceMock.findAll()).thenReturn(expectedList);
		mockMvc.perform(get("/classrooms")).andExpect(status().isOk()).andExpect(view().name("classrooms/list"))
				.andExpect(model().attribute("classrooms", expectedList));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(classroomServiceMock.findAll()).thenThrow(expectedException);
		mockMvc.perform(get("/classrooms"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testAddViewGet() throws Exception {
		ClassroomDTO expectedClassroomDTO = new ClassroomDTO();
		mockMvc.perform(get("/classrooms/add")).andExpectAll(model().attribute("classroomDTO", expectedClassroomDTO))
				.andExpect(result -> assertEquals("classrooms/add", result.getModelAndView().getViewName()));
	}

	@Test
	void testAddViewPostWithNewDTO() throws Exception {
		ClassroomDTO expectedClassroomDTO = new ClassroomDTO();
		expectedClassroomDTO.setName("TEST");
		when(classroomServiceMock.checkIfExists(expectedClassroomDTO)).thenReturn(false);
		RequestBuilder request = post("/classrooms/add").flashAttr("classroomDTO", expectedClassroomDTO);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("classroomDTO", expectedClassroomDTO),
						model().attribute("result", "A classroom was successfully added."))
				.andExpect(result -> assertEquals("classrooms/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testAddViewPostWithExistentDTO() throws Exception {
		ClassroomDTO expectedClassroomDTO = new ClassroomDTO();
		expectedClassroomDTO.setName("TEST");
		when(classroomServiceMock.checkIfExists(expectedClassroomDTO)).thenReturn(true);
		RequestBuilder request = post("/classrooms/add").flashAttr("classroomDTO", expectedClassroomDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.classroomDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The classroom with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(expectedClassroomDTO, mockResult.getModel().get("classroomDTO"));
		assertEquals("classrooms/add", mockResult.getViewName());
	}

	@Test
	void testAddViewPostWithBlankDTO_shouldReturnAddView() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName(" ");
		when(classroomServiceMock.checkIfExists(classroomDTO)).thenReturn(false);
		RequestBuilder request = post("/classrooms/add").flashAttr("classroomDTO", classroomDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.classroomDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Classroom name cannot be empty", bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(classroomDTO, mockResult.getModel().get("classroomDTO"));
		assertEquals("classrooms/add", mockResult.getViewName());
	}

	@Test
	void testAddViewPostWithDataAreNotUpdatedException() throws Exception {
		DataAreNotUpdatedException expectedException = new DataAreNotUpdatedException("Error Message");
		ClassroomDTO expectedClassroomDTO = new ClassroomDTO();
		expectedClassroomDTO.setName("TEST");
		when(classroomServiceMock.checkIfExists(expectedClassroomDTO)).thenReturn(false);
		when(classroomServiceMock.create(expectedClassroomDTO)).thenThrow(expectedException);
		RequestBuilder request = post("/classrooms/add").flashAttr("classroomDTO", expectedClassroomDTO);
		mockMvc.perform(request).andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testEditViewPost() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("TEST");
		when(classroomServiceMock.retrieve("TEST")).thenReturn(classroomDTO);
		mockMvc.perform(post("/classrooms/edit").param("name", "TEST"))
				.andExpectAll(model().attribute("classroomDTO", classroomDTO),
						model().attribute("initialName", classroomDTO.getName()))
				.andExpect(result -> assertEquals("classrooms/edit", result.getModelAndView().getViewName()));
	}

	@Test
	void testEditResultViewPostWithNewDTO_shouldReturnResultView() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("NAME");
		when(classroomServiceMock.checkIfExists(classroomDTO)).thenReturn(false);
		when(classroomServiceMock.retrieve(classroomDTO.getName())).thenReturn(classroomDTO);
		RequestBuilder request = post("/classrooms/edit/result").flashAttr("classroomDTO", classroomDTO)
				.flashAttr("initialName", "JustAnotherName");
		mockMvc.perform(request)
				.andExpectAll(model().attribute("classroomDTO", classroomDTO),
						model().attribute("result", "Classroom was successfully updated"))
				.andExpect(result -> assertEquals("classrooms/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testEditResultViewPostWithExistentDTO_shouldReturnEditView() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("NAME");
		when(classroomServiceMock.checkIfExists(classroomDTO)).thenReturn(true);
		RequestBuilder request = post("/classrooms/edit/result").flashAttr("classroomDTO", classroomDTO)
				.flashAttr("initialName", "JustAnotherName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.classroomDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The classroom with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(classroomDTO, mockResult.getModel().get("classroomDTO"));
		assertEquals("classrooms/edit", mockResult.getViewName());
	}

	@Test
	void testEditResultViewPostWithWithBlankClassroomName_ShouldReturnEditView() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName(" ");
		RequestBuilder request = post("/classrooms/edit/result").flashAttr("classroomDTO", classroomDTO)
				.flashAttr("initialName", "JustAnotherName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.classroomDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Classroom name cannot be empty", bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(classroomDTO, mockResult.getModel().get("classroomDTO"));
		assertEquals("classrooms/edit", mockResult.getViewName());
	}

	@Test
	void testDeleteViewPost() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("TEST");
		when(classroomServiceMock.retrieve("TEST")).thenReturn(classroomDTO);
		mockMvc.perform(post("/classrooms/delete").param("name", "TEST"))
				.andExpectAll(model().attribute("classroomDTO", classroomDTO))
				.andExpect(result -> assertEquals("classrooms/delete", result.getModelAndView().getViewName()));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("TEST");
		RequestBuilder request = post("/classrooms/delete/result").flashAttr("classroomDTO", classroomDTO);
		mockMvc.perform(request)
				.andExpectAll(model().attribute("classroomDTO", classroomDTO),
						model().attribute("result", "A classroom was successfully deleted."))
				.andExpect(result -> assertEquals("classrooms/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testDeleteResultViewPostWithBlankClassroomName_ShouldReturnClassroomListView() throws Exception {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName(" ");
		RequestBuilder request = post("/classrooms/delete/result").flashAttr("classroomDTO", classroomDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.classroomDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Classroom name cannot be empty", bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(classroomDTO, mockResult.getModel().get("classroomDTO"));
		assertEquals("classrooms/list", mockResult.getViewName());
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/classrooms/wrong")).andExpect(status().is4xxClientError());
	}
}
