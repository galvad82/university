package ua.com.foxminded.galvad.university.controllers.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.CourseService;

@Component
public class CourseValidator implements Validator {

	@Autowired
	CourseService courseService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CourseValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return CourseDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		CourseDTO courseDTO = (CourseDTO) target;
		try {
			courseService.retrieve(courseDTO.getName());
			errors.rejectValue("name", "", "The course with the same name is already added to the database!");
		} catch (DataNotFoundException e) {
			LOGGER.info("A course with name \"{}\" is not found.", courseDTO.getName());
		}
	}

}
