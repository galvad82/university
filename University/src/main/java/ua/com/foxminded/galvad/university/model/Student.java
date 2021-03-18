package ua.com.foxminded.galvad.university.model;

import java.util.ArrayList;
import java.util.List;

public class Student {

	private static List<Student> listOfStudents = new ArrayList<>();

	private int id;
	private String firstName;
	private String lastName;

	public Student(int id, String firstName, String lastName) {
		if ((!firstName.isEmpty()) && (!lastName.isEmpty()) && (id >= 0)) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			listOfStudents.add(this);
		}
	}

	public int getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public static List<Student> getListOfStudents() {
		return listOfStudents;
	}

}
