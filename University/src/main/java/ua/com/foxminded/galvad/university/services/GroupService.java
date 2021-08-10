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
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@Service
public class GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
	private static final String CONVERSION_TO_ENTITY_COMPLETED = "Conversion of groupDTO (name={}) to group completed";
	private static final String CONVERTED = "Converted groupDTO to group (name={})";
	private static final String GOING_TO_CONVERT = "Going to convert groupDTO (name={}) to group";
	private static final String GOING_TO_SET_LIST = "Going to set a list of students for group (name={})";
	private static final String SETTING_OF_LIST_DONE = "Set a list of students ({} in total) for group (name={})";
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private StudentDAO studentDAO;
	@Autowired
	private LessonService lessonService;

	public void create(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		groupDAO.create(convertToEntityWithoutID(groupDTO));
	}

	public GroupDTO retrieve(String groupName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve groupDTO from group with name={}", groupName);
		LOGGER.trace("Going to retrieve group by name={}", groupName);
		Group group = groupDAO.retrieve(groupName);
		LOGGER.trace("Retrieved a group with name={}", group.getName());
		LOGGER.trace("Going to retrieve groupDTO from a group with name={}", group.getName());
		GroupDTO groupDTO = convertToDTO(group);
		LOGGER.trace("Retrieved groupDTO from a group with name={}", group.getName());
		return groupDTO;
	}

	public GroupDTO retrieveWithListOfStudents(String groupName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve groupDTO from group with name={} and list of students", groupName);
		LOGGER.trace("Going to retrieve group by name={}", groupName);
		Group group = groupDAO.retrieve(groupName);
		LOGGER.trace("Retrieved a group with name={}", group.getName());
		LOGGER.trace("Going to retrieve a list of students for the group with name={}", group.getName());
		List<Student> listOfStudents = groupDAO.findAllStudentsForGroup(group);
		group.setListOfStudent(listOfStudents);
		LOGGER.trace("The list of students for the group with name={} was retrieved", group.getName());
		LOGGER.trace("Converting group with name={} to groupDTO", group.getName());
		GroupDTO groupDTO = convertToDTO(group);
		LOGGER.trace("Converting group with name={} to groupDTO completed", groupDTO.getName());
		return groupDTO;
	}

	public String getGroupNameForStudent(Student student) {
		try {
			LOGGER.trace("Going to retrieve a group name for a student with First_Name ={}, Last_Name ={}",
					student.getFirstName(), student.getLastName());
			Group group = groupDAO.getGroupForStudent(student);
			LOGGER.info("Retrieved the group (name={}) for a student from DB. First_Name ={}, Last_Name ={}",
					group.getName(), student.getFirstName(), student.getLastName());
			return group.getName();
		} catch (DataNotFoundException e) {
			LOGGER.info("A student (First_Name ={}, Last_Name ={}) is not assigned to any group!",
					student.getFirstName(), student.getLastName());
			return "NONE";
		}
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

	public List<GroupDTO> findAllWithoutStudentList() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<GroupDTO> list = groupDAO.findAllWithoutStudentList().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
		LOGGER.trace("List of ALL GroupDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public List<LessonDTO> findAllLessonsForGroup(String groupName) throws DataNotFoundException {
		LOGGER.trace("Going to get list of all lessons for group (name={})", groupName);
		List<LessonDTO> listOfLessons = lessonService.findAll().stream()
				.filter(s -> s.getGroup().getName().equals(groupName))
				.sorted((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime())).collect(Collectors.toList());
		LOGGER.trace("The list of lessons for group (name={}) retrieved successfully", groupName);
		return listOfLessons;
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
		LOGGER.trace(GOING_TO_CONVERT, groupDTO.getName());
		Group entity = modelMapper.map(groupDTO, Group.class);
		LOGGER.trace(CONVERTED, entity.getName());
		LOGGER.trace(GOING_TO_SET_LIST, entity.getName());
		List<Student> listOfStudents = new ArrayList<>();
		groupDTO.getListOfStudent().stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			listOfStudents.add(student);
			entity.setListOfStudent(listOfStudents);
			LOGGER.trace(SETTING_OF_LIST_DONE, listOfStudents.size(), entity.getName());
		});
		LOGGER.trace("Going to set an ID for a group (name={})", entity.getName());
		Integer id = groupDAO.getId(entity);
		entity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, entity.getName());
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, groupDTO.getName());
		return entity;
	}

	private Group convertToEntityWithoutID(GroupDTO groupDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT, groupDTO.getName());
		Group entity = modelMapper.map(groupDTO, Group.class);
		LOGGER.trace(CONVERTED, entity.getName());
		LOGGER.trace(GOING_TO_SET_LIST, entity.getName());
		List<Student> listOfStudents = new ArrayList<>();
		groupDTO.getListOfStudent().stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			listOfStudents.add(student);
			entity.setListOfStudent(listOfStudents);
			LOGGER.trace(SETTING_OF_LIST_DONE, listOfStudents.size(), entity.getName());
		});
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, groupDTO.getName());
		return entity;
	}

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT, newDTO.getName());
		Group entity = modelMapper.map(newDTO, Group.class);
		LOGGER.trace(CONVERTED, entity.getName());
		LOGGER.trace(GOING_TO_SET_LIST, entity.getName());
		List<Student> listOfStudents = new ArrayList<>();
		newDTO.getListOfStudent().stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			listOfStudents.add(student);
		});
		entity.setListOfStudent(listOfStudents);
		LOGGER.trace(SETTING_OF_LIST_DONE, listOfStudents.size(), entity.getName());
		LOGGER.trace("Going to set an ID for a group (name={})", entity.getName());
		Integer id = groupDAO.getId(convertToEntity(oldDTO));
		entity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, entity.getName());
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, newDTO.getName());
		return entity;
	}

}
