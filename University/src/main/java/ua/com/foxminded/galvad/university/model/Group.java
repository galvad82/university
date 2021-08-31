package ua.com.foxminded.galvad.university.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "groups")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	@Column(name = "name")
	private String name;
	@OneToMany(mappedBy="group", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Set<Student> setOfStudent = new HashSet<>();

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

	public Integer getId() {
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

	public Set<Student> getSetOfStudent() {
		return setOfStudent;
	}

	public void setSetOfStudent(Set<Student> setOfStudent) {
		if (!setOfStudent.isEmpty()) {
		setOfStudent.stream().forEach(student -> student.setGroup(this));
		this.setOfStudent = setOfStudent;
		}
	}
}
