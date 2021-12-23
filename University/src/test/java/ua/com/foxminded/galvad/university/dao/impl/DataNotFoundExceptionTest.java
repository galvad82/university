package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.NoResultException;

import org.junit.jupiter.api.Test;

class DataNotFoundExceptionTest {

	@Test
	void test() {
		DataNotFoundException dataNotFoundExceptionException = new DataNotFoundException("ErrorMessage");
		assertEquals(dataNotFoundExceptionException.getErrorMessage(), "ErrorMessage");
	}

	@Test
	void testWithException() {
		Exception exception = new NoResultException("Error");
		DataNotFoundException dataNotFoundExceptionException = new DataNotFoundException("ErrorMessage",
				exception);
		assertEquals(dataNotFoundExceptionException.getErrorMessage(), "ErrorMessage");
		assertEquals(dataNotFoundExceptionException.getCauseDescription(), "Error");
		assertEquals(dataNotFoundExceptionException.getException(), exception);
	}

}
