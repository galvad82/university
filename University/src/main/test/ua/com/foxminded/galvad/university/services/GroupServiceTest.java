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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.galvad.university.config.SpringConfig;
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;

@SpringJUnitConfig(SpringConfig.class)
class GroupServiceTest {

	@Autowired
	private GroupDAO groupDAO;
	@Autowired
	private StudentDAO studentDAO;
	@Autowired
	private StudentService studentService;
	@Autowired
	private GroupService groupService = new GroupService();

	private DataSource dataSource;

	@BeforeEach
	void setDatasoure() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:schema.sql")
				.addScript("classpath:test-data.sql").build();
		groupDAO.setDataSource(dataSource);
		studentDAO.setDataSource(dataSource);
	}

	@Test
	void testCreate() {
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("TestName");
		List<StudentDTO> list = new ArrayList<>();
		list.add(studentService.findAll().get(0));
		list.add(studentService.findAll().get(1));
		groupDTO.setListOfStudent(list);
		groupService.create(groupDTO);
		GroupDTO retrievedDTO = groupService.findAll().get(2);
		assertEquals(groupDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		GroupDTO groupDTO = groupService.retrieve(1);
		assertEquals("AB-123", groupDTO.getName());
		assertEquals("John", groupDTO.getListOfStudent().get(0).getFirstName());
		assertEquals("Nick", groupDTO.getListOfStudent().get(1).getFirstName());
		assertEquals("Davidson", groupDTO.getListOfStudent().get(0).getLastName());
		assertEquals("Johnson", groupDTO.getListOfStudent().get(1).getLastName());
	}

	@Test
	void testRetrieveByFindAll() {
		GroupDTO groupDTO = groupService.findAll().get(0);
		assertEquals("AB-123", groupDTO.getName());
		assertEquals("John", groupDTO.getListOfStudent().get(0).getFirstName());
		assertEquals("Nick", groupDTO.getListOfStudent().get(1).getFirstName());
		assertEquals("Davidson", groupDTO.getListOfStudent().get(0).getLastName());
		assertEquals("Johnson", groupDTO.getListOfStudent().get(1).getLastName());
	}

	@Test
	void testUpdate() {
		GroupDTO initialDTO = groupService.findAll().get(0);
		GroupDTO newDTO = new GroupDTO();
		newDTO.setName(initialDTO.getName());
		newDTO.setListOfStudent(initialDTO.getListOfStudent());
		newDTO.setName("NewName");
		groupService.update(initialDTO, newDTO);
		GroupDTO updatedGroupDTO = groupService.findAll().get(0);
		assertEquals(newDTO, updatedGroupDTO);
	}

	@Test
	void testDelete() {
		List<GroupDTO> listDTO = groupService.findAll();
		int count = 1;
		if (!listDTO.isEmpty()) {
			count = listDTO.size();
		}
		groupService.delete(listDTO.get(0));
		List<GroupDTO> listAfterDel = groupService.findAll();
		int countAfterDel = 0;
		if (!listAfterDel.isEmpty()) {
			countAfterDel = listAfterDel.size();
		}
		assertEquals((count - 1), countAfterDel);
	}

	@Test
	void testFindAll() {
		assertEquals(2, groupService.findAll().size());
	}

}
