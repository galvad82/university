package ua.com.foxminded.galvad.university.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Group {

	private Integer id;
	private String name;
	private List<Student> listOfStudent = new ArrayList<>();

	public Group(Integer id, String name) {
		if ((id != null) && (id >= 0) && (!StringUtils.isEmpty(name))) {
			this.id = id;
			this.name = name;
		} else {
			throw new IllegalArgumentException("Cannot create a Group because of illegal arguments");
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

}
