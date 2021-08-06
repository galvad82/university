package ua.com.foxminded.galvad.university.controllers;

import java.util.ArrayList;
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
import ua.com.foxminded.galvad.university.services.LessonService;

@Controller
@RequestMapping("/lessons")
public class LessonsController {
	private static final String LESSON="lesson";
	private static final String LESSON_DTO="lessonDTO";
	private static final String LESSON_RESULT="lessons/result";
	private static final String RESULT="result";
	private final LessonService lessonService;
	private final GroupService groupService;
	private final CourseService courseService;
	private final ClassroomService classroomService;

	@Autowired
	public LessonsController(LessonService lessonService, GroupService groupService, CourseService courseService,
			ClassroomService classroomService) {
		this.lessonService = lessonService;
		this.groupService = groupService;
		this.courseService = courseService;
		this.classroomService = classroomService;
	}

	@GetMapping()
	public String findAll(Model model) {
		List<LessonDTO> listOfLessonDTOs = lessonService.findAll();
		model.addAttribute("lessons", listOfLessonDTOs);
		model.addAttribute(LESSON_DTO, new LessonDTO());
		return "lessons/list";
	}

	@GetMapping("/add")
	public String create(Model model) {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setStartTime(0);
		lessonDTO.setDuration(0);
		model.addAttribute(LESSON_DTO, lessonDTO);

		List<String> listOfGroupNames = new ArrayList<>();
		listOfGroupNames.add("");
		groupService.findAllWithoutStudentList().stream().forEach(s -> listOfGroupNames.add(s.getName()));
		model.addAttribute("groups", listOfGroupNames);

		List<String> listOfCourseNames = new ArrayList<>();
		listOfCourseNames.add("");
		courseService.findAll().stream().forEach(s -> listOfCourseNames.add(s.getName()));
		model.addAttribute("courses", listOfCourseNames);

		List<String> listOfClassroomNames = new ArrayList<>();
		listOfClassroomNames.add("");
		classroomService.findAll().stream().forEach(s -> listOfClassroomNames.add(s.getName()));
		model.addAttribute("classrooms", listOfClassroomNames);
		return "lessons/add";
	}

	@PostMapping("/add")
	public String createDTO(@ModelAttribute("group") String groupName, @ModelAttribute("course") String courseName,
			@ModelAttribute("classroom") String classroomName, @ModelAttribute("startTime") String startTime,
			@ModelAttribute("duration") String duration, Model model) {

		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setCourse(courseService.retrieve(courseName));
		lessonDTO.setGroup(groupService.retrieve(groupName));
		lessonDTO.setStartTime(lessonService.convertDateToMil(startTime));
		lessonDTO.setDuration(lessonService.convertTimeToMil(duration));
		lessonDTO.setClassroom(classroomService.retrieve(classroomName));
		lessonService.create(lessonDTO);
		model.addAttribute(LESSON, lessonDTO);
		model.addAttribute(RESULT, "A lesson was successfully added.");
		return LESSON_RESULT;
	}

	@PostMapping("/edit")
	public String editDTO(@ModelAttribute("groupName") String groupName, @ModelAttribute("courseName") String courseName,
			@ModelAttribute("classroomName") String classroomName, @ModelAttribute("startTime") String startTimeString,
			@ModelAttribute("duration") String durationString, Model model) {

		List<String> listOfGroupNames = new ArrayList<>();
		groupService.findAllWithoutStudentList().stream().forEach(s -> listOfGroupNames.add(s.getName()));
		model.addAttribute("groups", listOfGroupNames);

		List<String> listOfCourseNames = new ArrayList<>();
		courseService.findAll().stream().forEach(s -> listOfCourseNames.add(s.getName()));
		model.addAttribute("courses", listOfCourseNames);

		List<String> listOfClassroomNames = new ArrayList<>();
		classroomService.findAll().stream().forEach(s -> listOfClassroomNames.add(s.getName()));
		model.addAttribute("classrooms", listOfClassroomNames);		
		
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setStartTime(lessonService.convertDateToMil(startTimeString));
		lessonDTO.setDuration(lessonService.convertTimeToMil(durationString));
		model.addAttribute(LESSON_DTO, lessonDTO);
		
		model.addAttribute("initialGroup", groupName);
		model.addAttribute("initialCourse", courseName);
		model.addAttribute("initialClassroom", classroomName);
		model.addAttribute("initialStartTime", startTimeString);
		model.addAttribute("initialDuration", durationString);
		model.addAttribute(LESSON_DTO, lessonDTO);
		
		return "lessons/edit";
	}
	
	@PostMapping("/edit/result")
	public String editDTOResult(@ModelAttribute("initialGroup") String initialGroupName, @ModelAttribute("initialCourse") String initialCourseName,
			@ModelAttribute("initialClassroom") String initialClassroomName, @ModelAttribute("initialStartTime") String initialStartTimeString,
			@ModelAttribute("initialDuration") String initialDurationString, @ModelAttribute("group") String updatedGroupName, @ModelAttribute("course") String updatedCourseName,
			@ModelAttribute("classroom") String updatedClassroomName, @ModelAttribute("startTime") String updatedStartTimeString,
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
		
		lessonService.update(initialLessonDTO, updatedLessonDTO);
		model.addAttribute(RESULT, "Lesson was successfully updated");
		model.addAttribute(LESSON, updatedLessonDTO);
		return LESSON_RESULT;
	}
	
	@PostMapping("/delete")
	public String deleteDTO(@ModelAttribute("groupName") String groupName, @ModelAttribute("courseName") String courseName,
			@ModelAttribute("classroomName") String classroomName, @ModelAttribute("startTime") String startTimeString,
			@ModelAttribute("duration") String durationString, Model model) {
		
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setGroup(groupService.retrieve(groupName));
		lessonDTO.setClassroom(classroomService.retrieve(classroomName));
		lessonDTO.setCourse(courseService.retrieve(courseName));
		lessonDTO.setStartTime(lessonService.convertDateToMil(startTimeString));
		lessonDTO.setDuration(lessonService.convertTimeToMil(durationString));

		model.addAttribute(LESSON, lessonDTO);
		return "lessons/delete";
	}
	
	@PostMapping("/delete/result")
	public String deleteCourseFromDB(@ModelAttribute("lesson") LessonDTO lessonDTO, Model model) {

		lessonDTO.setCourse(courseService.retrieve(lessonDTO.getCourse().getName()));
		lessonService.delete(lessonDTO);		
		model.addAttribute(RESULT, "A lesson was successfully added");
		model.addAttribute(LESSON, lessonDTO);
		return LESSON_RESULT;
	}
}
