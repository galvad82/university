package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;

@Service
public class CourseService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);

	private ModelMapper modelMapper = new ModelMapper();
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private TeacherDAO teacherDAO;

	public void create(CourseDTO courseDTO) {
		try {
			Course course = convertToEntity(courseDTO);
			courseDAO.create(course);
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public CourseDTO retrieve(Integer id) {
		CourseDTO courseDTO = new CourseDTO();
		try {
			LOGGER.trace("Going to retrieve course by ID={}", id);
			Course course = courseDAO.retrieve(id);
			LOGGER.trace("Retrieved a course with ID={}", id);
			LOGGER.trace("Going to retrieve CourseDTO from a course with ID={}", id);
			courseDTO = convertToDTO(course);
			LOGGER.trace("Retrieved CourseDTO from a course with ID={}", id);
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return courseDTO;
	}

	public void update(CourseDTO oldDTO, CourseDTO newDTO) {
		LOGGER.trace("Going to update CourseDTO with newName={} ", newDTO.getName());
		try {
			courseDAO.update(convertToEntity(oldDTO, newDTO));
			LOGGER.trace("Updated CourseDTO with newName={} ", newDTO.getName());
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public void delete(CourseDTO courseDTO) {
		LOGGER.trace("Going to delete CourseDTO by entity (name={})", courseDTO.getName());
		try {
			courseDAO.delete(convertToEntity(courseDTO));
		} catch (DataNotFoundException | DataAreNotUpdatedException ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.error(ex.getCause().toString());
		}
	}

	public List<CourseDTO> findAll() {
		LOGGER.trace("Going to get list of ALL CourseDTO from DB");
		List<CourseDTO> list = new ArrayList<>();
		try {
			list = courseDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
			LOGGER.trace("List of ALL CourseDTO retrieved from DB, {} were found", list.size());
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return list;
	}

	public Course setId(Course course) {
		LOGGER.trace("Going to set ID for a course (name={})", course.getName());
		Integer id = courseDAO.getId(course);
		course.setId(id);
		LOGGER.trace("Set ID={} for a classroom (name={}) successfully", id, course.getName());
		return course;
	}

	private CourseDTO convertToDTO(Course entity) {
		LOGGER.trace("Going to convert course (name={}) to courseDTO", entity.getName());
		CourseDTO courseDTO = modelMapper.map(entity, CourseDTO.class);
		LOGGER.trace("Converted course to courseDTO (name={})", courseDTO.getName());
		LOGGER.trace("Going to set a teacher for courseDTO(name={})", courseDTO.getName());
		courseDTO.setTeacher(modelMapper.map(teacherDAO.retrieve(entity.getTeacher().getId()), TeacherDTO.class));
		LOGGER.trace("Set a teacher for courseDTO(name={})", courseDTO.getName());
		LOGGER.trace("Conversion of a course (name={}) to courseDTO completed successfully", entity.getName());
		return courseDTO;
	}

	protected Course convertToEntity(CourseDTO courseDTO) {
		LOGGER.trace("Going to convert courseDTO (name={}) to entity", courseDTO.getName());
		Course entity = modelMapper.map(courseDTO, Course.class);
		LOGGER.trace("Converted courseDTO to entity (name={})", entity.getName());
		LOGGER.trace("Going to set Teacher for entity course (name={})", entity.getName());
		Teacher teacher = entity.getTeacher();
		Integer id = 0;
		try {
			id = teacherDAO.getId(teacher);
			teacher.setId(id);
			LOGGER.trace("Set ID={} for Teacher for entity course (name={})", id, entity.getName());
		} catch (DataNotFoundException e) {
			LOGGER.error("Set ID={} for TeacherID for entity course (name={})", id, entity.getName());
			teacher.setId(id);
		}
		entity.setTeacher(teacher);
		LOGGER.trace("Set Teacher for entity course (name={}) successfully", entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		id = 0;
		try {
			id = courseDAO.getId(entity);
			entity.setId(id);
			LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		} catch (DataNotFoundException e) {
			LOGGER.error("Set ID={} for entity course (name={})", id, entity.getName());
			entity.setId(id);
		}
		return entity;
	}

	private Course convertToEntity(CourseDTO oldDTO, CourseDTO newDTO) {
		LOGGER.trace("Going to convert courseDTO (name={}) to entity", newDTO.getName());
		Course entity = modelMapper.map(newDTO, Course.class);
		LOGGER.trace("Converted courseDTO to entity (name={})", entity.getName());
		LOGGER.trace("Going to set Teacher for entity course (name={})", entity.getName());
		Teacher teacher = entity.getTeacher();
		Integer id = 0;
		try {
			id = teacherDAO.getId(teacher);
			teacher.setId(id);
			LOGGER.trace("Set ID={} for Teacher for entity course (name={})", id, entity.getName());
		} catch (DataNotFoundException e) {
			LOGGER.error("Set ID={} for TeacherID for entity course (name={})", id, entity.getName());
			teacher.setId(id);
		}
		entity.setTeacher(teacher);
		LOGGER.trace("Set Teacher for entity course (name={}) successfully", entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		id = 0;
		try {
			entity.setId(courseDAO.getId(convertToEntity(oldDTO)));
			LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		} catch (DataNotFoundException e) {
			LOGGER.error("Set ID={} for entity course (name={})", id, entity.getName());
			entity.setId(id);
		}
		return entity;
	}
}
