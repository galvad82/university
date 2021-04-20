package ua.com.foxminded.galvad.university.services;

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

	public CourseDTO retrieve(Integer id) throws DataNotFoundException {
		CourseDTO courseDTO = new CourseDTO();
		LOGGER.trace("Going to retrieve course by ID={}", id);
		Course course = courseDAO.retrieve(id);
		LOGGER.trace("Retrieved a course with ID={}", id);
		LOGGER.trace("Going to retrieve CourseDTO from a course with ID={}", id);
		courseDTO = convertToDTO(course);
		LOGGER.trace("Retrieved CourseDTO from a course with ID={}", id);
		return courseDTO;
	}

	public void update(CourseDTO oldDTO, CourseDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update CourseDTO with newName={} ", newDTO.getName());
		courseDAO.update(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated CourseDTO with newName={} ", newDTO.getName());
	}

	public void delete(CourseDTO courseDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete CourseDTO by entity (name={})", courseDTO.getName());
		courseDAO.delete(convertToEntity(courseDTO));
	}

	public List<CourseDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL CourseDTO from DB");
		List<CourseDTO> list = courseDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL CourseDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public Course setId(Course course) throws DataNotFoundException {
		LOGGER.trace("Going to set ID for a course (name={})", course.getName());
		Integer id = courseDAO.getId(course);
		course.setId(id);
		LOGGER.trace("Set ID={} for a classroom (name={}) successfully", id, course.getName());
		return course;
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
		LOGGER.trace("Going to convert courseDTO (name={}) to entity", courseDTO.getName());
		Course entity = modelMapper.map(courseDTO, Course.class);
		LOGGER.trace("Converted courseDTO to entity (name={})", entity.getName());
		LOGGER.trace("Going to set Teacher for entity course (name={})", entity.getName());
		Teacher teacher = entity.getTeacher();
		Integer id = teacherDAO.getId(teacher);
		teacher.setId(id);
		LOGGER.trace("Set ID={} for Teacher for entity course (name={})", id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace("Set Teacher for entity course (name={}) successfully", entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		id = courseDAO.getId(entity);
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		return entity;
	}

	private Course convertToEntity(CourseDTO oldDTO, CourseDTO newDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert courseDTO (name={}) to entity", newDTO.getName());
		Course entity = modelMapper.map(newDTO, Course.class);
		LOGGER.trace("Converted courseDTO to entity (name={})", entity.getName());
		LOGGER.trace("Going to set Teacher for entity course (name={})", entity.getName());
		Teacher teacher = entity.getTeacher();
		Integer id = teacherDAO.getId(teacher);
		teacher.setId(id);
		LOGGER.trace("Set ID={} for Teacher for entity course (name={})", id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace("Set Teacher for entity course (name={}) successfully", entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		entity.setId(courseDAO.getId(convertToEntity(oldDTO)));
		LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		return entity;
	}
}
