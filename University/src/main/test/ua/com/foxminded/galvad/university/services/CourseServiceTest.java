package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;

@SpringJUnitWebConfig(SpringConfigTest.class)
class CourseServiceTest {

	@Autowired
	private CourseDAO courseDAO;
	@Autowired
	private TeacherDAO teacherDAO;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CourseService courseService = new CourseService();

	private DataSource dataSource;

	@BeforeEach
	void setDatasoure() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		courseDAO.setDataSource(dataSource);
		teacherDAO.setDataSource(dataSource);
	}

	@Test
	void testCreate() {
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("TestName");
		courseDTO.setTeacher(teacherService.findAll().get(0));
		courseService.create(courseDTO);
		CourseDTO retrievedDTO = courseService.findAll().get(2);
		assertEquals(courseDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		CourseDTO courseDTO = courseService.retrieve("Science");
		assertEquals("Science", courseDTO.getName());
		assertEquals("Jennie", courseDTO.getTeacher().getFirstName());
		assertEquals("Crigler", courseDTO.getTeacher().getLastName());
	}

	@Test
	void testUpdate() {
		CourseDTO initialDTO = courseService.findAll().get(0);
		CourseDTO newDTO = new CourseDTO();
		newDTO.setName(initialDTO.getName());
		newDTO.setTeacher(initialDTO.getTeacher());
		newDTO.setName("NewName");
		courseService.update(initialDTO, newDTO);
		CourseDTO updatedCourseDTO = courseService.findAll().get(0);
		assertEquals(newDTO, updatedCourseDTO);
	}

	@Test
	void testDelete() {
		List<CourseDTO> listBeforeDel = courseService.findAll();
		courseService.delete(listBeforeDel.get(0));
		List<CourseDTO> listAfterDel = courseService.findAll();
		assertEquals(listBeforeDel.size()-1, listAfterDel.size());
	}

	@Test
	void testFindAll() {
		CourseDTO courseDTO = courseService.findAll().get(0);
		assertEquals("Math", courseDTO.getName());
		assertEquals("Gladys", courseDTO.getTeacher().getFirstName());
		assertEquals("Swon", courseDTO.getTeacher().getLastName());
		CourseDTO courseDTO2 = courseService.findAll().get(1);
		assertEquals("Science", courseDTO2.getName());
		assertEquals("Jennie", courseDTO2.getTeacher().getFirstName());
		assertEquals("Crigler", courseDTO2.getTeacher().getLastName());
		assertEquals(2, courseService.findAll().size());
	}
	
	@Test
	void testFindAllLessonsForCourse() {
		List<LessonDTO> listOfLessons = courseService.findAllLessonsForCourse("Science");
		assertEquals("ROOM-15", listOfLessons.get(0).getClassroom().getName());
		assertEquals("Science", listOfLessons.get(0).getCourse().getName());
		assertEquals("Crigler", listOfLessons.get(0).getCourse().getTeacher().getLastName());
		assertEquals("AB-123", listOfLessons.get(0).getGroup().getName());
		assertEquals("Davidson", listOfLessons.get(0).getGroup().getListOfStudent().get(0).getLastName());
		assertEquals(2700000l, listOfLessons.get(0).getDuration());
		assertEquals(1616510000000l, listOfLessons.get(0).getStartTime());
		assertEquals(1, listOfLessons.size());
	}
}
