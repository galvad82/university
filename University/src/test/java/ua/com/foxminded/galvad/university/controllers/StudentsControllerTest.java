package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
class StudentsControllerTest {

	@Mock
	StudentService studentServiceMock;

	@Mock
	GroupService groupServiceMock;

	@InjectMocks
	StudentsController studentsControllerUnderTest;

	@Mock
	CustomExceptionHandler customExceptionHandlerMock;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentsControllerUnderTest)
				.setControllerAdvice(customExceptionHandlerMock).build();
	}

	@Test
	void testListView() throws Exception {
		Map<StudentDTO, String> studentGroupMap = new LinkedHashMap<>();
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("Abbey");
		studentDTO.setLastName("Wilkes");
		studentGroupMap.put(studentDTO, "AB-123");
		studentDTO.setFirstName("Bernice");
		studentDTO.setLastName("Bone");
		studentGroupMap.put(studentDTO, "CD-456");
		when(studentServiceMock.buildStudentGroupMap()).thenReturn(studentGroupMap);
		mockMvc.perform(get("/students")).andExpect(status().isOk()).andExpect(view().name("students/list"))
				.andExpect(model().attribute("students", studentGroupMap));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(studentServiceMock.buildStudentGroupMap()).thenThrow(expectedException);
		mockMvc.perform(get("/students"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testAddViewGet() throws Exception {
		StudentDTO studentDTO = new StudentDTO();
		List<String> listOfGroupNames = new ArrayList<>();
		listOfGroupNames.add("NONE");
		listOfGroupNames.add("Test");
		List<GroupDTO> listOfGroups = new ArrayList<>();
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("Test");
		listOfGroups.add(groupDTO);
		when(groupServiceMock.findAll()).thenReturn(listOfGroups);
		mockMvc.perform(get("/students/add"))
				.andExpectAll(model().attribute("listGroupNames", listOfGroupNames),
						model().attribute("studentDTO", studentDTO), model().attribute("group", "NONE"))
				.andExpect(view().name("students/add"));
	}

	@Test
	void testAddViewPostWithNewDTO() throws Exception {
		StudentDTO expectedStudentDTO = new StudentDTO();
		expectedStudentDTO.setFirstName("TestName");
		expectedStudentDTO.setLastName("TestLastName");
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		expectedStudentDTO.setGroupDTO(groupDTO);

		when(studentServiceMock.checkIfExists(expectedStudentDTO)).thenReturn(false);
		when(groupServiceMock.retrieve(groupDTO.getName())).thenReturn(groupDTO);
		RequestBuilder request = post("/students/add").flashAttr("studentDTO", expectedStudentDTO).flashAttr("group",
				"AB-123");
		mockMvc.perform(request)
				.andExpectAll(model().attribute("studentDTO", expectedStudentDTO),
						model().attribute("groupName", "AB-123"),
						model().attribute("result", "A student was successfully added."))
				.andExpect(result -> assertEquals("students/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testAddViewPostWithExistentDTO() throws Exception {
		StudentDTO expectedStudentDTO = new StudentDTO();
		expectedStudentDTO.setFirstName("TestName");
		expectedStudentDTO.setLastName("TestLastName");
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		expectedStudentDTO.setGroupDTO(groupDTO);

		when(studentServiceMock.checkIfExists(expectedStudentDTO)).thenReturn(true);
		when(groupServiceMock.retrieve(groupDTO.getName())).thenReturn(groupDTO);
		RequestBuilder request = post("/students/add").flashAttr("studentDTO", expectedStudentDTO).flashAttr("group",
				"AB-123");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.studentDTO");
		assertEquals("firstName", bindingResult.getFieldErrors().get(0).getField());
		assertEquals("The student with the same name is already added to the database!",
				bindingResult.getFieldErrors().get(0).getDefaultMessage());
		assertEquals("", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals(expectedStudentDTO, mockResult.getModel().get("studentDTO"));
		assertEquals("students/add", mockResult.getViewName());
	}

	@Test
	void testAddViewPostWithBlankDTO() throws Exception {
		StudentDTO expectedStudentDTO = new StudentDTO();
		expectedStudentDTO.setFirstName(" ");
		expectedStudentDTO.setLastName(" ");
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		expectedStudentDTO.setGroupDTO(groupDTO);

		when(studentServiceMock.checkIfExists(expectedStudentDTO)).thenReturn(false);
		RequestBuilder request = post("/students/add").flashAttr("studentDTO", expectedStudentDTO).flashAttr("group",
				"AB-123");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.studentDTO");
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("firstName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("lastName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("First name cannot be empty"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("Last name cannot be empty"));
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(1).getCode());
		assertEquals(expectedStudentDTO, mockResult.getModel().get("studentDTO"));
		assertEquals("students/add", mockResult.getViewName());
	}

	@Test
	void testAddViewPostWithUnassignedStudent() throws Exception {
		StudentDTO expectedStudentDTO = new StudentDTO();
		expectedStudentDTO.setFirstName("TestName");
		expectedStudentDTO.setLastName("TestLastName");

		when(studentServiceMock.checkIfExists(expectedStudentDTO)).thenReturn(false);
		RequestBuilder request = post("/students/add").flashAttr("studentDTO", expectedStudentDTO).flashAttr("group",
				"NONE");
		mockMvc.perform(request)
				.andExpectAll(model().attribute("studentDTO", expectedStudentDTO),
						model().attribute("groupName", "NONE"),
						model().attribute("result", "A student was successfully added."))
				.andExpect(result -> assertEquals("students/result", result.getModelAndView().getViewName()));
	}

	@Test
	void testEditViewPost() throws Exception {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("TestName");
		studentDTO.setLastName("TestLastName");

		List<GroupDTO> listOfGroups = new ArrayList<>();
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		listOfGroups.add(groupDTO);

		List<String> listGroupNames = new ArrayList<>();
		listGroupNames.add("NONE");
		listGroupNames.add(groupDTO.getName());

		when(groupServiceMock.findAll()).thenReturn(listOfGroups);
		when(studentServiceMock.retrieve("TestName", "TestLastName")).thenReturn(studentDTO);
		RequestBuilder request = post("/students/edit").flashAttr("firstName", "TestName")
				.flashAttr("lastName", "TestLastName").flashAttr("groupName", "AB-123");

		mockMvc.perform(request).andExpectAll(model().attribute("listGroupNames", listGroupNames),
				model().attribute("initialFirstName", "TestName"), model().attribute("initialLastName", "TestLastName"),
				model().attribute("groupName", "AB-123"), model().attribute("studentDTO", studentDTO))
				.andExpect(view().name("students/edit"));
	}

	@Test
	void testEditResultViewPostForNewStudentDTOAssignedToNewGroupName_shouldReturnResultView() throws Exception {
		testEditResultViewPost_WithParameters("NEW");
	}

	@Test
	void testEditResultViewPostForNewUnassignedStudentDTO_shouldReturnResultView() throws Exception {
		testEditResultViewPost_WithParameters("NONE");
	}

	@Test
	void testEditResultViewPostWithBlankNames_shouldReturnEditView() throws Exception {
		StudentDTO updatedStudentDTO = new StudentDTO();
		updatedStudentDTO.setFirstName(" ");
		updatedStudentDTO.setLastName(" ");
		
		List<GroupDTO> listOfGroups = new ArrayList<>();
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("TEST");
		listOfGroups.add(groupDTO);
		
		List<String>expectedListOfGroupName = Arrays.asList("NONE","TEST");

		when(studentServiceMock.checkIfExists(updatedStudentDTO)).thenReturn(false);
		when(groupServiceMock.findAll()).thenReturn(listOfGroups);
		RequestBuilder request = post("/students/edit/result").flashAttr("studentDTO", updatedStudentDTO)
				.flashAttr("groupName", "TEST").flashAttr("initialFirstName", "OldFirstName")
				.flashAttr("initialLastName", "OldLastName");
		ModelAndView mockResult = mockMvc.perform(request).andReturn().getModelAndView();
		BindingResult bindingResult = (BindingResult) mockResult.getModel()
				.get("org.springframework.validation.BindingResult.studentDTO");
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("firstName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getField()).collect(Collectors.toList())
				.contains("lastName"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("First name cannot be empty"));
		assertTrue(bindingResult.getFieldErrors().stream().map(s -> s.getDefaultMessage()).collect(Collectors.toList())
				.contains("Last name cannot be empty"));
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(0).getCode());
		assertEquals("NotBlank", bindingResult.getFieldErrors().get(1).getCode());
		assertEquals(updatedStudentDTO, mockResult.getModel().get("studentDTO"));
		assertEquals("TEST", mockResult.getModel().get("groupName"));
		assertEquals(expectedListOfGroupName, mockResult.getModel().get("listGroupNames"));
		assertEquals("students/edit", mockResult.getViewName());
	}

	@Test
	void testDeleteViewPost() throws Exception {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("firstName");
		studentDTO.setLastName("lastName");
		when(studentServiceMock.retrieve("firstName", "lastName")).thenReturn(studentDTO);

		RequestBuilder request = post("/students/delete").flashAttr("firstName", "firstName")
				.flashAttr("lastName", "lastName").flashAttr("groupName", "OLD");

		mockMvc.perform(request)
				.andExpectAll(model().attribute("studentDTO", studentDTO), model().attribute("groupName", "OLD"))
				.andExpect(view().name("students/delete"));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("firstName");
		studentDTO.setLastName("lastName");

		RequestBuilder request = post("/students/delete/result").flashAttr("studentDTO", studentDTO)
				.flashAttr("groupName", "OLD");

		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "A student was successfully deleted."),
						model().attribute("studentDTO", studentDTO), model().attribute("groupName", "OLD"))
				.andExpect(view().name("students/result"));
	}

	@Test
	void testDeleteResultViewPostWithBlankNames_shouldReturnListView() throws Exception {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(" ");
		studentDTO.setLastName(" ");

		RequestBuilder request = post("/students/delete/result").flashAttr("studentDTO", studentDTO)
				.flashAttr("groupName", "OLD");

		mockMvc.perform(request).andExpect(model().attribute("studentDTO", studentDTO))
				.andExpect(view().name("students/list"));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/students/wrong")).andExpect(status().is4xxClientError());
	}

	private void testEditResultViewPost_WithParameters(String groupName) throws Exception {
		StudentDTO updatedStudentDTO = new StudentDTO();
		updatedStudentDTO.setFirstName("NewFirstName");
		updatedStudentDTO.setLastName("NewLastName");

		StudentDTO initialStudentDTO = new StudentDTO();
		updatedStudentDTO.setFirstName("OldFirstName");
		updatedStudentDTO.setLastName("OldLastName");

		if (!groupName.equals("NONE")) {
			GroupDTO groupDTO = new GroupDTO();
			groupDTO.setName(groupName);
			when(groupServiceMock.retrieve(groupName)).thenReturn(groupDTO);
		}
		when(studentServiceMock.checkIfExists(updatedStudentDTO)).thenReturn(false);
		when(studentServiceMock.retrieve("OldFirstName", "OldLastName")).thenReturn(initialStudentDTO);
		RequestBuilder request = post("/students/edit/result").flashAttr("studentDTO", updatedStudentDTO)
				.flashAttr("groupName", groupName).flashAttr("initialFirstName", "OldFirstName")
				.flashAttr("initialLastName", "OldLastName");

		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "Student was successfully updated"),
						model().attribute("groupName", groupName), model().attribute("studentDTO", updatedStudentDTO))
				.andExpect(view().name("students/result"));
	}

}
