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

import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.exceptions.DataAreNotUpdatedException;
import ua.com.foxminded.galvad.university.exceptions.DataNotFoundException;
import ua.com.foxminded.galvad.university.services.ClassroomService;
import ua.com.foxminded.galvad.university.services.LessonService;

@SpringBootTest
@AutoConfigureMockMvc
class ClassroomsRestControllerTest {

	private static final String CONTENT_TYPE_JSON = "application/json";
	private static final String NOT_FOUND_ERROR = "Classroom is not found";

	private static final int HTTP_STATUS_CODE_OK = 200;
	private static final int HTTP_STATUS_CODE_CREATED = 201;
	private static final int HTTP_STATUS_CODE_NO_CONTENT = 204;
	private static final int HTTP_STATUS_CODE_NOT_FOUND = 404;
	private static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

	private static ObjectWriter objectWriter = new ObjectMapper().writer();

	@Mock
	ClassroomService classroomServiceMock;

	@Mock
	LessonService lessonServiceMock;

	@InjectMocks
	ClassroomsRestController classroomsRestControllerUnderTest;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(classroomsRestControllerUnderTest).build();
	}

	@Test
	void testRetrieveWithExistentEntity_shouldReturnJSONAnd200Code() throws Exception {
		ClassroomDTO classroomDTO = createClassroomDTO(1, "ROOM-1");
		when(classroomServiceMock.retrieve(1)).thenReturn(classroomDTO);
		MockHttpServletResponse response = mockMvc.perform(get("/api/classrooms/1")).andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(classroomDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testRetrieveWithNotFoundEntity_shouldReturn404WithEmptyContent() throws Exception {
		when(classroomServiceMock.retrieve(999)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/classrooms/999")).andReturn().getResponse();
		assertEquals(NOT_FOUND_ERROR, response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testFindAllLessonsForClassroom_shouldReturnJSONAnd200Code() throws Exception {
		ClassroomDTO classroomDTO = createClassroomDTO(1, "John");
		LessonDTO lessonDTO1 = createLessonDTO(1, "John", "Doe");
		LessonDTO lessonDTO2 = createLessonDTO(2, "Jane", "Johnson");
		List<LessonDTO> listOfLessons = new ArrayList<>();
		listOfLessons.add(lessonDTO1);
		listOfLessons.add(lessonDTO2);
		when(classroomServiceMock.retrieve(1)).thenReturn(classroomDTO);
		when(lessonServiceMock.findAllLessonsForClassroom(classroomDTO.getName())).thenReturn(listOfLessons);
		MockHttpServletResponse response = mockMvc.perform(get("/api/classrooms/1/lessons")).andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(listOfLessons), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testFindAllLessonsForClassroomWithDataNotFoundException() throws Exception {
		ClassroomDTO classroomDTO = createClassroomDTO(1, "John");
		when(classroomServiceMock.retrieve(1)).thenReturn(classroomDTO);
		when(lessonServiceMock.findAllLessonsForClassroom(classroomDTO.getName()))
				.thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/classrooms/1/lessons")).andReturn().getResponse();
		assertEquals("No lessons were found for the classroom", response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testFindAllLessonsForClassroomWithDataAreNotUpdatedException() throws Exception {
		ClassroomDTO classroomDTO = createClassroomDTO(1, "John");
		when(classroomServiceMock.retrieve(1)).thenReturn(classroomDTO);
		when(lessonServiceMock.findAllLessonsForClassroom(classroomDTO.getName()))
				.thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/classrooms/1/lessons")).andReturn().getResponse();
		assertEquals("A list of lessons wasn't prepared", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testUpdate_shouldReturnJSONAnd200Code() throws Exception {
		ClassroomDTO initialClassroomDTO = createClassroomDTO(1, "OLD");
		ClassroomDTO updatedClassroomDTO = createClassroomDTO(1, "NEW");
		when(classroomServiceMock.retrieve(1)).thenReturn(initialClassroomDTO);
		when(classroomServiceMock.update(initialClassroomDTO, updatedClassroomDTO)).thenReturn(updatedClassroomDTO);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/classrooms/1").content(objectWriter.writeValueAsString(updatedClassroomDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(updatedClassroomDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testUpdateWithDataNotFoundException() throws Exception {
		ClassroomDTO initialClassroomDTO = createClassroomDTO(1, "OLD");
		ClassroomDTO updatedClassroomDTO = createClassroomDTO(1, "NEW");
		when(classroomServiceMock.retrieve(1)).thenReturn(initialClassroomDTO);
		when(classroomServiceMock.update(initialClassroomDTO, updatedClassroomDTO))
				.thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/classrooms/1").content(objectWriter.writeValueAsString(updatedClassroomDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(NOT_FOUND_ERROR, response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testUpdateWithDataAreNotUpdatedException() throws Exception {
		ClassroomDTO initialClassroomDTO = createClassroomDTO(1, "OLD");
		ClassroomDTO updatedClassroomDTO = createClassroomDTO(1, "NEW");
		when(classroomServiceMock.retrieve(1)).thenReturn(initialClassroomDTO);
		when(classroomServiceMock.update(initialClassroomDTO, updatedClassroomDTO))
				.thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(
				put("/api/classrooms/1").content(objectWriter.writeValueAsString(updatedClassroomDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals("Classroom wasn't updated", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testFindAll() throws Exception {
		ClassroomDTO firstClassroomDTO = createClassroomDTO(1, "ROOM-1");
		ClassroomDTO secondClassroomDTO = createClassroomDTO(2, "ROOM-2");
		List<ClassroomDTO> expectedList = new ArrayList<>();
		expectedList.add(firstClassroomDTO);
		expectedList.add(secondClassroomDTO);

		when(classroomServiceMock.findAll()).thenReturn(expectedList);
		MockHttpServletResponse response = mockMvc.perform(get("/api/classrooms")).andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(expectedList), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_OK, response.getStatus());
	}

	@Test
	void testFindAll_shouldReturn404IfNoneFound() throws Exception {
		when(classroomServiceMock.findAll()).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(get("/api/classrooms")).andReturn().getResponse();
		assertEquals("None of Classrooms is found", response.getErrorMessage());
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testCreate_shouldReturnJSONAnd200Code() throws Exception {
		ClassroomDTO classroomDTO = createClassroomDTO(1, "Name");
		when(classroomServiceMock.create(classroomDTO)).thenReturn(classroomDTO);
		MockHttpServletResponse response = mockMvc.perform(
				post("/api/classrooms").content(objectWriter.writeValueAsString(classroomDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals(objectWriter.writeValueAsString(classroomDTO), response.getContentAsString());
		assertEquals(CONTENT_TYPE_JSON, response.getContentType());
		assertEquals(HTTP_STATUS_CODE_CREATED, response.getStatus());
	}

	@Test
	void testCreateWithDataAreNotUpdatedException() throws Exception {
		ClassroomDTO classroomDTO = createClassroomDTO(1, "Name");
		when(classroomServiceMock.create(classroomDTO)).thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(
				post("/api/classrooms").content(objectWriter.writeValueAsString(classroomDTO).getBytes("UTF-8"))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		assertEquals("Classroom wasn't added", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testDelete_shouldReturn204Code() throws Exception {
		ClassroomDTO classroomDTO = createClassroomDTO(1, "Name");
		when(classroomServiceMock.retrieve(1)).thenReturn(classroomDTO);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/classrooms/1")).andReturn().getResponse();
		assertEquals(0, response.getContentLength());
		assertEquals(HTTP_STATUS_CODE_NO_CONTENT, response.getStatus());
	}

	@Test
	void testDeleteWithDataAreNotUpdatedException() throws Exception {
		when(classroomServiceMock.retrieve(1)).thenThrow(DataAreNotUpdatedException.class);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/classrooms/1")).andReturn().getResponse();
		assertEquals("Classroom wasn't deleted", response.getErrorMessage());
		assertEquals(HTTP_STATUS_INTERNAL_SERVER_ERROR, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	@Test
	void testDeleteWithDataNotFoundException() throws Exception {
		when(classroomServiceMock.retrieve(999)).thenThrow(DataNotFoundException.class);
		MockHttpServletResponse response = mockMvc.perform(delete("/api/classrooms/999")).andReturn().getResponse();
		assertEquals(HTTP_STATUS_CODE_NOT_FOUND, response.getStatus());
		assertEquals(0, response.getContentLength());
	}

	private LessonDTO createLessonDTO(Integer id, String name, String lastName) {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setId(id);
		groupDTO.setName(name);
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName(name);
		studentDTO.setLastName(lastName);
		studentDTO.setGroupDTO(groupDTO);
		List<StudentDTO> listOfStudents = new ArrayList<StudentDTO>();
		listOfStudents.add(studentDTO);
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setId(id);
		classroomDTO.setName(name);
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName(name);
		teacherDTO.setLastName(lastName);
		teacherDTO.setId(id);
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName(name);
		courseDTO.setId(id);
		courseDTO.setTeacher(teacherDTO);
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setClassroom(classroomDTO);
		lessonDTO.setCourse(courseDTO);
		lessonDTO.setDuration(100000l);
		lessonDTO.setGroup(groupDTO);
		lessonDTO.setId(id);
		lessonDTO.setStartTime(2000000l);
		return lessonDTO;
	}

	private ClassroomDTO createClassroomDTO(Integer id, String name) {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName(name);
		classroomDTO.setId(id);
		return classroomDTO;
	}
}
