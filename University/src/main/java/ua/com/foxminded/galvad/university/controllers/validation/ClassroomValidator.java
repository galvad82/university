package ua.com.foxminded.galvad.university.controllers.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.ClassroomService;

@Component
public class ClassroomValidator implements Validator {

	@Autowired
	ClassroomService classroomService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return ClassroomDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ClassroomDTO classroomDTO = (ClassroomDTO) target;
		try {
			classroomService.retrieve(classroomDTO.getName());
			errors.rejectValue("name", "", "The classroom with the same name is already added to the database!");
		} catch (DataNotFoundException e) {
			LOGGER.info("A classroom with name \"{}\" is not found.", classroomDTO.getName());
		}
	}

}
