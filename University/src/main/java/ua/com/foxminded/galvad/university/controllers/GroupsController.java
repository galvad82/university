package ua.com.foxminded.galvad.university.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.StudentService;

@Controller
@RequestMapping("/groups")
public class GroupsController {

	private static final String GROUPS_RESULT = "groups/result";
	private static final String GROUPS_LIST = "groups/list";
	private static final String GROUPS_ADD = "groups/add";
	private static final String GROUPS_EDIT = "groups/edit";
	private static final String GROUPS_DELETE = "groups/delete";
	private static final String GROUP = "group";
	private static final String RESULT = "result";
	private static final String SHOWTABLE="showTable";
	private final GroupService groupService;
	private final StudentService studentService;

	@Autowired
	public GroupsController(GroupService groupService, StudentService studentService) {
		this.groupService = groupService;
		this.studentService = studentService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<GroupDTO> listOfGroupDTOs = groupService.findAll();
		model.addAttribute("groups", listOfGroupDTOs);
		return GROUPS_LIST;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("name") String name, Model model) {
		GroupDTO groupDTO = groupService.retrieveWithListOfStudents(name);
		model.addAttribute("groupDTO", groupDTO);
		return GROUPS_EDIT;
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@ModelAttribute("groupDTO") GroupDTO groupDTO,
			@ModelAttribute("initialName") String initialName, Model model) {

		GroupDTO initialGroupDTO = groupService.retrieveWithListOfStudents(initialName);

		groupDTO.setListOfStudent(groupDTO.getListOfStudent().stream().filter(s -> !s.getFirstName().isEmpty())
				.collect(Collectors.toList()));
		groupService.update(initialGroupDTO, groupDTO);
		Boolean showTable=true;
		model.addAttribute(SHOWTABLE, showTable);	
		model.addAttribute(RESULT, "Group was successfully updated");
		model.addAttribute(GROUP, groupDTO);
		return GROUPS_RESULT;
	}

	@GetMapping("/add")
	public String create(Model model) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("");
		groupDTO.setListOfStudent(studentService.findAllUnassignedStudents());
		model.addAttribute("groupDTO", groupDTO);
		return GROUPS_ADD;
	}

	@PostMapping("/add")
	public String createDTO(@ModelAttribute("groupDTO") GroupDTO groupDTO, Model model) {
		groupDTO.setListOfStudent(groupDTO.getListOfStudent().stream().filter(s -> !s.getFirstName().isEmpty())
				.collect(Collectors.toList()));
		groupService.create(groupDTO);
		Boolean showTable=true;
		model.addAttribute(SHOWTABLE, showTable);
		model.addAttribute(GROUP, groupDTO);
		model.addAttribute(RESULT, "A group was successfully added.");
		return GROUPS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("name") String groupName, Model model) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(groupName);
		try {
			groupDTO = groupService.retrieveWithListOfStudents(groupName);
		} catch (DataNotFoundException e) {
			List<StudentDTO> listOfStudent = new ArrayList<>();
			StudentDTO emptyDto = new StudentDTO();
			emptyDto.setFirstName("NONE");
			emptyDto.setLastName("");
			listOfStudent.add(emptyDto);
			groupDTO.setListOfStudent(listOfStudent);
		}
		model.addAttribute(GROUP, groupDTO);
		return GROUPS_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteDTOresult(@ModelAttribute("name") String groupName, Model model) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(groupName);
		groupService.delete(groupDTO);
		Boolean showTable=false;
		model.addAttribute("groupName", groupName);
		model.addAttribute(SHOWTABLE, showTable);
		model.addAttribute(RESULT, "The group was successfully deleted.");
		return GROUPS_RESULT;
	}

}
