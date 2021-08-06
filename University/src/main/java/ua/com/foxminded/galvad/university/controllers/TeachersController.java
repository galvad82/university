package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeachersController {
	private static final String TEACHERS_RESULT = "teachers/result";
	private static final String TEACHER = "teacher";
	private static final String RESULT = "result";
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

	@GetMapping("/add")
	public String create(Model model) {
		TeacherDTO teacherDTO = new TeacherDTO();
		model.addAttribute("teacherDTO", teacherDTO);
		return "teachers/add";
	}

	@PostMapping("/add")
	public String createDTO(@ModelAttribute("teacherDTO") TeacherDTO teacherDTO, Model model) {
		teacherService.create(teacherDTO);
		model.addAttribute(TEACHER, teacherDTO);
		model.addAttribute(RESULT, "A teacher was successfully added.");
		return TEACHERS_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			Model model) {
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		return "teachers/edit";
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName, @ModelAttribute("initialFirstName") String initialFirstName,
			@ModelAttribute("initialLastName") String initialLastName, Model model) {

		TeacherDTO initialTeacherDTO = new TeacherDTO();
		initialTeacherDTO.setFirstName(initialFirstName);
		initialTeacherDTO.setLastName(initialLastName);

		TeacherDTO updatedTeacherDTO = new TeacherDTO();
		updatedTeacherDTO.setFirstName(firstName);
		updatedTeacherDTO.setLastName(lastName);

		teacherService.update(initialTeacherDTO, updatedTeacherDTO);
		model.addAttribute(RESULT, "Teacher was successfully updated");
		model.addAttribute(TEACHER, updatedTeacherDTO);
		return TEACHERS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			Model model) {
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		return "teachers/delete";
	}

	@PostMapping("/delete/result")
	public String deleteDTOresult(@ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName, Model model) {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(firstName);
		teacherDTO.setLastName(lastName);
		teacherService.delete(teacherDTO);
		model.addAttribute(TEACHER, teacherDTO);
		model.addAttribute(RESULT, "A teacher was successfully deleted.");
		return TEACHERS_RESULT;
	}
}
