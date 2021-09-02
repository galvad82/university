package ua.com.foxminded.galvad.university.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional
public class GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
	private static final String CONVERSION_TO_ENTITY_COMPLETED = "Conversion of groupDTO (name={}) to group completed";
	private static final String CONVERTED = "Converted groupDTO to group (name={})";
	private static final String GOING_TO_CONVERT = "Going to convert groupDTO (name={}) to group";

	private ModelMapper modelMapper;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private StudentDAO studentDAO;
	@Autowired
	private LessonDAO lessonDAO;
	@Autowired
	private LessonService lessonService;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public GroupService(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.modelMapper.addConverter(entityToDTO);
		this.modelMapper.addConverter(dtoToEntity);
	}

	public void create(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		Group entity = convertToEntity(groupDTO);
		groupDAO.create(entity);
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
		Group group = convertToEntity(groupDTO);
		LOGGER.trace("Going to delete students of GroupDTO (name={}) from the group", groupDTO.getName());
		if (!group.getSetOfStudent().isEmpty()) {
			group.getSetOfStudent().stream().forEach(student -> studentDAO.removeStudentFromGroups(student));
		} else {
			LOGGER.trace("GroupDTO (name={}) doesn't have students", groupDTO.getName());
		}
		entityManager.flush();
		LOGGER.trace("The students were deleted from the group");
		LOGGER.trace("Going to delete all the lessons for GroupDTO (name={})", groupDTO.getName());
		lessonDAO.deleteByGroupID(group.getId());
		LOGGER.trace("Going to delete GroupDTO (name={})", groupDTO.getName());
		groupDAO.delete(group);
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
		return groupDTO;
	}

	protected Group convertToEntity(GroupDTO groupDTO) throws DataNotFoundException {
		if (groupDTO != null) {
			LOGGER.trace(GOING_TO_CONVERT, groupDTO.getName());
			Group entity = modelMapper.map(groupDTO, Group.class);
			LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, groupDTO.getName());
			return entity;
		} else {
			return null;
		}
	}

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT, oldDTO.getName());
		Group oldEntity = modelMapper.map(oldDTO, Group.class);
		LOGGER.trace(CONVERTED, oldEntity.getName());
		oldEntity.getSetOfStudent().stream().forEach(s -> studentDAO.removeStudentFromGroups(s));

		LOGGER.trace(GOING_TO_CONVERT, newDTO.getName());
		Group newEntity = modelMapper.map(newDTO, Group.class);
		LOGGER.trace(CONVERTED, newEntity.getName());
		LOGGER.trace("Going to set an ID for a group (name={})", newEntity.getName());
		Integer id = groupDAO.getId(oldEntity);
		newEntity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, newEntity.getName());
		LOGGER.trace(CONVERSION_TO_ENTITY_COMPLETED, newDTO.getName());
		return newEntity;
	}

	Converter<Group, GroupDTO> entityToDTO = new Converter<Group, GroupDTO>() {
		@Override
		public GroupDTO convert(MappingContext<Group, GroupDTO> context) {
			GroupDTO groupDTO = new GroupDTO();
			groupDTO.setName(context.getSource().getName());
			for (Student student : context.getSource().getSetOfStudent()) {
				StudentDTO studentDTO = new StudentDTO();
				studentDTO.setFirstName(student.getFirstName());
				studentDTO.setLastName(student.getLastName());
				groupDTO.getListOfStudent().add(studentDTO);
			}
			Collections.sort(groupDTO.getListOfStudent(),
					Comparator.comparing(StudentDTO::getLastName).thenComparing(StudentDTO::getFirstName));
			return groupDTO;
		}
	};

	Converter<GroupDTO, Group> dtoToEntity = new Converter<GroupDTO, Group>() {
		@Override
		public Group convert(MappingContext<GroupDTO, Group> context) {
			Group group = new Group();
			group.setName(context.getSource().getName());
			for (StudentDTO studentDTO : context.getSource().getListOfStudent()) {
				Student student = new Student();
				student.setFirstName(studentDTO.getFirstName());
				student.setLastName(studentDTO.getLastName());
				student.setId(studentDAO.getId(student));
				group.getSetOfStudent().add(student);
			}
			try {
				group.setId(groupDAO.getId(group));
			} catch (DataNotFoundException e) {
				group.setId(null);
			}
			return group;
		}
	};

}
