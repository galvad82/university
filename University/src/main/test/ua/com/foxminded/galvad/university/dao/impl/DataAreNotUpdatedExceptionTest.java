package ua.com.foxminded.galvad.university.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import javax.persistence.NoResultException;

class DataAreNotUpdatedExceptionTest {

	@Test
	void test() {
		DataAreNotUpdatedException dataAreNotUpdatedExceptionException = new DataAreNotUpdatedException("ErrorMessage");
		assertEquals(dataAreNotUpdatedExceptionException.getErrorMessage(), "ErrorMessage");
	}

	@Test
	void testWithException() {
		Exception exception = new NoResultException("Error");
		DataAreNotUpdatedException dataAreNotUpdatedExceptionException = new DataAreNotUpdatedException("ErrorMessage",
				exception);
		assertEquals(dataAreNotUpdatedExceptionException.getErrorMessage(), "ErrorMessage");
		assertEquals(dataAreNotUpdatedExceptionException.getCauseDescription(), "Error");
		assertEquals(dataAreNotUpdatedExceptionException.getException(), exception);
	}

}
