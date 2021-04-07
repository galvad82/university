package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Student;

@Service
public class StudentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private StudentDAO studentDAO;

	public void create(StudentDTO studentDTO) {
		Student entity = modelMapper.map(studentDTO, Student.class);
		try {
			studentDAO.create(entity);
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public StudentDTO retrieve(Integer id) {
		Student student = new Student();
		try {
			student = studentDAO.retrieve(id);
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return convertToDTO(student);
	}

	public void update(StudentDTO oldDTO, StudentDTO newDTO) {
		LOGGER.trace("Going to update StudentDTO, firstName={}, lastName={}", newDTO.getFirstName(),
				newDTO.getLastName());
		try {
			studentDAO.update(convertToEntity(oldDTO, newDTO));
			LOGGER.trace("StudentDTO was updated successfully.");
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public void delete(StudentDTO studentDTO) {
		LOGGER.trace("Going to delete StudentDTO by entity, firstName={}, lastName={}", studentDTO.getFirstName(),
				studentDTO.getLastName());
		try {
			studentDAO.delete(convertToEntity(studentDTO));
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public void delete(Integer id) {
		LOGGER.trace("Going to delete StudentDTO by ID={}", id);
		try {
			studentDAO.delete(id);
			LOGGER.trace("StudentDTO with ID={} was deleted.", id);
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		} 

	}

	public List<StudentDTO> findAll() {
		LOGGER.trace("Going to get list of ALL StudentDTO from DB");
		List<StudentDTO> list = new ArrayList<>();
		try {
			list = studentDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
			LOGGER.trace("List of ALL StudentDTO retrieved from DB, {} were found", list.size());
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return list;
	}

	private StudentDTO convertToDTO(Student entity) {
		LOGGER.trace("Going to convert entity(firstName={}, lastName={}) to DTO", entity.getFirstName(), entity.getLastName());
		StudentDTO studentDTO = modelMapper.map(entity, StudentDTO.class);
		LOGGER.trace("Entity was converted to DTO successfully");
		return studentDTO;
	}

	private Student convertToEntity(StudentDTO studentDTO) {
		LOGGER.trace("Going to convert StudentDTO to entity");
		Student entity = modelMapper.map(studentDTO, Student.class);
		LOGGER.trace("StudentDTO converted successfully.");
		try {
			LOGGER.trace("Going to get ID for Student with firstName={}, lastName={}", entity.getFirstName(),
					entity.getLastName());
			Integer id = studentDAO.getId(entity);
			LOGGER.trace("ID={}", id);
			entity.setId(id);
			LOGGER.trace("ID was set for the entity successfully");
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return entity;
	}

	private Student convertToEntity(StudentDTO oldDTO, StudentDTO newDTO) {
		LOGGER.trace("Going to convert newDTO(firstName={}, lastName={}) to entity", newDTO.getFirstName(), newDTO.getLastName());
		Student entity = modelMapper.map(newDTO, Student.class);
		LOGGER.trace("DTO was converted successfully.");
		try {
			LOGGER.trace("Going to set ID of oldDTO to newDTO");
			entity.setId(studentDAO.getId(convertToEntity(oldDTO)));
			LOGGER.trace("ID of oldDTO was set to newDTO successfully");
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}

		return entity;
	}
}
