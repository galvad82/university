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

}
