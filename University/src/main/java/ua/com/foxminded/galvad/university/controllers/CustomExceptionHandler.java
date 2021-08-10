package ua.com.foxminded.galvad.university.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);
	
	@ExceptionHandler({DataAreNotUpdatedException.class})
	public String databaseError(Model model, DataAreNotUpdatedException exception) {
		LOGGER.error(exception.getMessage());
		LOGGER.error(exception.getCauseDescription());
		model.addAttribute("error", exception.getErrorMessage() );
	    return "/exception";
	  }
	
	@ExceptionHandler({DataNotFoundException.class})
	public String databaseError(Model model, DataNotFoundException exception) {
		LOGGER.error(exception.getErrorMessage());
		LOGGER.error(exception.getCauseDescription());
		model.addAttribute("error", exception.getErrorMessage());
		model.addAttribute("cause", exception.getCauseDescription());
	    return "/exception";
	  }
}
