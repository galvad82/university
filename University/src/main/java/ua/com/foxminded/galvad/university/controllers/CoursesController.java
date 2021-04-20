package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.services.CourseService;

@Controller
@RequestMapping("/courses")
public class CoursesController {

	public final CourseService courseService;

	@Autowired
	public CoursesController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<CourseDTO> listOfCourseDTOs = courseService.findAll();
		model.addAttribute("courses", listOfCourseDTOs);
		return "courses/list";
	}

	@GetMapping("/{id}")
	public String getStudents(@PathVariable("id") int id, Model model) {
		model.addAttribute("course", courseService.retrieve(id));
		model.addAttribute("id", id);
		return "courses/single";
	}

}
