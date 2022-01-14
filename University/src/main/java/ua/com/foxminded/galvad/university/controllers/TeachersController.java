package ua.com.foxminded.galvad.university.controllers;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeachersController.class);

	private static final String TEACHERS_RESULT = "teachers/result";
	private static final String TEACHERS_LIST = "teachers/list";
	private static final String TEACHERS_ADD = "teachers/add";
	private static final String TEACHERS_EDIT = "teachers/edit";
	private static final String TEACHERS_DELETE = "teachers/delete";
	private static final String TEACHERDTO = "teacherDTO";
	private static final String RESULT = "result";
	private final TeacherService teacherService;

	@Autowired
	public TeachersController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping()
	public String findAll(Model model) throws DataNotFoundException {
		model.addAttribute("teachers", teacherService.findAll());
		return TEACHERS_LIST;
	}

	@GetMapping("/add")
	public String create(Model model) {
		model.addAttribute(TEACHERDTO, new TeacherDTO());
		return TEACHERS_ADD;
	}

	@PostMapping("/add")
	public String createDTO(@Valid TeacherDTO teacherDTO, BindingResult result, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (teacherService.checkIfExists(teacherDTO)) {
			result.rejectValue("firstName", "", "The teacher with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			model.addAttribute(TEACHERDTO, teacherDTO);
			return TEACHERS_ADD;
		}
		teacherService.create(teacherDTO);
		model.addAttribute(RESULT, "A teacher was successfully added.");
		return TEACHERS_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			Model model) throws DataNotFoundException {
		model.addAttribute(TEACHERDTO, teacherService.retrieve(firstName, lastName));
		model.addAttribute("initialFirstName", firstName);
		model.addAttribute("initialLastName", lastName);
		return TEACHERS_EDIT;
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@Valid TeacherDTO teacherDTO, BindingResult result,
			@ModelAttribute("initialFirstName") String initialFirstName,
			@ModelAttribute("initialLastName") String initialLastName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if ((!teacherDTO.getFirstName().equals(initialFirstName) || !teacherDTO.getLastName().equals(initialLastName))
				&& (teacherService.checkIfExists(teacherDTO))) {
			result.rejectValue("firstName", "", "The teacher with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			model.addAttribute("initialFirstName", initialFirstName);
			model.addAttribute("initialLastName", initialLastName);
			return TEACHERS_EDIT;
		}
		TeacherDTO initialTeacherDTO = teacherService.retrieve(initialFirstName, initialLastName);
		teacherService.update(initialTeacherDTO, teacherDTO);
		model.addAttribute(RESULT, "Teacher was successfully updated");
		return TEACHERS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			Model model) throws DataNotFoundException {
		model.addAttribute(TEACHERDTO, teacherService.retrieve(firstName, lastName));
		return TEACHERS_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteDTOResult(@Valid TeacherDTO teacherDTO, BindingResult result, Model model)
			throws DataAreNotUpdatedException {
		if (result.hasErrors()) {
			return TEACHERS_LIST;
		}
		teacherService.delete(teacherDTO);
		model.addAttribute(RESULT, "A teacher was successfully deleted.");
		return TEACHERS_RESULT;
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
