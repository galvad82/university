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
	private static final String SCRIPT = "script";
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
		model.addAttribute(SCRIPT, scriptCreator(teacherService.findAllLessonsForTeacher(firstName, lastName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/group")
	public String forGroup(Model model) {
		model.addAttribute("listOfGroups", groupService.findAllWithoutStudentList());
		return "schedule/group";
	}

	@PostMapping("/group/result")
	public String forGroupResult(@ModelAttribute("group") String groupName, Model model) {
		model.addAttribute(SCRIPT, scriptCreator(groupService.findAllLessonsForGroup(groupName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/classroom")
	public String forClassroom(Model model) {
		model.addAttribute("listOfClassrooms", classroomService.findAll());
		return "schedule/classroom";
	}

	@PostMapping("/classroom/result")
	public String forClassroomResult(@ModelAttribute("classroom") String classrooomName, Model model) {
		model.addAttribute(SCRIPT, scriptCreator(classroomService.findAllLessonsForClassroom(classrooomName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/course")
	public String forCourse(Model model) {
		model.addAttribute("listOfCourses", courseService.findAll());
		return "schedule/course";
	}

	@PostMapping("/course/result")
	public String forCourseResult(@ModelAttribute("course") String courseName, Model model) {
		model.addAttribute(SCRIPT, scriptCreator(courseService.findAllLessonsForCourse(courseName)));
		return SCHEDULE_RESULT;
	}

	private String scriptCreator(List<LessonDTO> listOfLessons) {
		StringBuilder scriptSourceCode = new StringBuilder();
		scriptSourceCode
				.append("document.addEventListener(\"DOMContentLoaded\",function(){var e=document.getElementById"
						+ "(\"calendar\");new FullCalendar.Calendar(e,{themeSystem:\"standard\",bootstrapFontAwesome:{close:"
						+ "\"fa-times\",prev:\"fa-chevron-left\",next:\"fa-chevron-right\",prevYear:\"fa-angle-double-left\""
						+ ",nextYear:\"fa-angle-double-right\"},events:[");

		listOfLessons.stream()
				.forEach(lesson -> scriptSourceCode.append(String.format(
						"{ title  : 'Group: %s, Course: %s, Classroom: %s, Teacher: %s %s', start : %d, end : %d}, ",
						lesson.getGroup().getName(), lesson.getCourse().getName(), lesson.getClassroom().getName(),
						lesson.getCourse().getTeacher().getFirstName(), lesson.getCourse().getTeacher().getLastName(),
						lesson.getStartTime(), lesson.getStartTime() + lesson.getDuration())));
		scriptSourceCode
				.append("{}],eventTimeFormat:{hour:\"2-digit\",minute:\"2-digit\",meridiem:!1,hour12:!1},slotMinTime:"
						+ "\"08:00\",slotMaxTime:\"22:00\",slotLabelFormat:[{hour:\"2-digit\",minute:\"2-digit\",meridiem:"
						+ "!1,hour12:!1},{hour:\"2-digit\",minute:\"2-digit\",meridiem:!1,hour12:!1}],locale:\"en\","
						+ "firstDay:1,headerToolbar:{center:\"dayGridMonth,timeGridWeek,timeGridDay\"},views:{dayGridMonth"
						+ ":{titleFormat:{year:\"numeric\",month:\"long\"},navLinks:!0},timeGridWeek:{titleFormat:{month:"
						+ "\"short\",day:\"numeric\"},titleRangeSeparator:\" to \",hour:\"2-digit\",minute:\"2-digit\""
						+ ",meridiem:!1,hour12:!1}}}).render()});");
		return scriptSourceCode.toString();
	}

}
