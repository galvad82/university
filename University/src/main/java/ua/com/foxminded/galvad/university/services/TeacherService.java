package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

	public TeacherDTO create(TeacherDTO teacherDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to create a teacher with firstName={} and lastName={}", teacherDTO.getFirstName(),
				teacherDTO.getLastName());
		Teacher teacher = null;
		try {
			teacher = teacherRepository.save(convertToEntityWithoutID(teacherDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Teacher with firstName={} and lastName={} wasn't added to DB.", teacherDTO.getFirstName(),
					teacherDTO.getLastName());
			throw new DataAreNotUpdatedException(
					String.format("Teacher with firstName=%s and lastName=%s wasn't added to DB.",
							teacherDTO.getFirstName(), teacherDTO.getLastName()),
					e);
		}
		LOGGER.trace("The teacher with firstName={} and lastName={} created", teacherDTO.getFirstName(),
				teacherDTO.getLastName());
		return convertToDTO(teacher);
	}

	public TeacherDTO retrieve(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve TeacherDTO, firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Going to retrieve Teacher entity, firstName={}, lastName={}", firstName, lastName);
		Teacher teacher = null;
		try {
			teacher = teacherRepository.findByFirstNameAndLastName(firstName, lastName);
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve TeacherDTO, firstName={}, lastName={}", firstName, lastName);
			throw new DataNotFoundException(
					String.format("Can't retrieve TeacherDTO, firstName=%s, lastName=%s", firstName, lastName));
		}
		if (teacher == null) {
			LOGGER.info("A teacher (firstName={}, lastName={}) is not found.", firstName, lastName);
			throw new DataNotFoundException(
					String.format("A teacher (firstName=%s, lastName=%s) is not found.", firstName, lastName));
		}
		LOGGER.trace("Teacher entity retrieved, firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Converting teacher entity to DTO, firstName={}, lastName={}", firstName, lastName);
		TeacherDTO resultDTO = convertToDTO(teacher);
		LOGGER.trace("Teacher entity converted to DTO, firstName={}, lastName={}", resultDTO.getFirstName(),
				resultDTO.getLastName());
		return resultDTO;
	}

	public TeacherDTO retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve TeacherDTO, id{}", id);
		LOGGER.trace("Going to retrieve Teacher entity, id={}", id);
		Optional<Teacher> teacherOptional;
		try {
			teacherOptional = teacherRepository.findById(id);
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve TeacherDTO, id={}", id);
			throw new DataNotFoundException(String.format("Can't retrieve TeacherDTO, id=%s", id));
		}
		if (!teacherOptional.isPresent()) {
			LOGGER.info("A teacher (id={}) is not found.", id);
			throw new DataNotFoundException(String.format("A teacher (id=%s) is not found.", id));
		}
		LOGGER.trace("Teacher entity retrieved, id={}", id);
		LOGGER.trace("Converting Teacher entity to DTO, id={}", id);
		TeacherDTO resultDTO = convertToDTO(teacherOptional.get());
		LOGGER.trace("Teacher entity converted to DTO, firstName={}, lastName={}", resultDTO.getFirstName(),
				resultDTO.getLastName());
		return resultDTO;
	}

	@Transactional
	public TeacherDTO update(TeacherDTO oldDTO, TeacherDTO newDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update TeacherDTO, firstName={}, lastName={}", newDTO.getFirstName(),
				newDTO.getLastName());
		Teacher result = null;
		try {
			result = teacherRepository.save(convertToEntity(oldDTO, newDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't update a teacher (firstName={}, lastName={})", oldDTO.getFirstName(),
					oldDTO.getLastName());
			throw new DataAreNotUpdatedException(String.format("Can't update a teacher (firstName=%s, lastName=%s)",
					oldDTO.getFirstName(), oldDTO.getLastName()));
		}
		LOGGER.trace("TeacherDTO was updated successfully.");
		return convertToDTO(result);
	}

	public void delete(TeacherDTO teacherDTO) throws DataAreNotUpdatedException, DataNotFoundException {
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
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete the teacher (firstName={}, lastName={})", teacherDTO.getFirstName(),
					teacherDTO.getLastName());
			throw new DataAreNotUpdatedException(String.format("Can't delete the teacher (firstName=%s, lastName=%s)",
					teacherDTO.getFirstName(), teacherDTO.getLastName()));
		}
	}

	public List<TeacherDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL TeacherDTO from DB");
		List<TeacherDTO> list = new ArrayList<>();
		try {
			list = teacherRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a list of teachers.");
			throw new DataNotFoundException("Can't retrieve a list of teachers.");
		}
		LOGGER.trace("List of ALL TeacherDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public boolean checkIfExists(TeacherDTO teacherDTO) {
		try {
			retrieve(teacherDTO.getFirstName(), teacherDTO.getLastName());
			return true;
		} catch (DataNotFoundException e) {
			return false;
		}
	}

	public List<LessonDTO> findAllLessonsForTeacher(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to get list of lessons for Teacher (firstName={}, lastName={})", firstName, lastName);
		LOGGER.trace("Going to get DTO for Teacher (firstName={}, lastName={})", firstName, lastName);
		TeacherDTO teacherDTO = retrieve(firstName, lastName);
		LOGGER.trace("Got TeacherDTO (firstName={}, lastName={})", teacherDTO.getFirstName(), teacherDTO.getLastName());

		List<LessonDTO> listOfLessons = new ArrayList<>();
		try {
			listOfLessons = lessonService.findAll().stream().filter(s -> s.getCourse().getTeacher().equals(teacherDTO))
					.collect(Collectors.toList());
			Collections.sort(listOfLessons, (o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a list of lessons for Teacher (firstName={}, lastName={})", firstName,
					lastName);
			throw new DataNotFoundException(
					String.format("Can't retrieve a list of lessons for Teacher (firstName=%s, lastName=%s)",
							teacherDTO.getFirstName(), teacherDTO.getLastName()));
		}
		LOGGER.trace("The list of lessons for Teacher (firstName={}, lastName={}) retrieved successfully", firstName,
				lastName);
		return listOfLessons;
	}

	private TeacherDTO convertToDTO(Teacher entity) {
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
		Integer id;
		try {
			id = teacherRepository.findByFirstNameAndLastName(entity.getFirstName(), entity.getLastName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve an Id for teacher (firstName={}, lastName={})", entity.getFirstName(),
					entity.getLastName());
			throw new DataNotFoundException(
					String.format("Can't retrieve an Id for teacher (firstName=%s, lastName=%s)", entity.getFirstName(),
							entity.getLastName()));
		}
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
		try {
			entity.setId(teacherRepository.findByFirstNameAndLastName(oldEntity.getFirstName(), oldEntity.getLastName())
					.getId());
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve an Id for teacher (firstName={}, lastName={})", oldEntity.getFirstName(),
					oldEntity.getLastName());
			throw new DataNotFoundException(
					String.format("Can't retrieve an Id for teacher (firstName=%s, lastName=%s)",
							oldEntity.getFirstName(), oldEntity.getLastName()));
		}
		LOGGER.trace("ID of oldDTO was set to newDTO successfully");
		return entity;
	}

	private Teacher convertToEntityWithoutID(TeacherDTO teacherDTO) {
		LOGGER.trace("Going to convert TeacherDTO to entity");
		Teacher entity = modelMapper.map(teacherDTO, Teacher.class);
		LOGGER.trace("TeacherDTO converted successfully.");
		return entity;
	}
}
