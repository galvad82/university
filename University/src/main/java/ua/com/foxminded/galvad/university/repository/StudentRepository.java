package ua.com.foxminded.galvad.university.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

public interface StudentRepository extends CrudRepository<Student, Integer> {
	
	@SuppressWarnings("unchecked")
	Student save(Student entity);
	
	Optional<Student> findById(Integer id);

	Student findByFirstNameAndLastName(String firstName, String lastName);
	
	void deleteById(Integer id);
	
	void delete(Student entity);
	
	List<Student>findAll();

	@Modifying
	@Query("Update Student st SET st.group=?2 WHERE st.id=?1")
	void addStudentToGroup(Integer studentId, Group group);
	
	@Modifying
	@Query("Update Student st SET st.group=null WHERE st.id=?1")
	void removeStudentFromGroups(Integer studentId);
}
