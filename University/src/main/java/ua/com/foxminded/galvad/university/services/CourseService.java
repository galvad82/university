package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Teacher;
import ua.com.foxminded.galvad.university.repository.CourseRepository;
import ua.com.foxminded.galvad.university.repository.LessonRepository;
import ua.com.foxminded.galvad.university.repository.TeacherRepository;

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
	private CourseRepository courseRepository;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private LessonRepository lessonRepository;

	public void create(CourseDTO courseDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		courseRepository.save(convertToEntityWithoutID(courseDTO));
	}

	public CourseDTO retrieve(String courseName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve course by name={}", courseName);
		Course course = courseRepository.findByName(courseName);
		LOGGER.trace("Retrieved a course with name={}", courseName);
		LOGGER.trace("Going to set a teacher for the course with name={}", courseName);
		Teacher teacher = teacherRepository.findById(course.getTeacher().getId()).orElse(null);
		course.setTeacher(teacher);
		LOGGER.trace("Going to retrieve CourseDTO from a course with name={}", courseName);
		CourseDTO courseDTO = convertToDTO(course);
		LOGGER.trace("Retrieved CourseDTO from a course with name={}", courseName);
		return courseDTO;
	}

	@Transactional
	public void update(CourseDTO oldDTO, CourseDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update CourseDTO with newName={} ", newDTO.getName());
		courseRepository.save(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated CourseDTO with newName={} ", newDTO.getName());
	}

	@Transactional
	public void delete(CourseDTO courseDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete all the lessons for courseDTO (name={})", courseDTO.getName());
		lessonRepository.deleteByCourse(convertToEntity(courseDTO));
		LOGGER.trace("Going to delete CourseDTO by entity (name={})", courseDTO.getName());
		courseRepository.delete(convertToEntity(courseDTO));
	}

	public List<CourseDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL CourseDTO from DB");
		List<CourseDTO> list = courseRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL CourseDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	private CourseDTO convertToDTO(Course entity) throws DataNotFoundException {
		LOGGER.trace("Going to convert course (name={}) to courseDTO", entity.getName());
		CourseDTO courseDTO = modelMapper.map(entity, CourseDTO.class);
		LOGGER.trace("Converted course to courseDTO (name={})", courseDTO.getName());
		LOGGER.trace("Going to set a teacher for courseDTO(name={})", courseDTO.getName());
		Optional<Teacher> optionalTeacher = teacherRepository.findById(entity.getTeacher().getId());
		if (optionalTeacher.isPresent()) {
			courseDTO.setTeacher(modelMapper.map(optionalTeacher.get(), TeacherDTO.class));
		} else {
			courseDTO.setTeacher(null);
		}
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
		Integer id = teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName())
				.getId();
		teacher.setId(id);
		LOGGER.trace(SET_ID, id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace(SET_TEACHER_FOR_ENTITY, entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		id = courseRepository.findByName(entity.getName()).getId();
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
		Integer id = teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName())
				.getId();
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
		Integer id = teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName())
				.getId();
		teacher.setId(id);
		LOGGER.trace(SET_ID, id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace(SET_TEACHER_FOR_ENTITY, entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		entity.setId(courseRepository.findByName(convertToEntity(oldDTO).getName()).getId());
		LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		return entity;
	}
}
