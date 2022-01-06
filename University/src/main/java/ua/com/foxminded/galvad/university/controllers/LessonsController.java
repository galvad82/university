package ua.com.foxminded.galvad.university.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.galvad.university.controllers.validation.LessonValidator;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.services.ClassroomService;
import ua.com.foxminded.galvad.university.services.CourseService;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonsController {

	private static final String LESSON_RESULT = "lessons/result";
	private static final String LESSONS_LIST = "lessons/list";
	private static final String LESSONS_ADD = "lessons/add";
	private static final String LESSONS_EDIT = "lessons/edit";
	private static final String LESSONS_DELETE = "lessons/delete";
	private static final String RESULT = "result";
	private static final String LESSON_DTO = "lessonDTO";
	private static final String GROUPS = "groups";
	private static final String COURSES = "courses";
	private static final String CLASSROOMS = "classrooms";
	private static final String START_TIME = "startTime";
	private static final String DURATION = "duration";
	private static final String ERROR = "error";

	private final LessonService lessonService;
	private final GroupService groupService;
	private final CourseService courseService;
	private final ClassroomService classroomService;
	private final LessonValidator lessonValidator;

	@Autowired
	public LessonsController(LessonService lessonService, GroupService groupService, CourseService courseService,
			ClassroomService classroomService, LessonValidator lessonValidator) {
		this.lessonService = lessonService;
		this.groupService = groupService;
		this.courseService = courseService;
		this.classroomService = classroomService;
		this.lessonValidator = lessonValidator;
	}

	@GetMapping()
	public String findAll(Model model) {
		model.addAttribute("lessons", lessonService.findAll());
		return LESSONS_LIST;
	}

	@GetMapping("/add")
	public String create(Model model) {
		model.addAttribute(GROUPS, groupService.findAll().stream().map(GroupDTO::getName).collect(Collectors.toList()));
		model.addAttribute(COURSES,
				courseService.findAll().stream().map(CourseDTO::getName).collect(Collectors.toList()));
		model.addAttribute(CLASSROOMS,
				classroomService.findAll().stream().map(ClassroomDTO::getName).collect(Collectors.toList()));
		model.addAttribute(START_TIME, "01-01-2021 02:00");
		model.addAttribute(DURATION, "00:30");
		return LESSONS_ADD;
	}

	@PostMapping("/add")
	public String createDTO(String group, String course, String classroom, String startTime, String duration,
			Model model) {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setGroup(groupService.retrieve(group));
		lessonDTO.setCourse(courseService.retrieve(course));
		lessonDTO.setClassroom(classroomService.retrieve(classroom));
		lessonDTO.setStartTime(lessonService.convertDateToMil(startTime));
		lessonDTO.setDuration(lessonService.convertTimeToMil(duration));
		Errors result = new BeanPropertyBindingResult(lessonDTO, LESSON_DTO);
		lessonValidator.validate(lessonDTO, result);
		if (result.hasErrors()) {
			model.addAttribute("group", group);
			model.addAttribute("course", course);
			model.addAttribute("classroom", classroom);
			model.addAttribute(START_TIME, startTime);
			model.addAttribute(DURATION, duration);
			model.addAttribute(ERROR,
					result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList()));
			model.addAttribute(GROUPS,
					groupService.findAll().stream().map(GroupDTO::getName).collect(Collectors.toList()));
			model.addAttribute(COURSES,
					courseService.findAll().stream().map(CourseDTO::getName).collect(Collectors.toList()));
			model.addAttribute(CLASSROOMS,
					classroomService.findAll().stream().map(ClassroomDTO::getName).collect(Collectors.toList()));
			return LESSONS_ADD;
		}
		lessonService.create(lessonDTO);
		model.addAttribute(LESSON_DTO, lessonDTO);
		model.addAttribute(RESULT, "A lesson was successfully added.");
		return LESSON_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(String groupName, String courseName, String classroomName,
			@ModelAttribute("startTime") String startTimeString, @ModelAttribute("duration") String durationString,
			Model model) {

		model.addAttribute(GROUPS, groupService.findAll().stream().map(GroupDTO::getName).collect(Collectors.toList()));
		model.addAttribute(COURSES,
				courseService.findAll().stream().map(CourseDTO::getName).collect(Collectors.toList()));
		model.addAttribute(CLASSROOMS,
				classroomService.findAll().stream().map(ClassroomDTO::getName).collect(Collectors.toList()));
		model.addAttribute(START_TIME, startTimeString);
		model.addAttribute(DURATION, durationString);
		model.addAttribute("initialGroup", groupName);
		model.addAttribute("initialCourse", courseName);
		model.addAttribute("initialClassroom", classroomName);
		model.addAttribute("initialStartTime", startTimeString);
		model.addAttribute("initialDuration", durationString);
		return LESSONS_EDIT;
	}

	@PostMapping("/edit/result")
	public String editDTOResult(@ModelAttribute("initialGroup") String initialGroupName,
			@ModelAttribute("initialCourse") String initialCourseName,
			@ModelAttribute("initialClassroom") String initialClassroomName,
			@ModelAttribute("initialStartTime") String initialStartTimeString,
			@ModelAttribute("initialDuration") String initialDurationString,
			@ModelAttribute("group") String updatedGroupName, @ModelAttribute("course") String updatedCourseName,
			@ModelAttribute("classroom") String updatedClassroomName,
			@ModelAttribute("startTime") String updatedStartTimeString,
			@ModelAttribute("duration") String updatedDurationString, Model model) {

		LessonDTO initialLessonDTO = new LessonDTO();
		initialLessonDTO.setGroup(groupService.retrieve(initialGroupName));
		initialLessonDTO.setClassroom(classroomService.retrieve(initialClassroomName));
		initialLessonDTO.setCourse(courseService.retrieve(initialCourseName));
		initialLessonDTO.setStartTime(lessonService.convertDateToMil(initialStartTimeString));
		initialLessonDTO.setDuration(lessonService.convertTimeToMil(initialDurationString));

		LessonDTO updatedLessonDTO = new LessonDTO();
		updatedLessonDTO.setGroup(groupService.retrieve(updatedGroupName));
		updatedLessonDTO.setClassroom(classroomService.retrieve(updatedClassroomName));
		updatedLessonDTO.setCourse(courseService.retrieve(updatedCourseName));
		updatedLessonDTO.setStartTime(lessonService.convertDateToMil(updatedStartTimeString));
		updatedLessonDTO.setDuration(lessonService.convertTimeToMil(updatedDurationString));
		if (!updatedLessonDTO.equals(initialLessonDTO)) {
			Errors result = new BeanPropertyBindingResult(updatedLessonDTO, LESSON_DTO);
			lessonValidator.validate(updatedLessonDTO, result);
			if (result.hasErrors()) {
				model.addAttribute(ERROR, result.getAllErrors().stream().map(ObjectError::getDefaultMessage)
						.collect(Collectors.toList()));
				model.addAttribute(GROUPS,
						groupService.findAll().stream().map(GroupDTO::getName).collect(Collectors.toList()));
				model.addAttribute(COURSES,
						courseService.findAll().stream().map(CourseDTO::getName).collect(Collectors.toList()));
				model.addAttribute(CLASSROOMS,
						classroomService.findAll().stream().map(ClassroomDTO::getName).collect(Collectors.toList()));
				model.addAttribute("group", updatedGroupName);
				model.addAttribute("course", updatedCourseName);
				model.addAttribute("classroom", updatedClassroomName);
				model.addAttribute(START_TIME, updatedStartTimeString);
				model.addAttribute(DURATION, updatedDurationString);
				model.addAttribute("initialGroup", initialGroupName);
				model.addAttribute("initialCourse", initialCourseName);
				model.addAttribute("initialClassroom", initialClassroomName);
				model.addAttribute("initialStartTime", initialStartTimeString);
				model.addAttribute("initialDuration", initialDurationString);
				return LESSONS_EDIT;
			}
		}
		lessonService.update(initialLessonDTO, updatedLessonDTO);
		model.addAttribute(RESULT, "Lesson was successfully updated");
		model.addAttribute(LESSON_DTO, updatedLessonDTO);
		return LESSON_RESULT;
	}

	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("groupName") String groupName,
			@ModelAttribute("courseName") String courseName, @ModelAttribute("classroomName") String classroomName,
			@ModelAttribute("startTime") String startTimeString, @ModelAttribute("duration") String durationString,
			Model model) {

		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setGroup(groupService.retrieve(groupName));
		lessonDTO.setClassroom(classroomService.retrieve(classroomName));
		lessonDTO.setCourse(courseService.retrieve(courseName));
		lessonDTO.setStartTime(lessonService.convertDateToMil(startTimeString));
		lessonDTO.setDuration(lessonService.convertTimeToMil(durationString));
		model.addAttribute(LESSON_DTO, lessonDTO);
		return LESSONS_DELETE;
	}

	@PostMapping("/delete/result")
	public String deleteCourseFromDB(@ModelAttribute("lesson") LessonDTO lessonDTO, Model model) {
		lessonDTO.setCourse(courseService.retrieve(lessonDTO.getCourse().getName()));
		Errors result = new BeanPropertyBindingResult(lessonDTO, LESSON_DTO);
		lessonValidator.validate(lessonDTO, result);
		if (result.hasErrors()) {
			return LESSONS_LIST;
		}

		lessonService.delete(lessonDTO);
		model.addAttribute(RESULT, "A lesson was successfully deleted.");
		model.addAttribute(LESSON_DTO, lessonDTO);
		return LESSON_RESULT;
	}
}
