package ua.com.foxminded.galvad.university.model;

public class Event {

	private String title;
	private Long start;
	private Long end;

	public Event(String title, Long start, Long end) throws IllegalArgumentException {

		if ((!title.isEmpty()) && (start > 0) && (end >= start)) {
			this.title = title;
			this.start = start;
			this.end = end;
		} else {
			throw new IllegalArgumentException("Wrong parameters!");
		}
	}

	public String getTitle() {
		return title;
	}

	public Long getStart() {
		return start;
	}

	public Long getEnd() {
		return end;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Event other = (Event) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
}
