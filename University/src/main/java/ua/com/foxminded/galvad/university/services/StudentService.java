package ua.com.foxminded.galvad.university.services;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Student;

@Service

public class StudentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private StudentDAO studentDAO;

	@Autowired
	private GroupService groupService;

	@Transactional
	public void create(StudentDTO studentDTO) throws DataAreNotUpdatedException {
		studentDAO.create(convertToEntityWithoutID(studentDTO));
	}

	public StudentDTO retrieve(String firstName, String lastName) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to retrieve StudentDTO, firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Going to retrieve Student entity, firstName={}, lastName={}", firstName, lastName);
		Student student = studentDAO.retrieve(firstName, lastName);
		LOGGER.trace("Student entity retrieved, firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Converting Student entity to DTO, firstName={}, lastName={}", firstName, lastName);
		StudentDTO resultDTO = convertToDTO(student);
		LOGGER.trace("Student entity converted to DTO, firstName={}, lastName={}", resultDTO.getFirstName(),
				resultDTO.getLastName());
		return resultDTO;
	}

	@Transactional
	public void update(StudentDTO oldDTO, StudentDTO newDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to update StudentDTO, firstName={}, lastName={}", newDTO.getFirstName(),
				newDTO.getLastName());
		studentDAO.update(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("StudentDTO was updated successfully.");
	}

	public void updateGroup(StudentDTO studentDTO, GroupDTO groupDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to assign StudentDTO (firstName={}, lastName={}) to a groupDTO with name={}",
				studentDTO.getFirstName(), studentDTO.getLastName(), groupDTO.getName());
		studentDAO.addStudentToGroup(convertToEntity(studentDTO), groupService.convertToEntity(groupDTO));
		LOGGER.trace("StudentDTO was assigned successfully.");
	}

	@Transactional
	public void delete(StudentDTO studentDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to delete StudentDTO by entity, firstName={}, lastName={}", studentDTO.getFirstName(),
				studentDTO.getLastName());
		studentDAO.delete(convertToEntity(studentDTO));
	}

	public List<StudentDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL StudentDTO from DB");
		List<StudentDTO> list = studentDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL StudentDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public Map<StudentDTO, String> buildStudentGroupMap() throws DataNotFoundException {
		LOGGER.trace("Going to get a map (StudentDTO,GroupName)");
		Map<StudentDTO, String> studentGroupMap = new LinkedHashMap<>();
		studentDAO.findAll().stream().forEach(student -> {
			if (student.getGroup() == null) {
				studentGroupMap.put(convertToDTO(student), "NONE");
			} else {
				studentGroupMap.put(convertToDTO(student), student.getGroup().getName());
			}
		});
		LOGGER.trace("A map (StudentDTO,GroupName) is prepared.");
		return studentGroupMap;
	}

	public Set<StudentDTO> findAllUnassignedStudents() throws DataNotFoundException {
		LOGGER.trace("Going to find all unassigned students");
		Set<StudentDTO> setOfUnassignedStudents = studentDAO.findAll().stream().filter(s -> (s.getGroup() == null))
				.map(this::convertToDTO).collect(Collectors.toSet());
		LOGGER.trace("A list with all unassigned students is prepared.");
		return setOfUnassignedStudents;
	}

	@Transactional
	public void addToGroup(StudentDTO studentDTO, GroupDTO groupDTO) throws DataAreNotUpdatedException {
		studentDAO.addStudentToGroup(convertToEntity(studentDTO), groupService.convertToEntity(groupDTO));
	}

	@Transactional
	public void removeStudentFromGroup(StudentDTO studentDTO) throws DataNotFoundException {
		LOGGER.trace("Going to remove a studentDTO(firstName={}, lastName={}) from group", studentDTO.getFirstName(),
				studentDTO.getLastName());
		studentDAO.removeStudentFromGroups(convertToEntity(studentDTO));
		LOGGER.trace("A studentDTO(firstName={}, lastName={}) was removed from group successfully",
				studentDTO.getFirstName(), studentDTO.getLastName());
	}

	private StudentDTO convertToDTO(Student entity) throws DataNotFoundException {
		LOGGER.trace("Going to convert entity(firstName={}, lastName={}) to DTO", entity.getFirstName(),
				entity.getLastName());
		StudentDTO studentDTO = modelMapper.map(entity, StudentDTO.class);
		LOGGER.trace("Entity was converted to DTO successfully");
		return studentDTO;
	}

	private Student convertToEntity(StudentDTO studentDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert StudentDTO to entity");
		Student entity = modelMapper.map(studentDTO, Student.class);
		LOGGER.trace("StudentDTO converted successfully.");
		LOGGER.trace("Going to get ID for Student with firstName={}, lastName={}", entity.getFirstName(),
				entity.getLastName());
		Integer id = studentDAO.getId(entity);
		LOGGER.trace("ID={}", id);
		entity.setId(id);
		LOGGER.trace("ID was set for the entity successfully");
		return entity;
	}

	private Student convertToEntity(StudentDTO oldDTO, StudentDTO newDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert newDTO(firstName={}, lastName={}) to entity", newDTO.getFirstName(),
				newDTO.getLastName());
		Student entity = modelMapper.map(newDTO, Student.class);
		LOGGER.trace("DTO was converted successfully.");
		LOGGER.trace("Going to set ID of oldDTO to newDTO");
		entity.setId(studentDAO.getId(convertToEntity(oldDTO)));
		LOGGER.trace("ID of oldDTO was set to newDTO successfully");
		return entity;
	}

	private Student convertToEntityWithoutID(StudentDTO studentDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert StudentDTO to entity");
		Student entity = modelMapper.map(studentDTO, Student.class);
		LOGGER.trace("StudentDTO converted successfully.");
		return entity;
	}
}