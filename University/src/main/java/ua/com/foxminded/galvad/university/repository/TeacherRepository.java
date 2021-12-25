package ua.com.foxminded.galvad.university.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ua.com.foxminded.galvad.university.model.Teacher;

public interface TeacherRepository extends CrudRepository<Teacher, Integer> {

	@SuppressWarnings("unchecked")
	Teacher save(Teacher entity);
	
	Optional<Teacher> findById(Integer id);
	
	Teacher findByFirstNameAndLastName(String firstName, String lastName);
	
	void deleteById(Integer id);
	
	void delete(Teacher entity);
	
	List<Teacher>findAll();
	
}
