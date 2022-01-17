package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Controller
@RequestMapping("/courses")
public class CoursesController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoursesController.class);

	public final CourseService courseService;
	public final TeacherService teacherService;

	private static final String COURSES_RESULT = "courses/result";
	private static final String COURSES_LIST = "courses/list";
	private static final String COURSES_ADD = "courses/add";
	private static final String COURSES_EDIT = "courses/edit";
	private static final String COURSES_DELETE = "courses/delete";
	private static final String COURSE_DTO = "courseDTO";
	private static final String LIST_OF_TEACHERS = "listOfTeachers";
	private static final String RESULT = "result";

	@Autowired
	public CoursesController(CourseService courseService, TeacherService teacherService) {
		this.courseService = courseService;
		this.teacherService = teacherService;
	}

	@GetMapping()
	public String findAll(Model model) throws DataNotFoundException {
		List<CourseDTO> listOfCourseDTOs = courseService.findAll();
		model.addAttribute("courses", listOfCourseDTOs);
		return COURSES_LIST;
	}

	@PostMapping("/edit")
	public String editDTO(String name, Model model) throws DataNotFoundException {
		CourseDTO courseDTO = courseService.retrieve(name);
		model.addAttribute(COURSE_DTO, courseDTO);
		model.addAttribute(LIST_OF_TEACHERS, teacherService.findAll());
		model.addAttribute("initialName", courseDTO.getName());
		return COURSES_EDIT;
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@Valid CourseDTO courseDTO, BindingResult result, String initialName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (!courseDTO.getName().equals(initialName) && courseService.checkIfExists(courseDTO)) {
			result.rejectValue("name", "", "The course with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			model.addAttribute(LIST_OF_TEACHERS, teacherService.findAll());
			model.addAttribute("initialName", initialName);
			return COURSES_EDIT;
		}
		CourseDTO initialCourseDTO = courseService.retrieve(initialName);
		courseService.update(initialCourseDTO, courseDTO);
		model.addAttribute(RESULT, "Course was successfully updated");
		return COURSES_RESULT;
	}

	@GetMapping("/add")
	public String addCourse(Model model) throws DataNotFoundException {
		List<TeacherDTO> listOfTeachers = teacherService.findAll();
		model.addAttribute(LIST_OF_TEACHERS, listOfTeachers);
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("");
		courseDTO.setTeacher(listOfTeachers.get(0));
		model.addAttribute(COURSE_DTO, courseDTO);
		return COURSES_ADD;
	}

	@PostMapping("/add")
	public String addCourseToDB(@Valid CourseDTO courseDTO, BindingResult result, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (courseService.checkIfExists(courseDTO)) {
			result.rejectValue("name", "", "The course with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			model.addAttribute(LIST_OF_TEACHERS, teacherService.findAll());
			return COURSES_ADD;
		}
		courseService.create(courseDTO);
		model.addAttribute(RESULT, "A course was successfully added.");
		return COURSES_RESULT;
	}

	@PostMapping("/delete")
	public String deleteCourse(String name, Model model) throws DataNotFoundException {
		model.addAttribute(COURSE_DTO, courseService.retrieve(name));
		return COURSES_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteCourseFromDB(@Valid CourseDTO courseDTO, BindingResult result, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (result.hasErrors()) {
			return COURSES_LIST;
		}
		courseService.delete(courseDTO);
		model.addAttribute(RESULT, "A course was successfully deleted.");
		return COURSES_RESULT;
	}

	@ExceptionHandler({ DataAreNotUpdatedException.class })
	public String databaseError(Model model, DataAreNotUpdatedException exception) {
		LOGGER.error(exception.getMessage());
		LOGGER.error(exception.getCauseDescription());
		model.addAttribute("error", exception.getErrorMessage());
		return "/exception";
	}

	@ExceptionHandler({ DataNotFoundException.class })
	public String databaseError(Model model, DataNotFoundException exception) {
		LOGGER.error(exception.getErrorMessage());
		LOGGER.error(exception.getCauseDescription());
		model.addAttribute("error", exception.getErrorMessage());
		model.addAttribute("cause", exception.getCauseDescription());
		return "/exception";
	}

}
