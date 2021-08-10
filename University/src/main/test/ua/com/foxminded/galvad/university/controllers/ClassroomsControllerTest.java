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
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.services.ClassroomService;

@ExtendWith(MockitoExtension.class)
class ClassroomsControllerTest {

	@Mock
	ClassroomService classroomServiceMock;

	@InjectMocks
	ClassroomsController classroomsControllerUnderTest;

	@Mock
	CustomExceptionHandler customExceptionHandlerMock;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(classroomsControllerUnderTest)
				.setControllerAdvice(customExceptionHandlerMock).build();
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
		mockMvc.perform(get("/classrooms")).andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testAddViewGet() throws Exception {
		ClassroomDTO expectedClassroomDTO = new ClassroomDTO();
		mockMvc.perform(get("/classrooms/add")).andExpect(matchAll(model().attribute("classroomDTO", expectedClassroomDTO))).andExpect(view().name("classrooms/add"));
	}
	
	@Test
	void testAddViewPost() throws Exception {
		ClassroomDTO expectedClassroomDTO = new ClassroomDTO();
		expectedClassroomDTO.setName("TEST");
		RequestBuilder request = post("/classrooms/add").flashAttr("classroomDTO", expectedClassroomDTO);
		mockMvc
		.perform(request)
		.andExpect(matchAll(model().attribute("classroom", expectedClassroomDTO)))
		.andExpect(matchAll(model().attribute("result", "A classroom was successfully added.")))
		.andExpect(view().name("classrooms/result"));
	}
	
	@Test
	void testEditViewPost() throws Exception {
		mockMvc
		.perform(post("/classrooms/edit").param("name", "TEST"))
		.andExpect(matchAll(model().attribute("name", "TEST")))
		.andExpect(view().name("classrooms/edit"));
	}
	
	@Test
	void testEditResultViewPost() throws Exception {
		mockMvc
		.perform(post("/classrooms/edit/result").param("name", "name").param("initialName", "initialName"))
		.andExpect(matchAll(model().attribute("result", "Classroom was successfully updated")))
		.andExpect(view().name("classrooms/result"));
	}
	
	@Test
	void testDeleteViewPost() throws Exception {
		mockMvc
		.perform(post("/classrooms/delete").param("name", "TEST"))
		.andExpect(matchAll(model().attribute("name", "TEST")))
		.andExpect(view().name("classrooms/delete"));
	}
	
	@Test
	void testDeleteResultViewPost() throws Exception {
		mockMvc
		.perform(post("/classrooms/delete/result").param("name", "name"))
		.andExpect(matchAll(model().attribute("result", "A classroom was successfully deleted.")))
		.andExpect(view().name("classrooms/result"));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/classrooms/wrong")).andExpect(status().is4xxClientError());
	}
}
