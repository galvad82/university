package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@Service
public class CourseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);
	private static final String CONVERTED_DTO_TO_ENTITY = "Converted courseDTO to entity (name={})";
	private static final String GOING_TO_CONVERT_DTO_TO_ENTITY = "Going to convert courseDTO (name={}) to entity";
	private static final String GOING_TO_SET_TEACHER = "Going to set Teacher for entity course (name={})";
	private static final String SET_ID = "Set ID={} for Teacher for entity course (name={})";
	private static final String SET_TEACHER_FOR_ENTITY = "Set Teacher for entity course (name={}) successfully";

	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private TeacherDAO teacherDAO;
	@Autowired
	private LessonDAO lessonDAO;
	
	@Transactional
	public void create(CourseDTO courseDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		Course course = convertToEntityWithoutID(courseDTO);
		courseDAO.create(course);
	}

	public CourseDTO retrieve(String courseName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve course by name={}", courseName);
		Course course = courseDAO.retrieve(courseName);
		LOGGER.trace("Retrieved a course with name={}", courseName);
		LOGGER.trace("Going to set a teacher for the course with name={}", courseName);
		Teacher teacher = teacherDAO.retrieve(course.getTeacher().getId());
		course.setTeacher(teacher);
		LOGGER.trace("Going to retrieve CourseDTO from a course with name={}", courseName);
		CourseDTO courseDTO = convertToDTO(course);
		LOGGER.trace("Retrieved CourseDTO from a course with name={}", courseName);
		return courseDTO;
	}

	@Transactional
	public void update(CourseDTO oldDTO, CourseDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update CourseDTO with newName={} ", newDTO.getName());
		courseDAO.update(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated CourseDTO with newName={} ", newDTO.getName());
	}

	@Transactional
	public void delete(CourseDTO courseDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete all the lessons for courseDTO (name={})", courseDTO.getName());	
		lessonDAO.deleteByCourseID(convertToEntity(courseDTO).getId());		
		LOGGER.trace("Going to delete CourseDTO by entity (name={})", courseDTO.getName());
		courseDAO.delete(convertToEntity(courseDTO));
	}

	public List<CourseDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL CourseDTO from DB");
		List<CourseDTO> list = courseDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL CourseDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	private CourseDTO convertToDTO(Course entity) throws DataNotFoundException {
		LOGGER.trace("Going to convert course (name={}) to courseDTO", entity.getName());
		CourseDTO courseDTO = modelMapper.map(entity, CourseDTO.class);
		LOGGER.trace("Converted course to courseDTO (name={})", courseDTO.getName());
		LOGGER.trace("Going to set a teacher for courseDTO(name={})", courseDTO.getName());
		courseDTO.setTeacher(modelMapper.map(teacherDAO.retrieve(entity.getTeacher().getId()), TeacherDTO.class));
		LOGGER.trace("Set a teacher for courseDTO(name={})", courseDTO.getName());
		LOGGER.trace("Conversion of a course (name={}) to courseDTO completed successfully", entity.getName());
		return courseDTO;
	}

	protected Course convertToEntity(CourseDTO courseDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT_DTO_TO_ENTITY, courseDTO.getName());
		Course entity = modelMapper.map(courseDTO, Course.class);
		LOGGER.trace(CONVERTED_DTO_TO_ENTITY, entity.getName());
		LOGGER.trace(GOING_TO_SET_TEACHER, entity.getName());
		Teacher teacher = entity.getTeacher();
		Integer id = teacherDAO.getId(teacher);
		teacher.setId(id);
		LOGGER.trace(SET_ID, id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace(SET_TEACHER_FOR_ENTITY, entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		id = courseDAO.getId(entity);
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		return entity;
	}

	protected Course convertToEntityWithoutID(CourseDTO courseDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT_DTO_TO_ENTITY, courseDTO.getName());
		Course entity = modelMapper.map(courseDTO, Course.class);
		LOGGER.trace(CONVERTED_DTO_TO_ENTITY, entity.getName());
		LOGGER.trace(GOING_TO_SET_TEACHER, entity.getName());
		Teacher teacher = entity.getTeacher();
		Integer id = teacherDAO.getId(teacher);
		teacher.setId(id);
		LOGGER.trace(SET_ID, id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace(SET_TEACHER_FOR_ENTITY, entity.getName());
		return entity;
	}

	private Course convertToEntity(CourseDTO oldDTO, CourseDTO newDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT_DTO_TO_ENTITY, newDTO.getName());
		Course entity = modelMapper.map(newDTO, Course.class);
		LOGGER.trace(CONVERTED_DTO_TO_ENTITY, entity.getName());
		LOGGER.trace(GOING_TO_SET_TEACHER, entity.getName());
		Teacher teacher = entity.getTeacher();
		Integer id = teacherDAO.getId(teacher);
		teacher.setId(id);
		LOGGER.trace(SET_ID, id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace(SET_TEACHER_FOR_ENTITY, entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		entity.setId(courseDAO.getId(convertToEntity(oldDTO)));
		LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		return entity;
	}
}
