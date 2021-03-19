package ua.com.foxminded.galvad.university.model;

public class Lesson {

	private Integer id;
	private Group group;
	private Course course;
	private Classroom classroom;
	private Long startTime;
	private Long duration;

	public Lesson(Integer id, Group group, Course course, Classroom classroom, Long startTime, Long duration) {
		if ((id != null) && (id >= 0) && (startTime != null) && (startTime > 0) && (duration != null) && (duration > 0)
				&& (group != null) && (course != null) && (classroom != null)) {
			this.id = id;
			this.group = group;
			this.course = course;
			this.classroom = classroom;
			this.startTime = startTime;
			this.duration = duration;
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

	public int getId() {
		return id;
	}

}
