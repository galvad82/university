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

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.repository.ClassroomRepository;
import ua.com.foxminded.galvad.university.repository.LessonRepository;

@Service
public class ClassroomService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomService.class);
	private static final String CONVERSION_COMPLETED = "Conversion of classroomDTO (name={}) to classroom completed";
	private static final String CONVERSION_TO_ENTITY_COMPLETED = "Converted classroomDTO to entity (name={})";

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ClassroomRepository classroomRepository;
	@Autowired
	private LessonRepository lessonRepository;

	public ClassroomDTO create(ClassroomDTO classroomDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to create a classroom with the name={}", classroomDTO.getName());
		Classroom result;
		try {
			result = classroomRepository.save(convertToEntityWithoutID(classroomDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Classroom with name={} wasn't added to DB.", classroomDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Classroom with name=%s wasn't added to DB.", classroomDTO.getName()), e);
		}
		LOGGER.info("Classroom with name={} successfully added to DB.", classroomDTO.getName());
		return convertToDTO(result);
	}

	public ClassroomDTO retrieve(String classroomName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve classroom by name={}", classroomName);
		Classroom classroom = null;
		try {
			classroom = classroomRepository.findByName(classroomName);
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a classroom from DB. Name={}", classroomName);
			throw new DataNotFoundException(
					String.format("Can't retrieve a classroom from DB. Name=%s", classroomName));
		}
		if (classroom == null) {
			LOGGER.info("A classroom with name \"{}\" is not found.", classroomName);
			throw new DataNotFoundException(String.format("A classroom with name \"%s\" is not found.", classroomName));
		}
		LOGGER.trace("Retrieved a classroom with name={}", classroom.getName());
		LOGGER.trace("Converting classroom with name={} to DTO", classroom.getName());
		ClassroomDTO classroomDTO = convertToDTO(classroom);
		LOGGER.trace("Retrieved ClassroomDTO from a classroom with name={}", classroomDTO.getName());
		return classroomDTO;
	}

	public ClassroomDTO retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve classroom by id={}", id);
		Optional<Classroom> classroomOptional;

		try {
			classroomOptional = classroomRepository.findById(id);
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a classroom from DB. Id={}", id);
			throw new DataNotFoundException(String.format("Can't retrieve a classroom from DB. Id=%s", id));
		}
		if (!classroomOptional.isPresent()) {
			LOGGER.info("A classroom with id \"{}\" is not found.", id);
			throw new DataNotFoundException(String.format("A classroom with id \"%s\" is not found.", id));
		}
		Classroom classroom = classroomOptional.get();
		LOGGER.trace("Retrieved a classroom with name={}", classroom.getName());
		LOGGER.trace("Converting classroom with name={} to DTO", classroom.getName());
		ClassroomDTO classroomDTO = convertToDTO(classroom);
		LOGGER.trace("Retrieved ClassroomDTO from a classroom with name={}", classroomDTO.getName());
		return classroomDTO;
	}

	@Transactional
	public ClassroomDTO update(ClassroomDTO oldDTO, ClassroomDTO newDTO)
			throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to update ClassroomDTO with newName={} ", newDTO.getName());
		Classroom result;
		try {
			result = classroomRepository.save(convertToEntity(oldDTO, newDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't update a classroom with name={}", oldDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't update a classroom with name%s", oldDTO.getName()));
		}
		LOGGER.trace("Updated ClassroomDTO with newName={} ", newDTO.getName());
		return convertToDTO(result);
	}

	@Transactional
	public void delete(ClassroomDTO classroomDTO) throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to delete all the lessons for classroomDTO (name={})", classroomDTO.getName());
		try {
			lessonRepository.deleteByClassroom(convertToEntity(classroomDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete lessons for the classroom with name={}", classroomDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete lessons for the classroom with name=%s", classroomDTO.getName()));
		}
		LOGGER.trace("Going to delete ClassroomDTO (name={})", classroomDTO.getName());
		try {
			classroomRepository.delete(convertToEntity(classroomDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete a classroom with name={}", classroomDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete a classroom with name=%s", classroomDTO.getName()));
		}
		LOGGER.trace("ClassroomDTO (name={}) deleted successfully", classroomDTO.getName());
	}

	public List<ClassroomDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<ClassroomDTO> list = new ArrayList<>();
		try {
			list = classroomRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a list of classrooms.");
			throw new DataNotFoundException("Can't retrieve a list of classrooms.");
		}
		LOGGER.trace("List of ALL ClassroomDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public boolean checkIfExists(ClassroomDTO classroomDTO) {
		try {
			retrieve(classroomDTO.getName());
			return true;
		} catch (DataNotFoundException e) {
			return false;
		}
	}

	private ClassroomDTO convertToDTO(Classroom entity) {
		LOGGER.trace("Going to convert classroom (name={}) to DTO", entity.getName());
		ClassroomDTO classroomDTO = modelMapper.map(entity, ClassroomDTO.class);
		LOGGER.trace("Converted classroom (name={}) to classroomDTO (name={})", entity.getName(),
				classroomDTO.getName());
		return classroomDTO;
	}

	protected Classroom convertToEntity(ClassroomDTO classroomDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert classroomDTO (name={}) to entity", classroomDTO.getName());
		Classroom entity = modelMapper.map(classroomDTO, Classroom.class);
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, entity.getName());
		LOGGER.trace("Going to set ID for entity classroom (name={})", entity.getName());
		Integer id;
		try {
			id = classroomRepository.findByName(entity.getName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a classroom from DB. Name={} while converting to entity", entity.getName());
			throw new DataNotFoundException(String.format(
					"Can't retrieve a classroom from DB  while converting to entity. Name=%s", entity.getName()));
		}
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity classroom (name={})", id, entity.getName());
		LOGGER.trace(CONVERSION_COMPLETED, classroomDTO.getName());
		return entity;
	}

	protected Classroom convertToEntityWithoutID(ClassroomDTO classroomDTO) {
		LOGGER.trace("Going to convert classroomDTO (name={}) to entity without setting ID", classroomDTO.getName());
		Classroom entity = modelMapper.map(classroomDTO, Classroom.class);
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, entity.getName());
		LOGGER.trace(CONVERSION_COMPLETED, classroomDTO.getName());
		return entity;
	}

	protected Classroom convertToEntity(ClassroomDTO oldDTO, ClassroomDTO newDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert classroomDTO (name={}) to entity", newDTO.getName());
		Classroom entity = modelMapper.map(newDTO, Classroom.class);
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, entity.getName());
		LOGGER.trace("Going to set ID for entity classroom (name={})", entity.getName());
		Integer id;
		try {
			id = classroomRepository.findByName(convertToEntity(oldDTO).getName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a classroomDTO from DB. Name={} while updating",
					convertToEntity(oldDTO).getName());
			throw new DataNotFoundException(
					String.format("Can't retrieve a classroomDTO from DB  while updating. Name=%s",
							convertToEntity(oldDTO).getName()));
		}
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity classroom (name={})", id, entity.getName());
		LOGGER.trace(CONVERSION_COMPLETED, newDTO.getName());
		return entity;
	}
}
