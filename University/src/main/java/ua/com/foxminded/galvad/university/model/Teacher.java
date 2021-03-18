package ua.com.foxminded.galvad.university.model;

import java.util.ArrayList;
import java.util.List;

public class Teacher {

	private static List<Teacher> listOfTeachers = new ArrayList<>();

	private int id;
	private String firstName;
	private String lastName;

	public Teacher(int id, String firstName, String lastName) {
		if ((!firstName.isEmpty()) && (!lastName.isEmpty()) && (id >= 0)) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			listOfTeachers.add(this);
		}
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

	public int getId() {
		return id;
	}

	public static List<Teacher> getListOfTeachers() {
		return listOfTeachers;
	}

}
