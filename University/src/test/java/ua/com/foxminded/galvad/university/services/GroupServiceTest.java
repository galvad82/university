package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.repository.GroupRepository;
import ua.com.foxminded.galvad.university.repository.LessonRepository;
import ua.com.foxminded.galvad.university.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

	private static final String GROUP_NAME = "TestName";
	@Mock
	private GroupRepository mockGroupRepository;
	@Mock
	private StudentRepository mockStudentRepository;
	@Mock
	private LessonRepository mockLessonRepository;
	@Mock
	private ModelMapper mockModelMapper;
	@InjectMocks
	private GroupService groupService;

	@Test
	void testConvertionOfNullShouldReturnNull() {
		assertNull(groupService.convertToEntity(null));
	}

	@Test
	void testCreate() {
		GroupDTO groupDTO = createDTO(GROUP_NAME);
		Group groupEntity = createEntity(1, GROUP_NAME);
		when(mockModelMapper.map(groupDTO, Group.class)).thenReturn(groupEntity);
		when(mockGroupRepository.save(groupEntity)).thenReturn(groupEntity);
		when(mockModelMapper.map(groupEntity, GroupDTO.class)).thenReturn(groupDTO);
		groupService.create(groupDTO);
		verify(mockGroupRepository, times(1)).save(groupEntity);
	}

	@Test
	void testRetrieve() {
		Group groupEntity = createEntity(1, GROUP_NAME);
		GroupDTO groupDTO = createDTO(GROUP_NAME);
		when(mockGroupRepository.findByName(GROUP_NAME)).thenReturn(groupEntity);
		when(mockModelMapper.map(groupEntity, GroupDTO.class)).thenReturn(groupDTO);
		groupService.retrieve("TestName");
		verify(mockGroupRepository, times(1)).findByName(GROUP_NAME);
	}

	@Test
	void testUpdate() {
		GroupDTO oldDTO = createDTO(GROUP_NAME);
		Group oldEntity = createEntity(1, GROUP_NAME);
		GroupDTO newDTO = createDTO("NEWNAME");
		Group newEntity = createEntity(1, "NEWNAME");
		Student student = newEntity.getSetOfStudent().stream().findFirst().get();
		when(mockModelMapper.map(oldDTO, Group.class)).thenReturn(oldEntity);
		when(mockModelMapper.map(newDTO, Group.class)).thenReturn(newEntity);
		when(mockGroupRepository.findByName(GROUP_NAME)).thenReturn(oldEntity);
		when(mockStudentRepository.findByFirstNameAndLastName(student.getFirstName(), student.getLastName()))
				.thenReturn(student);
		when(mockGroupRepository.save(newEntity)).thenReturn(newEntity);
		when(mockModelMapper.map(newEntity, GroupDTO.class)).thenReturn(newDTO);
		groupService.update(oldDTO, newDTO);
		verify(mockGroupRepository, times(1)).save(any(Group.class));
	}

	@Test
	void testRemoveStudentsFromGroup() {
		Group groupEntity = createEntity(1, GROUP_NAME);
		GroupDTO groupDTO = createDTO(GROUP_NAME);
		when(mockModelMapper.map(groupDTO, Group.class)).thenReturn(groupEntity);
		when(mockModelMapper.map(groupEntity, GroupDTO.class)).thenReturn(groupDTO);
		groupService.removeStudentsFromGroup(groupDTO);
		verify(mockStudentRepository, times(1)).removeStudentFromGroups(anyInt());
	}

	@Test
	void testDelete() {
		Group groupEntity = createEntity(1, GROUP_NAME);
		GroupDTO groupDTO = createDTO(GROUP_NAME);
		when(mockModelMapper.map(groupDTO, Group.class)).thenReturn(groupEntity);
		groupService.delete(groupDTO);
		verify(mockGroupRepository, times(1)).delete(groupEntity);
	}

	@Test
	void testFindAll() {
		GroupDTO groupDTO = createDTO(GROUP_NAME);
		GroupDTO groupDTO1 = createDTO("TestName1");
		GroupDTO groupDTO2 = createDTO("TestName2");
		List<GroupDTO> expectedList = new ArrayList<>();
		expectedList.add(groupDTO);
		expectedList.add(groupDTO1);
		expectedList.add(groupDTO2);

		Group group = createEntity(1, GROUP_NAME);
		Group group1 = createEntity(2, "TestName1");
		Group group2 = createEntity(3, "TestName2");
		List<Group> listOfGroups = new ArrayList<>();
		listOfGroups.add(group);
		listOfGroups.add(group1);
		listOfGroups.add(group2);
		when(mockGroupRepository.findAll()).thenReturn(listOfGroups);
		when(mockModelMapper.map(group, GroupDTO.class)).thenReturn(groupDTO);
		when(mockModelMapper.map(group1, GroupDTO.class)).thenReturn(groupDTO1);
		when(mockModelMapper.map(group2, GroupDTO.class)).thenReturn(groupDTO2);
		List<GroupDTO> retrievedList = groupService.findAll();
		verify(mockGroupRepository, times(1)).findAll();
		assertEquals(retrievedList, expectedList);
	}

	private Group createEntity(Integer id, String name) {
		Group group = new Group(id, name);
		Student student = new Student(1, "FirstName", "LastName");
		Set<Student> setOfStudents = new HashSet<>();
		setOfStudents.add(student);
		group.setSetOfStudent(setOfStudents);
		return group;
	}

	private GroupDTO createDTO(String name) {
		GroupDTO group = new GroupDTO();
		group.setName(name);
		StudentDTO student = new StudentDTO();
		student.setFirstName("FirstName");
		student.setLastName("LastName");
		List<StudentDTO> listOfStudents = new ArrayList<>();
		listOfStudents.add(student);
		group.setListOfStudent(listOfStudents);
		return group;
	}
}
