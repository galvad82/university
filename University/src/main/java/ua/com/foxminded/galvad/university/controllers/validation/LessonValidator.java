package ua.com.foxminded.galvad.university.controllers.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.services.LessonService;

@Component
public class LessonValidator implements Validator {

	@Autowired
	LessonService lessonService;

	@Override
	public boolean supports(Class<?> clazz) {
		return LessonDTO.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LessonDTO lessonDTO = (LessonDTO) target;

		lessonService.findAll().stream().forEach(s -> {
			if (s.equals(lessonDTO))
				errors.reject("group", "The lesson is already added to the database!");
		});
		if (errors.getFieldErrorCount() > 0) {
			if (lessonDTO.getGroup() == null) {
				errors.reject("group", "The group field cannot be blank!");
			}
			if (lessonDTO.getCourse() == null) {
				errors.reject("course", "The course field cannot be blank!");
			}
			if (lessonDTO.getClassroom() == null) {
				errors.reject("classroom", "The classroom field cannot be blank!");
			}
			if (lessonDTO.getStartTime() == null || lessonDTO.getStartTime() < 1609459201000l) {
				errors.reject("startTime",
						"The startTime field cannot be blank or earlier than UTC Jan 01 2021 00:00:01!");
			}
			if (lessonDTO.getDuration() == null || lessonDTO.getDuration() < 1800000l) {
				errors.reject("duration", "The duration field cannot be blank or less than 30 minutes!");
			}
		}
	}

}
