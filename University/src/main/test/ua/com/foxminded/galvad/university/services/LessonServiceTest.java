package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dao.impl.ClassroomDAO;
import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.model.Event;

@SpringJUnitWebConfig(SpringConfigTest.class)
class LessonServiceTest {

	@Autowired
	private ClassroomDAO classroomDAO;
	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private LessonDAO lessonDAO;
	@Autowired
	private StudentDAO studentDAO;
	@Autowired
	private TeacherDAO teacherDAO;

	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService;

	private DataSource dataSource;

	@BeforeEach
	void setDatasoure() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		classroomDAO.setDataSource(dataSource);
		courseDAO.setDataSource(dataSource);
		groupDAO.setDataSource(dataSource);
		lessonDAO.setDataSource(dataSource);
		studentDAO.setDataSource(dataSource);
		teacherDAO.setDataSource(dataSource);
	}

	@Test
	void testCreate() {
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setClassroom(classroomService.findAll().get(0));
		lessonDTO.setCourse(courseService.findAll().get(0));
		lessonDTO.setGroup(groupService.findAll().get(0));
		lessonDTO.setDuration(555L);
		lessonDTO.setStartTime(777L);
		lessonService.create(lessonDTO);
		LessonDTO retrievedDTO = lessonService.findAll().get(2);
		assertEquals(lessonDTO, retrievedDTO);
	}

	@Test
	void testRetrieveByFindAll() {
		LessonDTO retrievedDTO = lessonService.findAll().get(0);
		assertEquals(classroomService.findAll().get(0), retrievedDTO.getClassroom());
		assertEquals(courseService.findAll().get(1), retrievedDTO.getCourse());
		assertEquals(teacherService.findAll().get(0), retrievedDTO.getCourse().getTeacher());
		assertEquals(groupService.findAll().get(0), retrievedDTO.getGroup());
		assertEquals(studentService.findAll().get(0), retrievedDTO.getGroup().getListOfStudent().get(0));
		assertEquals(2700000L, retrievedDTO.getDuration());
		assertEquals(1616510000000L, retrievedDTO.getStartTime());
	}

	@Test
	void testUpdate() {
		LessonDTO initialDTO = lessonService.findAll().get(0);
		LessonDTO newDTO = new LessonDTO();
		newDTO.setClassroom(initialDTO.getClassroom());
		newDTO.setCourse(initialDTO.getCourse());
		newDTO.setDuration(initialDTO.getDuration());
		newDTO.setGroup(initialDTO.getGroup());
		newDTO.setStartTime(initialDTO.getStartTime());
		newDTO.setStartTime(123456L);
		newDTO.setDuration(7890L);
		lessonService.update(initialDTO, newDTO);
		assertEquals(newDTO, lessonService.findAll().get(0));
	}

	@Test
	void testDelete() {
		List<LessonDTO> list = lessonService.findAll();
		int countBeforeDel = list.size();
		lessonService.delete(list.get(0));
		list = lessonService.findAll();
		int countAfterDel = list.size();
		assertEquals(countBeforeDel - 1, countAfterDel);
	}

	@Test
	void testDeleteByClassroom() {
		List<LessonDTO> list = lessonService.findAll();
		int countBeforeDel = list.size();
		lessonService.deleteByClassroom(classroomService.findAll().get(0));
		list = lessonService.findAll();
		int countAfterDel = list.size();
		assertEquals(countBeforeDel - 1, countAfterDel);
	}

	@Test
	void testDeleteByCourse() {
		List<LessonDTO> list = lessonService.findAll();
		int countBeforeDel = list.size();
		lessonService.deleteByCourse(courseService.findAll().get(0));
		list = lessonService.findAll();
		int countAfterDel = list.size();
		assertEquals(countBeforeDel - 1, countAfterDel);
	}

	@Test
	void testDeleteByGroup() {
		List<LessonDTO> list = lessonService.findAll();
		int countBeforeDel = list.size();
		lessonService.deleteByGroup(groupService.findAll().get(0));
		list = lessonService.findAll();
		int countAfterDel = list.size();
		assertEquals(countBeforeDel - 1, countAfterDel);
	}

	@Test
	void testConvertDateToMil() {
		long expectedMillis = 1628011620000l;
		String date = "03-08-2021 20:27";
		long resultMillis = lessonService.convertDateToMil(date);
		assertEquals(expectedMillis, resultMillis);
		assertNotEquals(123456l, resultMillis);
	}

	@Test
	void testConvertTimeToMil() {
		long expectedMillis = 73620000l;
		String date = "20:27";
		long resultMillis = lessonService.convertTimeToMil(date);
		assertEquals(expectedMillis, resultMillis);
		assertNotEquals(123456l, resultMillis);
	}

	@Test
	void testEventListForCalendarCreator() {
		List<LessonDTO> listOfLessons = new ArrayList<>();
		listOfLessons.add(lessonService.findAll().get(0));
		List<Event> expectedEventList = new ArrayList<>();
		expectedEventList.add(new Event("Group: AB-123, Course: Science, Classroom: ROOM-15, Teacher: Jennie Crigler", 1616510000000l, 1616512700000l));
		assertEquals(expectedEventList, lessonService.eventListForCalendarCreator(listOfLessons));
	}
	
	@Test
	void testEventListForCalendarCreator_shouldSkipLessonsWithStartTimeIsNegative() {
		List<LessonDTO> listOfLessons = new ArrayList<>();
		LessonDTO lessonDTO = lessonService.findAll().get(0);
		lessonDTO.setStartTime(-100);
		listOfLessons.add(lessonDTO);
		List<Event> expectedEventList = new ArrayList<>();
		assertEquals(expectedEventList, lessonService.eventListForCalendarCreator(listOfLessons));
	}
	
	@Test
	void testEventListForCalendarCreator_shouldSkipLessonsWithStartTimeEqualsZero() {
		List<LessonDTO> listOfLessons = new ArrayList<>();
		LessonDTO lessonDTO = lessonService.findAll().get(0);
		lessonDTO.setStartTime(0);
		listOfLessons.add(lessonDTO);
		List<Event> expectedEventList = new ArrayList<>();
		assertEquals(expectedEventList, lessonService.eventListForCalendarCreator(listOfLessons));
	}
	
	@Test
	void testEventListForCalendarCreator_shouldSkipLessonsWithNegativeDuration() {
		List<LessonDTO> listOfLessons = new ArrayList<>();
		LessonDTO lessonDTO = lessonService.findAll().get(0);
		lessonDTO.setDuration(-1);
		listOfLessons.add(lessonDTO);
		List<Event> expectedEventList = new ArrayList<>();
		assertEquals(expectedEventList, lessonService.eventListForCalendarCreator(listOfLessons));
	}

}