package ua.com.foxminded.galvad.university.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.model.Teacher;
import ua.com.foxminded.galvad.university.repository.TeacherRepository;

@Service
public class TeacherService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private LessonService lessonService;

	public void create(TeacherDTO teacherDTO) throws DataAreNotUpdatedException {
		teacherRepository.save(convertToEntityWithoutID(teacherDTO));
	}

	public TeacherDTO retrieve(String firstName, String lastName) throws DataAreNotUpdatedException {
		return convertToDTO(teacherRepository.findByFirstNameAndLastName(firstName, lastName));

	}

	@Transactional
	public void update(TeacherDTO oldDTO, TeacherDTO newDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update TeacherDTO, firstName={}, lastName={}", newDTO.getFirstName(),
				newDTO.getLastName());
		teacherRepository.save(convertToEntity(oldDTO, newDTO));
	}

	public void delete(TeacherDTO teacherDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete TeacherDTO by entity, firstName={}, lastName={}", teacherDTO.getFirstName(),
				teacherDTO.getLastName());
		Teacher teacher = convertToEntity(teacherDTO);
		try {
			teacherRepository.delete(teacher);
		} catch (DataIntegrityViolationException e) {
			LOGGER.info("Can't delete the teacher \"{} {}\" because of Teacher-Course connection.",
					teacher.getFirstName(), teacher.getLastName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete the teacher \"%s %s\" because of Teacher-Course connection.",
							teacher.getFirstName(), teacher.getLastName()));
		}
	}

	public List<TeacherDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL TeacherDTO from DB");
		List<TeacherDTO> list = teacherRepository.findAll().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
		LOGGER.trace("List of ALL TeacherDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public List<LessonDTO> findAllLessonsForTeacher(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to get list of lessons for Teacher (firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Going to get DTO for Teacher (firstName={}, lastName={}", firstName, lastName);
		TeacherDTO teacherDTO = retrieve(firstName, lastName);
		LOGGER.trace("Got TeacherDTO (firstName={}, lastName={}", teacherDTO.getFirstName(), teacherDTO.getLastName());
		List<LessonDTO> listOfLessons = lessonService.findAll().stream()
				.filter(s -> s.getCourse().getTeacher().equals(teacherDTO)).collect(Collectors.toList());
		Collections.sort(listOfLessons, (o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
		LOGGER.trace("The list of lessons for Teacher (firstName={}, lastName={} retrieved successfully", firstName,
				lastName);
		return listOfLessons;
	}

	private TeacherDTO convertToDTO(Teacher entity) throws DataNotFoundException {
		LOGGER.trace("Going to convert entity(firstName={}, lastName={}) to TeacherDTO", entity.getFirstName(),
				entity.getLastName());
		TeacherDTO teacherDTO = modelMapper.map(entity, TeacherDTO.class);
		LOGGER.trace("Entity was converted to TeacherDTO successfully");
		return teacherDTO;
	}

	private Teacher convertToEntity(TeacherDTO teacherDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert TeacherDTO to entity");
		Teacher entity = modelMapper.map(teacherDTO, Teacher.class);
		LOGGER.trace("TeacherDTO converted successfully.");
		LOGGER.trace("Going to get ID for Teacher with firstName={}, lastName={}", entity.getFirstName(),
				entity.getLastName());
		Integer id = teacherRepository.findByFirstNameAndLastName(entity.getFirstName(), entity.getLastName()).getId();
		LOGGER.trace("ID={}", id);
		entity.setId(id);
		LOGGER.trace("ID was set for the entity successfully");
		return entity;
	}

	private Teacher convertToEntity(TeacherDTO oldDTO, TeacherDTO newDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert newDTO(firstName={}, lastName={}) to entity", newDTO.getFirstName(),
				newDTO.getLastName());
		Teacher entity = modelMapper.map(newDTO, Teacher.class);
		LOGGER.trace("DTO was converted successfully.");
		LOGGER.trace("Going to set ID of oldDTO to newDTO");
		Teacher oldEntity = convertToEntity(oldDTO);
		entity.setId(teacherRepository.findByFirstNameAndLastName(oldEntity.getFirstName(), oldEntity.getLastName())
				.getId());
		LOGGER.trace("ID of oldDTO was set to newDTO successfully");
		return entity;
	}

	private Teacher convertToEntityWithoutID(TeacherDTO teacherDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert TeacherDTO to entity");
		Teacher entity = modelMapper.map(teacherDTO, Teacher.class);
		LOGGER.trace("TeacherDTO converted successfully.");
		return entity;
	}
}
