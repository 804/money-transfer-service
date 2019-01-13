package org.eightofour.moneytransfer.app.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NoSuchMoneyException creation testing")
class NoSuchMoneyExceptionTest {
    private static final String TEST_MESSAGE = "Test message";
    private static final Exception TEST_CAUSE = new Exception();

    @Test
    @DisplayName("Simple creation testing")
    void testConstruct() {
        NoSuchMoneyException exception = new NoSuchMoneyException(TEST_MESSAGE);
        assertEquals(
            TEST_MESSAGE, exception.getMessage(),
            "Exception message must be the same as in exception constructor"
        );
    }

    @Test
    @DisplayName("Creation with cause testing")
    void testConstructWithCause() {
        NoSuchMoneyException exception = new NoSuchMoneyException(TEST_MESSAGE, TEST_CAUSE);
        assertEquals(
            TEST_MESSAGE, exception.getMessage(),
            "Exception message must be the same as in exception constructor"
        );
        assertEquals(
            TEST_CAUSE, exception.getCause(),
            "Exception cause must be the same as in exception constructor"
        );
    }
}