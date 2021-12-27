package ua.com.foxminded.galvad.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.com.foxminded.galvad.university.model.Group;
import ua.com.foxminded.galvad.university.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	Student findByFirstNameAndLastName(String firstName, String lastName);

	@Modifying(clearAutomatically = true)
	@Query("Update Student st SET st.group=?2 WHERE st.id=?1")
	void addStudentToGroup(Integer studentId, Group group);
	
	@Modifying(clearAutomatically = true)
	@Query("Update Student st SET st.group=null WHERE st.id=?1")
	void removeStudentFromGroups(Integer studentId);
}
