package ua.com.foxminded.galvad.university.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ua.com.foxminded.galvad.university.model.Group;

public interface GroupRepository extends CrudRepository<Group, Integer> {
	
	@SuppressWarnings("unchecked")
	Group save(Group entity);
	
	Optional<Group> findById(Integer id);
	
	Group findByName(String name);
	
	void deleteById(Integer id);
	
	void delete(Group entity);
	
	List<Group>findAll();

}
