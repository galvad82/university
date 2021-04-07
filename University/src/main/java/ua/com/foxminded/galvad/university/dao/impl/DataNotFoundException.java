package ua.com.foxminded.galvad.university.dao.impl;

public class DataNotFoundException extends RuntimeException {

	private String errorMessage;
	private Exception exception;
	private String causeDescription = "";

	public DataNotFoundException(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public DataNotFoundException(String errorMessage, Exception e) {
		this.errorMessage = errorMessage;
		this.causeDescription = e.getCause().toString();
		this.exception = e;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Exception getException() {
		return exception;
	}

	public String getCauseDescription() {
		return causeDescription;
	}

}
