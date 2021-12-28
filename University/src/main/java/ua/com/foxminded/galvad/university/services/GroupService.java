package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.repository.GroupRepository;
import ua.com.foxminded.galvad.university.repository.LessonRepository;
import ua.com.foxminded.galvad.university.repository.StudentRepository;

@Service
public class GroupService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GroupService.class);
	private static final String CONVERSION_TO_ENTITY_COMPLETED = "Conversion of groupDTO (name={}) to group completed";
	private static final String CONVERTED = "Converted groupDTO to group (name={})";
	private static final String GOING_TO_CONVERT = "Going to convert groupDTO (name={}) to group";

	private ModelMapper modelMapper;
	private GroupRepository groupRepository;
	private StudentRepository studentRepository;
	private LessonRepository lessonRepository;

	@Autowired
	public GroupService(ModelMapper modelMapper, GroupRepository groupRepository, StudentRepository studentRepository,
			LessonRepository lessonRepository) {
		this.modelMapper = modelMapper;
		this.modelMapper.addConverter(entityToDTO);
		this.modelMapper.addConverter(dtoToEntity);
		this.groupRepository = groupRepository;
		this.studentRepository = studentRepository;
		this.lessonRepository = lessonRepository;
	}

	public void create(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to create a group with the name={}", groupDTO.getName());
		try {
			groupRepository.save(convertToEntity(groupDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Group with name={} wasn't added to DB.", groupDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Group with name=%s wasn't added to DB.", groupDTO.getName()), e);
		}
		LOGGER.trace("The group with the name={} created", groupDTO.getName());
	}

	public GroupDTO retrieve(String groupName) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to retrieve group by name={}", groupName);
		Group group = null;
		try {
			group = groupRepository.findByName(groupName);
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a group from DB. Name={}", groupName);
			throw new DataNotFoundException(String.format("Can't retrieve a group from DB. Name=%s", groupName));
		}
		if (group == null) {
			LOGGER.info("A group with name \"{}\" is not found.", groupName);
			throw new DataNotFoundException(String.format("A group with name \"%s\" is not found.", groupName));
		}
		LOGGER.trace("Retrieved a group with name={}", group.getName());
		LOGGER.trace("Going to retrieve groupDTO from a group with name={}", group.getName());
		GroupDTO groupDTO = convertToDTO(group);
		LOGGER.trace("Retrieved groupDTO from a group with name={}", group.getName());
		return groupDTO;
	}

	@Transactional
	public void update(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update GroupDTO with newName={} ", newDTO.getName());
		try {
			groupRepository.save(convertToEntity(oldDTO, newDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't update a group with name={}", oldDTO.getName());
			throw new DataAreNotUpdatedException(String.format("Can't update a group with name%s", oldDTO.getName()));
		}
		LOGGER.trace("Updated GroupDTO with newName={} ", newDTO.getName());
	}

	@Transactional
	public GroupDTO removeStudentsFromGroup(GroupDTO groupDTO)
			throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to convert DTO (name={}) to group", groupDTO.getName());
		Group group = convertToEntity(groupDTO);
		LOGGER.trace("Going to delete students from group (name={})", group.getName());
		try {
			group.getSetOfStudent().stream().map(Student::getId).forEach(studentRepository::removeStudentFromGroups);
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete students from the group with name={}", groupDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete students from the group with name=%s", groupDTO.getName()));
		}
		LOGGER.trace("Students were deleted from group (name={})", group.getName());
		group.setSetOfStudent(new HashSet<>());
		return convertToDTO(group);
	}

	@Transactional
	public void delete(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to convert DTO (name={}) to group", groupDTO.getName());
		Group group = convertToEntity(groupDTO);
		LOGGER.trace("Going to delete all the lessons for GroupDTO (name={})", groupDTO.getName());
		try {
			lessonRepository.deleteByGroup(group);
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete lessons for the group with name={}", groupDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete lessons for the group with name=%s", groupDTO.getName()));
		}
		LOGGER.trace("Going to delete GroupDTO (name={})", groupDTO.getName());
		try {
			groupRepository.delete(group);
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete a group with name={}", groupDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete a group with name=%s", groupDTO.getName()));
		}
		LOGGER.trace("GroupDTO (name={}) deleted successfully", groupDTO.getName());
	}

	public List<GroupDTO> findAll() throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<GroupDTO> list = new ArrayList<>();
		try {
			list = groupRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a list of groups.");
			throw new DataNotFoundException("Can't retrieve a list of groups.");
		}
		LOGGER.trace("List of ALL GroupDTO retrieved from DB, {} were found", list.size());
		return list;
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

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO)
			throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace(GOING_TO_CONVERT, oldDTO.getName());
		Group oldEntity = modelMapper.map(oldDTO, Group.class);
		LOGGER.trace(CONVERTED, oldEntity.getName());
		try {
			oldEntity.getSetOfStudent().stream().forEach(s -> studentRepository.removeStudentFromGroups(s.getId()));
		} catch (DataAccessException e) {
			LOGGER.info("Can't remove students from old group DTO with name {}", oldEntity.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't remove students from old group DTO with name %s", oldEntity.getName()));
		}
		LOGGER.trace(GOING_TO_CONVERT, newDTO.getName());
		Group newEntity = modelMapper.map(newDTO, Group.class);
		LOGGER.trace(CONVERTED, newEntity.getName());
		LOGGER.trace("Going to set an ID for a group (name={})", newEntity.getName());
		Integer id;
		try {
			id = groupRepository.findByName(oldEntity.getName()).getId();
		} catch (DataAccessException e) {
			LOGGER.info("Can't find an old groupDTO with name {} while updating groupDTO.", oldEntity.getName());
			throw new DataNotFoundException(String
					.format("Can't find an old groupDTO with name %s while updating groupDTO.", oldEntity.getName()));
		}
		newEntity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, newEntity.getName());
		try {
			newDTO.getListOfStudent().stream()
					.map(s -> studentRepository.findByFirstNameAndLastName(s.getFirstName(), s.getLastName()))
					.forEach(s -> studentRepository.addStudentToGroup(s.getId(), newEntity));
		} catch (DataAccessException e) {
			LOGGER.info("Can't process list of students for new group DTO with name {}", newDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't process list of students for new group DTO with name %s", newDTO.getName()));
		}
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
		public Group convert(MappingContext<GroupDTO, Group> context) throws DataNotFoundException {
			GroupDTO groupDTO = context.getSource();
			Group retrievedEntity = null;
			try {
				retrievedEntity = groupRepository.findByName(groupDTO.getName());
			} catch (DataAccessException e) {
				LOGGER.info("Can't find a group with name {} while converting GroupDto to entity.", groupDTO.getName());
				throw new DataNotFoundException(String.format(
						"Can't find a group with name %s while converting GroupDto to entity.", groupDTO.getName()));
			}
			if (retrievedEntity != null) {
				return retrievedEntity;
			} else {
				Group group = new Group();
				group.setName(groupDTO.getName());
				return group;
			}
		}
	};

}
