package com.json4orm.web.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.json4orm.exception.Json4ormException;
import com.json4orm.web.Constants;
import com.json4orm.web.QueryResponse;

@ControllerAdvice
public class Json4ormExceptionHandler {
    @ExceptionHandler(Json4ormException.class)
    public final ResponseEntity<QueryResponse<String>> handleHeaderException(final Exception ex,
            final WebRequest request) {
        final QueryResponse<String> error = new QueryResponse<String>();
        error.setStatus(Constants.STATUS_FAIL);
        error.setError(ex.getLocalizedMessage());
        return new ResponseEntity<QueryResponse<String>>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<QueryResponse<String>> handleAllExceptions(final Exception ex,
            final WebRequest request) {
        final QueryResponse<String> error = new QueryResponse<String>();
        error.setStatus(Constants.STATUS_FAIL);
        error.setError(ex.getLocalizedMessage());
        return new ResponseEntity<QueryResponse<String>>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
