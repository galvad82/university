package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.repository.GroupRepository;
import ua.com.foxminded.galvad.university.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

	private static final String FIRST_NAME = "FirstName";
	private static final String LAST_NAME = "LastName";
	private static final String FIRST_NAME_B = "FirstNameB";
	private static final String LAST_NAME_B = "LastNameB";
	private static final String FIRST_NAME_C = "FirstNameC";
	private static final String LAST_NAME_C = "LastNameC";
	private static final String GROUP_NAME = "G1";
	private static final String GROUP_NAME2 = "G2";
	private static final String GROUP_NAME3 = "G2";
	private static final String NONE = "NONE";

	@Mock
	private ModelMapper mockModelMapper;

	@Mock
	private StudentRepository mockStudentRepository;

	@Mock
	private GroupRepository mockGroupRepository;

	@Mock
	private GroupService mockGroupService;

	@InjectMocks
	private StudentService studentService;

	@Test
	void testCreate() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		when(mockStudentRepository.save(studentEntity)).thenReturn(studentEntity);
		when(mockModelMapper.map(studentEntity, StudentDTO.class)).thenReturn(studentDTO);
		studentService.create(studentDTO);
		verify(mockStudentRepository, times(1)).save(any(Student.class));
	}

	@Test
	void testRetrieve() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenReturn(studentEntity);
		when(mockModelMapper.map(studentEntity, StudentDTO.class)).thenReturn(studentDTO);
		studentService.retrieve("FirstName", "LastName");
		verify(mockStudentRepository, times(1)).findByFirstNameAndLastName(FIRST_NAME, LAST_NAME);
	}

	@Test
	void testUpdate() {
		StudentDTO oldDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student oldEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		StudentDTO newDTO = createStudentDTO(FIRST_NAME_B, LAST_NAME_B, GROUP_NAME);
		Student newEntity = createStudentEntity(1, FIRST_NAME_B, LAST_NAME_B, 1, GROUP_NAME);
		when(mockModelMapper.map(newDTO, Student.class)).thenReturn(newEntity);
		when(mockModelMapper.map(oldDTO, Student.class)).thenReturn(oldEntity);
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenReturn(oldEntity);
		when(mockStudentRepository.save(newEntity)).thenReturn(newEntity);
		when(mockModelMapper.map(newEntity, StudentDTO.class)).thenReturn(newDTO);
		studentService.update(oldDTO, newDTO);
		verify(mockStudentRepository, times(1)).save(any(Student.class));
	}

	@Test
	void testDelete() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		studentService.delete(studentDTO);
		verify(mockStudentRepository, times(1)).delete(studentEntity);
	}

	@Test
	void testFindAll() {
		StudentDTO DTO1 = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		StudentDTO DTO2 = createStudentDTO(FIRST_NAME_B, LAST_NAME_B, "Group2");
		StudentDTO DTO3 = createStudentDTO(FIRST_NAME_C, LAST_NAME_C, "Group3");
		List<StudentDTO> expectedListDTO = new ArrayList<>();
		expectedListDTO.add(DTO1);
		expectedListDTO.add(DTO2);
		expectedListDTO.add(DTO3);
		Student entity1 = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		Student entity2 = createStudentEntity(2, FIRST_NAME_B, LAST_NAME_B, 2, GROUP_NAME2);
		Student entity3 = createStudentEntity(3, FIRST_NAME_C, LAST_NAME_C, 3, GROUP_NAME3);
		List<Student> listOfStudents = new ArrayList<>();
		listOfStudents.add(entity1);
		listOfStudents.add(entity2);
		listOfStudents.add(entity3);
		when(mockStudentRepository.findAll()).thenReturn(listOfStudents);
		when(mockModelMapper.map(entity1, StudentDTO.class)).thenReturn(DTO1);
		when(mockModelMapper.map(entity2, StudentDTO.class)).thenReturn(DTO2);
		when(mockModelMapper.map(entity3, StudentDTO.class)).thenReturn(DTO3);
		List<StudentDTO> retrievedList = studentService.findAll();
		assertEquals(retrievedList, expectedListDTO);
	}

	@Test
	void testFindAllUnassignedStudents() {
		StudentDTO DTO1 = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		StudentDTO DTO2 = createStudentDTO(FIRST_NAME_B, LAST_NAME_B, GROUP_NAME2);
		StudentDTO DTO3 = createStudentDTO(FIRST_NAME_C, LAST_NAME_C, GROUP_NAME3);
		List<StudentDTO> fullListDTO = new ArrayList<>();
		fullListDTO.add(DTO1);
		fullListDTO.add(DTO2);
		fullListDTO.add(DTO3);
		Set<StudentDTO> setOfUnassignedDTO = new HashSet<>();
		DTO1.setGroupDTO(null);
		DTO3.setGroupDTO(null);
		setOfUnassignedDTO.add(DTO1);
		setOfUnassignedDTO.add(DTO3);

		Student entity1 = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		Student entity2 = createStudentEntity(2, FIRST_NAME_B, LAST_NAME_B, 2, GROUP_NAME2);
		Student entity3 = createStudentEntity(3, FIRST_NAME_C, LAST_NAME_C, 3, GROUP_NAME3);
		entity1.setGroup(null);
		entity3.setGroup(null);
		List<Student> listOfEntities = new ArrayList<>();
		listOfEntities.add(entity1);
		listOfEntities.add(entity2);
		listOfEntities.add(entity3);
		when(mockStudentRepository.findAll()).thenReturn(listOfEntities);
		when(mockModelMapper.map(entity1, StudentDTO.class)).thenReturn(DTO1);
		when(mockModelMapper.map(entity3, StudentDTO.class)).thenReturn(DTO3);

		Set<StudentDTO> retrievedSet = studentService.findAllUnassignedStudents();
		verify(mockStudentRepository, times(1)).findAll();
		assertEquals(retrievedSet, setOfUnassignedDTO);
	}

	@Test
	void testBuildStudentGroupMap() {
		StudentDTO DTO1 = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		StudentDTO DTO2 = createStudentDTO(FIRST_NAME_B, LAST_NAME_B, GROUP_NAME2);
		StudentDTO DTO3 = createStudentDTO(FIRST_NAME_C, LAST_NAME_C, GROUP_NAME3);
		DTO1.setGroupDTO(null);
		DTO3.setGroupDTO(null);
		Map<StudentDTO, String> expectedStudentGroupMap = new LinkedHashMap<>();
		expectedStudentGroupMap.put(DTO1, NONE);
		expectedStudentGroupMap.put(DTO2, DTO2.getGroupDTO().getName());
		expectedStudentGroupMap.put(DTO3, NONE);

		Student entity1 = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		Student entity2 = createStudentEntity(2, FIRST_NAME_B, LAST_NAME_B, 2, GROUP_NAME2);
		Student entity3 = createStudentEntity(3, FIRST_NAME_C, LAST_NAME_C, 3, GROUP_NAME3);
		entity1.setGroup(null);
		entity3.setGroup(null);
		List<Student> listOfEntities = new ArrayList<>();
		listOfEntities.add(entity1);
		listOfEntities.add(entity2);
		listOfEntities.add(entity3);
		when(mockStudentRepository.findAll()).thenReturn(listOfEntities);
		when(mockModelMapper.map(entity1, StudentDTO.class)).thenReturn(DTO1);
		when(mockModelMapper.map(entity2, StudentDTO.class)).thenReturn(DTO2);
		when(mockModelMapper.map(entity3, StudentDTO.class)).thenReturn(DTO3);
		Map<StudentDTO, String> retrievedStudentGroupMap = studentService.buildStudentGroupMap();
		verify(mockStudentRepository, times(1)).findAll();
		assertEquals(3, retrievedStudentGroupMap.size());
		assertEquals(NONE, retrievedStudentGroupMap.get(DTO1));
		assertEquals(GROUP_NAME2, retrievedStudentGroupMap.get(DTO2));
		assertEquals(NONE, retrievedStudentGroupMap.get(DTO3));
	}

	@Test
	void testAddToGroup() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		GroupDTO newGroupDTO = new GroupDTO();
		newGroupDTO.setName(GROUP_NAME2);
		Group newGroupEntity = new Group(2, GROUP_NAME2);

		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		when(mockGroupService.convertToEntity(newGroupDTO)).thenReturn(newGroupEntity);
		studentService.addToGroup(studentDTO, newGroupDTO);
		verify(mockStudentRepository, times(1)).addStudentToGroup(studentEntity.getId(), newGroupEntity);
	}

	@Test
	void testRemoveStudentFromGroup() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		studentService.removeStudentFromGroup(studentDTO);
		verify(mockStudentRepository, times(1)).removeStudentFromGroups(studentEntity.getId());
	}

	private Student createStudentEntity(Integer studentID, String firstName, String lastName, Integer groupID,
			String groupName) {
		Group group = new Group(groupID, groupName);
		Student student = new Student(studentID, firstName, lastName);
		student.setGroup(group);
		return student;
	}

	private StudentDTO createStudentDTO(String firstName, String lastName, String groupName) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(groupName);
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(firstName);
		studentDTO.setLastName(lastName);
		studentDTO.setGroupDTO(groupDTO);
		return studentDTO;
	}

}
