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
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Service
public class GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);

	private ModelMapper modelMapper = new ModelMapper();
	private GroupDAO groupDAO;
	private StudentDAO studentDAO;

	@Autowired
	public void setGroupDAO(GroupDAO groupDAO) {
		if (groupDAO != null) {
			this.groupDAO = groupDAO;
		} else {
			throw new IllegalArgumentException("GroupDAO cannot be null!");
		}
	}

	@Autowired
	public void setStudentDAO(StudentDAO studentDAO) {
		if (studentDAO != null) {
			this.studentDAO = studentDAO;
		} else {
			throw new IllegalArgumentException("StudentDAO cannot be null!");
		}
	}

	public void create(GroupDTO groupDTO) {
		try {
			Group group = convertToEntity(groupDTO);
			groupDAO.create(group);
		} catch (DataNotFoundException ex) {
			LOGGER.error(ex.getErrorMessage());
			LOGGER.error(ex.getCauseDescription());
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}

	}

	public GroupDTO retrieve(Integer id) {
		LOGGER.trace("Going to retrieve groupDTO from group with ID={}", id);
		GroupDTO groupDTO = new GroupDTO();
		try {
			LOGGER.trace("Going to retrieve group by ID={}", id);
			Group group = groupDAO.retrieve(id);
			LOGGER.trace("Retrieved a group with ID={}", id);
			LOGGER.trace("Going to retrieve groupDTO from a group with ID={}", id);
			groupDTO = convertToDTO(group);
			LOGGER.trace("Retrieved groupDTO from a group with ID={}", id);
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return groupDTO;
	}

	public void update(GroupDTO oldDTO, GroupDTO newDTO) {
		LOGGER.trace("Going to update GroupDTO with newName={} ", newDTO.getName());
		try {
			groupDAO.update(convertToEntity(oldDTO, newDTO));
			LOGGER.trace("Updated GroupDTO with newName={} ", newDTO.getName());
		} catch (DataNotFoundException ex) {
			LOGGER.error(ex.getErrorMessage());
			LOGGER.error(ex.getCauseDescription());
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public void delete(GroupDTO groupDTO) {
		LOGGER.trace("Going to delete GroupDTO (name={})", groupDTO.getName());
		try {
			groupDAO.delete(convertToEntity(groupDTO));
		} catch (DataNotFoundException ex) {
			LOGGER.error(ex.getErrorMessage());
			LOGGER.error(ex.getCauseDescription());
		} catch (DataAreNotUpdatedException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
	}

	public List<GroupDTO> findAll() {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<GroupDTO> list = new ArrayList<>();
		try {
			list = groupDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
			LOGGER.trace("List of ALL GroupDTO retrieved from DB, {} were found", list.size());
		} catch (DataNotFoundException e) {
			LOGGER.error(e.getErrorMessage());
			LOGGER.error(e.getCauseDescription());
		}
		return list;
	}

	private GroupDTO convertToDTO(Group entity) {
		LOGGER.trace("Going to convert group (name={}) to DTO", entity.getName());
		GroupDTO groupDTO = modelMapper.map(entity, GroupDTO.class);
		LOGGER.trace("Converted a group to groupDTO (name={})", groupDTO.getName());
		LOGGER.trace("Going to set a list of students for a groupDTO (name={})", groupDTO.getName());
		groupDTO.setListOfStudent(
				entity.getListOfStudent().stream().map(student -> studentDAO.retrieve(student.getId()))
						.map(student -> modelMapper.map(student, StudentDTO.class)).collect(Collectors.toList()));
		LOGGER.trace("Set a list of students for a groupDTO (name={}) successfully", groupDTO.getName());
		return groupDTO;
	}

	protected Group convertToEntity(GroupDTO groupDTO) {
		LOGGER.trace("Going to convert groupDTO (name={}) to group", groupDTO.getName());
		Group entity = modelMapper.map(groupDTO, Group.class);
		LOGGER.trace("Converted groupDTO to group (name={})", entity.getName());
		LOGGER.trace("Going to set a list of students for group (name={})", entity.getName());
		List<Student> listOfStudents = new ArrayList<>();
		try {
			groupDTO.getListOfStudent().stream().forEach(studentDTO -> {
				Student student = modelMapper.map(studentDTO, Student.class);
				student.setId(studentDAO.getId(student));
				listOfStudents.add(student);
				entity.setListOfStudent(listOfStudents);
				LOGGER.trace("Set a list of students ({} in total) for group (name={})", listOfStudents.size(),
						entity.getName());
			});
		} catch (DataNotFoundException e) {
			entity.setListOfStudent(listOfStudents);
			LOGGER.error("Set a list of students ({} in total) for group (name={})", listOfStudents.size(),
					entity.getName());
		}
		LOGGER.trace("Going to set an ID for a group (name={})", entity.getName());
		Integer id = 0;
		try {
			id = groupDAO.getId(entity);
			entity.setId(id);
			LOGGER.trace("Set an ID={} for a group (name={})", id, entity.getName());
		} catch (DataNotFoundException e) {
			entity.setId(id);
			LOGGER.error("Set an ID={} for a group (name={})", id, entity.getName());
		}
		LOGGER.trace("Conversion of groupDTO (name={}) to group completed", groupDTO.getName());
		return entity;
	}

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO) {
		LOGGER.trace("Going to convert groupDTO (name={}) to group", newDTO.getName());
		Group entity = modelMapper.map(newDTO, Group.class);
		LOGGER.trace("Converted groupDTO to group (name={})", entity.getName());
		LOGGER.trace("Going to set a list of students for group (name={})", entity.getName());
		List<Student> listOfStudents = new ArrayList<>();
		try {
			newDTO.getListOfStudent().stream().forEach(studentDTO -> {
				Student student = modelMapper.map(studentDTO, Student.class);
				student.setId(studentDAO.getId(student));	
				listOfStudents.add(student);
			});
			entity.setListOfStudent(listOfStudents);
			LOGGER.trace("Set a list of students ({} in total) for group (name={})", listOfStudents.size(),
					entity.getName());
		} catch (DataNotFoundException e) {
			entity.setListOfStudent(listOfStudents);
			LOGGER.error("Set a list of students ({} in total) for group (name={})", listOfStudents.size(),
					entity.getName());
		}
		LOGGER.trace("Going to set an ID for a group (name={})", entity.getName());
		Integer id = 0;
		try {
			id = groupDAO.getId(convertToEntity(oldDTO));
			entity.setId(id);
			LOGGER.trace("Set an ID={} for a group (name={})", id, entity.getName());
		} catch (DataNotFoundException e) {
			entity.setId(id);
			LOGGER.error("Set an ID={} for a group (name={})", id, entity.getName());
		}
		LOGGER.trace("Conversion of groupDTO (name={}) to group completed", newDTO.getName());
		return entity;
	}

}
