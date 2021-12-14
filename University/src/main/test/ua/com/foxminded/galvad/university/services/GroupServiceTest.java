package ua.com.foxminded.galvad.university.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ua.com.foxminded.galvad.university.dto.GroupDTO;
import ua.com.foxminded.galvad.university.dto.LessonDTO;
import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;
import ua.com.foxminded.galvad.university.model.Student;
import ua.com.foxminded.galvad.university.model.Teacher;

@DataJpaTest
@ComponentScan("ua.com.foxminded.galvad.university")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class GroupServiceTest {

	@Autowired
	private GroupService groupService;

	@Autowired
	private LessonService lessonService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void testCreate() {
		GroupDTO groupDTO = createDTO("TestName");
		GroupDTO retrievedDTO = groupService.findAll().get(0);
		assertEquals(groupDTO, retrievedDTO);
	}

	@Test
	void testRetrieve() {
		GroupDTO groupDTO = createDTO("TestName");
		GroupDTO retrievedDTO = groupService.retrieve("TestName");
		assertEquals(groupDTO, retrievedDTO);
	}

	@Test
	void testRetrieveByFindAll() {
		GroupDTO groupDTO = createDTO("TestName");
		GroupDTO retrievedDTO = groupService.findAll().get(0);
		assertEquals(groupDTO, retrievedDTO);
	}

	@Test
	void testUpdate() {
		GroupDTO initialDTO = createDTO("TestName");
		GroupDTO newDTO = new GroupDTO();
		newDTO.setListOfStudent(initialDTO.getListOfStudent());
		newDTO.setName("AA-123");
		groupService.update(initialDTO, newDTO);
		GroupDTO updatedGroupDTO = groupService.findAll().get(0);
		assertEquals(newDTO, updatedGroupDTO);
	}

	@Test
	void testDelete() {
		List<GroupDTO> expectedList = new ArrayList<>();
		expectedList.add(createDTO("TestName"));
		expectedList.add(createDTO("TestName2"));
		List<GroupDTO> listBeforeDel = groupService.findAll();
		assertEquals(expectedList, listBeforeDel);
		groupService.delete(expectedList.get(1));
		expectedList.remove(1);
		List<GroupDTO> listAfterDel = groupService.findAll();
		assertEquals(expectedList, listAfterDel);
	}

	@Test
	void testFindAll() {
		GroupDTO group = createDTO("TestName");
		GroupDTO group1 = createDTO("TestName1");
		GroupDTO group2 = createDTO("TestName2");
		List<GroupDTO> retrievedList = groupService.findAll();
		assertEquals(group, retrievedList.get(0));
		assertEquals(group1, retrievedList.get(1));
		assertEquals(group2, retrievedList.get(2));
	}

	@Test
	void testFindAllLessonsForGroup() {
		createLesson("Name", "FirstName", "LastName");
		createLesson("Name2", "FirstName2", "LastName2");
		createLesson("Name3", "FirstName3", "LastName3");
		List<LessonDTO> listOfLessons = lessonService.findAllLessonsForGroup("Name3");
		assertEquals("Name3", listOfLessons.get(0).getClassroom().getName());
		assertEquals("Name3", listOfLessons.get(0).getCourse().getName());
		assertEquals("LastName3", listOfLessons.get(0).getCourse().getTeacher().getLastName());
		assertEquals("Name3", listOfLessons.get(0).getGroup().getName());
		assertEquals(2222L, listOfLessons.get(0).getDuration());
		assertEquals(111111L, listOfLessons.get(0).getStartTime());
		assertEquals(1, listOfLessons.size());
	}

	private GroupDTO createDTO(String name) {
		Group group = new Group();
		group.setName(name);
		Student student = new Student();
		student.setFirstName(name);
		student.setLastName("LastName");
		entityManager.persist(student);
		Set<Student> setOfStudents = new HashSet<>();
		setOfStudents.add(student);
		group.setSetOfStudent(setOfStudents);
		entityManager.persist(group);
		return groupService.retrieve(name);
	}

	private Lesson createLesson(String name, String firstName, String lastName) {
		Student student = new Student();
		student.setFirstName(firstName);
		student.setLastName(lastName);
		entityManager.persist(student);
		Group group = new Group();
		group.setName(name);
		Set<Student> setOfStudents = new HashSet<>();
		setOfStudents.add(student);
		group.setSetOfStudent(setOfStudents);
		entityManager.persist(group);
		Teacher teacher = new Teacher();
		teacher.setFirstName(firstName);
		teacher.setLastName(lastName);
		entityManager.persist(teacher);
		Course course = new Course();
		course.setName(name);
		course.setTeacher(teacher);
		entityManager.persist(course);
		Classroom classroom = new Classroom();
		classroom.setName(name);
		entityManager.persist(classroom);
		Lesson entity = new Lesson();
		entity.setClassroom(classroom);
		entity.setCourse(course);
		entity.setGroup(group);
		entity.setStartTime(111111L);
		entity.setDuration(2222L);
		entityManager.persist(entity);
		return entity;
	}
}
