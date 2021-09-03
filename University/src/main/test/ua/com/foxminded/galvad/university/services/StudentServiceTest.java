package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

@SpringJUnitWebConfig(SpringConfigTest.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
class StudentServiceTest {

	@Autowired
	private StudentService studentService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void testCreate() {
		StudentDTO studentDTO = createDTO("FirstName", "LastName", "Group");
		StudentDTO retrievedDTO = studentService.findAll().get(0);
		assertEquals(studentDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		StudentDTO studentDTO = createDTO("FirstName", "LastName", "Group");
		StudentDTO retrievedDTO = studentService.retrieve("FirstName", "LastName");
		assertEquals(studentDTO, retrievedDTO);
	}

	@Test
	void testRetrieveByFindAll() {
		createDTO("FirstName", "LastName", "Group");
		StudentDTO expectedDTO = createDTO("FirstNameA", "LastNameA", "Group2");
		StudentDTO retrievedDTO = studentService.findAll().get(1);
		assertEquals(expectedDTO, retrievedDTO);
	}

	@Test
	void testUpdate() {
		createDTO("FirstName", "LastName", "Group");
		StudentDTO initialDTO = studentService.findAll().get(0);
		StudentDTO newDTO = new StudentDTO();
		newDTO.setFirstName("NewName");
		newDTO.setLastName("NewLastName");
		newDTO.setGroupDTO(null);
		studentService.update(initialDTO, newDTO);
		StudentDTO updatedStudentDTO = studentService.findAll().get(0);
		assertEquals(newDTO, updatedStudentDTO);
	}

	@Test
	void testUpdateGroup_shouldReturnSecondGroupForStudentAfterUpdate() {
		createDTO("FirstName", "LastName", "Group");
		createDTO("FirstNameB", "LastNameB", "Group2");
		StudentDTO studentDTO = studentService.findAll().get(0);
		assertEquals("Group", studentDTO.getGroupDTO().getName());
		studentService.updateGroup(studentDTO, studentService.findAll().get(1).getGroupDTO());
		studentDTO = studentService.findAll().get(0);
		assertEquals("Group2", studentDTO.getGroupDTO().getName());
	}

	@Test
	void testDelete() {
		List<StudentDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("FirstName", "LastName", "Group"));
		expectedList.add(createDTO("FirstNameB", "LastNameB", "Group2"));
		expectedList.add(createDTO("FirstNameC", "LastNameC", "Group3"));
		List<StudentDTO> listBeforeDel = studentService.findAll();
		assertEquals(expectedList, listBeforeDel);
		studentService.delete(expectedList.get(0));
		List<StudentDTO> listAfterDel = studentService.findAll();
		expectedList.remove(0);
		assertEquals(expectedList, listAfterDel);
		assertEquals(1, listBeforeDel.size() - listAfterDel.size());
	}

	@Test
	void testFindAll() {
		List<StudentDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("FirstName", "LastName", "Group"));
		expectedList.add(createDTO("FirstNameB", "LastNameB", "Group2"));
		expectedList.add(createDTO("FirstNameC", "LastNameC", "Group3"));
		List<StudentDTO> retrievedList = studentService.findAll();
		assertEquals(expectedList, retrievedList);
	}
	
	@Test
	void testFindAllUnassignedStudents(){
		createDTO("FirstName", "LastName", "Group");
		StudentDTO studentDTO= createDTO("FirstNameB", "LastNameB", "Group2");
		createDTO("FirstNameC", "LastNameC", "Group3");
		studentService.removeStudentFromGroup(studentDTO);
		studentDTO = studentService.retrieve("FirstNameB", "LastNameB");
		Set<StudentDTO> listOfStudents = studentService.findAllUnassignedStudents();
		assertTrue(listOfStudents.contains(studentDTO));
		assertEquals(1, listOfStudents.size());	
	}
	
	@Test
	void testBuildStudentGroupMap(){
		StudentDTO studentDTO = createDTO("FirstName", "LastName", "Group");
		StudentDTO studentDTO2= createDTO("FirstNameB", "LastNameB", "Group2");
		StudentDTO studentDTO3= createDTO("FirstNameC", "LastNameC", "Group2");
		StudentDTO studentDTO4= createDTO("FirstNameD", "LastNameD", "Group3");
		studentService.removeStudentFromGroup(studentDTO4);
		studentDTO4=studentService.findAll().get(3);
		Map<StudentDTO, String> mapOfStudents = studentService.buildStudentGroupMap();
		assertEquals(4, mapOfStudents.size());
		assertEquals("Group",mapOfStudents.get(studentDTO));
		assertEquals("Group2",mapOfStudents.get(studentDTO2));
		assertEquals("Group2",mapOfStudents.get(studentDTO3));
		assertEquals("NONE",mapOfStudents.get(studentDTO4));
	}

	@Test
	void testAddToGroup(){
		StudentDTO studentDTO = createDTO("FirstName", "LastName", "Group");
		StudentDTO studentDTO2= createDTO("FirstNameB", "LastNameB", "Group2");
		assertEquals("Group", studentDTO.getGroupDTO().getName());
		studentService.removeStudentFromGroup(studentDTO);
		studentDTO = studentService.retrieve("FirstName", "LastName");
		assertNull(studentDTO.getGroupDTO());
		studentService.addToGroup(studentDTO, studentDTO2.getGroupDTO());
		studentDTO = studentService.retrieve("FirstName", "LastName");
		assertEquals("Group2", studentDTO.getGroupDTO().getName());		
	}
	
	@Test
	void testRemoveStudentFromGroup(){
		StudentDTO studentDTO= createDTO("FirstNameB", "LastNameB", "Group2");
		assertEquals("Group2", studentDTO.getGroupDTO().getName());
		studentService.removeStudentFromGroup(studentDTO);
		studentDTO = studentService.retrieve("FirstNameB", "LastNameB");
		assertNull(studentDTO.getGroupDTO());
	}

	private StudentDTO createDTO(String firstName, String lastName, String groupName) {
		Group group = new Group();
		group.setName(groupName);
		entityManager.persist(group);
		Student student = new Student();
		student.setFirstName(firstName);
		student.setLastName(lastName);
		student.setGroup(group);
		entityManager.persist(student);
		return studentService.retrieve(firstName, lastName);
	}
}
