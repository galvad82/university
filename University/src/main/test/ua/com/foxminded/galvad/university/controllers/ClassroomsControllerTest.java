package ua.com.foxminded.galvad.university.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.services.ClassroomService;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(SpringConfigTest.class)
class ClassroomsControllerTest {

	@Mock
	ClassroomService classroomServiceMock;

	@InjectMocks
	ClassroomsController classroomsControllerUnderTest;

	@Autowired
	CustomExceptionHandler customExceptionHandler;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	ClassroomsController classroomsController;

	MockMvc mockMvc;
	
	@Autowired
	DataSource dataSource;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
	}

	@Test
	void testListView_shouldReturnListView() throws Exception {
		mockMvc.perform(get("/classrooms")).andExpect(view().name("classrooms/list"));
	}

	@Test
	void testSingleView_shouldReturnSingleView() throws Exception {
		mockMvc.perform(get("/classrooms/{id}", 1)).andExpect(view().name("classrooms/single"));
	}

//	@Test
//	void testIDView_shouldReturnIDView() throws Exception {
//		mockMvc.perform(get("/classrooms/id")).andExpect(view().name("classrooms/id"));
//	}

	@Test
	void testClassroomAttributeForSingleView() throws Exception {
		ClassroomDTO expectedClassroomDTO = new ClassroomDTO();
		expectedClassroomDTO.setName("ROOM-15");
		mockMvc.perform(get("/classrooms/{id}", 1)).andExpect(matchAll(model().attribute("classroom", expectedClassroomDTO)));
	}

	@Test
	void testSingleView_shouldSetCorrectIdAttributeValue() throws Exception {
		mockMvc.perform(get("/classrooms/{id}", 1)).andExpect(matchAll(model().attribute("id", 1)));
	}

	@Test
	void testSingleView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/classrooms/{id}", "fff")).andExpect(status().is4xxClientError());
	}

	@Test
	void testSingleView_shouldForwardToExceptionViewAndReturnCorrectErrorMessage() throws Exception {
		mockMvc.perform(get("/classrooms/{id}", 99999)).andExpect(view().name("/exception"));
		mockMvc.perform(get("/classrooms/{id}", 99999))
				.andExpect(matchAll(model().attribute("error", "A classroom with ID=99999 is not found")));
	}

	@Test
	void testListView_shouldReturnExpectedList() throws Exception {
		List<ClassroomDTO> expectedList = new ArrayList<>();
		ClassroomDTO firstClassroomDTO = new ClassroomDTO();
		firstClassroomDTO.setName("ROOM-1");
		ClassroomDTO secondClassroomDTO = new ClassroomDTO();
		secondClassroomDTO.setName("ROOM-2");
		expectedList.add(firstClassroomDTO);
		expectedList.add(secondClassroomDTO);

		when(classroomServiceMock.findAll()).thenReturn(expectedList);
		this.mockMvc = MockMvcBuilders.standaloneSetup(classroomsControllerUnderTest).build();
		mockMvc.perform(get("/classrooms")).andExpect(status().isOk()).andExpect(view().name("classrooms/list"))
				.andExpect(model().attribute("classrooms", expectedList));
	}

	@Test
	void testListView_shouldForwardToExceptionView() throws Exception {
		DataNotFoundException exception = new DataNotFoundException("Error Message");
		when(classroomServiceMock.findAll()).thenThrow(exception);
		this.mockMvc = MockMvcBuilders.standaloneSetup(classroomsControllerUnderTest)
				.setControllerAdvice(customExceptionHandler).build();
		mockMvc.perform(get("/classrooms")).andExpect(view().name("/exception"))
				.andExpect(matchAll(model().attribute("error", "Error Message")));
	}

}
