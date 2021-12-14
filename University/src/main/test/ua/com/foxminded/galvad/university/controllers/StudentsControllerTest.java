package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
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
		mockMvc.perform(get("/students/add")).andExpectAll(model().attribute("listGroupNames", listOfGroupNames),
				model().attribute("studentDTO", studentDTO)).andExpect(view().name("students/add"));
	}

	@Test
	void testAddViewPost() throws Exception {
		StudentDTO expectedStudentDTO = new StudentDTO();
		expectedStudentDTO.setFirstName("TestName");
		expectedStudentDTO.setFirstName("TestLastName");
		RequestBuilder request = post("/students/add").flashAttr("studentDTO", expectedStudentDTO).flashAttr("group",
				"AB-123");
		mockMvc.perform(request)
				.andExpectAll(model().attribute("student", expectedStudentDTO),
						model().attribute("groupName", "AB-123"),
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
		GroupDTO noneDto = new GroupDTO();
		noneDto.setName("NONE");
		List<GroupDTO> listOfGroupsWithNone = new ArrayList<>();
		listOfGroupsWithNone.addAll(listOfGroups);
		listOfGroupsWithNone.add(0, noneDto);

		when(groupServiceMock.findAll()).thenReturn(listOfGroups);
		RequestBuilder request = post("/students/edit").flashAttr("firstName", studentDTO.getFirstName())
				.flashAttr("lastName", studentDTO.getLastName()).flashAttr("groupName", "AB-123");

		mockMvc.perform(request)
				.andExpectAll(model().attribute("listOfGroups", listOfGroupsWithNone),
						model().attribute("initialGroup", "AB-123"), model().attribute("studentDTO", studentDTO))
				.andExpect(view().name("students/edit"));
	}

	@Test
	void testEditResultViewPost() throws Exception {
		testEditResultViewPost_WithParameters("NEW", "OLD");
	}

	@Test
	void testEditResultViewPost_WithNewGroupNameNONE() throws Exception {
		testEditResultViewPost_WithParameters("NONE", "OLD");
	}

	@Test
	void testEditResultViewPost_WithInitialGroupNameNONE() throws Exception {
		testEditResultViewPost_WithParameters("NEW", "NONE");
	}

	@Test
	void testDeleteViewPost() throws Exception {
		RequestBuilder request = post("/students/delete").flashAttr("firstName", "firstName")
				.flashAttr("lastName", "lastName").flashAttr("groupName", "OLD");

		mockMvc.perform(request).andExpectAll(model().attribute("firstName", "firstName"),
				model().attribute("lastName", "lastName"), model().attribute("groupName", "OLD"))
				.andExpect(view().name("students/delete"));
	}

	@Test
	void testDeleteResultViewPost() throws Exception {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("firstName");
		studentDTO.setLastName("lastName");

		RequestBuilder request = post("/students/delete/result").flashAttr("firstName", "firstName")
				.flashAttr("lastName", "lastName").flashAttr("groupName", "OLD");

		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "A student was successfully deleted."),
						model().attribute("student", studentDTO), model().attribute("groupName", "OLD"))
				.andExpect(view().name("students/result"));
	}

	@Test
	void testWrongView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/students/wrong")).andExpect(status().is4xxClientError());
	}

	private void testEditResultViewPost_WithParameters(String gName, String attrInitialGroup) throws Exception {
		String groupName = gName;
		StudentDTO updatedStudentDTO = new StudentDTO();
		updatedStudentDTO.setFirstName("NewFirstName");
		updatedStudentDTO.setLastName("NewLastName");

		RequestBuilder request = post("/students/edit/result").flashAttr("studentDTO", updatedStudentDTO)
				.flashAttr("groupName", groupName).flashAttr("initialGroup", attrInitialGroup)
				.flashAttr("initialFirstName", "OldFirstName").flashAttr("initialLastName", "OldLastName");

		mockMvc.perform(request)
				.andExpectAll(model().attribute("result", "Student was successfully updated"),
						model().attribute("groupName", groupName), model().attribute("student", updatedStudentDTO))
				.andExpect(view().name("students/result"));
	}

}
