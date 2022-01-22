package ua.com.foxminded.galvad.university.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.StudentService;
import ua.com.foxminded.galvad.university.services.LessonService;

@SpringBootTest
@AutoConfigureMockMvc
class StudentsRestControllerTest {

	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String NOT_FOUND_ERROR = "Student is not found";

	private static final int HTTP_STATUS_CODE_OK = 200;
	private static final int HTTP_STATUS_CODE_CREATED = 201;
	private static final int HTTP_STATUS_CODE_NO_CONTENT = 204;
	private static final int HTTP_STATUS_CODE_NOT_FOUND = 404;
	private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

	private static ObjectWriter objectWriter = new ObjectMapper().writer();

	@Mock
	StudentService studentServiceMock;

	@Mock
	LessonService lessonServiceMock;

	@InjectMocks
	StudentsRestController studentsRestControllerUnderTest;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(studentsRestControllerUnderTest).build();
	}

	@Test
	void testRetrieveWithExistentEntity_shouldReturnJSONAnd200Code() throws Exception {
		StudentDTO studentDTO = createStudentDTO(1, "Math");
		when(studentServiceMock.retrieve(1)).thenReturn(studentDTO);
		MockHttpServletResponse response = mockMvc.perform(get("/api/students/1")).andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(studentDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testRetrieveWithNotFoundEntity_shouldReturn404WithEmptyContent() throws Exception {
		when(studentServiceMock.retrieve(999)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/students/999")).andReturn().getResponse();
		assertEquals(NOT_FOUND_ERROR, response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testUpdate_shouldReturnJSONAnd200Code() throws Exception {
		StudentDTO initialStudentDTO = createStudentDTO(1, "OLD");
		StudentDTO updatedStudentDTO = createStudentDTO(1, "NEW");
		when(studentServiceMock.retrieve(1)).thenReturn(initialStudentDTO);
		when(studentServiceMock.update(initialStudentDTO, updatedStudentDTO)).thenReturn(updatedStudentDTO);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/students/1").content(objectWriter.writeValueAsString(updatedStudentDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(updatedStudentDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testUpdateWithDataNotFoundException() throws Exception {
		StudentDTO initialStudentDTO = createStudentDTO(1, "OLD");
		StudentDTO updatedStudentDTO = createStudentDTO(1, "NEW");
		when(studentServiceMock.retrieve(1)).thenReturn(initialStudentDTO);
		when(studentServiceMock.update(initialStudentDTO, updatedStudentDTO)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/students/1").content(objectWriter.writeValueAsString(updatedStudentDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(NOT_FOUND_ERROR, response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testUpdateWithDataAreNotUpdatedException() throws Exception {
		StudentDTO initialStudentDTO = createStudentDTO(1, "OLD");
		StudentDTO updatedStudentDTO = createStudentDTO(1, "NEW");
		when(studentServiceMock.retrieve(1)).thenReturn(initialStudentDTO);
		when(studentServiceMock.update(initialStudentDTO, updatedStudentDTO))
				.thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/students/1").content(objectWriter.writeValueAsString(updatedStudentDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals("Student wasn't updated", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testFindAll() throws Exception {
		List<StudentDTO> expectedList = new ArrayList<>();
		StudentDTO firstStudentDTO = createStudentDTO(1, "Math");
		StudentDTO secondStudentDTO = createStudentDTO(2, "Science");
		expectedList.add(firstStudentDTO);
		expectedList.add(secondStudentDTO);

		when(studentServiceMock.findAll()).thenReturn(expectedList);
		MockHttpServletResponse response = mockMvc.perform(get("/api/students")).andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(expectedList), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testFindAll_shouldReturn404IfNoneFound() throws Exception {
		when(studentServiceMock.findAll()).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/students")).andReturn().getResponse();
		assertEquals("None of Students is found", response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testCreate_shouldReturnJSONAnd200Code() throws Exception {
		StudentDTO studentDTO = createStudentDTO(1, "Math");
		when(studentServiceMock.create(studentDTO)).thenReturn(studentDTO);
		MockHttpServletResponse response = mockMvc
				.perform(post("/api/students").content(objectWriter.writeValueAsString(studentDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(studentDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_CREATED, response.getStatus());
	}

	@Test
	void testCreateWithDataAreNotUpdatedException() throws Exception {
		StudentDTO studentDTO = createStudentDTO(1, "Math");
		when(studentServiceMock.create(studentDTO)).thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc
				.perform(post("/api/students").content(objectWriter.writeValueAsString(studentDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals("Student wasn't added", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testDelete_shouldReturn204Code() throws Exception {
		StudentDTO studentDTO = createStudentDTO(1, "Math");
		when(studentServiceMock.retrieve(1)).thenReturn(studentDTO);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/students/1")).andReturn().getResponse();
		assertEquals(0, response.getContentLength());
		assertEquals(HTTP_STATUS_CODE_NO_CONTENT, response.getStatus());
	}

	@Test
	void testDeleteWithDataAreNotUpdatedException() throws Exception {
		when(studentServiceMock.retrieve(1)).thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/students/1")).andReturn().getResponse();
		assertEquals("Student wasn't deleted", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testDeleteWithDataNotFoundException() throws Exception {
		when(studentServiceMock.retrieve(999)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/students/999")).andReturn().getResponse();
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	private StudentDTO createStudentDTO(Integer id, String name) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(name);
		studentDTO.setLastName(name);
		studentDTO.setId(id);
		return studentDTO;
	}
}
