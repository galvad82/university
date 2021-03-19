package ua.com.foxminded.galvad.university.model;

import org.apache.commons.lang3.StringUtils;

public class Course {

	private Integer id;
	private String name;
	private Teacher teacher;

	public Course(Integer id, String name) {
		if ((id != null) && (id >= 0) && (!StringUtils.isEmpty(name))) {
			this.id = id;
			this.name = name;
		} else {
			throw new IllegalArgumentException("Cannot create a Course because of illegal arguments");
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

	public int getId() {
		return id;
	}

}
