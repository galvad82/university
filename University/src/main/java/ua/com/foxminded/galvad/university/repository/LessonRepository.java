package ua.com.foxminded.galvad.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.galvad.university.model.Classroom;
import ua.com.foxminded.galvad.university.model.Course;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Lesson;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

	@Modifying
	@Query("DELETE FROM Lesson l WHERE l.classroom=?1")
	void deleteByClassroom(Classroom classroom);

	@Modifying
	@Query("DELETE FROM Lesson l WHERE l.course=?1")
	void deleteByCourse(Course course);

	@Modifying
	@Query("DELETE FROM Lesson l WHERE l.group=?1")
	void deleteByGroup(Group group);

	@Query("SELECT l.id FROM Lesson AS l WHERE l.group=:#{#lesson.group} AND"
			+ " l.course=:#{#lesson.course} AND l.classroom=:#{#lesson.classroom}"
			+ " AND l.startTime=:#{#lesson.startTime} AND l.duration=:#{#lesson.duration}")
	Integer getId(@Param("lesson") Lesson lesson);
}