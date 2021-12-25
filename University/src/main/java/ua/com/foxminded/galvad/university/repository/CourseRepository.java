package ua.com.foxminded.galvad.university.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ua.com.foxminded.galvad.university.model.Course;

public interface CourseRepository extends CrudRepository<Course, Integer> {
	
	@SuppressWarnings("unchecked")
	Course save(Course entity);
	
	Optional<Course> findById(Integer id);
	
	Course findByName(String name);
	
	void deleteById(Integer id);
	
	void delete(Course entity);
	
	List<Course>findAll();

}
