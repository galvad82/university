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
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;

@SpringJUnitConfig(SpringConfig.class)
class ClassroomServiceTest {

	@Autowired
	private ClassroomService classroomService = new ClassroomService();
	@Autowired
	private ClassroomDAO classroomDAO;

	private DataSource dataSource;

	@BeforeEach
	void setDatasoure() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		classroomDAO.setDataSource(dataSource);
	}

	@Test
	void testCreate() {
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("TestName");
		classroomService.create(classroomDTO);
		ClassroomDTO retrievedDTO = classroomService.findAll().get(2);
		assertEquals(classroomDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		ClassroomDTO classroomDTO = classroomService.retrieve(1);
		assertEquals("ROOM-15", classroomDTO.getName());
	}

	@Test
	void testRetrieveByFindAll() {
		ClassroomDTO classroomDTO = classroomService.findAll().get(0);
		assertEquals("ROOM-15", classroomDTO.getName());
	}

	@Test
	void testUpdate() {
		ClassroomDTO initialDTO = classroomService.findAll().get(0);
		ClassroomDTO newDTO = new ClassroomDTO();
		newDTO.setName(initialDTO.getName());
		newDTO.setName("NewName");
		classroomService.update(initialDTO, newDTO);
		ClassroomDTO updatedClassroomDTO = classroomService.findAll().get(0);
		assertEquals(newDTO, updatedClassroomDTO);
	}

	@Test
	void testDelete() {
		List<ClassroomDTO> listDTO = classroomService.findAll();
		int count = 1;
		if (!listDTO.isEmpty()) {
			count = listDTO.size();
		}
		classroomService.delete(listDTO.get(0));
		List<ClassroomDTO> listAfterDel = classroomService.findAll();
		int countAfterDel = 0;
		if (!listAfterDel.isEmpty()) {
			countAfterDel = listAfterDel.size();
		}
		assertEquals((count - 1), countAfterDel);
	}

	@Test
	void testFindAll() {
		assertEquals(2, classroomService.findAll().size());
	}

}
