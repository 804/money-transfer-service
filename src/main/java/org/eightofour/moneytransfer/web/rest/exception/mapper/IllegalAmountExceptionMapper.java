package org.eightofour.moneytransfer.web.rest.exception.mapper;

import org.eightofour.moneytransfer.app.exception.IllegalAmountException;
import org.eightofour.moneytransfer.web.rest.model.response.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Exception mapper for converting thrown {@link IllegalAmountException}
 * to error response with 400 HTTP code.
 *
 * @author Evgeny Zubenko
 */
public class IllegalAmountExceptionMapper implements ExceptionMapper<IllegalAmountException> {
    @Override
    public Response toResponse(IllegalAmountException exception) {
        return Response.status(400)
            .type(MediaType.APPLICATION_JSON_TYPE)
            .entity(new ErrorResponse(400, exception.getMessage()))
            .build();
    }
}