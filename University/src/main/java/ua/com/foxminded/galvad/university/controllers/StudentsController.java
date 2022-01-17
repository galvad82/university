package ua.com.foxminded.galvad.university.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentsController.class);

	private static final String STUDENTS_RESULT = "students/result";
	private static final String STUDENTS_LIST = "students/list";
	private static final String STUDENTS_ADD = "students/add";
	private static final String STUDENTS_EDIT = "students/edit";
	private static final String STUDENTS_DELETE = "students/delete";
	private static final String RESULT = "result";
	private static final String STUDENTDTO = "studentDTO";
	private static final String GROUP_NAME = "groupName";
	private static final String LIST_OF_GROUP_NAMES = "listGroupNames";
	private static final String NONE = "NONE";

	private final GroupService groupService;
	private final StudentService studentService;

	@Autowired
	public StudentsController(StudentService studentService, GroupService groupService) {
		this.studentService = studentService;
		this.groupService = groupService;
	}

	@GetMapping()
	public String findAll(Model model) throws DataNotFoundException {
		Map<StudentDTO, String> studentGroupMap = studentService.buildStudentGroupMap();
		model.addAttribute("students", studentGroupMap);
		StudentDTO studentDTO = new StudentDTO();
		model.addAttribute(STUDENTDTO, studentDTO);
		return STUDENTS_LIST;
	}

	@GetMapping("/add")
	public String create(Model model) throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute(STUDENTDTO, new StudentDTO());
		model.addAttribute(LIST_OF_GROUP_NAMES, getListOfGroupNames());
		model.addAttribute("group", NONE);
		return STUDENTS_ADD;
	}

	@PostMapping("/add")
	public String createDTO(@Valid StudentDTO studentDTO, BindingResult result,
			@ModelAttribute("group") String groupDTOName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {

		if (studentService.checkIfExists(studentDTO)) {
			result.rejectValue("firstName", "", "The student with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			model.addAttribute(LIST_OF_GROUP_NAMES, getListOfGroupNames());
			return STUDENTS_ADD;
		}
		if (!groupDTOName.equals(NONE)) {
			studentDTO.setGroupDTO(groupService.retrieve(groupDTOName));
		} else {
			studentDTO.setGroupDTO(null);
		}
		studentService.create(studentDTO);
		model.addAttribute(GROUP_NAME, groupDTOName);
		model.addAttribute(RESULT, "A student was successfully added.");
		return STUDENTS_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			@ModelAttribute("groupName") String groupName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute(STUDENTDTO, studentService.retrieve(firstName, lastName));
		model.addAttribute(LIST_OF_GROUP_NAMES, getListOfGroupNames());
		model.addAttribute("initialFirstName", firstName);
		model.addAttribute("initialLastName", lastName);
		model.addAttribute(GROUP_NAME, groupName);
		return STUDENTS_EDIT;
	}

	@PostMapping("/edit/result")
	public String updateDTO(@Valid StudentDTO studentDTO, BindingResult result,
			@ModelAttribute("groupName") String groupName, @ModelAttribute("initialFirstName") String initialFirstName,
			@ModelAttribute("initialLastName") String initialLastName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if ((!studentDTO.getFirstName().equals(initialFirstName) || !studentDTO.getLastName().equals(initialLastName))
				&& (studentService.checkIfExists(studentDTO))) {
			result.rejectValue("firstName", "", "The student with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			model.addAttribute(LIST_OF_GROUP_NAMES, getListOfGroupNames());
			model.addAttribute("initialFirstName", initialFirstName);
			model.addAttribute("initialLastName", initialLastName);
			model.addAttribute(GROUP_NAME, groupName);
			return STUDENTS_EDIT;
		}
		StudentDTO initialStudentDTO = studentService.retrieve(initialFirstName, initialLastName);
		studentService.update(initialStudentDTO, studentDTO);
		if (groupName.equals(NONE)) {
			studentService.removeStudentFromGroup(studentDTO);
		} else {
			GroupDTO groupDTO = groupService.retrieve(groupName);
			studentService.addToGroup(studentDTO, groupDTO);
		}
		model.addAttribute(RESULT, "Student was successfully updated");
		model.addAttribute(GROUP_NAME, groupName);
		return STUDENTS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			@ModelAttribute("groupName") String groupName, Model model) throws DataNotFoundException {
		model.addAttribute(STUDENTDTO, studentService.retrieve(firstName, lastName));
		model.addAttribute(GROUP_NAME, groupName);
		return STUDENTS_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteDTOResult(@Valid StudentDTO studentDTO, BindingResult result,
			@ModelAttribute("groupName") String groupName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (result.hasErrors()) {
			return STUDENTS_LIST;
		}
		studentService.delete(studentDTO);
		model.addAttribute(GROUP_NAME, groupName);
		model.addAttribute(RESULT, "A student was successfully deleted.");
		return STUDENTS_RESULT;
	}

	private List<String> getListOfGroupNames() throws DataAreNotUpdatedException, DataNotFoundException {
		List<String> list = new ArrayList<>();
		list.add(NONE);
		groupService.findAll().stream().forEach(s -> list.add(s.getName()));
		return list;
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
