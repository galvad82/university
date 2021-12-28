package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.repository.GroupRepository;
import ua.com.foxminded.galvad.university.repository.StudentRepository;

@Service
public class StudentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

	private ModelMapper modelMapper;
	private StudentRepository studentRepository;
	private GroupRepository groupRepository;
	private GroupService groupService;

	@Autowired
	public StudentService(ModelMapper modelMapper, StudentRepository studentRepository, GroupRepository groupRepository,
			GroupService groupService) {
		this.modelMapper = modelMapper;
		this.modelMapper.addConverter(entityToDTO);
		this.modelMapper.addConverter(dtoToEntity);
		this.studentRepository = studentRepository;
		this.groupRepository = groupRepository;
		this.groupService = groupService;
	}

	public void create(StudentDTO studentDTO) throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to create a student with firstName={} and lastName={}", studentDTO.getFirstName(),
				studentDTO.getLastName());
		try {
			studentRepository.save(convertToEntity(studentDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Student with firstName={} and lastName={} wasn't added to DB.", studentDTO.getFirstName(),
					studentDTO.getLastName());
			throw new DataAreNotUpdatedException(
					String.format("Student with firstName=%s and lastName=%s wasn't added to DB.",
							studentDTO.getFirstName(), studentDTO.getLastName()),
					e);
		}
		LOGGER.trace("The student with firstName={} and lastName={} created", studentDTO.getFirstName(),
				studentDTO.getLastName());
	}

	public StudentDTO retrieve(String firstName, String lastName) throws DataNotFoundException {
		LOGGER.trace("Going to retrieve StudentDTO, firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Going to retrieve Student entity, firstName={}, lastName={}", firstName, lastName);
		Student student = null;
		try {
			student = studentRepository.findByFirstNameAndLastName(firstName, lastName);
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve StudentDTO, firstName={}, lastName={}", firstName, lastName);
			throw new DataNotFoundException(
					String.format("Can't retrieve StudentDTO, firstName=%s, lastName=%s", firstName, lastName));
		}
		if (student == null) {
			LOGGER.info("A student (firstName={}, lastName={}) is not found.", firstName, lastName);
			throw new DataNotFoundException(
					String.format("A student (firstName=%s, lastName=%s) is not found.", firstName, lastName));
		}
		LOGGER.trace("Student entity retrieved, firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Converting Student entity to DTO, firstName={}, lastName={}", firstName, lastName);
		StudentDTO resultDTO = convertToDTO(student);
		LOGGER.trace("Student entity converted to DTO, firstName={}, lastName={}", resultDTO.getFirstName(),
				resultDTO.getLastName());
		return resultDTO;
	}

	@Transactional
	public void update(StudentDTO oldDTO, StudentDTO newDTO) throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to update StudentDTO, firstName={}, lastName={}", newDTO.getFirstName(),
				newDTO.getLastName());
		try {
			studentRepository.save(convertToEntity(oldDTO, newDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't update a student (firstName={}, lastName={})", oldDTO.getFirstName(),
					oldDTO.getLastName());
			throw new DataAreNotUpdatedException(String.format("Can't update a student (firstName=%s, lastName=%s)",
					oldDTO.getFirstName(), oldDTO.getLastName()));
		}
		LOGGER.trace("StudentDTO was updated successfully.");
	}

	@Transactional
	public void addToGroup(StudentDTO studentDTO, GroupDTO groupDTO)
			throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to assign StudentDTO (firstName={}, lastName={}) to a groupDTO with name={}",
				studentDTO.getFirstName(), studentDTO.getLastName(), groupDTO.getName());
		try {
			studentRepository.addStudentToGroup(convertToEntity(studentDTO).getId(),
					groupService.convertToEntity(groupDTO));
		} catch (DataAccessException e) {
			LOGGER.info("Can't assign StudentDTO (firstName={}, lastName={}) to a groupDTO with name={}",
					studentDTO.getFirstName(), studentDTO.getLastName(), groupDTO.getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't assign StudentDTO (firstName=%s, lastName=%s) to a groupDTO with name=%s",
							studentDTO.getFirstName(), studentDTO.getLastName(), groupDTO.getName()));
		}
		LOGGER.trace("StudentDTO was assigned successfully.");
	}

	public void delete(StudentDTO studentDTO) throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to delete StudentDTO by entity, firstName={}, lastName={}", studentDTO.getFirstName(),
				studentDTO.getLastName());
		Student student = convertToEntity(studentDTO);
		try {
			studentRepository.delete(student);
		} catch (DataIntegrityViolationException e) {
			LOGGER.info("Can't delete the student \"{} {}\" as he or she is assigned to the group \"{}\".",
					student.getFirstName(), student.getLastName(), student.getGroup().getName());
			throw new DataAreNotUpdatedException(
					String.format("Can't delete the student \"%s %s\" as he or she is assigned to the group \"%s\".",
							student.getFirstName(), student.getLastName(), student.getGroup().getName()));
		} catch (DataAccessException e) {
			LOGGER.info("Can't delete the student (firstName={}, lastName={})", student.getFirstName(),
					student.getLastName());
			throw new DataAreNotUpdatedException(String.format("Can't delete the student (firstName=%s, lastName=%s)",
					student.getFirstName(), student.getLastName()));
		}
	}

	public List<StudentDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL StudentDTO from DB");
		List<StudentDTO> list = new ArrayList<>();
		try {
			list = studentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
		} catch (DataAccessException e) {
			LOGGER.info("Can't retrieve a list of students.");
			throw new DataNotFoundException("Can't retrieve a list of students.");
		}
		LOGGER.trace("List of ALL StudentDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public Map<StudentDTO, String> buildStudentGroupMap() throws DataNotFoundException {
		LOGGER.trace("Going to get a map (StudentDTO,GroupName)");
		Map<StudentDTO, String> studentGroupMap = new LinkedHashMap<>();
		try {
			studentRepository.findAll().stream().forEach(student -> {
				if (student.getGroup() == null) {
					studentGroupMap.put(convertToDTO(student), "NONE");
				} else {
					studentGroupMap.put(convertToDTO(student), student.getGroup().getName());
				}
			});
		} catch (DataAccessException e) {
			LOGGER.info("Can't build a map (StudentDTO,GroupName).");
			throw new DataNotFoundException("Can't build a map (StudentDTO,GroupName).");
		}
		LOGGER.trace("A map (StudentDTO,GroupName) is prepared.");
		return studentGroupMap;
	}

	public Set<StudentDTO> findAllUnassignedStudents() throws DataNotFoundException {
		LOGGER.trace("Going to find all unassigned students");
		Set<StudentDTO> setOfUnassignedStudents = new HashSet<>();
		try {
			setOfUnassignedStudents = studentRepository.findAll().stream().filter(s -> (s.getGroup() == null))
					.map(this::convertToDTO).collect(Collectors.toSet());
		} catch (DataAccessException e) {
			LOGGER.info("Can't build a list of unassigned students.");
			throw new DataNotFoundException("Can't build a list of unassigned students.");
		}
		LOGGER.trace("A list with all unassigned students is prepared.");
		return setOfUnassignedStudents;
	}

	@Transactional
	public void removeStudentFromGroup(StudentDTO studentDTO) throws DataAreNotUpdatedException, DataNotFoundException {
		LOGGER.trace("Going to remove a studentDTO(firstName={}, lastName={}) from group", studentDTO.getFirstName(),
				studentDTO.getLastName());
		try {
			studentRepository.removeStudentFromGroups(convertToEntity(studentDTO).getId());
		} catch (DataAccessException e) {
			LOGGER.info("Can't remove a studentDTO(firstName={}, lastName={}) from group.", studentDTO.getFirstName(),
					studentDTO.getLastName());
			throw new DataAreNotUpdatedException(
					String.format("Can't remove a studentDTO(firstName=%s, lastName=%s) from group.",
							studentDTO.getFirstName(), studentDTO.getLastName()));
		}
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
		LOGGER.debug("Going to convert StudentDTO to entity");
		Student entity = modelMapper.map(studentDTO, Student.class);
		LOGGER.debug("StudentDTO converted successfully.");
		return entity;
	}

	private Student convertToEntity(StudentDTO oldDTO, StudentDTO newDTO) throws DataNotFoundException {
		LOGGER.trace("Going to convert newDTO(firstName={}, lastName={}) to entity", newDTO.getFirstName(),
				newDTO.getLastName());
		Student entity = convertToEntity(newDTO);
		LOGGER.trace("DTO was converted successfully.");
		LOGGER.trace("Going to set ID of oldDTO to newDTO");
		Student oldEntity = convertToEntity(oldDTO);
		try {
			entity.setId(studentRepository.findByFirstNameAndLastName(oldEntity.getFirstName(), oldEntity.getLastName())
					.getId());
		} catch (DataAccessException e) {
			LOGGER.info("Can't find a student oldEntity (firstName={}, lastName={}) while updating StudentDto.",
					oldEntity.getFirstName(), oldEntity.getLastName());
			throw new DataNotFoundException(String.format(
					"Can't find a student oldEntity (firstName=%s, lastName=%s) while updating StudentDto.",
					oldEntity.getFirstName(), oldEntity.getLastName()));
		}

		LOGGER.trace("ID of oldDTO was set to newDTO successfully");
		return entity;
	}

	Converter<Student, StudentDTO> entityToDTO = new Converter<Student, StudentDTO>() {
		@Override
		public StudentDTO convert(MappingContext<Student, StudentDTO> context) throws DataNotFoundException {
			StudentDTO studentDTO = new StudentDTO();
			studentDTO.setFirstName(context.getSource().getFirstName());
			studentDTO.setLastName(context.getSource().getLastName());
			if (context.getSource().getGroup() != null) {
				try {
					studentDTO.setGroupDTO(groupService.retrieve(context.getSource().getGroup().getName()));
				} catch (DataAccessException e) {
					LOGGER.info("Can't find a groupDTO with name {} while converting Student entity to DTO.",
							context.getSource().getGroup().getName());
					throw new DataNotFoundException(
							String.format("Can't find a groupDTO with name %s while converting Student entity to DTO.",
									context.getSource().getGroup().getName()));
				}
			}
			return studentDTO;
		}
	};

	Converter<StudentDTO, Student> dtoToEntity = new Converter<StudentDTO, Student>() {
		@Override
		public Student convert(MappingContext<StudentDTO, Student> context) throws DataNotFoundException {
			StudentDTO studentDTO = context.getSource();
			Student retrievedEntity = studentRepository.findByFirstNameAndLastName(studentDTO.getFirstName(),
					studentDTO.getLastName());
			if (retrievedEntity != null) {
				return retrievedEntity;
			} else {
				Student student = new Student();
				student.setFirstName(studentDTO.getFirstName());
				student.setLastName(studentDTO.getLastName());
				if (studentDTO.getGroupDTO() != null) {
					try {
						student.setGroup(groupRepository.findByName(studentDTO.getGroupDTO().getName()));
					} catch (DataAccessException e) {
						LOGGER.info("Can't find a group with name {} while converting StudentDto to entity.",
								studentDTO.getGroupDTO().getName());
						throw new DataNotFoundException(
								String.format("Can't find a group with name %s while converting StudentDto to entity.",
										studentDTO.getGroupDTO().getName()));
					}
				}
				return student;
			}
		}
	};

}
