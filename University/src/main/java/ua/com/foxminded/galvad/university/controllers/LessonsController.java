package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.services.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonsController {

	private final LessonService lessonService;

	@Autowired
	public LessonsController(LessonService lessonService) {
		this.lessonService = lessonService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<LessonDTO> listOfLessonDTOs = lessonService.findAll();
		model.addAttribute("lessons", listOfLessonDTOs);
		return "lessons/list";
	}

	@GetMapping("/{id}")
	public String getStudents(@PathVariable("id") int id, Model model) {
		model.addAttribute("lesson", lessonService.retrieve(id));
		model.addAttribute("id", id);
		return "lessons/single";
	}

}
