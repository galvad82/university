package ua.com.foxminded.galvad.university.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.StudentService;

@Controller
@RequestMapping("/students")
public class StudentsController {

	private static final String STUDENTS_RESULT = "students/result";
	private static final String STUDENTS_LIST = "students/list";
	private static final String STUDENTS_ADD = "students/add";
	private static final String STUDENTS_EDIT = "students/edit";
	private static final String STUDENTS_DELETE = "students/delete";
	private static final String STUDENT = "student";
	private static final String RESULT = "result";
	private static final String STUDENTDTO = "studentDTO";
	private static final String GROUP_NAME = "groupName";

	private final GroupService groupService;
	private final StudentService studentService;
	private static final String NONE = "NONE";

	@Autowired
	public StudentsController(StudentService studentService, GroupService groupService) {
		this.studentService = studentService;
		this.groupService = groupService;
	}

	@GetMapping()
	public String findAll(Model model) {
		Map<StudentDTO, String> studentGroupMap = studentService.buildStudentGroupMap();
		model.addAttribute("students", studentGroupMap);
		StudentDTO studentDTO = new StudentDTO();
		model.addAttribute(STUDENTDTO, studentDTO);
		return STUDENTS_LIST;
	}

	@GetMapping("/add")
	public String create(Model model) {
		StudentDTO studentDTO = new StudentDTO();
		model.addAttribute(STUDENTDTO, studentDTO);
		List<String> listGroupNames = new ArrayList<>();
		listGroupNames.add(NONE);
		groupService.findAll().stream().forEach(s -> listGroupNames.add(s.getName()));
		model.addAttribute("listGroupNames", listGroupNames);
		return STUDENTS_ADD;
	}

	@PostMapping("/add")
	public String createDTO(@ModelAttribute("studentDTO") StudentDTO studentDTO,
			@ModelAttribute("group") String groupDTOName, Model model) {
		studentService.create(studentDTO);
		if (!groupDTOName.equals(NONE)) {
			studentService.addToGroup(studentDTO, groupService.retrieve(groupDTOName));
		}
		model.addAttribute(STUDENT, studentDTO);
		model.addAttribute(GROUP_NAME, groupDTOName);
		model.addAttribute(RESULT, "A student was successfully added.");
		return STUDENTS_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			@ModelAttribute("groupName") String groupName, Model model) {
		List<GroupDTO> listOfGroups = groupService.findAll();
		GroupDTO noneDto = new GroupDTO();
		noneDto.setName(NONE);
		listOfGroups.add(0, noneDto);
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(firstName);
		studentDTO.setLastName(lastName);

		model.addAttribute("listOfGroups", listOfGroups);
		model.addAttribute("initialGroup", groupName);
		model.addAttribute(STUDENTDTO, studentDTO);
		return STUDENTS_EDIT;
	}

	@PostMapping("/edit/result")
	public String updateDTO(@ModelAttribute("studentDTO") StudentDTO updatedStudentDTO,
			@ModelAttribute("groupName") String groupName, @ModelAttribute("initialGroup") String initialGroupName,
			@ModelAttribute("initialFirstName") String initialFirstName,
			@ModelAttribute("initialLastName") String initialLastName, Model model) {

		StudentDTO initialStudentDTO = new StudentDTO();
		initialStudentDTO.setFirstName(initialFirstName);
		initialStudentDTO.setLastName(initialLastName);

		studentService.update(initialStudentDTO, updatedStudentDTO);
		if (groupName.equals(NONE)) {
			studentService.removeStudentFromGroup(updatedStudentDTO);
		} else {
			GroupDTO groupDTO = groupService.retrieve(groupName);
			if (initialGroupName.equals(NONE)) {
				studentService.addToGroup(updatedStudentDTO, groupDTO);
			} else {
				studentService.updateGroup(updatedStudentDTO, groupDTO);
			}
		}

		model.addAttribute(RESULT, "Student was successfully updated");
		model.addAttribute(GROUP_NAME, groupName);
		model.addAttribute(STUDENT, updatedStudentDTO);
		return STUDENTS_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("firstName") String firstName, @ModelAttribute("lastName") String lastName,
			@ModelAttribute("groupName") String groupName, Model model) {
		model.addAttribute(GROUP_NAME, groupName);
		model.addAttribute("firstName", firstName);
		model.addAttribute("lastName", lastName);
		return STUDENTS_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteDTOResult(@ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName, @ModelAttribute("groupName") String groupName, Model model) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(firstName);
		studentDTO.setLastName(lastName);
		studentService.delete(studentDTO);
		model.addAttribute(STUDENT, studentDTO);
		model.addAttribute(GROUP_NAME, groupName);
		model.addAttribute(RESULT, "A student was successfully deleted.");
		return STUDENTS_RESULT;
	}

}
