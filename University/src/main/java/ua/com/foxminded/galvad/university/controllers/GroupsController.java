package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.services.GroupService;

@Controller
@RequestMapping("/groups")
public class GroupsController {
	
	private final GroupService groupService;

	@Autowired
	public GroupsController(GroupService groupService) {
		this.groupService = groupService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<GroupDTO> listOfGroupDTOs = groupService.findAll();
		model.addAttribute("groups", listOfGroupDTOs);
		return "groups/list";
	}

	@GetMapping("/{id}")
	public String getStudents(@PathVariable("id") int id, Model model) {
		model.addAttribute("group", groupService.retrieve(id));
		model.addAttribute("id", id);
		return "groups/single";
	}

}
