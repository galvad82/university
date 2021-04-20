package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.services.ClassroomService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomsController {

	public final ClassroomService classroomService;

	@Autowired
	public ClassroomsController(ClassroomService classroomService) {
		this.classroomService = classroomService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<ClassroomDTO> listOfClassroomDTOs = classroomService.findAll();
		model.addAttribute("classrooms", listOfClassroomDTOs);
		return "classrooms/list";
	}

	@GetMapping("/{id}")
	public String getStudents(@PathVariable("id") int id, Model model) {
		model.addAttribute("classroom", classroomService.retrieve(id));
		model.addAttribute("id", id);
		return "classrooms/single";
	}

}
