package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.ClassroomDAO;
import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.model.Classroom;

@Service
public class ClassroomService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomService.class);
	private static final String CONVERSION_COMPLETED = "Conversion of classroomDTO (name={}) to classroom completed";
	private static final String CONVERSION_TO_ENTITY_COMPLETED = "Converted classroomDTO to entity (name={})";
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ClassroomDAO classroomDAO;
	@Autowired
	private LessonDAO lessonDAO;

	@Transactional
	public void create(ClassroomDTO classroomDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		classroomDAO.create(convertToEntityWithoutID(classroomDTO));
	}

	public ClassroomDTO retrieve(String classroomName) {
		LOGGER.trace("Going to retrieve classroom by name={}", classroomName);
		Classroom classroom = classroomDAO.retrieve(classroomName);
		LOGGER.trace("Retrieved a classroom with name={}", classroom.getName());
		LOGGER.trace("Converting classroom with name={} to DTO", classroom.getName());
		ClassroomDTO classroomDTO = convertToDTO(classroom);
		LOGGER.trace("Retrieved ClassroomDTO from a classroom with name={}", classroomDTO.getName());
		return classroomDTO;
	}

	@Transactional
	public void update(ClassroomDTO oldDTO, ClassroomDTO newDTO)
			throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update ClassroomDTO with newName={} ", newDTO.getName());
		classroomDAO.update(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated ClassroomDTO with newName={} ", newDTO.getName());
	}

	@Transactional
	public void delete(ClassroomDTO classroomDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete all the lessons for classroomDTO (name={})", classroomDTO.getName());	
		lessonDAO.deleteByClassroomID(convertToEntity(classroomDTO).getId());
		LOGGER.trace("Going to delete ClassroomDTO (name={})", classroomDTO.getName());
		classroomDAO.delete(convertToEntity(classroomDTO));
	}

	public List<ClassroomDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<ClassroomDTO> list = classroomDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL ClassroomDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	private ClassroomDTO convertToDTO(Classroom entity) throws DataNotFoundException {
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
		Integer id = classroomDAO.getId(entity);
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity classroom (name={})", id, entity.getName());
		LOGGER.trace(CONVERSION_COMPLETED, classroomDTO.getName());
		return entity;
	}

	protected Classroom convertToEntityWithoutID(ClassroomDTO classroomDTO) throws DataNotFoundException {
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
		Integer id = classroomDAO.getId(convertToEntity(oldDTO));
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity classroom (name={})", id, entity.getName());
		LOGGER.trace(CONVERSION_COMPLETED, newDTO.getName());
		return entity;
	}
}
