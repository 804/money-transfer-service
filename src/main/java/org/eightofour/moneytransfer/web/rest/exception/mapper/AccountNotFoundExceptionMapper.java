package org.eightofour.moneytransfer.web.rest.exception.mapper;

import org.eightofour.moneytransfer.app.exception.AccountNotFoundException;
import org.eightofour.moneytransfer.web.rest.model.response.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Exception mapper for converting thrown {@link AccountNotFoundException}
 * to error response with 404 HTTP code.
 *
 * @author Evgeny Zubenko
 */
public class AccountNotFoundExceptionMapper implements ExceptionMapper<AccountNotFoundException> {
    @Override
    public Response toResponse(AccountNotFoundException exception) {
        return Response.status(404)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorResponse(404, exception.getMessage()))
            .build();
    }
}