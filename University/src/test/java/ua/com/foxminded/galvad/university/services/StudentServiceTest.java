package ua.com.foxminded.galvad.university.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
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
	void testCreateWithDataAccessException_shouldThrowDataAreNotUpdatedException() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		when(mockStudentRepository.save(studentEntity)).thenThrow(new DataAccessResourceFailureException("error"));
		DataAreNotUpdatedException exception = assertThrows(DataAreNotUpdatedException.class,
				() -> studentService.create(studentDTO));
		assertEquals("Student with firstName=FirstName and lastName=LastName wasn't added to DB.",
				exception.getErrorMessage());
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
	void testRetrieveWithDataAccessException_shouldThrowDataNotFoundException() {
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
				.thenThrow(new DataAccessResourceFailureException("error"));
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
				() -> studentService.retrieve("FirstName", "LastName"));
		verify(mockStudentRepository, times(1)).findByFirstNameAndLastName(FIRST_NAME, LAST_NAME);
		assertEquals("Can't retrieve StudentDTO, firstName=FirstName, lastName=LastName", exception.getErrorMessage());
	}

	@Test
	void testRetrieveWithNullStudent_shouldThrowDataNotFoundException() {
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenReturn(null);
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
				() -> studentService.retrieve("FirstName", "LastName"));
		assertEquals("A student (firstName=FirstName, lastName=LastName) is not found.", exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).findByFirstNameAndLastName(FIRST_NAME, LAST_NAME);
	}

	@Test
	void testRetrieveById() {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(FIRST_NAME);
		studentDTO.setLastName(LAST_NAME);
		studentDTO.setId(1);
		Optional<Student> studentOptional = Optional.of(new Student(1, FIRST_NAME, LAST_NAME));
		when(mockStudentRepository.findById(1)).thenReturn(studentOptional);
		when(mockModelMapper.map(studentOptional.get(), StudentDTO.class)).thenReturn(studentDTO);
		studentService.retrieve(1);
		verify(mockStudentRepository, times(1)).findById(1);
	}

	@Test
	void testRetrieveByIdWithDataAccessException_shouldThrowDataNotFoundException() {
		when(mockStudentRepository.findById(1)).thenThrow(new DataAccessResourceFailureException("error"));
		DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> studentService.retrieve(1));
		assertEquals("Can't retrieve StudentDTO, id=1", exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).findById(1);
	}

	@Test
	void testRetrieveByIdWithNullResult_shouldThrowDataNotFoundException() {
		when(mockStudentRepository.findById(1)).thenReturn(Optional.empty());
		DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> studentService.retrieve(1));
		assertEquals("A student (id=1) is not found.", exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).findById(1);
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
	void testUpdateWithDataAccessException_shouldThrowDataAreNotUpdatedException() {
		StudentDTO oldDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student oldEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		StudentDTO newDTO = createStudentDTO(FIRST_NAME_B, LAST_NAME_B, GROUP_NAME);
		Student newEntity = createStudentEntity(1, FIRST_NAME_B, LAST_NAME_B, 1, GROUP_NAME);
		when(mockModelMapper.map(newDTO, Student.class)).thenReturn(newEntity);
		when(mockModelMapper.map(oldDTO, Student.class)).thenReturn(oldEntity);
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenReturn(oldEntity);
		when(mockStudentRepository.save(newEntity)).thenThrow(new DataAccessResourceFailureException("error"));
		DataAreNotUpdatedException exception = assertThrows(DataAreNotUpdatedException.class,
				() -> studentService.update(oldDTO, newDTO));
		assertEquals("Can't update a student (firstName=FirstName, lastName=LastName)", exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).save(any(Student.class));
	}

	@Test
	void testUpdateWithDataAccessExceptionForConvertToEntity_shouldThrowDataNotFoundException() {
		StudentDTO oldDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student oldEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		StudentDTO newDTO = createStudentDTO(FIRST_NAME_B, LAST_NAME_B, GROUP_NAME);
		Student newEntity = createStudentEntity(1, FIRST_NAME_B, LAST_NAME_B, 1, GROUP_NAME);
		when(mockModelMapper.map(newDTO, Student.class)).thenReturn(newEntity);
		when(mockModelMapper.map(oldDTO, Student.class)).thenReturn(oldEntity);
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME))
				.thenThrow(new DataAccessResourceFailureException("error"));
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
				() -> studentService.update(oldDTO, newDTO));
		assertEquals(
				"Can't find a student oldEntity (firstName=FirstName, lastName=LastName) while updating StudentDto.",
				exception.getErrorMessage());
		verify(mockStudentRepository, times(0)).save(any(Student.class));
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
	void testDeleteWithDataIntegrityViolationException_shouldThrowDataAreNotUpdatedException() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		doThrow(new DataIntegrityViolationException("error")).when(mockStudentRepository).delete(studentEntity);
		DataAreNotUpdatedException exception = assertThrows(DataAreNotUpdatedException.class,
				() -> studentService.delete(studentDTO));
		assertEquals("Can't delete the student \"FirstName LastName\" as he or she is assigned to the group \"G1\".",
				exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).delete(studentEntity);
	}

	@Test
	void testDeleteWithDataAccessException_shouldThrowDataAreNotUpdatedException() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		doThrow(new DataAccessResourceFailureException("error")).when(mockStudentRepository).delete(studentEntity);
		DataAreNotUpdatedException exception = assertThrows(DataAreNotUpdatedException.class,
				() -> studentService.delete(studentDTO));
		assertEquals("Can't delete the student (firstName=FirstName, lastName=LastName)", exception.getErrorMessage());
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
	void testFindAllWithDataAccessException_shouldThrowDataNotFoundException() {
		when(mockStudentRepository.findAll()).thenThrow(new DataAccessResourceFailureException("error"));
		DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> studentService.findAll());
		assertEquals("Can't retrieve a list of students.", exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).findAll();
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
	void testFindAllUnassignedStudentsWithDataAccessException_shouldThrowDataNotFoundException() {
		when(mockStudentRepository.findAll()).thenThrow(new DataAccessResourceFailureException("error"));
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
				() -> studentService.findAllUnassignedStudents());
		assertEquals("Can't build a list of unassigned students.", exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).findAll();
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
	void testBuildStudentGroupMapWithDataAccessException_shouldThrowDataNotFoundException() {
		when(mockStudentRepository.findAll()).thenThrow(new DataAccessResourceFailureException("error"));
		DataNotFoundException exception = assertThrows(DataNotFoundException.class,
				() -> studentService.buildStudentGroupMap());
		assertEquals("Can't build a map (StudentDTO,GroupName).", exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).findAll();
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
	void testAddToGroupWithDataAccessException_shouldThrowDataAreNotUpdatedException() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		GroupDTO newGroupDTO = new GroupDTO();
		newGroupDTO.setName(GROUP_NAME2);
		Group newGroupEntity = new Group(2, GROUP_NAME2);

		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		when(mockGroupService.convertToEntity(newGroupDTO)).thenReturn(newGroupEntity);
		doThrow(new DataAccessResourceFailureException("error")).when(mockStudentRepository)
				.addStudentToGroup(studentEntity.getId(), newGroupEntity);
		DataAreNotUpdatedException exception = assertThrows(DataAreNotUpdatedException.class,
				() -> studentService.addToGroup(studentDTO, newGroupDTO));
		assertEquals("Can't assign StudentDTO (firstName=FirstName, lastName=LastName) to a groupDTO with name=G2",
				exception.getErrorMessage());
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

	@Test
	void testRemoveStudentFromGroupWithDataAccessException_shouldThrowDataAreNotUpdatedException() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockModelMapper.map(studentDTO, Student.class)).thenReturn(studentEntity);
		doThrow(new DataAccessResourceFailureException("error")).when(mockStudentRepository)
				.removeStudentFromGroups(studentEntity.getId());
		DataAreNotUpdatedException exception = assertThrows(DataAreNotUpdatedException.class,
				() -> studentService.removeStudentFromGroup(studentDTO));
		assertEquals("Can't remove a studentDTO(firstName=FirstName, lastName=LastName) from group.",
				exception.getErrorMessage());
		verify(mockStudentRepository, times(1)).removeStudentFromGroups(studentEntity.getId());
	}

	@Test
	void testCheckIfExistWithExistentEntity_shouldReturnTrue() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		Student studentEntity = createStudentEntity(1, FIRST_NAME, LAST_NAME, 1, GROUP_NAME);
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenReturn(studentEntity);
		when(mockModelMapper.map(studentEntity, StudentDTO.class)).thenReturn(studentDTO);
		assertTrue(studentService.checkIfExists(studentDTO));
	}

	@Test
	void testCheckIfExistWithNewEntity_shouldReturnTrue() {
		StudentDTO studentDTO = createStudentDTO(FIRST_NAME, LAST_NAME, GROUP_NAME);
		when(mockStudentRepository.findByFirstNameAndLastName(FIRST_NAME, LAST_NAME)).thenReturn(null);
		assertFalse(studentService.checkIfExists(studentDTO));
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
