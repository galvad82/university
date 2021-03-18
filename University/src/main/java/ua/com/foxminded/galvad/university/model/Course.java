package ua.com.foxminded.galvad.university.model;

import java.util.ArrayList;
import java.util.List;

public class Course {
	private static List<Course> listOfCourses = new ArrayList<>();

	private int id;
	private String name;
	private Teacher teacher;

	public Course(int id, String name) {
		if ((id >= 0) && (!name.isEmpty())) {
			this.id = id;
			this.name = name;
			listOfCourses.add(this);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public static List<Course> getListOfCourses() {
		return listOfCourses;
	}

	public int getId() {
		return id;
	}

}
