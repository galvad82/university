package ua.com.foxminded.galvad.university.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ua.com.foxminded.galvad.university.model.Classroom;

public interface ClassroomRepository extends CrudRepository<Classroom, Integer> {
	
	@SuppressWarnings("unchecked")
	Classroom save(Classroom entity);
	
	Optional<Classroom> findById(Integer id);
	
	Classroom findByName(String name);
	
	void deleteById(Integer id);
	
	void delete(Classroom entity);
	
	List<Classroom>findAll();

}
