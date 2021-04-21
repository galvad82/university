package ua.com.foxminded.galvad.university.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.GroupService;


@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(SpringConfig.class)
class CustomExceptionHandlerTest {

	@InjectMocks
	GroupsController groupsControllerUnderTest;
		
	@Mock
	GroupService groupServiceMock;
	
	@Autowired
	CustomExceptionHandler customExceptionHandler;

	@Autowired
	private WebApplicationContext webAppContext;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webAppContext).build();
	}

	@Test
	void testHandlingOfDataNotFoundException() throws Exception {
		DataNotFoundException exception = new DataNotFoundException("Error Message");
		when(groupServiceMock.findAll()).thenThrow(exception);
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupsControllerUnderTest)
				.setControllerAdvice(customExceptionHandler).build();
		mockMvc.perform(get("/groups")).andExpect(view().name("/exception"))
				.andExpect(matchAll(model().attribute("error", "Error Message")));
	}
	
	@Test
	void testHandlingOfDataAreNotUpdatedException() throws Exception {
		DataAreNotUpdatedException exception = new DataAreNotUpdatedException("Error Message");
		when(groupServiceMock.findAll()).thenThrow(exception);
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupsControllerUnderTest)
				.setControllerAdvice(customExceptionHandler).build();
		mockMvc.perform(get("/groups")).andExpect(view().name("/exception"))
				.andExpect(matchAll(model().attribute("error", "Error Message")));
	}
	

}
