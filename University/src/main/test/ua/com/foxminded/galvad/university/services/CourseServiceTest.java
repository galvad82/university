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

import ua.com.foxminded.galvad.university.config.SpringConfig;
import ua.com.foxminded.galvad.university.dao.impl.CourseDAO;
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;

@SpringJUnitWebConfig(SpringConfig.class)
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
		CourseDTO courseDTO = courseService.retrieve(1);
		assertEquals("Science", courseDTO.getName());
		assertEquals("Jennie", courseDTO.getTeacher().getFirstName());
		assertEquals("Crigler", courseDTO.getTeacher().getLastName());
	}

	@Test
	void testRetrieveByFindAll() {
		CourseDTO courseDTO = courseService.findAll().get(0);
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
		List<CourseDTO> listDTO = courseService.findAll();
		int count = 1;
		if (!listDTO.isEmpty()) {
			count = listDTO.size();
		}
		courseService.delete(listDTO.get(0));
		List<CourseDTO> listAfterDel = courseService.findAll();
		int countAfterDel = 0;
		if (!listAfterDel.isEmpty()) {
			countAfterDel = listAfterDel.size();
		}
		assertEquals((count - 1), countAfterDel);
	}

	@Test
	void testFindAll() {
		assertEquals(2, courseService.findAll().size());
	}

}
