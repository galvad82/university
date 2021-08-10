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
import ua.com.foxminded.galvad.university.dao.impl.ClassroomDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;

@SpringJUnitWebConfig(SpringConfigTest.class)
class ClassroomServiceTest {

	@Autowired
	private ClassroomService classroomService;
	
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
		ClassroomDTO classroomDTO = classroomService.retrieve("ROOM-15");
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

	@Test
	void testFindAllLessonsForClassroom() {
		List<LessonDTO> listOfLessons = classroomService.findAllLessonsForClassroom("ROOM-15");
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
