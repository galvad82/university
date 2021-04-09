package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.ClassroomDAO;
import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Teacher;

@Service
public class LessonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LessonService.class);

	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CourseService courseService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private ClassroomDAO classroomDAO;
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private TeacherDAO teacherDAO;
	@Autowired
	private LessonDAO lessonDAO;

	public void create(LessonDTO lessonDTO) {
		try {
			Lesson lesson = convertToEntity(lessonDTO);
			lessonDAO.create(lesson);
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public LessonDTO retrieve(Integer id) {
		LOGGER.trace("Going to retrieve LessonDTO from lesson with ID={}", id);
		LessonDTO lessonDTO = new LessonDTO();
		try {
			LOGGER.trace("Going to retrieve lesson by ID={}", id);
			Lesson lesson = lessonDAO.retrieve(id);
			LOGGER.trace("Retrieved a lesson with ID={}", id);
			LOGGER.trace("Going to retrieve LessonDTO from a lesson with ID={}", id);
			lessonDTO = convertToDTO(lesson);
			LOGGER.trace("Retrieved LessonDTO from a lesson with ID={}", id);
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return lessonDTO;
	}

	public void update(LessonDTO oldDTO, LessonDTO newDTO) {
		LOGGER.trace("Going to update LessonDTO");
		try {
			lessonDAO.update(convertToEntity(oldDTO, newDTO));
			LOGGER.trace("Updated LessonDTO successfully");
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public void delete(LessonDTO lessonDTO) {
		LOGGER.trace("Going to delete LessonDTO");
		try {
			lessonDAO.delete(convertToEntity(lessonDTO));
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public List<LessonDTO> findAll() {
		LOGGER.trace("Going to get list of ALL LessonDTO from DB");
		List<LessonDTO> list = new ArrayList<>();
		try {
			list = lessonDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
			LOGGER.trace("List of ALL LessonDTO retrieved from DB, {} were found", list.size());
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return list;
	}

	public void deleteByClassroom(ClassroomDTO classroomDTO) {
		LOGGER.trace("Going to delete LessonDTO by classroomDTO (name={})", classroomDTO.getName());
		try {
			LOGGER.trace("Going to get an entity for classroomDTO (name={})", classroomDTO.getName());
			Classroom classroom = classroomService.convertToEntity(classroomDTO);
			LOGGER.trace("Got an entity for classroomDTO (name={})", classroomDTO.getName());
			LOGGER.trace("Going to retrieve ID for classroom entity (name={})", classroom.getName());
			Integer id = classroom.getId();
			LOGGER.trace("Retrieved ID={} for classroom (name={})", id, classroom.getName());
			LOGGER.trace("Going to delete LessonDTO by classroom (id={}, name={})", id, classroomDTO.getName());
			lessonDAO.deleteByClassroomID(id);
			LOGGER.trace("Deleted LessonDTO by classroom (id={}, name={}) successfully", id, classroomDTO.getName());
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public void deleteByCourse(CourseDTO courseDTO) {
		LOGGER.trace("Going to delete LessonDTO by courseDTO (name={})", courseDTO.getName());
		try {
			LOGGER.trace("Going to get an entity for courseDTO (name={})", courseDTO.getName());
			Course course = courseService.convertToEntity(courseDTO);
			LOGGER.trace("Got an entity for courseDTO (name={})", courseDTO.getName());
			LOGGER.trace("Going to retrieve ID for course entity (name={})", course.getName());
			Integer id = course.getId();
			LOGGER.trace("Retrieved ID={} for course (name={})", id, course.getName());
			LOGGER.trace("Going to delete LessonDTO by course (id={}, name={})", id, courseDTO.getName());
			lessonDAO.deleteByCourseID(id);
			LOGGER.trace("Deleted LessonDTO by course (id={}, name={}) successfully", id, courseDTO.getName());
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public void deleteByGroup(GroupDTO groupDTO) {
		LOGGER.trace("Going to delete LessonDTO by groupDTO (name={})", groupDTO.getName());
		try {
			LOGGER.trace("Going to get an entity for groupDTO (name={})", groupDTO.getName());
			Group group = groupService.convertToEntity(groupDTO);
			LOGGER.trace("Got an entity for groupDTO (name={})", groupDTO.getName());
			LOGGER.trace("Going to retrieve ID for group entity (name={})", group.getName());
			Integer id = group.getId();
			LOGGER.trace("Retrieved ID={} for group (name={})", id, group.getName());
			LOGGER.trace("Going to delete LessonDTO by group (id={}, name={})", id, groupDTO.getName());
			lessonDAO.deleteByGroupID(id);
			LOGGER.trace("Deleted LessonDTO by group (id={}, name={}) successfully", id, groupDTO.getName());
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	private LessonDTO convertToDTO(Lesson entity) throws DataNotFoundException {
		LOGGER.trace("Starting conversion of lesson to LessonDTO");
		LOGGER.trace("Retrieving a classroom by classroomID={} of Lesson", entity.getClassroom().getId());
		Classroom classroom = classroomDAO.retrieve(entity.getClassroom().getId());
		LOGGER.trace("Setting a classroom (id={}, name={}) for Lesson", classroom.getId(), classroom.getName());
		entity.setClassroom(classroom);
		LOGGER.trace("Retrieving a course by courseID={} of Lesson", entity.getClassroom().getId());
		Course course = courseDAO.retrieve(entity.getCourse().getId());
		LOGGER.trace("Retrieving a teacher (ID={}) for the course(ID={})", course.getTeacher().getId(), course.getId());
		Teacher teacher = teacherDAO.retrieve(course.getTeacher().getId());
		LOGGER.trace("Setting the teacher(ID={}) for the course(ID={})", teacher.getId(), course.getId());
		course.setTeacher(teacher);
		LOGGER.trace("Setting the course(ID={}) for the lesson", course.getId());
		entity.setCourse(course);
		LOGGER.trace("Retrieving a group by groupID={} of Lesson", entity.getGroup().getId());
		Group group = groupDAO.retrieve(entity.getGroup().getId());
		LOGGER.trace("Setting the group (ID={}) for the lesson", group.getId());
		entity.setGroup(group);
		LOGGER.trace("Converting lesson to LessonDTO");
		LessonDTO lessonDTO = modelMapper.map(entity, LessonDTO.class);
		LOGGER.trace("Finished conversion of lesson to LessonDTO");
		return lessonDTO;
	}

	private Lesson convertToEntity(LessonDTO lessonDTO) {
		Lesson entity = convertToEntityWithoutID(lessonDTO);
		LOGGER.trace("Setting ID for the lesson");
		Integer id = 0;
		try {
			id = lessonDAO.getId(entity);
			entity.setId(id);
			LOGGER.trace("Set ID={} for the lesson", id);
		} catch (DataNotFoundException e) {
			entity.setId(id);
			LOGGER.error("Set ID={} for the lesson", id);
		}
		LOGGER.trace("Finished conversion of LessonDTO to lesson");
		return entity;
	}

	private Lesson convertToEntity(LessonDTO oldDTO, LessonDTO newDTO) {
		Lesson entity = convertToEntityWithoutID(newDTO);
		LOGGER.trace("Setting ID for the lesson");
		Integer id = 0;
		try {
			id = lessonDAO.getId(convertToEntity(oldDTO));
			entity.setId(id);
			LOGGER.trace("Set ID={} for the lesson", id);
		} catch (DataNotFoundException e) {
			entity.setId(id);
			LOGGER.error("Set ID={} for the lesson", id);
		}
		LOGGER.trace("Finished conversion of LessonDTO to lesson");
		return entity;
	}

	private Lesson convertToEntityWithoutID(LessonDTO lessonDTO) {
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
}
