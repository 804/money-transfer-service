package org.eightofour.moneytransfer.web.rest.exception.mapper;

import org.eightofour.moneytransfer.app.exception.AccountNotFoundException;
import org.eightofour.moneytransfer.web.rest.model.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Exception mapper for AccountNotFoundException testing")
class AccountNotFoundExceptionMapperTest {
    private static final AccountNotFoundExceptionMapper EXCEPTION_MAPPER =
            new AccountNotFoundExceptionMapper();
    private static final String TEST_ID = "test-id";
    private static final int STATUS_CODE = 404;

    @Test
    @DisplayName("Common mapping logic testing")
    void testToResponse() {
        AccountNotFoundException exception = new AccountNotFoundException(TEST_ID);
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