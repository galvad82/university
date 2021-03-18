package ua.com.foxminded.galvad.university.model;

import java.util.ArrayList;
import java.util.List;

public class Classroom {

	private static List<Classroom> listOfClassrooms = new ArrayList<>();

	private int id;
	private String name;

	public Classroom(int id, String name) {
		if ((id >= 0) && (!name.isEmpty())) {
			this.id = id;
			this.name = name;
			listOfClassrooms.add(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Classroom> getListOfClassrooms() {
		return listOfClassrooms;
	}

	public int getId() {
		return id;
	}

}
