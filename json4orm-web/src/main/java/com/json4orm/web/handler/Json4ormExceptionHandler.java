/**
 * Copyright 2020 Xianhua Liu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.json4orm.web.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.json4orm.exception.Json4ormException;
import com.json4orm.web.QueryResponse;
import com.json4orm.web.Status;

/**
 * The Class Json4ormExceptionHandler.
 *
 * @author Xianhua Liu
 */
@ControllerAdvice
public class Json4ormExceptionHandler {
    
    /** The Constant LOG. */
    private static final Logger LOG = LogManager.getLogger(Json4ormExceptionHandler.class);

    /**
     * Handle header exception.
     *
     * @param ex the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(Json4ormException.class)
    public final ResponseEntity<QueryResponse<String>> handleHeaderException(final Exception ex,
            final WebRequest request) {
        LOG.error(ex);
        final QueryResponse<String> error = new QueryResponse<String>();
        error.setStatus(Status.FAIL);
        error.setError(ex.getLocalizedMessage());
        return new ResponseEntity<QueryResponse<String>>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all exceptions.
     *
     * @param ex the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<QueryResponse<String>> handleAllExceptions(final Exception ex,
            final WebRequest request) {
        LOG.error(ex);
        final QueryResponse<String> error = new QueryResponse<String>();
        error.setStatus(Status.FAIL);
        error.setError(ex.getLocalizedMessage());
        return new ResponseEntity<QueryResponse<String>>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
