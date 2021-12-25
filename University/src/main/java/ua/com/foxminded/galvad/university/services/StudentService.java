package ua.com.foxminded.galvad.university.services;

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

	public void create(StudentDTO studentDTO) throws DataAreNotUpdatedException {
		studentRepository.save(convertToEntity(studentDTO));
	}

	public StudentDTO retrieve(String firstName, String lastName) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to retrieve StudentDTO, firstName={}, lastName={}", firstName, lastName);
		LOGGER.trace("Going to retrieve Student entity, firstName={}, lastName={}", firstName, lastName);
		Student student = studentRepository.findByFirstNameAndLastName(firstName, lastName);
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
		studentRepository.save(convertToEntity(oldDTO, newDTO));
		LOGGER.trace("StudentDTO was updated successfully.");
	}

	@Transactional
	public void addToGroup(StudentDTO studentDTO, GroupDTO groupDTO) throws DataAreNotUpdatedException {
		LOGGER.trace("Going to assign StudentDTO (firstName={}, lastName={}) to a groupDTO with name={}",
				studentDTO.getFirstName(), studentDTO.getLastName(), groupDTO.getName());
		studentRepository.addStudentToGroup(convertToEntity(studentDTO).getId(),
				groupService.convertToEntity(groupDTO));
		LOGGER.trace("StudentDTO was assigned successfully.");
	}

	public void delete(StudentDTO studentDTO) throws DataAreNotUpdatedException {
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
		}
	}

	public List<StudentDTO> findAll() throws DataNotFoundException {
		LOGGER.trace("Going to get list of ALL StudentDTO from DB");
		List<StudentDTO> list = studentRepository.findAll().stream().map(this::convertToDTO)
				.collect(Collectors.toList());
		LOGGER.trace("List of ALL StudentDTO retrieved from DB, {} were found", list.size());
		return list;
	}

	public Map<StudentDTO, String> buildStudentGroupMap() throws DataNotFoundException {
		LOGGER.trace("Going to get a map (StudentDTO,GroupName)");
		Map<StudentDTO, String> studentGroupMap = new LinkedHashMap<>();
		studentRepository.findAll().stream().forEach(student -> {
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
		Set<StudentDTO> setOfUnassignedStudents = studentRepository.findAll().stream()
				.filter(s -> (s.getGroup() == null)).map(this::convertToDTO).collect(Collectors.toSet());
		LOGGER.trace("A list with all unassigned students is prepared.");
		return setOfUnassignedStudents;
	}

	@Transactional
	public void removeStudentFromGroup(StudentDTO studentDTO) throws DataNotFoundException {
		LOGGER.trace("Going to remove a studentDTO(firstName={}, lastName={}) from group", studentDTO.getFirstName(),
				studentDTO.getLastName());
		studentRepository.removeStudentFromGroups(convertToEntity(studentDTO).getId());
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
		entity.setId(studentRepository.findByFirstNameAndLastName(oldEntity.getFirstName(), oldEntity.getLastName())
				.getId());
		LOGGER.trace("ID of oldDTO was set to newDTO successfully");
		return entity;
	}

	Converter<Student, StudentDTO> entityToDTO = new Converter<Student, StudentDTO>() {
		@Override
		public StudentDTO convert(MappingContext<Student, StudentDTO> context) {
			StudentDTO studentDTO = new StudentDTO();
			studentDTO.setFirstName(context.getSource().getFirstName());
			studentDTO.setLastName(context.getSource().getLastName());
			if (context.getSource().getGroup() != null) {
				studentDTO.setGroupDTO(groupService.retrieve(context.getSource().getGroup().getName()));
			}
			return studentDTO;
		}
	};

	Converter<StudentDTO, Student> dtoToEntity = new Converter<StudentDTO, Student>() {
		@Override
		public Student convert(MappingContext<StudentDTO, Student> context) {
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
					student.setGroup(groupRepository.findByName(studentDTO.getGroupDTO().getName()));
				}
				return student;
			}
		}
	};

}
