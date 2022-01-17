package ua.com.foxminded.galvad.university.controllers;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupsController.class);

	private static final String GROUPS_RESULT = "groups/result";
	private static final String GROUPS_LIST = "groups/list";
	private static final String GROUPS_ADD = "groups/add";
	private static final String GROUPS_EDIT = "groups/edit";
	private static final String GROUPS_DELETE = "groups/delete";
	private static final String GROUP_DTO = "groupDTO";
	private static final String RESULT = "result";
	private static final String SHOWTABLE = "showTable";
	private final GroupService groupService;
	private final StudentService studentService;

	@Autowired
	public GroupsController(GroupService groupService, StudentService studentService) {
		this.groupService = groupService;
		this.studentService = studentService;
	}

	@GetMapping()
	public String findAll(Model model) throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute("groups", groupService.findAll());
		return GROUPS_LIST;
	}

	@PostMapping("/edit")
	public String editDTO(String name, Model model) throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute(GROUP_DTO, groupService.retrieve(name));
		return GROUPS_EDIT;
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@Valid GroupDTO groupDTO, BindingResult result, String initialName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (!groupDTO.getName().equals(initialName) && groupService.checkIfExists(groupDTO)) {
			result.rejectValue("name", "", "The group with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			return GROUPS_EDIT;
		}
		GroupDTO initialGroupDTO = groupService.retrieve(initialName);
		groupDTO.setListOfStudent(groupDTO.getListOfStudent().stream().filter(s -> !s.getFirstName().isEmpty())
				.collect(Collectors.toList()));
		groupService.update(initialGroupDTO, groupDTO);
		model.addAttribute(SHOWTABLE, true);
		model.addAttribute(RESULT, "Group was successfully updated");
		return GROUPS_RESULT;
	}

	@GetMapping("/add")
	public String create(Model model) throws DataNotFoundException {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setListOfStudent(new ArrayList<>(studentService.findAllUnassignedStudents()));
		model.addAttribute(GROUP_DTO, groupDTO);
		return GROUPS_ADD;
	}

	@PostMapping("/add")
	public String createDTO(@Valid GroupDTO groupDTO, BindingResult result, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (groupService.checkIfExists(groupDTO)) {
			result.rejectValue("name", "", "The group with the same name is already added to the database!");
		}
		if (result.hasErrors()) {
			groupDTO.setListOfStudent(new ArrayList<>(studentService.findAllUnassignedStudents()));
			return GROUPS_ADD;
		}
		groupDTO.setListOfStudent(groupDTO.getListOfStudent().stream().filter(s -> !s.getFirstName().isEmpty())
				.collect(Collectors.toList()));
		groupService.create(groupDTO);
		groupDTO.getListOfStudent().stream().forEach(s -> studentService.addToGroup(s, groupDTO));

		model.addAttribute(SHOWTABLE, true);
		model.addAttribute(RESULT, "A group was successfully added.");
		return GROUPS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(String name, Model model) throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute(GROUP_DTO, groupService.retrieve(name));
		return GROUPS_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteDTOResult(@Valid GroupDTO groupDTO, BindingResult result, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		if (result.hasErrors()) {
			return GROUPS_LIST;
		}
		groupService.delete(groupService.removeStudentsFromGroup(groupDTO));
		model.addAttribute(SHOWTABLE, false);
		model.addAttribute(RESULT, String.format("The group %s was successfully deleted.", groupDTO.getName()));
		return GROUPS_RESULT;
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