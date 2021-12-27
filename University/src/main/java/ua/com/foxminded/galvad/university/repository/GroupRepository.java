package ua.com.foxminded.galvad.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.galvad.university.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
	
	Group findByName(String name);

}
