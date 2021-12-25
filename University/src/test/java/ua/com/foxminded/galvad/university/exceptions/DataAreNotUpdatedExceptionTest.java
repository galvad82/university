package ua.com.foxminded.galvad.university.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import javax.persistence.NoResultException;

class DataAreNotUpdatedExceptionTest {
	
	private static final String ERROR_MESSAGE="ErrorMessage";

	@Test
	void test() {
		DataAreNotUpdatedException dataAreNotUpdatedExceptionException = new DataAreNotUpdatedException(ERROR_MESSAGE);
		assertEquals(ERROR_MESSAGE, dataAreNotUpdatedExceptionException.getErrorMessage());
	}

	@Test
	void testWithException() {
		Exception exception = new NoResultException("Error");
		DataAreNotUpdatedException dataAreNotUpdatedExceptionException = new DataAreNotUpdatedException(ERROR_MESSAGE,
				exception);
		assertEquals(ERROR_MESSAGE, dataAreNotUpdatedExceptionException.getErrorMessage());
		assertEquals("Error", dataAreNotUpdatedExceptionException.getCauseDescription());
		assertEquals(dataAreNotUpdatedExceptionException.getException(), exception);
	}

}
