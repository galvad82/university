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
import ua.com.foxminded.galvad.university.dao.impl.TeacherDAO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;

@SpringJUnitConfig(SpringConfig.class)
class TeacherServiceTest {

	@Autowired
	private TeacherService teacherService = new TeacherService();
	@Autowired
	private TeacherDAO teacherDAO;

	private DataSource dataSource;

	@BeforeEach
	void setDatasoure() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		teacherDAO.setDataSource(dataSource);
	}

	@Test
	void testCreate() {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("First Name");
		teacherDTO.setLastName("Last Name");
		teacherService.create(teacherDTO);
		TeacherDTO retrievedDTO = teacherService.findAll().get(2);
		assertEquals(teacherDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		TeacherDTO teacherDTO = teacherService.retrieve(1);
		assertEquals("Jennie", teacherDTO.getFirstName());
		assertEquals("Crigler", teacherDTO.getLastName());
	}

	@Test
	void testRetrieveByFindAll() {
		TeacherDTO teacherDTO = teacherService.findAll().get(0);
		assertEquals("Jennie", teacherDTO.getFirstName());
		assertEquals("Crigler", teacherDTO.getLastName());
	}

	@Test
	void testUpdate() {
		TeacherDTO initialDTO = teacherService.findAll().get(0);
		TeacherDTO newDTO = new TeacherDTO();
		newDTO.setFirstName(initialDTO.getFirstName());
		newDTO.setLastName(initialDTO.getLastName());
		newDTO.setFirstName("NewName");
		teacherService.update(initialDTO, newDTO);
		TeacherDTO updatedTeacherDTO = teacherService.findAll().get(0);
		assertEquals(newDTO, updatedTeacherDTO);
	}

	@Test
	void testDelete() {
		List<TeacherDTO> listDTO = teacherService.findAll();
		int count = 1;
		if (!listDTO.isEmpty()) {
			count = listDTO.size();
		}
		teacherService.delete(listDTO.get(0));
		List<TeacherDTO> listAfterDel = teacherService.findAll();
		int countAfterDel = 0;
		if (!listAfterDel.isEmpty()) {
			countAfterDel = listAfterDel.size();
		}
		assertEquals((count - 1), countAfterDel);
	}

	@Test
	void testFindAll() {
		assertEquals(2, teacherService.findAll().size());
	}

}
