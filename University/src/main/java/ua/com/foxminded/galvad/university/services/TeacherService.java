package ua.com.foxminded.galvad.university.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Teacher;

@Service
public class TeacherService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private TeacherDAO teacherDAO;

	public void create(TeacherDTO teacherDTO) throws DataAreNotUpdatedException {
		teacherDAO.create(convertToEntity(teacherDTO));
	}

	public TeacherDTO retrieve(Integer id) throws DataNotFoundException {
		return convertToDTO(teacherDAO.retrieve(id));
	}

	public void update(TeacherDTO oldDTO, TeacherDTO newDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update TeacherDTO, firstName={}, lastName={}", newDTO.getFirstName(),
				newDTO.getLastName());
		teacherDAO.update(convertToEntity(oldDTO, newDTO));
	}

	public void delete(TeacherDTO teacherDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete TeacherDTO by entity, firstName={}, lastName={}", teacherDTO.getFirstName(),
				teacherDTO.getLastName());
		teacherDAO.delete(convertToEntity(teacherDTO));
	}

	public List<TeacherDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL TeacherDTO from DB");
		List<TeacherDTO> list = teacherDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL TeacherDTO retrieved from DB, {} were found", list.size());
		return list;
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
		Integer id = teacherDAO.getId(entity);
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
		entity.setId(teacherDAO.getId(convertToEntity(oldDTO)));
		LOGGER.trace("ID of oldDTO was set to newDTO successfully");
		return entity;
	}
}
