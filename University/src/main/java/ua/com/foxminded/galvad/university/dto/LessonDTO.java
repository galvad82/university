package ua.com.foxminded.galvad.university.dto;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lesson DTO")
public class LessonDTO extends RepresentationModel<LessonDTO> {

	@Schema(description = "Unique identifier", example = "1", required = true)
	private Integer id;
	@Schema(description = "Group DTO", required = true)
	private GroupDTO group;
	@Schema(description = "Course DTO", required = true)
	private CourseDTO course;
	@Schema(description = "Classroom DTO", required = true)
	private ClassroomDTO classroom;
	@Schema(description = "Start time in Millis cannot be earlier than UTC Jan-01-2021 00:00:01!", example = "1609459201000", required = true)
	private Long startTime;
	@Schema(description = "Duration cannot be less than 30 minutes!", example = "1800000", required = true)
	private Long duration;
	@Schema(description = "Start time in regular format (Jan-01-2021 00:00:01)", accessMode = Schema.AccessMode.READ_ONLY)
	private String startTimeString;
	@Schema(description = "Duration in regular format (00:30)", accessMode = Schema.AccessMode.READ_ONLY)
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

	public Long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
		this.durationString = convertMilToTime(duration);
	}

	public String getStartTimeString() {
		return startTimeString;
	}

	public String getDurationString() {
		return durationString;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private String convertMilToDate(long millis) {
		Instant instance = java.time.Instant.ofEpochMilli(millis);
		return java.time.LocalDateTime.ofInstant(instance, java.time.ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
	}

	private String convertMilToTime(long millis) {
		long minutes = (millis / (1000 * 60)) % 60;
		long hours = (millis / (1000 * 60 * 60)) % 24;
		return String.format("%02d:%02d", hours, minutes);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classroom == null) ? 0 : classroom.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((durationString == null) ? 0 : durationString.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((startTimeString == null) ? 0 : startTimeString.hashCode());
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
		if (durationString == null) {
			if (other.durationString != null)
				return false;
		} else if (!durationString.equals(other.durationString))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		if (startTimeString == null) {
			if (other.startTimeString != null)
				return false;
		} else if (!startTimeString.equals(other.startTimeString))
			return false;
		return true;
	}

}
