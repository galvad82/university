package ua.com.foxminded.galvad.university.model;

import org.apache.commons.lang3.StringUtils;

public class Teacher {

	private Integer id;
	private String firstName;
	private String lastName;

	public Teacher(Integer id, String firstName, String lastName) {
		if ((id != null) && (id >= 0) && (!StringUtils.isEmpty(firstName)) && (!StringUtils.isEmpty(lastName))) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
		} else {
			throw new IllegalArgumentException("Cannot create a Teacher because of illegal arguments");
		}
	}

	public Teacher(Integer id) {
		if ((id != null) && (id >= 0)) {
			this.id = id;
		} else {
			throw new IllegalArgumentException("Cannot create a Teacher because of illegal arguments");
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
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
		Teacher other = (Teacher) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

}
