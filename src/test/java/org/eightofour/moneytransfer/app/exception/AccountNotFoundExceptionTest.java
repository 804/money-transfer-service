package org.eightofour.moneytransfer.app.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AccountNotFoundException creation testing")
class AccountNotFoundExceptionTest {
    private static final String TEST_ID = "test-id";
    private static final String TEST_MSG_TEMPLATE = "Account with id '%s' isn't found";
    private static final String TEST_MESSAGE = String.format(TEST_MSG_TEMPLATE, TEST_ID);

    @Test
    @DisplayName("Simple creation testing")
    void testConstruct() {
        AccountNotFoundException exception = new AccountNotFoundException(TEST_ID);
        assertEquals(
            TEST_MESSAGE, exception.getMessage(),
            "Exception message is incorrect"
        );
    }
}