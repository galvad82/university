package ua.com.foxminded.galvad.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.galvad.university.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
	
	Course findByName(String name);

}
