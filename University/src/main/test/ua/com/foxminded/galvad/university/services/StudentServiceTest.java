package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;

@SpringJUnitWebConfig(SpringConfigTest.class)
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
		studentDTO.setLastName("ZLast Name");
		studentService.create(studentDTO);
		StudentDTO retrievedDTO = studentService.findAll().get(6);
		assertEquals(studentDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		StudentDTO studentDTO = studentService.retrieve("John", "Davidson");
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
	void testUpdateGroup_shouldReturnSecondGroupForStudentAfterUpdate() {
		StudentDTO studentDTO = studentService.findAll().get(0);
		Map<StudentDTO, String> studentGroupMap = studentService.buildStudentGroupMap();
		String initialGroupName=studentGroupMap.get(studentDTO);
		assertEquals("AB-123", initialGroupName);
		GroupDTO newGroupDTO = new GroupDTO();
		newGroupDTO.setName("CD-456");
		studentService.updateGroup(studentDTO, newGroupDTO);
		studentGroupMap = studentService.buildStudentGroupMap();
		String newGroupName=studentGroupMap.get(studentDTO);
		assertEquals("CD-456", newGroupName);
	}

	@Test
	void testDelete() {
		List<StudentDTO> listBeforeDel = studentService.findAll();
		studentService.delete(listBeforeDel.get(0));
		List<StudentDTO> listAfterDel = studentService.findAll();
		assertEquals(listBeforeDel.size()-1, listAfterDel.size());
	}

	@Test
	void testFindAll_shouldReturnListOfSixSortedDTOs() {
		List<StudentDTO> retrievedList = studentService.findAll();
		assertEquals(6, retrievedList.size());
		List<StudentDTO> expectedList = new ArrayList<>();
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("John");
		studentDTO.setLastName("Davidson");
		expectedList.add(studentDTO);
		studentDTO = new StudentDTO();
		studentDTO.setFirstName("Mike");
		studentDTO.setLastName("Dombrovsky");
		expectedList.add(studentDTO);
		studentDTO = new StudentDTO();
		studentDTO.setFirstName("Peter");
		studentDTO.setLastName("Eastwood");
		expectedList.add(studentDTO);
		studentDTO = new StudentDTO();
		studentDTO.setFirstName("Nick");
		studentDTO.setLastName("Johnson");
		expectedList.add(studentDTO);
		studentDTO = new StudentDTO();
		studentDTO.setFirstName("Michael");
		studentDTO.setLastName("Murray");
		expectedList.add(studentDTO);
		studentDTO = new StudentDTO();
		studentDTO.setFirstName("Student");
		studentDTO.setLastName("WithoutGroup");
		expectedList.add(studentDTO);
		assertEquals(expectedList, retrievedList);
	}
	
	@Test
	void testFindAllUnassignedStudents(){
		List<StudentDTO> listOfStudents = studentService.findAllUnassignedStudents();
		assertEquals(1, listOfStudents.size());
		assertEquals("Student", listOfStudents.get(0).getFirstName());
		assertEquals("WithoutGroup", listOfStudents.get(0).getLastName());	
	}
	
	@Test
	void testBuildStudentGroupMap(){
		Map<StudentDTO, String> mapOfStudents = studentService.buildStudentGroupMap();
		assertEquals(6, mapOfStudents.size());
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("John");
		studentDTO.setLastName("Davidson");
		assertEquals("AB-123",mapOfStudents.get(studentDTO));
		studentDTO.setFirstName("Nick");
		studentDTO.setLastName("Johnson");
		assertEquals("AB-123",mapOfStudents.get(studentDTO));
		studentDTO.setFirstName("Peter");
		studentDTO.setLastName("Eastwood");
		assertEquals("CD-456",mapOfStudents.get(studentDTO));
		studentDTO.setFirstName("Michael");
		studentDTO.setLastName("Murray");
		assertEquals("CD-456",mapOfStudents.get(studentDTO));
		studentDTO.setFirstName("Mike");
		studentDTO.setLastName("Dombrovsky");
		assertEquals("CD-456",mapOfStudents.get(studentDTO));
		studentDTO.setFirstName("Student");
		studentDTO.setLastName("WithoutGroup");
		assertEquals("NONE",mapOfStudents.get(studentDTO));
	}

	@Test
	void testAddToGroup(){
		List<StudentDTO> listOfStudents = studentService.findAllUnassignedStudents();
		assertEquals(1, listOfStudents.size());
		StudentDTO studentDTO = listOfStudents.get(0);
		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		studentService.addToGroup(studentDTO, groupDTO);
		listOfStudents = studentService.findAllUnassignedStudents();
		assertEquals(0, listOfStudents.size());
	}
	
	@Test
	void testRemoveStudentFromGroup(){
		List<StudentDTO> listOfStudents = studentService.findAllUnassignedStudents();
		assertEquals(1, listOfStudents.size());
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.setFirstName("John");
		studentDTO.setLastName("Davidson");
		studentService.removeStudentFromGroup(studentDTO);
		listOfStudents = studentService.findAllUnassignedStudents();
		assertEquals(2, listOfStudents.size());
	}
}
