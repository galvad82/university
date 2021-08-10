package ua.com.foxminded.galvad.university.dto;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class LessonDTO {
	
	private GroupDTO group;
	private CourseDTO course;
	private ClassroomDTO classroom;
	private Long startTime;
	private Long duration;
	private String startTimeString;
	private String durationString;


	public GroupDTO getGroup() {
		return group;
	}

	public void setGroup(GroupDTO group) {
		this.group = group;
	}

	public CourseDTO getCourse() {
		return course;
	}

	public void setCourse(CourseDTO course) {
		this.course = course;
	}

	public ClassroomDTO getClassroom() {
		return classroom;
	}

	public void setClassroom(ClassroomDTO classroom) {
		this.classroom = classroom;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
		this.startTimeString = convertMilToDate(startTime);
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
		this.durationString=convertMilToTime(duration);
	}
	
	public String getStartTimeString() {
		return startTimeString;
	}

	public String getDurationString() {
		return durationString;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classroom == null) ? 0 : classroom.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
		LessonDTO other = (LessonDTO) obj;
		if (classroom == null) {
			if (other.classroom != null)
				return false;
		} else if (!classroom.equals(other.classroom))
			return false;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
	
	
	
	private String convertMilToDate(long millis) {
		Instant instance = java.time.Instant.ofEpochMilli(millis);
		return java.time.LocalDateTime.ofInstant(instance, java.time.ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
	}

	private String convertMilToTime(long millis) {
		long minutes = (millis / (1000*60)) % 60;
		long hours   = (millis / (1000*60*60)) % 24;
		return String.format("%02d:%02d",hours,minutes);
	}
	
}
