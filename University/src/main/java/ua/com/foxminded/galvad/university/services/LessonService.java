package ua.com.foxminded.galvad.university.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Event;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;

@Service
@Transactional
public class LessonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonService.class);
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CourseService courseService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private LessonDAO lessonDAO;

	public void create(LessonDTO lessonDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		lessonDAO.create(convertToEntityWithoutID(lessonDTO));
	}

	public void update(LessonDTO oldDTO, LessonDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update LessonDTO");
		lessonDAO.update(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated LessonDTO successfully");
	}

	@Transactional
	public void delete(LessonDTO lessonDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete LessonDTO");
		lessonDAO.delete(convertToEntity(lessonDTO));
	}
	
	public List<LessonDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL LessonDTO from DB");
		List<Lesson> listOfLessons = lessonDAO.findAll();
		List<LessonDTO> list = listOfLessons.stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL LessonDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public void deleteByClassroom(ClassroomDTO classroomDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete LessonDTO by classroomDTO (name={})", classroomDTO.getName());
		LOGGER.trace("Going to get an entity for classroomDTO (name={})", classroomDTO.getName());
		Classroom classroom = classroomService.convertToEntity(classroomDTO);
		LOGGER.trace("Got an entity for classroomDTO (name={})", classroomDTO.getName());
		LOGGER.trace("Going to retrieve ID for classroom entity (name={})", classroom.getName());
		Integer id = classroom.getId();
		LOGGER.trace("Retrieved ID={} for classroom (name={})", id, classroom.getName());
		LOGGER.trace("Going to delete LessonDTO by classroom (id={}, name={})", id, classroomDTO.getName());
		lessonDAO.deleteByClassroomID(id);
		LOGGER.trace("Deleted LessonDTO by classroom (id={}, name={}) successfully", id, classroomDTO.getName());
	}

	public void deleteByCourse(CourseDTO courseDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete LessonDTO by courseDTO (name={})", courseDTO.getName());
		LOGGER.trace("Going to get an entity for courseDTO (name={})", courseDTO.getName());
		Course course = courseService.convertToEntity(courseDTO);
		LOGGER.trace("Got an entity for courseDTO (name={})", courseDTO.getName());
		LOGGER.trace("Going to retrieve ID for course entity (name={})", course.getName());
		Integer id = course.getId();
		LOGGER.trace("Retrieved ID={} for course (name={})", id, course.getName());
		LOGGER.trace("Going to delete LessonDTO by course (id={}, name={})", id, courseDTO.getName());
		lessonDAO.deleteByCourseID(id);
		LOGGER.trace("Deleted LessonDTO by course (id={}, name={}) successfully", id, courseDTO.getName());
	}

	public void deleteByGroup(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete LessonDTO by groupDTO (name={})", groupDTO.getName());
		LOGGER.trace("Going to get an entity for groupDTO (name={})", groupDTO.getName());
		Group group = groupService.convertToEntity(groupDTO);
		LOGGER.trace("Got an entity for groupDTO (name={})", groupDTO.getName());
		LOGGER.trace("Going to retrieve ID for group entity (name={})", group.getName());
		Integer id = group.getId();
		LOGGER.trace("Retrieved ID={} for group (name={})", id, group.getName());
		LOGGER.trace("Going to delete LessonDTO by group (id={}, name={})", id, groupDTO.getName());
		lessonDAO.deleteByGroupID(id);
		LOGGER.trace("Deleted LessonDTO by group (id={}, name={}) successfully", id, groupDTO.getName());
	}

	@Transactional
	private LessonDTO convertToDTO(Lesson entity) throws DataNotFoundException {
		LOGGER.trace("Converting lesson to LessonDTO");
		LessonDTO lessonDTO = modelMapper.map(entity, LessonDTO.class);
		LOGGER.trace("Finished conversion of lesson to LessonDTO");
		return lessonDTO;
	}

	private Lesson convertToEntity(LessonDTO lessonDTO) throws DataNotFoundException {
		Lesson entity = convertToEntityWithoutID(lessonDTO);
		LOGGER.trace("Setting ID for the lesson");
		Integer id = lessonDAO.getId(entity);
		entity.setId(id);
		LOGGER.trace("Set ID={} for the lesson", id);
		LOGGER.trace("Finished conversion of LessonDTO to lesson");
		return entity;
	}

	private Lesson convertToEntity(LessonDTO oldDTO, LessonDTO newDTO) throws DataNotFoundException {
		Lesson entity = convertToEntityWithoutID(newDTO);
		LOGGER.trace("Setting ID for the lesson");
		Integer id = lessonDAO.getId(convertToEntity(oldDTO));
		entity.setId(id);
		LOGGER.trace("Set ID={} for the lesson", id);
		LOGGER.trace("Finished conversion of LessonDTO to lesson");
		return entity;
	}

	private Lesson convertToEntityWithoutID(LessonDTO lessonDTO) throws DataNotFoundException {
		LOGGER.trace("Starting conversion of LessonDTO to lesson");
		LOGGER.trace("Converting groupDTO to group");
		Group group = groupService.convertToEntity(lessonDTO.getGroup());
		LOGGER.trace("Converting courseDTO to course");
		Course course = courseService.convertToEntity(lessonDTO.getCourse());
		LOGGER.trace("Converting classroomDTO to classroom");
		Classroom classroom = classroomService.convertToEntity(lessonDTO.getClassroom());
		LOGGER.trace("Creating a new lesson with converted group, course and classroom");
		Lesson entity = new Lesson(group, course, classroom, lessonDTO.getStartTime(), lessonDTO.getDuration());
		LOGGER.trace("Created a new lesson with converted group, course and classroom");
		return entity;
	}

	public long convertDateToMil(String date) {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")).atZone(ZoneId.systemDefault())
				.toInstant().toEpochMilli();
	}

	public long convertTimeToMil(String timeString) {
		String[] time = timeString.split(":");
		return (long) (Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1])) * 1000 * 60;
	}

	public List<Event> eventListForCalendarCreator(List<LessonDTO> listOfLessons) {
		LOGGER.trace("Going to create a list of calendar events from the list of lessons");
		return listOfLessons.stream().filter(s -> s.getStartTime() > 0 && s.getDuration() >= 0)
				.map(this::convertLessonToEvent).collect(Collectors.toList());
	}

	private Event convertLessonToEvent(LessonDTO lesson) {
		String title = String.format("Group: %s, Course: %s, Classroom: %s, Teacher: %s %s",
				lesson.getGroup().getName(), lesson.getCourse().getName(), lesson.getClassroom().getName(),
				lesson.getCourse().getTeacher().getFirstName(), lesson.getCourse().getTeacher().getLastName());
		return new Event(title, lesson.getStartTime(), lesson.getStartTime() + lesson.getDuration());
	}

}
