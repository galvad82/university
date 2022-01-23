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

import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.TeacherService;
import ua.com.foxminded.galvad.university.services.LessonService;

@SpringBootTest
@AutoConfigureMockMvc
class TeachersRestControllerTest {

	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String NOT_FOUND_ERROR = "Teacher is not found";

	private static final int HTTP_STATUS_CODE_OK = 200;
	private static final int HTTP_STATUS_CODE_CREATED = 201;
	private static final int HTTP_STATUS_CODE_NO_CONTENT = 204;
	private static final int HTTP_STATUS_CODE_NOT_FOUND = 404;
	private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

	private static ObjectWriter objectWriter = new ObjectMapper().writer();

	@Mock
	TeacherService teacherServiceMock;

	@Mock
	LessonService lessonServiceMock;

	@InjectMocks
	TeachersRestController teachersRestControllerUnderTest;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(teachersRestControllerUnderTest).build();
	}

	@Test
	void testRetrieveWithExistentEntity_shouldReturnJSONAnd200Code() throws Exception {
		TeacherDTO teacherDTO = createTeacherDTO(1, "Math");
		when(teacherServiceMock.retrieve(1)).thenReturn(teacherDTO);
		MockHttpServletResponse response = mockMvc.perform(get("/api/teachers/1")).andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(teacherDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testRetrieveWithNotFoundEntity_shouldReturn404WithEmptyContent() throws Exception {
		when(teacherServiceMock.retrieve(999)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/teachers/999")).andReturn().getResponse();
		assertEquals(NOT_FOUND_ERROR, response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testUpdate_shouldReturnJSONAnd200Code() throws Exception {
		TeacherDTO initialTeacherDTO = createTeacherDTO(1, "OLD");
		TeacherDTO updatedTeacherDTO = createTeacherDTO(1, "NEW");
		when(teacherServiceMock.retrieve(1)).thenReturn(initialTeacherDTO);
		when(teacherServiceMock.update(initialTeacherDTO, updatedTeacherDTO)).thenReturn(updatedTeacherDTO);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/teachers/1").content(objectWriter.writeValueAsString(updatedTeacherDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(updatedTeacherDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testUpdateWithDataNotFoundException() throws Exception {
		TeacherDTO initialTeacherDTO = createTeacherDTO(1, "OLD");
		TeacherDTO updatedTeacherDTO = createTeacherDTO(1, "NEW");
		when(teacherServiceMock.retrieve(1)).thenReturn(initialTeacherDTO);
		when(teacherServiceMock.update(initialTeacherDTO, updatedTeacherDTO)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/teachers/1").content(objectWriter.writeValueAsString(updatedTeacherDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(NOT_FOUND_ERROR, response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testUpdateWithDataAreNotUpdatedException() throws Exception {
		TeacherDTO initialTeacherDTO = createTeacherDTO(1, "OLD");
		TeacherDTO updatedTeacherDTO = createTeacherDTO(1, "NEW");
		when(teacherServiceMock.retrieve(1)).thenReturn(initialTeacherDTO);
		when(teacherServiceMock.update(initialTeacherDTO, updatedTeacherDTO))
				.thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/teachers/1").content(objectWriter.writeValueAsString(updatedTeacherDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals("Teacher wasn't updated", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testFindAll() throws Exception {
		List<TeacherDTO> expectedList = new ArrayList<>();
		TeacherDTO firstTeacherDTO = createTeacherDTO(1, "Math");
		TeacherDTO secondTeacherDTO = createTeacherDTO(2, "Science");
		expectedList.add(firstTeacherDTO);
		expectedList.add(secondTeacherDTO);

		when(teacherServiceMock.findAll()).thenReturn(expectedList);
		MockHttpServletResponse response = mockMvc.perform(get("/api/teachers")).andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(expectedList), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testFindAll_shouldReturn404IfNoneFound() throws Exception {
		when(teacherServiceMock.findAll()).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/teachers")).andReturn().getResponse();
		assertEquals("None of Teachers is found", response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testCreate_shouldReturnJSONAnd200Code() throws Exception {
		TeacherDTO teacherDTO = createTeacherDTO(1, "Math");
		when(teacherServiceMock.create(teacherDTO)).thenReturn(teacherDTO);
		MockHttpServletResponse response = mockMvc
				.perform(post("/api/teachers").content(objectWriter.writeValueAsString(teacherDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(teacherDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_CREATED, response.getStatus());
	}

	@Test
	void testCreateWithDataAreNotUpdatedException() throws Exception {
		TeacherDTO teacherDTO = createTeacherDTO(1, "Math");
		when(teacherServiceMock.create(teacherDTO)).thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc
				.perform(post("/api/teachers").content(objectWriter.writeValueAsString(teacherDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals("Teacher wasn't added", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testDelete_shouldReturn204Code() throws Exception {
		TeacherDTO teacherDTO = createTeacherDTO(1, "Math");
		when(teacherServiceMock.retrieve(1)).thenReturn(teacherDTO);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/teachers/1")).andReturn().getResponse();
		assertEquals(0, response.getContentLength());
		assertEquals(HTTP_STATUS_CODE_NO_CONTENT, response.getStatus());
	}

	@Test
	void testDeleteWithDataAreNotUpdatedException() throws Exception {
		when(teacherServiceMock.retrieve(1)).thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/teachers/1")).andReturn().getResponse();
		assertEquals("Teacher wasn't deleted", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testDeleteWithDataNotFoundException() throws Exception {
		when(teacherServiceMock.retrieve(999)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/teachers/999")).andReturn().getResponse();
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	private TeacherDTO createTeacherDTO(Integer id, String name) {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(name);
		teacherDTO.setLastName(name);
		teacherDTO.setId(id);
		return teacherDTO;
	}
}
