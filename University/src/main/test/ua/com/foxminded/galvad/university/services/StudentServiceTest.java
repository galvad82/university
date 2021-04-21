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
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;

@SpringJUnitWebConfig(SpringConfig.class)
class StudentServiceTest {

	@Autowired
	private StudentService studentService = new StudentService();
	@Autowired
	private StudentDAO studentDAO;

	private DataSource dataSource;

	@BeforeEach
	void setDatasoure() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		studentDAO.setDataSource(dataSource);
	}

	@Test
	void testCreate() {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("First Name");
		studentDTO.setLastName("Last Name");
		studentService.create(studentDTO);
		StudentDTO retrievedDTO = studentService.findAll().get(5);
		assertEquals(studentDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		StudentDTO studentDTO = studentService.retrieve(1);
		assertEquals("John", studentDTO.getFirstName());
		assertEquals("Davidson", studentDTO.getLastName());
	}

	@Test
	void testRetrieveByFindAll() {
		StudentDTO studentDTO = studentService.findAll().get(0);
		assertEquals("John", studentDTO.getFirstName());
		assertEquals("Davidson", studentDTO.getLastName());
	}

	@Test
	void testUpdate() {
		StudentDTO initialDTO = studentService.findAll().get(0);
		StudentDTO newDTO = new StudentDTO();
		newDTO.setFirstName(initialDTO.getFirstName());
		newDTO.setLastName(initialDTO.getLastName());
		newDTO.setFirstName("NewName");
		studentService.update(initialDTO, newDTO);
		StudentDTO updatedStudentDTO = studentService.findAll().get(0);
		assertEquals(newDTO, updatedStudentDTO);
	}

	@Test
	void testDelete() {
		List<StudentDTO> listDTO = studentService.findAll();
		int count = 1;
		if (!listDTO.isEmpty()) {
			count = listDTO.size();
		}
		studentService.delete(listDTO.get(0));
		List<StudentDTO> listAfterDel = studentService.findAll();
		int countAfterDel = 0;
		if (!listAfterDel.isEmpty()) {
			countAfterDel = listAfterDel.size();
		}
		assertEquals((count - 1), countAfterDel);
	}

	@Test
	void testFindAll() {
		assertEquals(5, studentService.findAll().size());
	}

}
