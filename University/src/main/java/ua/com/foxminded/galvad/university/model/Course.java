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

	public Course(Integer id) {
		if ((id != null) && (id >= 0)) {
			this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
		Course other = (Course) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} else if (!teacher.equals(other.teacher))
			return false;
		return true;
	}

}
