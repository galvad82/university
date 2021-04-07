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
import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.model.Classroom;

@Service
public class ClassroomService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClassroomService.class);

	private ModelMapper modelMapper;
	private ClassroomDAO classroomDAO;

	@Autowired
	public void setModelMapper(ModelMapper modelMapper) {
		if (modelMapper != null) {
			this.modelMapper = modelMapper;
		} else {
			throw new IllegalArgumentException("ModelMapper cannot be null!");
		}
	}

	@Autowired
	public void setClassroomDAO(ClassroomDAO classroomDAO) {
		if (classroomDAO != null) {
			this.classroomDAO = classroomDAO;
		} else {
			throw new IllegalArgumentException("ClassroomDAO cannot be null!");
		}
	}

	public void create(ClassroomDTO classroomDTO) {
		try {
			Classroom classroom = convertToEntity(classroomDTO);
			classroomDAO.create(classroom);
		} catch (DataNotFoundException ex) {
			LOGGER.error(ex.getErrorMessage());
			LOGGER.error(ex.getCauseDescription());
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public ClassroomDTO retrieve(Integer id) {
		LOGGER.trace("Going to retrieve ClassroomDTO from classroom with ID={}", id);
		ClassroomDTO classroomDTO = new ClassroomDTO();
		try {
			LOGGER.trace("Going to retrieve classroom by ID={}", id);
			Classroom classroom = classroomDAO.retrieve(id);
			LOGGER.trace("Retrieved a classroom with ID={}", id);
			LOGGER.trace("Going to retrieve ClassroomDTO from a classroom with ID={}", id);
			classroomDTO = convertToDTO(classroom);
			LOGGER.trace("Retrieved ClassroomDTO from a classroom with ID={}", id);
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return classroomDTO;
	}

	public void update(ClassroomDTO oldDTO, ClassroomDTO newDTO) {
		LOGGER.trace("Going to update ClassroomDTO with newName={} ", newDTO.getName());
		try {
			classroomDAO.update(convertToEntity(oldDTO, newDTO));
			LOGGER.trace("Updated ClassroomDTO with newName={} ", newDTO.getName());
		} catch (DataNotFoundException ex) {
			LOGGER.error(ex.getErrorMessage());
			LOGGER.error(ex.getCauseDescription());
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public void delete(ClassroomDTO classroomDTO) {
		LOGGER.trace("Going to delete ClassroomDTO (name={})", classroomDTO.getName());
		try {
			classroomDAO.delete(convertToEntity(classroomDTO));
		} catch (DataNotFoundException ex) {
			LOGGER.error(ex.getErrorMessage());
			LOGGER.error(ex.getCauseDescription());
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public List<ClassroomDTO> findAll() {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<ClassroomDTO> list = new ArrayList<>();
		try {
			list = classroomDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
			LOGGER.trace("List of ALL ClassroomDTO retrieved from DB, {} were found", list.size());
		}	catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return list;
	}

	public Classroom setId(Classroom classroom) throws DataNotFoundException {
		LOGGER.trace("Going to set ID for a classroom (name={})", classroom.getName());
		Integer id = classroomDAO.getId(classroom);
		classroom.setId(id);
		LOGGER.trace("Set ID={} for a classroom (name={}) successfully", id, classroom.getName());
		return classroom;
	}

	private ClassroomDTO convertToDTO(Classroom entity) {
		LOGGER.trace("Going to convert classroom (name={}) to DTO", entity.getName());
		ClassroomDTO classroomDTO = modelMapper.map(entity, ClassroomDTO.class);
		LOGGER.trace("Converted classroom (name={}) to classroomDTO (name={})", entity.getName(),
				classroomDTO.getName());
		return classroomDTO;
	}

	protected Classroom convertToEntity(ClassroomDTO classroomDTO){
		LOGGER.trace("Going to convert classroomDTO (name={}) to entity", classroomDTO.getName());
		Classroom entity = modelMapper.map(classroomDTO, Classroom.class);
		LOGGER.trace("Converted classroomDTO to entity (name={})", entity.getName());
		LOGGER.trace("Going to set ID for entity classroom (name={})", entity.getName());
		Integer id=0;
		try {
		id = classroomDAO.getId(entity);
		entity.setId(id);
		} catch (DataNotFoundException e) {
			entity.setId(id);	
		}
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity classroom (name={})", id, entity.getName());
		LOGGER.trace("Conversion of classroomDTO (name={}) to classroom completed", classroomDTO.getName());
		return entity;
	}

	protected Classroom convertToEntity(ClassroomDTO oldDTO, ClassroomDTO newDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert classroomDTO (name={}) to entity", newDTO.getName());
		Classroom entity = modelMapper.map(newDTO, Classroom.class);
		LOGGER.trace("Converted classroomDTO to entity (name={})", entity.getName());
		LOGGER.trace("Going to set ID for entity classroom (name={})", entity.getName());
		Integer id = classroomDAO.getId(convertToEntity(oldDTO));
		entity.setId(id);
		LOGGER.trace("Set ID={} for entity classroom (name={})", id, entity.getName());
		LOGGER.trace("Conversion of classroomDTO (name={}) to classroom completed", newDTO.getName());
		return entity;
	}
}
