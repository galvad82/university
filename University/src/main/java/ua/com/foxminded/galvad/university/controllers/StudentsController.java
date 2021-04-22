package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.services.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

	private final StudentService studentService;
	
	@Autowired
	public StudentsController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<StudentDTO> listOfStudentDTOs = studentService.findAll();
		model.addAttribute("students", listOfStudentDTOs);
		return "students/list";
	}
	
	@PostMapping()
	public String retrieveByID(@RequestParam(value="studentID") String studentID, Model model) {
		System.out.println(studentID);
		int id = Integer.parseInt(studentID);
		model.addAttribute("student", studentService.retrieve(id));
		model.addAttribute("id", id);
		return "students/single";
	}

	@GetMapping("/{id}")
	public String getStudents(@PathVariable("id") int id, Model model) {
		model.addAttribute("student", studentService.retrieve(id));
		model.addAttribute("id", id);
		return "students/single";
	}
	
	@GetMapping("/id")
	public String findByID(Model model) {
		return "students/id";
	}

}
