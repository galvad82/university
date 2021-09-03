package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.StudentService;

@ExtendWith(MockitoExtension.class)
class GroupsControllerTest {

	@Mock
	GroupService groupServiceMock;

	@Mock
	StudentService studentServiceMock;

	@InjectMocks
	GroupsController groupsControllerUnderTest;

	@Mock
	CustomExceptionHandler customExceptionHandlerMock;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(groupsControllerUnderTest)
				.setControllerAdvice(customExceptionHandlerMock).build();
	}

	@Test
	void testListView() throws Exception {
		List<GroupDTO> expectedList = new ArrayList<>();
		GroupDTO firstGroupDTO = new GroupDTO();
		firstGroupDTO.setName("AB-101");
		GroupDTO secondGroupDTO = new GroupDTO();
		secondGroupDTO.setName("AB-201");
		expectedList.add(firstGroupDTO);
		expectedList.add(secondGroupDTO);

		when(groupServiceMock.findAll()).thenReturn(expectedList);
		mockMvc.perform(get("/groups")).andExpect(status().isOk()).andExpect(view().name("groups/list"))
				.andExpect(model().attribute("groups", expectedList));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(groupServiceMock.findAll()).thenThrow(expectedException);
		mockMvc.perform(get("/groups"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()))
				.andExpect(result -> assertEquals(expectedException.getException(), result.getResolvedException().getCause()));
	}

	@Test
	void testAddViewGet() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("");
		Set<StudentDTO> listOfStudents = new HashSet<>();
		when(studentServiceMock.findAllUnassignedStudents()).thenReturn(listOfStudents);
		mockMvc.perform(get("/groups/add")).andExpect(view().name("groups/add"))
				.andExpect(matchAll(model().attribute("groupDTO", groupDTO)));
	}

	@Test
	void testAddViewPost() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("TEST");
		Boolean showTable = true;
		mockMvc.perform(post("/groups/add").param("name", "TEST"))
				.andExpect(matchAll(model().attribute("group", groupDTO)))
				.andExpect(matchAll(model().attribute("showTable", showTable)))
				.andExpect(matchAll(model().attribute("result", "A group was successfully added.")))
				.andExpect(view().name("groups/result"));
	}
	
	@Test
	void testAddViewPost_WithEmptyStudentsFirstName() throws Exception {
		List<StudentDTO> initialListOfStudents = new ArrayList<>();
		StudentDTO firstStudentDTO = new StudentDTO();
		firstStudentDTO.setFirstName("");
		firstStudentDTO.setLastName("LastName");
		StudentDTO secondStudentDTO = new StudentDTO();
		secondStudentDTO.setFirstName("FirstName");
		secondStudentDTO.setLastName("LastName");
		initialListOfStudents.add(firstStudentDTO);
		initialListOfStudents.add(secondStudentDTO);
		GroupDTO initialGroupDTO = new GroupDTO();
		initialGroupDTO.setName("Name");
		initialGroupDTO.setListOfStudent(initialListOfStudents);
	
		List<StudentDTO> listOfStudentsWithoutEmpty = new ArrayList<>();
		listOfStudentsWithoutEmpty.add(secondStudentDTO);
		GroupDTO expectedGroupDTO = new GroupDTO();
		expectedGroupDTO.setName(initialGroupDTO.getName());
		expectedGroupDTO.setListOfStudent(listOfStudentsWithoutEmpty);
		
		RequestBuilder request = post("/groups/add").flashAttr("groupDTO", initialGroupDTO);
		Boolean showTable = true;
		
		mockMvc.perform(request)
				.andExpect(matchAll(model().attribute("group", expectedGroupDTO)))
				.andExpect(matchAll(model().attribute("showTable", showTable)))
				.andExpect(matchAll(model().attribute("result", "A group was successfully added.")))
				.andExpect(view().name("groups/result"));
	}
	

	@Test
	void testEditViewPost() throws Exception {
		mockMvc.perform(post("/groups/edit").param("name", "TEST"))
				.andExpect(matchAll(model().attribute("name", "TEST"))).andExpect(view().name("groups/edit"));
	}

	@Test
	void testEditResultViewPost() throws Exception {

		List<StudentDTO> listOfStudents = new ArrayList<>();
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("FirstName");
		studentDTO.setLastName("LastName");
		listOfStudents.add(studentDTO);

		GroupDTO initialGroupDTO = new GroupDTO();
		initialGroupDTO.setName("OldName");
		initialGroupDTO.setListOfStudent(listOfStudents);

		GroupDTO expectedGroupDTO = new GroupDTO();
		expectedGroupDTO.setName("NewName");
		expectedGroupDTO.setListOfStudent(listOfStudents);

		when(groupServiceMock.retrieve(initialGroupDTO.getName())).thenReturn(initialGroupDTO);
		RequestBuilder request = post("/groups/edit/result").flashAttr("groupDTO", expectedGroupDTO)
				.flashAttr("initialName", initialGroupDTO.getName());
		mockMvc.perform(request).andExpect(matchAll(model().attribute("result", "Group was successfully updated")))
				.andExpect(matchAll(model().attribute("showTable", true)))
				.andExpect(matchAll(model().attribute("group", expectedGroupDTO)))
				.andExpect(view().name("groups/result"));
	}
	
	@Test
	void testEditResultViewPost_WithEmptyStudentsFirstName() throws Exception {
		List<StudentDTO> initialListOfStudents = new ArrayList<>();
		StudentDTO firstStudentDTO = new StudentDTO();
		firstStudentDTO.setFirstName("FirstName");
		firstStudentDTO.setLastName("LastName");
		StudentDTO secondStudentDTO = new StudentDTO();
		secondStudentDTO.setFirstName("FirstName");
		secondStudentDTO.setLastName("LastName");
		initialListOfStudents.add(firstStudentDTO);
		initialListOfStudents.add(secondStudentDTO);
		GroupDTO initialGroupDTO = new GroupDTO();
		initialGroupDTO.setName("OldName");
		initialGroupDTO.setListOfStudent(initialListOfStudents);
		
		firstStudentDTO.setFirstName("");
		GroupDTO newGroupDTO = new GroupDTO();
		newGroupDTO.setName("NewName");
		newGroupDTO.setListOfStudent(initialListOfStudents);
		
		List<StudentDTO> listOfStudentsWithoutEmpty = new ArrayList<>();
		listOfStudentsWithoutEmpty.add(secondStudentDTO);
		GroupDTO expectedGroupDTO = new GroupDTO();
		expectedGroupDTO.setName("NewName");
		expectedGroupDTO.setListOfStudent(listOfStudentsWithoutEmpty);

		when(groupServiceMock.retrieve(initialGroupDTO.getName())).thenReturn(initialGroupDTO);
		RequestBuilder request = post("/groups/edit/result").flashAttr("groupDTO", newGroupDTO)
				.flashAttr("initialName", initialGroupDTO.getName());
		mockMvc.perform(request).andExpect(matchAll(model().attribute("result", "Group was successfully updated")))
				.andExpect(matchAll(model().attribute("showTable", true)))
				.andExpect(matchAll(model().attribute("group", expectedGroupDTO)))
				.andExpect(view().name("groups/result"));
	}

	@Test
	void testDeleteViewPost() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		when(groupServiceMock.retrieve("AB-123")).thenReturn(groupDTO);
		mockMvc.perform(post("/groups/delete").param("name", "AB-123"))
				.andExpect(matchAll(model().attribute("group", groupDTO))).andExpect(view().name("groups/delete"));
	}

	@Test
	void testDeleteViewPost_withDataNotFoundException() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("TEST");
		List<StudentDTO> listOfStudent = new ArrayList<>();
		StudentDTO emptyDto = new StudentDTO();
		emptyDto.setFirstName("NONE");
		emptyDto.setLastName("");
		listOfStudent.add(emptyDto);
		groupDTO.setListOfStudent(listOfStudent);

		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(groupServiceMock.retrieve("TEST")).thenThrow(expectedException);
		mockMvc.perform(post("/groups/delete").param("name", "TEST"))
				.andExpect(matchAll(model().attribute("group", groupDTO))).andExpect(view().name("groups/delete"));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		mockMvc.perform(post("/groups/delete/result").param("name", "name"))
				.andExpect(matchAll(model().attribute("result", "The group name was successfully deleted.")))
				.andExpect(view().name("groups/result"));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/groups/wrong")).andExpect(status().is4xxClientError());
	}

}
