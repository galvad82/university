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

	public Group(Integer id) {
		if ((id != null) && (id >= 0)) {
			this.id = id;
		} else {
			throw new IllegalArgumentException("Cannot create a Group because of illegal arguments");
		}
	}

	public Group() {

	}

	public int getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((listOfStudent == null) ? 0 : listOfStudent.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (listOfStudent == null) {
			if (other.listOfStudent != null)
				return false;
		} else if (!listOfStudent.equals(other.listOfStudent))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
