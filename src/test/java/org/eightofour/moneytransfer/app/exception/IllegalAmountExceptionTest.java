package org.eightofour.moneytransfer.app.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IllegalAmountException creation testing")
class IllegalAmountExceptionTest {
    private static final String TEST_MESSAGE = "Test message";

    @Test
    @DisplayName("Simple creation testing")
    void testConstruct() {
        IllegalAmountException exception = new IllegalAmountException(TEST_MESSAGE);
        assertEquals(
            TEST_MESSAGE, exception.getMessage(),
            "Exception message must be the same as in exception constructor"
        );
    }
}