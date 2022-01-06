package ua.com.foxminded.galvad.university.controllers.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.StudentService;

@Component
public class StudentValidator implements Validator {

	@Autowired
	StudentService studentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return StudentDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		StudentDTO studentDTO = (StudentDTO) target;
		try {
			studentService.retrieve(studentDTO.getFirstName(), studentDTO.getLastName());
			errors.rejectValue("firstName", "", "The student with the same name is already added to the database!");
		} catch (DataNotFoundException e) {
			LOGGER.info("A student (firstName={}, lastName={}) is not found.", studentDTO.getFirstName(),
					studentDTO.getLastName());
		}
	}

}
