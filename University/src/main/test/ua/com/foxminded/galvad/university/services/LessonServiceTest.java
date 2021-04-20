package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.galvad.university.config.SpringConfig;
import ua.com.foxminded.galvad.university.dao.impl.ClassroomDAO;
import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.LessonDAO;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;

@SpringJUnitConfig(SpringConfig.class)
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
	void testRetrieve() {
		LessonDTO retrievedDTO = lessonService.retrieve(1);
		assertEquals(classroomService.retrieve(1), retrievedDTO.getClassroom());
		assertEquals(courseService.retrieve(1), retrievedDTO.getCourse());
		assertEquals(teacherService.retrieve(1), retrievedDTO.getCourse().getTeacher());
		assertEquals(groupService.retrieve(1), retrievedDTO.getGroup());
		assertEquals(studentService.retrieve(1), retrievedDTO.getGroup().getListOfStudent().get(0));
		assertEquals(2700000L, retrievedDTO.getDuration());
		assertEquals(1616510000000L, retrievedDTO.getStartTime());
	}

	@Test
	void testRetrieveByFindAll() {
		LessonDTO retrievedDTO = lessonService.findAll().get(0);
		assertEquals(classroomService.findAll().get(0), retrievedDTO.getClassroom());
		assertEquals(courseService.findAll().get(0), retrievedDTO.getCourse());
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
		int count = 1;
		if (!list.isEmpty()) {
			count = list.size();
		}
		lessonService.delete(lessonService.retrieve(1));
		list = lessonService.findAll();
		int countAfterDel = 0;
		if (!list.isEmpty()) {
			countAfterDel = list.size();
		}
		assertEquals(count - 1, countAfterDel);
	}

	@Test
	void testDeleteByClassroom() {
		List<LessonDTO> list = lessonService.findAll();
		int count = 1;
		if (!list.isEmpty()) {
			count = list.size();
		}
		lessonService.deleteByClassroom(classroomService.retrieve(1));
		list = lessonService.findAll();
		int countAfterDel = 0;
		if (!list.isEmpty()) {
			countAfterDel = list.size();
		}
		assertEquals(count - 1, countAfterDel);
	}

	@Test
	void testDeleteByCourse() {
		List<LessonDTO> list = lessonService.findAll();
		int count = 1;
		if (!list.isEmpty()) {
			count = list.size();
		}
		lessonService.deleteByCourse(courseService.retrieve(1));
		list = lessonService.findAll();
		int countAfterDel = 0;
		if (!list.isEmpty()) {
			countAfterDel = list.size();
		}
		assertEquals(count - 1, countAfterDel);
	}

	@Test
	void testDeleteByGroup() {
		List<LessonDTO> list = lessonService.findAll();
		int count = 1;
		if (!list.isEmpty()) {
			count = list.size();
		}
		lessonService.deleteByGroup(groupService.retrieve(1));
		list = lessonService.findAll();
		int countAfterDel = 0;
		if (!list.isEmpty()) {
			countAfterDel = list.size();
		}
		assertEquals(count - 1, countAfterDel);
	}

}
