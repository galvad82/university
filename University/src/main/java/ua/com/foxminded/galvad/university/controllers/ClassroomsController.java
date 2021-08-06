package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.services.ClassroomService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomsController {

	public final ClassroomService classroomService;
	private static final String CLASSROOMS_RESULT = "classrooms/result";
	private static final String CLASSROOM = "classroom";
	private static final String RESULT = "result";

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

	@GetMapping("/add")
	public String create(Model model) {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		model.addAttribute("classroomDTO", classroomDTO);
		return "classrooms/add";
	}

	@PostMapping("/add")
	public String createDTO(@ModelAttribute("classroomDTO") ClassroomDTO classroomDTO, Model model) {
		classroomService.create(classroomDTO);
		model.addAttribute(CLASSROOM, classroomDTO);
		model.addAttribute(RESULT, "A classroom was successfully added.");
		return CLASSROOMS_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("name") String name, Model model) {
		model.addAttribute("name", name);
		return "classrooms/edit";
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@ModelAttribute("name") String name, @ModelAttribute("initialName") String initialName,
			Model model) {

		ClassroomDTO initialClassroomDTO = new ClassroomDTO();
		initialClassroomDTO.setName(initialName);
		ClassroomDTO updatedClassroomDTO = new ClassroomDTO();
		updatedClassroomDTO.setName(name);

		classroomService.update(initialClassroomDTO, updatedClassroomDTO);
		model.addAttribute(RESULT, "Classroom was successfully updated");
		model.addAttribute(CLASSROOM, updatedClassroomDTO);
		return CLASSROOMS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("name") String name, Model model) {
		model.addAttribute("name", name);
		return "classrooms/delete";
	}

	@PostMapping("/delete/result")
	public String deleteDTOresult(@ModelAttribute("name") String name, Model model) {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName(name);
		classroomService.delete(classroomDTO);
		model.addAttribute(CLASSROOM, classroomDTO);
		model.addAttribute(RESULT, "A classroom was successfully deleted.");
		return CLASSROOMS_RESULT;
	}

}
