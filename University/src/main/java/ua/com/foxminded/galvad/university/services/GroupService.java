package ua.com.foxminded.galvad.university.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.com.foxminded.galvad.university.dao.impl.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
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
	private LessonDAO lessonDAO;
	@Autowired
	private LessonService lessonService;

	public void create(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		Group entity = convertToEntityWithoutID(groupDTO);
		groupDAO.create(entity);
		LOGGER.trace(GOING_TO_SET_LIST, entity.getName());
		entity.setSetOfStudent(convertListDTOToSetEntity(groupDTO.getListOfStudent()));
		groupDAO.update(entity);
		LOGGER.trace(SETTING_OF_LIST_DONE, entity.getSetOfStudent().size(), entity.getName());
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

	public void update(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update GroupDTO with newName={} ", newDTO.getName());
		groupDAO.update(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated GroupDTO with newName={} ", newDTO.getName());
	}

	public void delete(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {	
		if (!groupDTO.getListOfStudent().isEmpty()) {
			LOGGER.trace("Going to delete students of GroupDTO (name={}) from the group", groupDTO.getName());
			convertListDTOToSetEntity(groupDTO.getListOfStudent()).stream().forEach(student -> studentDAO.removeStudentFromGroups(student));
			LOGGER.trace("The students were deleted from the group");
		} else {
			LOGGER.trace("GroupDTO (name={}) doesn't have students", groupDTO.getName());
		}
		LOGGER.trace("Going to delete all the lessons for GroupDTO (name={})", groupDTO.getName());	
		lessonDAO.deleteByGroupID(convertToEntity(groupDTO).getId());
		LOGGER.trace("Going to delete GroupDTO (name={})", groupDTO.getName());	
		groupDAO.delete(convertToEntity(groupDTO));
	}

	
	public List<GroupDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<GroupDTO> list = groupDAO.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
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
		groupDTO.setListOfStudent(convertSetEntityToListDTO(entity.getSetOfStudent()));
		LOGGER.trace("Set a list of students for a groupDTO (name={}) successfully", groupDTO.getName());
		return groupDTO;
	}

	protected Group convertToEntity(GroupDTO groupDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT, groupDTO.getName());
		Group entity = modelMapper.map(groupDTO, Group.class); 
		LOGGER.trace(CONVERTED, entity.getName());
		LOGGER.trace(GOING_TO_SET_LIST, entity.getName());	
		entity.setSetOfStudent(convertListDTOToSetEntity(groupDTO.getListOfStudent()));
		LOGGER.trace(SETTING_OF_LIST_DONE, entity.getSetOfStudent().size(), entity.getName());
		LOGGER.trace("Going to set an ID for a group (name={})", entity.getName());
		entity.setId(groupDAO.getId(entity));
		LOGGER.trace("Set an ID={} for a group (name={})", entity.getId(), entity.getName());
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, groupDTO.getName());
		return entity;
	}

	private Group convertToEntityWithoutID(GroupDTO groupDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT, groupDTO.getName());
		Group entity = modelMapper.map(groupDTO, Group.class);
		LOGGER.trace(CONVERTED, entity.getName());
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, groupDTO.getName());
		return entity;
	}

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT, newDTO.getName());
		Group newEntity = modelMapper.map(newDTO, Group.class);
		LOGGER.trace(CONVERTED, newEntity.getName());
		LOGGER.trace(GOING_TO_CONVERT, oldDTO.getName());
		Group oldEntity = modelMapper.map(oldDTO, Group.class);
		oldEntity.setSetOfStudent(convertListDTOToSetEntity(oldDTO.getListOfStudent()));
		LOGGER.trace(CONVERTED, oldEntity.getName());
		LOGGER.trace(GOING_TO_SET_LIST, newEntity.getName());
		oldEntity.getSetOfStudent().stream().forEach(s->studentDAO.removeStudentFromGroups(s));
		newEntity.setSetOfStudent(convertListDTOToSetEntity(newDTO.getListOfStudent()));
		LOGGER.trace(SETTING_OF_LIST_DONE, newEntity.getSetOfStudent().size(), newEntity.getName());
		LOGGER.trace("Going to set an ID for a group (name={})", newEntity.getName());
		Integer id = groupDAO.getId(oldEntity);
		newEntity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, newEntity.getName());
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, newDTO.getName());
		return newEntity;
	}
	
	private Set<Student> convertListDTOToSetEntity(List<StudentDTO>listOfStudent){
		Set<Student> setOfStudents = new HashSet<>();
		listOfStudent.stream().forEach(studentDTO -> {
			Student student = modelMapper.map(studentDTO, Student.class);
			student.setId(studentDAO.getId(student));
			setOfStudents.add(student);
		});
		return setOfStudents;
	}
	
	private List<StudentDTO> convertSetEntityToListDTO (Set<Student> setOfStudents){
		List<StudentDTO> listOfStudent = setOfStudents.stream().map(student -> studentDAO.retrieve(student.getId()))
		.map(student -> modelMapper.map(student, StudentDTO.class)).collect(Collectors.toList());
		Collections.sort(listOfStudent,
				Comparator.comparing(StudentDTO::getLastName).thenComparing(StudentDTO::getFirstName));
		return listOfStudent;
	}
}
