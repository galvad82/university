package ua.com.foxminded.galvad.university.controllers;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.galvad.university.config.SpringConfig;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.services.GroupService;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(SpringConfig.class)
class GroupsControllerTest {

	@Mock
	GroupService groupServiceMock;

	@InjectMocks
	GroupsController groupsControllerUnderTest;

	@Autowired
	CustomExceptionHandler customExceptionHandler;

	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	GroupsController groupsController;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
	}

	@Test
	void testListView_shouldReturnListView() throws Exception {
		mockMvc.perform(get("/groups")).andExpect(view().name("groups/list"));
	}

	@Test
	void testSingleView_shouldReturnSingleView() throws Exception {
		mockMvc.perform(get("/groups/{id}", 1)).andExpect(view().name("groups/single"));
	}

//	@Test
//	void testIDView_shouldReturnIDView() throws Exception {
//		mockMvc.perform(get("/groups/id")).andExpect(view().name("groups/id"));
//	}

	@Test
	void testGroupAttributeForSingleView() throws Exception {
		GroupDTO expectedGroupDTO = new GroupDTO();
		expectedGroupDTO.setName("AB-101");		
		when(groupServiceMock.retrieve(1)).thenReturn(expectedGroupDTO);
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupsControllerUnderTest).build();
		mockMvc.perform(get("/groups/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("groups/single"))
				.andExpect(model().attribute("group", expectedGroupDTO));
	}

	@Test
	void testSingleView_shouldSetCorrectIdAttributeValue() throws Exception {
		mockMvc.perform(get("/groups/{id}", 1)).andExpect(matchAll(model().attribute("id", 1)));
	}

	@Test
	void testSingleView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/groups/{id}", "fff")).andExpect(status().is4xxClientError());
	}

	@Test
	void testSingleView_shouldForwardToExceptionViewAndReturnCorrectErrorMessage() throws Exception {
		mockMvc.perform(get("/groups/{id}", 99999)).andExpect(view().name("/exception"));
		mockMvc.perform(get("/groups/{id}", 99999))
				.andExpect(matchAll(model().attribute("error", "A group with ID=99999 is not found")));
	}

	@Test
	void testListView_shouldReturnExpectedList() throws Exception {
		List<GroupDTO> expectedList = new ArrayList<>();
		GroupDTO firstGroupDTO = new GroupDTO();
		firstGroupDTO.setName("AB-101");
		GroupDTO secondGroupDTO = new GroupDTO();
		secondGroupDTO.setName("AB-201");
		expectedList.add(firstGroupDTO);
		expectedList.add(secondGroupDTO);

		when(groupServiceMock.findAll()).thenReturn(expectedList);
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupsControllerUnderTest).build();
		mockMvc.perform(get("/groups")).andExpect(status().isOk()).andExpect(view().name("groups/list"))
				.andExpect(model().attribute("groups", expectedList));
	}

	@Test
	void testListView_shouldForwardToExceptionView() throws Exception {
		DataNotFoundException exception = new DataNotFoundException("Error Message");
		when(groupServiceMock.findAll()).thenThrow(exception);
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupsControllerUnderTest)
				.setControllerAdvice(customExceptionHandler).build();
		mockMvc.perform(get("/groups")).andExpect(view().name("/exception"))
				.andExpect(matchAll(model().attribute("error", "Error Message")));
	}


}
