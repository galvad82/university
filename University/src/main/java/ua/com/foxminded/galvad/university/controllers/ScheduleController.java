package ua.com.foxminded.galvad.university.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.services.ClassroomService;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
	private static final String LESSONS = "lessons";
	private static final String SCHEDULE_RESULT = "schedule/result";

	private final GroupService groupService;
	private final CourseService courseService;
	private final ClassroomService classroomService;
	private final TeacherService teacherService;

	@Autowired
	public ScheduleController(GroupService groupService, CourseService courseService, ClassroomService classroomService,
			TeacherService teacherService) {
		this.groupService = groupService;
		this.courseService = courseService;
		this.classroomService = classroomService;
		this.teacherService = teacherService;
	}

	@GetMapping("/teacher")
	public String forTeacher(Model model) {
		model.addAttribute("listOfTeachers", teacherService.findAll());
		model.addAttribute("firstName", "");
		model.addAttribute("lastName", "");
		return "schedule/teacher";
	}

	@PostMapping("/teacher/result")
	public String forTeacherResult(@ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName, Model model) {
		model.addAttribute(LESSONS, eventListForCalendarCreator(teacherService.findAllLessonsForTeacher(firstName, lastName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/group")
	public String forGroup(Model model) {
		model.addAttribute("listOfGroups", groupService.findAllWithoutStudentList());
		return "schedule/group";
	}

	@PostMapping("/group/result")
	public String forGroupResult(@ModelAttribute("group") String groupName, Model model) {
		model.addAttribute(LESSONS, eventListForCalendarCreator(groupService.findAllLessonsForGroup(groupName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/classroom")
	public String forClassroom(Model model) {
		model.addAttribute("listOfClassrooms", classroomService.findAll());
		return "schedule/classroom";
	}

	@PostMapping("/classroom/result")
	public String forClassroomResult(@ModelAttribute("classroom") String classrooomName, Model model) {
		model.addAttribute(LESSONS, eventListForCalendarCreator(classroomService.findAllLessonsForClassroom(classrooomName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/course")
	public String forCourse(Model model) {
		model.addAttribute("listOfCourses", courseService.findAll());
		return "schedule/course";
	}

	@PostMapping("/course/result")
	public String forCourseResult(@ModelAttribute("course") String courseName, Model model) {
		model.addAttribute(LESSONS, eventListForCalendarCreator(courseService.findAllLessonsForCourse(courseName)));
		return SCHEDULE_RESULT;
	}

	private String eventListForCalendarCreator(List<LessonDTO> listOfLessons) {
		StringBuilder events = new StringBuilder();
		listOfLessons.stream()
				.forEach(lesson -> events.append(String.format(
						"{ title  : 'Group: %s, Course: %s, Classroom: %s, Teacher: %s %s', start : %d, end : %d}, ",
						lesson.getGroup().getName(), lesson.getCourse().getName(), lesson.getClassroom().getName(),
						lesson.getCourse().getTeacher().getFirstName(), lesson.getCourse().getTeacher().getLastName(),
						lesson.getStartTime(), lesson.getStartTime() + lesson.getDuration())));
		events.append("{}");
		return events.toString();
	}

}
