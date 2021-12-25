package ua.com.foxminded.galvad.university.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.NoResultException;

import org.junit.jupiter.api.Test;

class DataNotFoundExceptionTest {

	@Test
	void test() {
		DataNotFoundException dataNotFoundExceptionException = new DataNotFoundException("ErrorMessage");
		assertEquals("ErrorMessage", dataNotFoundExceptionException.getErrorMessage());
	}

	@Test
	void testWithException() {
		Exception exception = new NoResultException("Error");
		DataNotFoundException dataNotFoundExceptionException = new DataNotFoundException("ErrorMessage", exception);
		assertEquals("ErrorMessage", dataNotFoundExceptionException.getErrorMessage());
		assertEquals("Error", dataNotFoundExceptionException.getCauseDescription());
		assertEquals(dataNotFoundExceptionException.getException(), exception);
	}

}
