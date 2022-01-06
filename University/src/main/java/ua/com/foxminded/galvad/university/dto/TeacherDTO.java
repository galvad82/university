package ua.com.foxminded.galvad.university.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TeacherDTO {

	@NotBlank(message = "First name cannot be empty")
	@Pattern(regexp="^[a-zA-Z ]+$",message="Only letters and spaces are accepted")
	private String firstName;
	
	@NotBlank(message = "Last name cannot be empty")
	@Pattern(regexp="^[a-zA-Z ]+$",message="Only letters and spaces are accepted")
	private String lastName;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
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
		TeacherDTO other = (TeacherDTO) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

}
