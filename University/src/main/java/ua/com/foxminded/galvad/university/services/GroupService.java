package ua.com.foxminded.galvad.university.services;

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
		groupRepository.save(convertToEntity(groupDTO));
		LOGGER.trace("The group with the name={} created", groupDTO.getName());
	}

	public GroupDTO retrieve(String groupName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve groupDTO from group with name={}", groupName);
		LOGGER.trace("Going to retrieve group by name={}", groupName);
		Group group = groupRepository.findByName(groupName);
		LOGGER.trace("Retrieved a group with name={}", group.getName());
		LOGGER.trace("Going to retrieve groupDTO from a group with name={}", group.getName());
		GroupDTO groupDTO = convertToDTO(group);
		LOGGER.trace("Retrieved groupDTO from a group with name={}", group.getName());
		return groupDTO;
	}

	@Transactional
	public void update(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to update GroupDTO with newName={} ", newDTO.getName());
		groupRepository.save(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("Updated GroupDTO with newName={} ", newDTO.getName());
	}

	@Transactional
	public GroupDTO removeStudentsFromGroup(GroupDTO groupDTO)
			throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to convert DTO (name={}) to group", groupDTO.getName());
		Group group = convertToEntity(groupDTO);
		LOGGER.trace("Going to delete students from group (name={})", group.getName());
		group.getSetOfStudent().stream().forEach(s -> {
			s.setGroup(null);
			studentRepository.save(s);
		});
		LOGGER.trace("Students were deleted from group (name={})", group.getName());
		group.setSetOfStudent(new HashSet<>());
		return convertToDTO(group);
	}

	@Transactional
	public void delete(GroupDTO groupDTO) throws DataNotFoundException, DataAreNotUpdatedException {
		LOGGER.trace("Going to convert DTO (name={}) to group", groupDTO.getName());
		Group group = convertToEntity(groupDTO);
		LOGGER.trace("Going to delete all the lessons for GroupDTO (name={})", groupDTO.getName());
		lessonRepository.deleteByGroup(group);
		LOGGER.trace("Going to delete GroupDTO (name={})", groupDTO.getName());
		groupRepository.delete(group);
	}

	public List<GroupDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL ClassroomDTO from DB");
		List<GroupDTO> list = groupRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
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

	private Group convertToEntity(GroupDTO oldDTO, GroupDTO newDTO) throws DataNotFoundException {
		LOGGER.trace(GOING_TO_CONVERT, oldDTO.getName());
		Group oldEntity = modelMapper.map(oldDTO, Group.class);
		LOGGER.trace(CONVERTED, oldEntity.getName());
		oldEntity.getSetOfStudent().stream().forEach(s -> studentRepository.removeStudentFromGroups(s.getId()));
		LOGGER.trace(GOING_TO_CONVERT, newDTO.getName());
		Group newEntity = modelMapper.map(newDTO, Group.class);
		LOGGER.trace(CONVERTED, newEntity.getName());
		LOGGER.trace("Going to set an ID for a group (name={})", newEntity.getName());
		Integer id = groupRepository.findByName(oldEntity.getName()).getId();
		newEntity.setId(id);
		LOGGER.trace("Set an ID={} for a group (name={})", id, newEntity.getName());
		newDTO.getListOfStudent().stream()
				.map(s -> studentRepository.findByFirstNameAndLastName(s.getFirstName(), s.getLastName()))
				.forEach(s -> studentRepository.addStudentToGroup(s.getId(), newEntity));
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
			GroupDTO groupDTO = context.getSource();
			Group retrievedEntity = groupRepository.findByName(groupDTO.getName());
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
