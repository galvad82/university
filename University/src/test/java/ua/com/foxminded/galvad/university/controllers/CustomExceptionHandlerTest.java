package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;

import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;

class CustomExceptionHandlerTest {

	CustomExceptionHandler customExceptionHandler = new CustomExceptionHandler();

	@Test
	void testHandlingOfDataNotFoundException() throws Exception {
		DataNotFoundException exception = new DataNotFoundException("Error Message");
		ExtendedModelMap map = new ExtendedModelMap();
		assertEquals("/exception", customExceptionHandler.databaseError(map, exception));
		assertTrue(map.containsKey("error"));
		assertTrue(map.containsValue("Error Message"));
	}

	@Test
	void testHandlingOfDataAreNotUpdatedException() throws Exception {
		DataAreNotUpdatedException exception = new DataAreNotUpdatedException("Error Message");
		ExtendedModelMap map = new ExtendedModelMap();
		assertEquals("/exception", customExceptionHandler.databaseError(map, exception));
		assertTrue(map.containsKey("error"));
		assertTrue(map.containsValue("Error Message"));
	}

}
