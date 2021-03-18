package ua.com.foxminded.galvad.university.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
	private static List<Group> listOfGroups = new ArrayList<>();

	private int id;
	private String name;
	private List<Student> listOfStudent = new ArrayList<>();

	public Group(int id, String name) {
		if ((!name.isEmpty()) && (id >= 0)) {
			this.id = id;
			this.name = name;
			listOfGroups.add(this);
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Student> getListOfStudent() {
		return listOfStudent;
	}

	public void setListOfStudent(List<Student> listOfStudent) {
		this.listOfStudent = listOfStudent;
	}

	public static List<Group> getListOfGroups() {
		return listOfGroups;
	}
}
