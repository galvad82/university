package ua.com.foxminded.galvad.university.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.ClassroomService;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.LessonService;
import ua.com.foxminded.galvad.university.services.TeacherService;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

	private static final String SCHEDULE_TEACHER = "schedule/teacher";
	private static final String SCHEDULE_GROUP = "schedule/group";
	private static final String SCHEDULE_CLASSROOM = "schedule/classroom";
	private static final String SCHEDULE_COURSE = "schedule/course";

	private static final String LESSONS = "lessons";
	private static final String SCHEDULE_RESULT = "schedule/result";

	private final GroupService groupService;
	private final CourseService courseService;
	private final ClassroomService classroomService;
	private final TeacherService teacherService;
	private final LessonService lessonService;

	@Autowired
	public ScheduleController(GroupService groupService, CourseService courseService, ClassroomService classroomService,
			TeacherService teacherService, LessonService lessonService) {
		this.groupService = groupService;
		this.courseService = courseService;
		this.classroomService = classroomService;
		this.teacherService = teacherService;
		this.lessonService = lessonService;
	}

	@GetMapping("/teacher")
	public String forTeacher(Model model) throws DataNotFoundException {
		model.addAttribute("listOfTeachers", teacherService.findAll());
		model.addAttribute("firstName", "");
		model.addAttribute("lastName", "");
		return SCHEDULE_TEACHER;
	}

	@PostMapping("/teacher/result")
	public String forTeacherResult(@ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName, Model model) throws DataNotFoundException {
		model.addAttribute(LESSONS, lessonService
				.eventListForCalendarCreator(teacherService.findAllLessonsForTeacher(firstName, lastName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/group")
	public String forGroup(Model model) throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute("listOfGroups", groupService.findAll());
		return SCHEDULE_GROUP;
	}

	@PostMapping("/group/result")
	public String forGroupResult(@ModelAttribute("group") String groupName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute(LESSONS,
				lessonService.eventListForCalendarCreator(lessonService.findAllLessonsForGroup(groupName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/classroom")
	public String forClassroom(Model model) throws DataNotFoundException {
		model.addAttribute("listOfClassrooms", classroomService.findAll());
		return SCHEDULE_CLASSROOM;
	}

	@PostMapping("/classroom/result")
	public String forClassroomResult(@ModelAttribute("classroom") String classrooomName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute(LESSONS,
				lessonService.eventListForCalendarCreator(lessonService.findAllLessonsForClassroom(classrooomName)));
		return SCHEDULE_RESULT;
	}

	@GetMapping("/course")
	public String forCourse(Model model) throws DataNotFoundException {
		model.addAttribute("listOfCourses", courseService.findAll());
		return SCHEDULE_COURSE;
	}

	@PostMapping("/course/result")
	public String forCourseResult(@ModelAttribute("course") String courseName, Model model)
			throws DataAreNotUpdatedException, DataNotFoundException {
		model.addAttribute(LESSONS,
				lessonService.eventListForCalendarCreator(lessonService.findAllLessonsForCourse(courseName)));
		return SCHEDULE_RESULT;
	}

	@ExceptionHandler({ DataAreNotUpdatedException.class })
	public String databaseError(Model model, DataAreNotUpdatedException exception) {
		LOGGER.error(exception.getMessage());
		LOGGER.error(exception.getCauseDescription());
		model.addAttribute("error", exception.getErrorMessage());
		return "/exception";
	}

	@ExceptionHandler({ DataNotFoundException.class })
	public String databaseError(Model model, DataNotFoundException exception) {
		LOGGER.error(exception.getErrorMessage());
		LOGGER.error(exception.getCauseDescription());
		model.addAttribute("error", exception.getErrorMessage());
		model.addAttribute("cause", exception.getCauseDescription());
		return "/exception";
	}
}
