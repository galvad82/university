package ua.com.foxminded.galvad.university.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.controllers.validation.ClassroomValidator;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.services.ClassroomService;

@Controller
@RequestMapping("/classrooms")
public class ClassroomsController {

	public final ClassroomService classroomService;
	public final ClassroomValidator classroomValidator;
	private static final String CLASSROOMS_RESULT = "classrooms/result";
	private static final String CLASSROOMS_LIST = "classrooms/list";
	private static final String CLASSROOMS_ADD = "classrooms/add";
	private static final String CLASSROOMS_EDIT = "classrooms/edit";
	private static final String CLASSROOMS_DELETE = "classrooms/delete";
	private static final String CLASSROOM_DTO = "classroomDTO";
	private static final String RESULT = "result";

	@Autowired
	public ClassroomsController(ClassroomService classroomService, ClassroomValidator classroomValidator) {
		this.classroomService = classroomService;
		this.classroomValidator = classroomValidator;
	}

	@GetMapping()
	public String findAll(Model model) {
		model.addAttribute("classrooms", classroomService.findAll());
		return CLASSROOMS_LIST;
	}

	@GetMapping("/add")
	public String create(Model model) {
		model.addAttribute(CLASSROOM_DTO, new ClassroomDTO());
		return CLASSROOMS_ADD;
	}

	@PostMapping("/add")
	public String createDTO(@Valid ClassroomDTO classroomDTO, BindingResult result, Model model) {
		classroomValidator.validate(classroomDTO, result);
		if (result.hasErrors()) {
			return CLASSROOMS_ADD;
		}
		classroomService.create(classroomDTO);
		model.addAttribute(CLASSROOM_DTO, classroomDTO);
		model.addAttribute(RESULT, "A classroom was successfully added.");
		return CLASSROOMS_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(String name, Model model) {
		ClassroomDTO classroomDTO = classroomService.retrieve(name);
		model.addAttribute(CLASSROOM_DTO, classroomDTO);
		model.addAttribute("initialName", classroomDTO.getName());
		return CLASSROOMS_EDIT;
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@Valid ClassroomDTO classroomDTO, BindingResult result,
			@ModelAttribute("initialName") String initialName, Model model) {
		if (!classroomDTO.getName().equals(initialName)) {
			classroomValidator.validate(classroomDTO, result);
		}
		if (result.hasErrors()) {
			return CLASSROOMS_EDIT;
		}
		ClassroomDTO initialClassroomDTO = classroomService.retrieve(initialName);
		classroomService.update(initialClassroomDTO, classroomDTO);
		model.addAttribute(RESULT, "Classroom was successfully updated");
		return CLASSROOMS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(String name, Model model) {
		model.addAttribute(CLASSROOM_DTO, classroomService.retrieve(name));
		return CLASSROOMS_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteDTOResult(@Valid ClassroomDTO classroomDTO, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return CLASSROOMS_LIST;
		}
		classroomService.delete(classroomDTO);
		model.addAttribute(RESULT, "A classroom was successfully deleted.");
		return CLASSROOMS_RESULT;
	}

}
