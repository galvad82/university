package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Service
public class GroupService {

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
		groupDAO.create(convertToEntity(groupDTO));
	}

	public GroupDTO retrieve(Integer id) {
		return convertToDTO(groupDAO.retrieve(id));
	}

	public void update(GroupDTO oldDTO, GroupDTO newDTO) {
		groupDAO.update(convertToEntity(oldDTO, newDTO));
	}

	public void delete(GroupDTO groupDTO) {
		groupDAO.delete(convertToEntity(groupDTO));
	}

	public void delete(Integer id) {
		groupDAO.delete(id);
	}

	public List<GroupDTO> findAll() {
		return groupDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private GroupDTO convertToDTO(Group entity) {
		GroupDTO groupDTO = modelMapper.map(entity, GroupDTO.class);
		groupDTO.setListOfStudent(
				entity.getListOfStudent().stream().map(student -> studentDAO.retrieve(student.getId()))
						.map(student -> modelMapper.map(student, StudentDTO.class)).collect(Collectors.toList()));
		return groupDTO;
	}

	protected Group convertToEntity(GroupDTO groupDTO) {
		Group entity = modelMapper.map(groupDTO, Group.class);
		List<Student> listOfStudents = new ArrayList<>();
		groupDTO.getListOfStudent().stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			listOfStudents.add(student);
		});
		entity.setListOfStudent(listOfStudents);
		entity.setId(groupDAO.getId(entity));
		return entity;
	}

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO) {
		Group entity = modelMapper.map(newDTO, Group.class);
		List<Student> listOfStudents = new ArrayList<>();
		newDTO.getListOfStudent().stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			listOfStudents.add(student);
		});
		entity.setListOfStudent(listOfStudents);
		entity.setId(groupDAO.getId(convertToEntity(oldDTO)));
		return entity;
	}

}
