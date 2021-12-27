package ua.com.foxminded.galvad.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.galvad.university.model.Classroom;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {
		
	Classroom findByName(String name);

}
