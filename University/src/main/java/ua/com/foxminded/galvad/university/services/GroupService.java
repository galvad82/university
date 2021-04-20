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

	public void create(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		groupDAO.create(convertToEntity(groupDTO));
	}

	public GroupDTO retrieve(Integer id) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve groupDTO from group with ID={}", id);
		GroupDTO groupDTO = new GroupDTO();
		LOGGER.trace("Going to retrieve group by ID={}", id);
		Group group = groupDAO.retrieve(id);
		LOGGER.trace("Retrieved a group with ID={}", id);
		LOGGER.trace("Going to retrieve groupDTO from a group with ID={}", id);
		groupDTO = convertToDTO(group);
		LOGGER.trace("Retrieved groupDTO from a group with ID={}", id);
		return groupDTO;
	}

	public void update(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update GroupDTO with newName={} ", newDTO.getName());
		groupDAO.update(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated GroupDTO with newName={} ", newDTO.getName());
	}

	public void delete(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to delete GroupDTO (name={})", groupDTO.getName());
		groupDAO.delete(convertToEntity(groupDTO));
	}

	public List<GroupDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<GroupDTO> list = groupDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		LOGGER.trace("List of ALL GroupDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	private GroupDTO convertToDTO(Group entity) throws DataNotFoundException {
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

	protected Group convertToEntity(GroupDTO groupDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert groupDTO (name={}) to group", groupDTO.getName());
		Group entity = modelMapper.map(groupDTO, Group.class);
		LOGGER.trace("Converted groupDTO to group (name={})", entity.getName());
		LOGGER.trace("Going to set a list of students for group (name={})", entity.getName());
		List<Student> listOfStudents = new ArrayList<>();
		groupDTO.getListOfStudent().stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			listOfStudents.add(student);
			entity.setListOfStudent(listOfStudents);
			LOGGER.trace("Set a list of students ({} in total) for group (name={})", listOfStudents.size(),
					entity.getName());
		});
		LOGGER.trace("Going to set an ID for a group (name={})", entity.getName());
		Integer id = groupDAO.getId(entity);
		entity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, entity.getName());
		LOGGER.trace("Conversion of groupDTO (name={}) to group completed", groupDTO.getName());
		return entity;
	}

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert groupDTO (name={}) to group", newDTO.getName());
		Group entity = modelMapper.map(newDTO, Group.class);
		LOGGER.trace("Converted groupDTO to group (name={})", entity.getName());
		LOGGER.trace("Going to set a list of students for group (name={})", entity.getName());
		List<Student> listOfStudents = new ArrayList<>();
		newDTO.getListOfStudent().stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			listOfStudents.add(student);
		});
		entity.setListOfStudent(listOfStudents);
		LOGGER.trace("Set a list of students ({} in total) for group (name={})", listOfStudents.size(),
				entity.getName());
		LOGGER.trace("Going to set an ID for a group (name={})", entity.getName());
		Integer id = groupDAO.getId(convertToEntity(oldDTO));
		entity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, entity.getName());
		LOGGER.trace("Conversion of groupDTO (name={}) to group completed", newDTO.getName());
		return entity;
	}

}
