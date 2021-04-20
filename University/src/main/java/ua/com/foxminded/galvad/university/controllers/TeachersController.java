package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {

	private final TeacherService teacherService;
	
	@Autowired
	public TeachersController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<TeacherDTO> listOfTeacherDTOs = teacherService.findAll();
		model.addAttribute("teachers", listOfTeacherDTOs);
		return "teachers/list";
	}

	@GetMapping("/{id}")
	public String getStudents(@PathVariable("id") int id, Model model) {
		model.addAttribute("teacher", teacherService.retrieve(id));
		model.addAttribute("id", id);
		return "teachers/single";
	}
	
}
