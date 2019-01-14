package org.eightofour.moneytransfer.web.rest.model.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Error response testing")
class ErrorResponseTest {
    private static final int TEST_CODE = 400;
    private static final String TEST_MESSAGE = "Test message";

    @Test
    @DisplayName("Construction")
    void testCreation() {
        ErrorResponse errorResponse = new ErrorResponse(TEST_CODE, TEST_MESSAGE);

        assertEquals(
            TEST_CODE, errorResponse.getCode(),
            "Code in response must be the same as in constructor"
        );
        assertEquals(
            TEST_MESSAGE, errorResponse.getMessage(),
            "Message in response must be the same as in constructor"
        );
    }
}