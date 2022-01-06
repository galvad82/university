package ua.com.foxminded.galvad.university.controllers.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.GroupService;

@Component
public class GroupValidator implements Validator {

	@Autowired
	GroupService groupService;

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupValidator.class);

	@Override
	public boolean supports(Class<?> clazz) {
		return GroupDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		GroupDTO groupDTO = (GroupDTO) target;
		try {
			groupService.retrieve(groupDTO.getName());
			errors.rejectValue("name", "", "The group with the same name is already added to the database!");
		} catch (DataNotFoundException e) {
			LOGGER.info("A group with name \"{}\" is not found.", groupDTO.getName());
		}
	}

}
