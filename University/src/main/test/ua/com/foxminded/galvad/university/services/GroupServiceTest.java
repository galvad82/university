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
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import ua.com.foxminded.galvad.university.config.SpringConfigTest;
import ua.com.foxminded.galvad.university.dao.impl.GroupDAO;
import ua.com.foxminded.galvad.university.dao.impl.StudentDAO;
import ua.com.foxminded.galvad.university.dto.ClassroomDTO;
import ua.com.foxminded.galvad.university.dto.CourseDTO;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.dto.StudentDTO;
import ua.com.foxminded.galvad.university.dto.TeacherDTO;
import ua.com.foxminded.galvad.university.model.Student;

@SpringJUnitWebConfig(SpringConfigTest.class)
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
		GroupDTO groupDTO = groupService.retrieve("AB-123");
		assertEquals("AB-123", groupDTO.getName());
		assertTrue(groupDTO.getListOfStudent().isEmpty());
	}
	
	@Test
	void testRetrieveWithListOfStudents() {
		GroupDTO groupDTO = groupService.retrieveWithListOfStudents("AB-123");
		assertEquals("AB-123", groupDTO.getName());
		assertEquals(2, groupDTO.getListOfStudent().size());
		assertEquals("John", groupDTO.getListOfStudent().get(0).getFirstName());
		assertEquals("Nick", groupDTO.getListOfStudent().get(1).getFirstName());
		assertEquals("Davidson", groupDTO.getListOfStudent().get(0).getLastName());
		assertEquals("Johnson", groupDTO.getListOfStudent().get(1).getLastName());
	}
	
	@Test
	void testGetGroupNameForStudent() {
		assertEquals("AB-123", groupService.getGroupNameForStudent(new Student(1, "John", "Davidson")));
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
		newDTO.setName("AA-123");
		groupService.update(initialDTO, newDTO);
		GroupDTO updatedGroupDTO = groupService.findAll().get(0);
		assertEquals(newDTO, updatedGroupDTO);
	}

	@Test
	void testDelete() {
		List<GroupDTO> listBeforeDel = groupService.findAll();
		groupService.delete(listBeforeDel.get(0));
		List<GroupDTO> listAfterDel = groupService.findAll();
		assertEquals(listBeforeDel.size() - 1, listAfterDel.size());
	}

	@Test
	void testFindAll() {
		List<GroupDTO> list = groupService.findAll();
		GroupDTO groupDTO = list.get(0);
		assertEquals(2, list.size());
		assertEquals("AB-123", groupDTO.getName());
		assertEquals("John", groupDTO.getListOfStudent().get(0).getFirstName());
		assertEquals("Nick", groupDTO.getListOfStudent().get(1).getFirstName());
		assertEquals("Davidson", groupDTO.getListOfStudent().get(0).getLastName());
		assertEquals("Johnson", groupDTO.getListOfStudent().get(1).getLastName());
		assertEquals(2, groupDTO.getListOfStudent().size());
		GroupDTO groupDTO2 = list.get(1);
		assertEquals("CD-456", groupDTO2.getName());
		assertEquals("Mike", groupDTO2.getListOfStudent().get(0).getFirstName());
		assertEquals("Peter", groupDTO2.getListOfStudent().get(1).getFirstName());
		assertEquals("Dombrovsky", groupDTO2.getListOfStudent().get(0).getLastName());
		assertEquals("Eastwood", groupDTO2.getListOfStudent().get(1).getLastName());
		assertEquals(3, groupDTO2.getListOfStudent().size());
	}

	@Test
	void testFindAllWithoutStudentList() {
		List<GroupDTO> list = groupService.findAllWithoutStudentList();
		GroupDTO groupDTO = list.get(0);
		assertEquals(2, list.size());
		assertEquals("AB-123", groupDTO.getName());
		assertTrue(groupDTO.getListOfStudent().isEmpty());
		GroupDTO groupDTO2 = list.get(1);
		assertEquals("CD-456", groupDTO2.getName());
		assertTrue(groupDTO2.getListOfStudent().isEmpty());
	}
	
	@Test
	void testFindAllLessonsForGroup() {

		GroupDTO groupDTO = new GroupDTO();
		groupDTO.setName("AB-123");
		StudentDTO studentA = new StudentDTO();
		studentA.setFirstName("John");
		studentA.setLastName("Davidson");
		StudentDTO studentB=new StudentDTO();
		studentB.setFirstName("Nick");
		studentB.setLastName("Johnson");
		List<StudentDTO> listOfStudents = new ArrayList<>();
		listOfStudents.add(studentA);
		listOfStudents.add(studentB);
		groupDTO.setListOfStudent(listOfStudents);		
		
		ClassroomDTO classroomDTO = new ClassroomDTO();
		classroomDTO.setName("ROOM-15");
		CourseDTO courseDTO = new CourseDTO();
		courseDTO.setName("Science");
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setFirstName("Jennie");
		teacherDTO.setLastName("Crigler");
		courseDTO.setTeacher(teacherDTO);
		
		LessonDTO lessonDTO = new LessonDTO();
		lessonDTO.setClassroom(classroomDTO);
		lessonDTO.setCourse(courseDTO);
		lessonDTO.setGroup(groupDTO);
		lessonDTO.setDuration(2700000l);
		lessonDTO.setStartTime(1616510000000l);		
		List<LessonDTO> listOfLessons = new ArrayList<LessonDTO>();
		listOfLessons.add(lessonDTO);

		assertEquals(listOfLessons, groupService.findAllLessonsForGroup("AB-123"));
	}
}
