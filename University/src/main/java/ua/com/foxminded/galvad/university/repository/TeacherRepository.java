package ua.com.foxminded.galvad.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.foxminded.galvad.university.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	
	Teacher findByFirstNameAndLastName(String firstName, String lastName);
	
}
