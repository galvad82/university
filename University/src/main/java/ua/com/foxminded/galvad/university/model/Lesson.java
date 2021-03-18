package ua.com.foxminded.galvad.university.model;

import java.util.ArrayList;
import java.util.List;

public class Lesson {

	private static List<Lesson> listOfLessons = new ArrayList<>();

	private int id;
	private Group group;
	private Course course;
	private Classroom classroom;
	private long startTime;
	private long duration;

	public Lesson(int id, Group group, Course course, Classroom classroom, long startTime, long duration) {
		if ((id >= 0) && (startTime > 0) && (duration > 0) && (group != null) && (course != null)
				&& (classroom != null)) {
			this.id = id;
			this.group = group;
			this.course = course;
			this.classroom = classroom;
			this.startTime = startTime;
			this.duration = duration;
			listOfLessons.add(this);
		}
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public static List<Lesson> getListOfLessons() {
		return listOfLessons;
	}

	public int getId() {
		return id;
	}

}
