package ua.com.foxminded.galvad.university.dto;

import java.util.ArrayList;
import java.util.List;

public class GroupDTO {
	private String name;
	private List<StudentDTO> listOfStudent = new ArrayList<>();


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StudentDTO> getListOfStudent() {
		return listOfStudent;
	}

	public void setListOfStudent(List<StudentDTO> listOfStudent) {
		this.listOfStudent = listOfStudent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		GroupDTO other = (GroupDTO) obj;
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
