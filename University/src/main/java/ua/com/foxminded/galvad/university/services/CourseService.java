package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
		LOGGER.trace("Going to create a course with the name={}", courseDTO.getName());
		try {
			courseRepository.save(convertToEntityWithoutID(courseDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Course with name={} wasn't added to DB.", courseDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Course with name=%s wasn't added to DB.", courseDTO.getName()), e);
		}
		LOGGER.info("Course with name={} successfully added to DB.", courseDTO.getName());
	}

	public CourseDTO retrieve(String courseName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve course by name={}", courseName);
		Course course = null;
		try {
			course = courseRepository.findByName(courseName);
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a course from DB. Name={}", courseName);
			throw new DataNotFoundException(String.format("Can't retrieve a course from DB. Name=%s", courseName));
		}
		if (course == null) {
			LOGGER.info("A course with name \"{}\" is not found.", courseName);
			throw new DataNotFoundException(String.format("A course with name \"%s\" is not found.", courseName));
		}
		LOGGER.trace("Retrieved a course with name={}", courseName);
		LOGGER.trace("Going to set a teacher for the course with name={}", courseName);
		try {
			Teacher teacher = teacherRepository.findById(course.getTeacher().getId()).orElse(null);
			course.setTeacher(teacher);
		} catch (DataAccessException e) {
			LOGGER.info("Can't set a teacher for the course with name={}", courseName);
			throw new DataNotFoundException(
					String.format("Can't set a teacher for the course with name=%s", courseName));
		}
		LOGGER.trace("Going to retrieve CourseDTO from a course with name={}", courseName);
		CourseDTO courseDTO = convertToDTO(course);
		LOGGER.trace("Retrieved CourseDTO from a course with name={}", courseName);
		return courseDTO;
	}

	@Transactional
	public void update(CourseDTO oldDTO, CourseDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update CourseDTO with newName={} ", newDTO.getName());
		try {
			courseRepository.save(convertToEntity(oldDTO, newDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't update a course with name={}", oldDTO.getName());
			throw new DataAreNotUpdatedException(String.format("Can't update a course with name%s", oldDTO.getName()));
		}
		LOGGER.trace("Updated CourseDTO with newName={} ", newDTO.getName());
	}

	@Transactional
	public void delete(CourseDTO courseDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete all the lessons for courseDTO (name={})", courseDTO.getName());
		try {
			lessonRepository.deleteByCourse(convertToEntity(courseDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete all the lessons for courseDTO (name={})", courseDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete all the lessons for courseDTO (name=%s)", courseDTO.getName()));
		}
		LOGGER.trace("Going to delete CourseDTO by entity (name={})", courseDTO.getName());
		LOGGER.trace("Going to delete the courseDTO (name={})", courseDTO.getName());
		try {
			courseRepository.delete(convertToEntity(courseDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete a course with name={}", courseDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete a course with name=%s", courseDTO.getName()));
		}
	}

	public List<CourseDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL CourseDTO from DB");
		List<CourseDTO> list = new ArrayList<>();
		try {
			list = courseRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a list of courses.");
			throw new DataNotFoundException("Can't retrieve a list of courses.");
		}
		LOGGER.trace("List of ALL CourseDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	private CourseDTO convertToDTO(Course entity) throws DataNotFoundException {
		LOGGER.trace("Going to convert course (name={}) to courseDTO", entity.getName());
		CourseDTO courseDTO = modelMapper.map(entity, CourseDTO.class);
		LOGGER.trace("Converted course to courseDTO (name={})", courseDTO.getName());
		LOGGER.trace("Going to set a teacher for courseDTO(name={})", courseDTO.getName());
		Optional<Teacher> optionalTeacher;
		try {
			optionalTeacher = teacherRepository.findById(entity.getTeacher().getId());
		} catch (DataAccessException e) {
			LOGGER.info("A teacherDTO (firstName={}, lastName={}) is not found while converting a courseDTO to entity.",
					entity.getTeacher().getFirstName(), entity.getTeacher().getLastName());
			throw new DataNotFoundException(String.format(
					"A teacherDTO (firstName=%s, lastName=%s) is not found while converting a courseDTO to entity.",
					entity.getTeacher().getFirstName(), entity.getTeacher().getLastName()));
		}
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
		Integer id;
		try {
			id = teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info("A teacherDTO (firstName={}, lastName={}) is not found while converting a courseDTO to entity.",
					teacher.getFirstName(), teacher.getLastName());
			throw new DataNotFoundException(String.format(
					"A teacher (firstName=%s, lastName=%s) is not found while converting a courseDTO to entity.",
					teacher.getFirstName(), teacher.getLastName()));
		}
		teacher.setId(id);
		LOGGER.trace(SET_ID, id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace(SET_TEACHER_FOR_ENTITY, entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		try {
			id = courseRepository.findByName(entity.getName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a course entity with name={}", entity.getName());
			throw new DataNotFoundException(
					String.format("Can't retrieve a course entity with name=%s", entity.getName()));
		}
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
		Integer id;
		try {
			id = teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info(
					"A teacher (firstName={}, lastName={}) is not found while converting a new courseDTO to entity.",
					teacher.getFirstName(), teacher.getLastName());
			throw new DataNotFoundException(String.format(
					"A teacher (firstName=%s, lastName=%s) is not found while converting a new courseDTO to entity.",
					teacher.getFirstName(), teacher.getLastName()));
		}
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
		Integer id;
		try {
			id = teacherRepository.findByFirstNameAndLastName(teacher.getFirstName(), teacher.getLastName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info("A teacher (firstName={}, lastName={}) is not found while updating courseDTO.",
					teacher.getFirstName(), teacher.getLastName());
			throw new DataNotFoundException(
					String.format("A teacher (firstName=%s, lastName=%s) is not found while updating courseDTO.",
							teacher.getFirstName(), teacher.getLastName()));
		}
		teacher.setId(id);
		LOGGER.trace(SET_ID, id, entity.getName());
		entity.setTeacher(teacher);
		LOGGER.trace(SET_TEACHER_FOR_ENTITY, entity.getName());
		LOGGER.trace("Going to set ID for entity course (name={})", entity.getName());
		try {
			entity.setId(courseRepository.findByName(convertToEntity(oldDTO).getName()).getId());
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve an old courseDTO from DB with name={}", convertToEntity(oldDTO).getName());
			throw new DataNotFoundException(String.format("Can't retrieve an old courseDTO from DB with name=%s",
					convertToEntity(oldDTO).getName()));
		}
		LOGGER.trace("Set ID={} for entity course (name={})", id, entity.getName());
		return entity;
	}
}
