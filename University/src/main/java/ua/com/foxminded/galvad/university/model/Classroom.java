package ua.com.foxminded.galvad.university.model;
import org.apache.commons.lang3.StringUtils;

public class Classroom {

	private Integer id;
	private String name;

	public Classroom(Integer id, String name) {
		if ((id != null) && (id >= 0) && (!StringUtils.isEmpty(name))) {
			this.id = id;
			this.name = name;
		} else {
			throw new IllegalArgumentException("Cannot create a Classroom because of illegal arguments");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

}
