package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Controller
@RequestMapping("/courses")
public class CoursesController {

	public final CourseService courseService;
	public final TeacherService teacherService;

	private static final String COURSES_RESULT = "courses/result";
	private static final String COURSES_LIST = "courses/list";
	private static final String COURSES_ADD = "courses/add";
	private static final String COURSES_EDIT = "courses/edit";
	private static final String COURSES_DELETE = "courses/delete";
	private static final String COURSE = "course";
	private static final String RESULT = "result";

	@Autowired
	public CoursesController(CourseService courseService, TeacherService teacherService) {
		this.courseService = courseService;
		this.teacherService = teacherService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<CourseDTO> listOfCourseDTOs = courseService.findAll();
		model.addAttribute("courses", listOfCourseDTOs);
		return COURSES_LIST;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("name") String name, Model model) {
		CourseDTO courseDTO = courseService.retrieve(name);
		model.addAttribute("courseDTO", courseDTO);
		List<TeacherDTO> listOfTeachers = teacherService.findAll();
		model.addAttribute("listOfTeachers", listOfTeachers);
		return COURSES_EDIT;
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@ModelAttribute("courseDTO") CourseDTO updatedCourseDTO,
			@ModelAttribute("initialName") String initialName, Model model) {
		CourseDTO initialCourseDTO = courseService.retrieve(initialName);
		courseService.update(initialCourseDTO, updatedCourseDTO);
		model.addAttribute(RESULT, "Course was successfully updated");
		model.addAttribute(COURSE, updatedCourseDTO);
		return COURSES_RESULT;
	}

	@GetMapping("/add")
	public String addCourse(Model model) {
		List<TeacherDTO> listOfTeachers = teacherService.findAll();
		TeacherDTO emptyDTO = new TeacherDTO();
		emptyDTO.setFirstName("");
		emptyDTO.setLastName("");
		listOfTeachers.add(0, emptyDTO);
		model.addAttribute("listOfTeachers", listOfTeachers);
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("");
		courseDTO.setTeacher(emptyDTO);
		model.addAttribute("courseDTO", courseDTO);
		return COURSES_ADD;
	}

	@PostMapping("/add")
	public String addCourseToDB(@ModelAttribute("courseDTO") CourseDTO courseDTO, Model model) {
		courseService.create(courseDTO);
		model.addAttribute(RESULT, "A course was successfully added.");
		model.addAttribute(COURSE, courseDTO);
		return COURSES_RESULT;
	}

	@PostMapping("/delete")
	public String deleteCourse(@ModelAttribute("name") String name, Model model) {
		model.addAttribute(COURSE, courseService.retrieve(name));
		return COURSES_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteCourseFromDB(@ModelAttribute("course") CourseDTO courseDTO, Model model) {
		courseService.delete(courseDTO);
		model.addAttribute(RESULT, "A course was successfully deleted.");
		model.addAttribute(COURSE, courseDTO);
		return COURSES_RESULT;
	}

}
