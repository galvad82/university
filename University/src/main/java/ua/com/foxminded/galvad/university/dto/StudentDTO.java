package ua.com.foxminded.galvad.university.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Student DTO")
public class StudentDTO extends RepresentationModel<StudentDTO> {

	@Schema(description = "Unique identifier", example = "1", required = true)
	private Integer id;

	@NotBlank(message = "First name cannot be empty")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Only letters and spaces are accepted")
	@Schema(description = "First Name, Only letters and spaces are accepted", example = "John Fitzgerald", required = true)
	private String firstName;

	@NotBlank(message = "Last name cannot be empty")
	@Pattern(regexp = "^[a-zA-Z ]+$", message = "Only letters and spaces are accepted")
	@Schema(description = "Last Name, Only letters and spaces are accepted", example = "Kennedy", required = true)
	private String lastName;

	@Schema(description = "Group DTO", example = "Group1", required = false)
	private GroupDTO groupDTO;

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

	public GroupDTO getGroupDTO() {
		return groupDTO;
	}

	public void setGroupDTO(GroupDTO groupDTO) {
		this.groupDTO = groupDTO;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((groupDTO == null) ? 0 : groupDTO.hashCode());
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
		StudentDTO other = (StudentDTO) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (groupDTO == null) {
			if (other.groupDTO != null)
				return false;
		} else if (!groupDTO.equals(other.groupDTO))
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
