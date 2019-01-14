package org.eightofour.moneytransfer.web.rest.exception.mapper;

import org.eightofour.moneytransfer.app.exception.NoSuchMoneyException;
import org.eightofour.moneytransfer.web.rest.model.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Exception mapper for NoSuchMoneyException testing")
class NoSuchMoneyExceptionMapperTest {
    private static final NoSuchMoneyExceptionMapper EXCEPTION_MAPPER =
            new NoSuchMoneyExceptionMapper();
    private static final int STATUS_CODE = 400;
    private static final String TEST_MESSAGE = "Test message";

    @Test
    @DisplayName("Common mapping logic")
    void testToResponse() {
        NoSuchMoneyException exception = new NoSuchMoneyException(TEST_MESSAGE);
        Response response = EXCEPTION_MAPPER.toResponse(exception);

        assertEquals(
            response.getStatus(), STATUS_CODE,
            "Response code mapped by mapper isn't correct"
        );
        assertEquals(
            response.getMediaType(), MediaType.APPLICATION_JSON_TYPE,
            "Response content type mapped by mapper isn't correct"
        );
        assertEquals(
            response.getEntity(),
            new ErrorResponse(STATUS_CODE, exception.getMessage()),
            "Response entity mapped by mapper isn't correct"
        );
    }
}