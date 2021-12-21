package ua.com.foxminded.galvad.university.dao.impl;

public class DataAreNotUpdatedException extends RuntimeException {

	private String errorMessage;
	private Exception exception;
	private String causeDescription = "";

	public DataAreNotUpdatedException(String errorMessage, Exception e) {
		this.errorMessage = errorMessage;
		this.exception = e;
		this.causeDescription = e.getMessage();
	}

	public DataAreNotUpdatedException(String errorMessage) {
		this.errorMessage = errorMessage;
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
