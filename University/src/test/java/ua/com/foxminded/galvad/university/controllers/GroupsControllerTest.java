package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.GroupService;
import ua.com.foxminded.galvad.university.services.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
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
				.andExpect(result -> assertEquals(expectedException.getException(),
						result.getResolvedException().getCause()));
	}

	@Test
	void testAddViewGet() throws Exception {
		when(studentServiceMock.findAllUnassignedStudents()).thenReturn(new HashSet<>());
		mockMvc.perform(get("/groups/add")).andExpect(model().attribute("groupDTO", new GroupDTO()))
				.andExpect(result -> assertEquals("groups/add", result.getModelAndView().getViewName()));
	}

	@Test
	void testAddViewPostWithNewDTO() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("TEST");
		Boolean showTable = true;
		when((groupServiceMock.checkIfExists(groupDTO))).thenReturn(false);
		mockMvc.perform(post("/groups/add").param("name", "TEST"))
				.andExpectAll(model().attribute("groupDTO", groupDTO), model().attribute("showTable", showTable),
						model().attribute("result", "A group was successfully added."))
				.andExpect(result -> assertEquals("groups/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testAddViewPostWithNewDTOAndEmptyStudentsFirstName_shouldAddDTOtoDBWithStudentsExcludingEmptyNames()
			throws Exception {
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
		when((groupServiceMock.checkIfExists(initialGroupDTO))).thenReturn(false);

		RequestBuilder request = post("/groups/add").flashAttr("groupDTO", initialGroupDTO);
		Boolean showTable = true;

		mockMvc.perform(request)
				.andExpectAll(model().attribute("groupDTO", expectedGroupDTO),
						model().attribute("showTable", showTable),
						model().attribute("result", "A group was successfully added."))
				.andExpect(result -> assertEquals("groups/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testAddViewPostWithExistentDTO() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("TEST");
		when((groupServiceMock.checkIfExists(groupDTO))).thenReturn(true);
		RequestBuilder request = post("/groups/add").flashAttr("groupDTO", groupDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.groupDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The group with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(groupDTO, mockResult.getModel().get("groupDTO"));
		assertEquals("groups/add", mockResult.getViewName());
	}

	@Test
	void testAddViewPostWithBlankCourseName() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(" ");
		when((groupServiceMock.checkIfExists(groupDTO))).thenReturn(true);
		RequestBuilder request = post("/groups/add").flashAttr("groupDTO", groupDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.groupDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Group name cannot be empty", bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(groupDTO, mockResult.getModel().get("groupDTO"));
		assertEquals("groups/add", mockResult.getViewName());
	}

	@Test
	void testEditViewPost() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("TEST");
		when(groupServiceMock.retrieve("TEST")).thenReturn(groupDTO);
		mockMvc.perform(post("/groups/edit").param("name", "TEST"))
				.andExpectAll(model().attribute("groupDTO", groupDTO))
				.andExpect(result -> assertEquals("groups/edit", result.getModelAndView().getViewName()));
	}

	@Test
	void testEditResultViewPostWithNewDTO_shouldReturnResultView() throws Exception {
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
		when(groupServiceMock.checkIfExists(expectedGroupDTO)).thenReturn(false);
		when(groupServiceMock.retrieve(initialGroupDTO.getName())).thenReturn(initialGroupDTO);
		RequestBuilder request = post("/groups/edit/result").flashAttr("groupDTO", expectedGroupDTO)
				.flashAttr("initialName", initialGroupDTO.getName());
		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "Group was successfully updated"),
						model().attribute("showTable", true), model().attribute("groupDTO", expectedGroupDTO))
				.andExpect(result -> assertEquals("groups/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testEditResultViewPostWithExistentDTO_shouldReturnEditView() throws Exception {
		List<StudentDTO> listOfStudents = new ArrayList<>();
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("FirstName");
		studentDTO.setLastName("LastName");
		listOfStudents.add(studentDTO);

		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("OldName");
		groupDTO.setListOfStudent(listOfStudents);

		GroupDTO expectedGroupDTO = new GroupDTO();
		expectedGroupDTO.setName("NewName");
		expectedGroupDTO.setListOfStudent(listOfStudents);
		when(groupServiceMock.checkIfExists(expectedGroupDTO)).thenReturn(true);
		when(groupServiceMock.retrieve("OldName")).thenReturn(groupDTO);
		RequestBuilder request = post("/groups/edit/result").flashAttr("groupDTO", expectedGroupDTO).flashAttr("initialName", "OldName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.groupDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The group with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(expectedGroupDTO, mockResult.getModel().get("groupDTO"));
		assertEquals("groups/edit", mockResult.getViewName());
	}
	
	@Test
	void testEditResultViewPostWithBlankGroupName_shouldReturnEditView() throws Exception {
		List<StudentDTO> listOfStudents = new ArrayList<>();
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("FirstName");
		studentDTO.setLastName("LastName");
		listOfStudents.add(studentDTO);

		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("OldName");
		groupDTO.setListOfStudent(listOfStudents);

		GroupDTO expectedGroupDTO = new GroupDTO();
		expectedGroupDTO.setName(" ");
		expectedGroupDTO.setListOfStudent(listOfStudents);
		when(groupServiceMock.checkIfExists(expectedGroupDTO)).thenReturn(true);
		RequestBuilder request = post("/groups/edit/result").flashAttr("groupDTO", expectedGroupDTO).flashAttr("initialName", "OldName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.groupDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Group name cannot be empty",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(expectedGroupDTO, mockResult.getModel().get("groupDTO"));
		assertEquals("groups/edit", mockResult.getViewName());
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
		RequestBuilder request = post("/groups/edit/result").flashAttr("groupDTO", newGroupDTO).flashAttr("initialName",
				initialGroupDTO.getName());
		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "Group was successfully updated"),
						model().attribute("showTable", true), model().attribute("groupDTO", expectedGroupDTO))
				.andExpect(result -> assertEquals("groups/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testDeleteViewPost() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		when(groupServiceMock.retrieve("AB-123")).thenReturn(groupDTO);
		mockMvc.perform(post("/groups/delete").param("name", "AB-123"))
				.andExpect(model().attribute("groupDTO", groupDTO))
				.andExpect(result -> assertEquals("groups/delete", result.getModelAndView().getViewName()));
	}

	@Test
	void testDeleteViewPost_withDataNotFoundException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(groupServiceMock.retrieve("TEST")).thenThrow(expectedException);
		mockMvc.perform(post("/groups/delete").param("name", "TEST"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()))
				.andExpect(result -> assertEquals(expectedException.getException(),
						result.getResolvedException().getCause()));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("name");
		mockMvc.perform(post("/groups/delete/result").flashAttr("groupDTO", groupDTO))
				.andExpectAll(model().attribute("result", "The group name was successfully deleted."), model().attribute("groupDTO", groupDTO))
				.andExpect(result -> assertEquals("groups/result", result.getModelAndView().getViewName()));
	}
	
	@Test
	void testDeleteResultViewPostWithBlankGroupName_shouldReturnListView() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName(" ");
		RequestBuilder request = post("/groups/delete/result").flashAttr("groupDTO", groupDTO);
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.groupDTO");
		assertEquals("name", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("Group name cannot be empty",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals("groups/list", mockResult.getViewName());
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/groups/wrong")).andExpect(status().is4xxClientError());
	}

}
