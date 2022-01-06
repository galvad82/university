package ua.com.foxminded.galvad.university.controllers.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Component
public class TeacherValidator implements Validator {

	@Autowired
	TeacherService teacherService;

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return TeacherDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		TeacherDTO teacherDTO = (TeacherDTO) target;
		try {
			teacherService.retrieve(teacherDTO.getFirstName(), teacherDTO.getLastName());
			errors.rejectValue("firstName", "", "The teacher with the same name is already added to the database!");
		} catch (DataNotFoundException e) {
			LOGGER.info("A teacher (firstName={}, lastName={}) is not found.", teacherDTO.getFirstName(),
					teacherDTO.getLastName());
		}
	}

}
