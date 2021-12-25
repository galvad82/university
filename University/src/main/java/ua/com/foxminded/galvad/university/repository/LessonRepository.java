package ua.com.foxminded.galvad.university.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;

public interface LessonRepository extends CrudRepository<Lesson, Integer> {
	
	@SuppressWarnings("unchecked")
	Lesson save(Lesson entity);
	
	void deleteById(Integer id);
	
	void delete(Lesson entity);
	
	List<Lesson>findAll();
	
	@Modifying
	@Query("DELETE FROM Lesson l WHERE l.classroom=?1")
	void deleteByClassroom(Classroom classroom);
	
	@Modifying
	@Query("DELETE FROM Lesson l WHERE l.course=?1")
	void deleteByCourse(Course course);
	
	@Modifying
	@Query("DELETE FROM Lesson l WHERE l.group=?1")
	void deleteByGroup(Group group);

	@Query("SELECT l.id FROM Lesson AS l WHERE l.group=?1 AND l.course=?2 AND l.classroom=?3 AND l.startTime=?4 AND l.duration=?5")
	Integer getID(Group group, Course course, Classroom classroom, Long startTime, Long duration);
}