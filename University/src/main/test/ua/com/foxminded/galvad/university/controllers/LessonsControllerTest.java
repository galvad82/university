package ua.com.foxminded.galvad.university.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.com.foxminded.galvad.university.dao.impl.DataNotFoundException;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.services.LessonService;

@ExtendWith(MockitoExtension.class)
class LessonsControllerTest {

	@Mock
	LessonService lessonServiceMock;

	@InjectMocks
	LessonsController lessonsControllerUnderTest;

	@Mock
	CustomExceptionHandler customExceptionHandlerMock;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		this.mockMvc = null;
		this.mockMvc = MockMvcBuilders.standaloneSetup(lessonsControllerUnderTest)
				.setControllerAdvice(customExceptionHandlerMock).build();
	}

	@Test
	void testListView_shouldReturnListView() throws Exception {
		mockMvc.perform(get("/lessons")).andExpect(view().name("lessons/list"));
	}

	@Test
	void testSingleView_shouldReturnSingleView() throws Exception {
		mockMvc.perform(get("/lessons/{id}", 1)).andExpect(view().name("lessons/single"));
	}

	@Test
	void testStudentAttributeForSingleView() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-101");
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("Advertising business");
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("ROOM-1");
		LessonDTO expectedLessonDTO = new LessonDTO();
		expectedLessonDTO.setGroup(groupDTO);
		expectedLessonDTO.setCourse(courseDTO);
		expectedLessonDTO.setClassroom(classroomDTO);
		expectedLessonDTO.setDuration(1);
		expectedLessonDTO.setStartTime(1);
		when(lessonServiceMock.retrieve(1)).thenReturn(expectedLessonDTO);
		this.mockMvc = MockMvcBuilders.standaloneSetup(lessonsControllerUnderTest).build();
		mockMvc.perform(get("/lessons/{id}", 1)).andExpect(status().isOk()).andExpect(view().name("lessons/single"))
				.andExpect(model().attribute("lesson", expectedLessonDTO));
	}

	@Test
	void testSingleView_shouldSetCorrectIdAttributeValue() throws Exception {
		mockMvc.perform(get("/lessons/{id}", 1)).andExpect(matchAll(model().attribute("id", 1)));
	}

	@Test
	void testSingleView_shouldReturn4xx() throws Exception {
		mockMvc.perform(get("/lessons/{id}", "fff")).andExpect(status().is4xxClientError());
	}

	@Test
	void testSingleView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(lessonServiceMock.retrieve(99999)).thenThrow(expectedException);
		mockMvc.perform(get("/lessons/{id}", 99999))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

	@Test
	void testListView_shouldReturnExpectedList() throws Exception {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-101");
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("Advertising business");
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("ROOM-1");
		LessonDTO firstLessonDTO = new LessonDTO();
		firstLessonDTO.setGroup(groupDTO);
		firstLessonDTO.setCourse(courseDTO);
		firstLessonDTO.setClassroom(classroomDTO);
		firstLessonDTO.setDuration(1);
		firstLessonDTO.setStartTime(1);
		LessonDTO secondLessonDTO = new LessonDTO();
		secondLessonDTO.setGroup(groupDTO);
		secondLessonDTO.setCourse(courseDTO);
		secondLessonDTO.setClassroom(classroomDTO);
		secondLessonDTO.setDuration(2);
		secondLessonDTO.setStartTime(2);
		List<LessonDTO> expectedList = new ArrayList<>();
		expectedList.add(firstLessonDTO);
		expectedList.add(secondLessonDTO);

		when(lessonServiceMock.findAll()).thenReturn(expectedList);
		mockMvc.perform(get("/lessons")).andExpect(status().isOk()).andExpect(view().name("lessons/list"))
				.andExpect(model().attribute("lessons", expectedList));
	}

	@Test
	void testListView_shouldThrowExpectedException() throws Exception {
		DataNotFoundException expectedException = new DataNotFoundException("Error Message");
		when(lessonServiceMock.findAll()).thenThrow(expectedException);
		mockMvc.perform(get("/lessons"))
				.andExpect(result -> assertEquals(expectedException, result.getResolvedException()));
	}

}
