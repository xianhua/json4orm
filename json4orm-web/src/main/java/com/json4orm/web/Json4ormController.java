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

package com.json4orm.web;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.json4orm.db.QueryExecutor;
import com.json4orm.db.QueryResult;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.addupdate.AddOrUpdate;
import com.json4orm.model.query.Pagination;
import com.json4orm.model.query.Query;
import com.json4orm.parser.Parser;
import com.json4orm.parser.ParserFactory;
import com.json4orm.parser.QueryParser;
import com.json4orm.util.Constants;

/**
 * The Class Json4ormController contains functions to serve the query request
 * and return query results. .
 *
 * @author Xianhua Liu
 */
@RestController
public class Json4ormController {

    /** The Constant LOG. */
    private static final Logger LOG = LogManager.getLogger(Json4ormController.class);

    /** The query executor. */
    @Autowired
    private QueryExecutor queryExecutor;


    /**
     * Execute query with simplified format.
     *
     * @param request the request in simplified format
     * @return the response entity
     * @throws Json4ormException when query is invalid or failure occurs during
     *                           database query
     */
    @PostMapping(path = "/json4orm/simplified", consumes = "text/plain", produces = "application/json")
    public ResponseEntity<Response> executeSimplified(@RequestBody final String request) throws Json4ormException {
        final QueryParser queryParser = (QueryParser)ParserFactory.getParser(Constants.QUERY);
        final Query query = queryParser.parse(request);
        return executeQuery(query);
    }

    /**
     * Execute query with normalized format.
     *
     * @param query the query in normalized format
     * @return the response entity
     * @throws Json4ormException when query is invalid or failure occurs during
     *                           database query
     */
    //@PostMapping(path = "/json4orm/normalized", consumes = "application/json", produces = "application/json")
    //public ResponseEntity<Response> executeNormalized(@RequestBody final Query query) throws Json4ormException {
    //    return executeQuery(query);
    //}

    @PostMapping(path = "/json4orm/normalized", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> processQuery(@RequestBody final Map<String, Object> request)
            throws Json4ormException {
        return executeQuery(request);
    }

    private ResponseEntity<Response> executeQuery(final Map<String, Object> request) {

        final QueryResponse<Map<String, Object>> response = new QueryResponse<>();
        final String action = getAction(request);
        if (action == null) {
            response.setStatus(Status.FAIL);
            response.setError("Invalid action: " + action);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        final Parser<?> parser = ParserFactory.getParser(action);

        if (parser == null) {
            response.setStatus(Status.FAIL);
            response.setError("No parser found for action: " + action);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            final Object query = parser.parse(request);
            if (query instanceof Query) {
                return executeQuery((Query) query);
            } else if (query instanceof AddOrUpdate) {
                return executeAddOrUpdate((AddOrUpdate) query);
            } else {
                throw new Json4ormException("Invalid query type.");
            }
        } catch (final Json4ormException e) {
            response.setStatus(Status.FAIL);
            response.setError("Failed to execute: " + action + " due to: " +e.getLocalizedMessage());
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }

    }

    private ResponseEntity<Response> executeQuery(final Query query) throws Json4ormException {
        LOG.debug("Query for: " + query.getQueryFor());
        final QueryResult result = queryExecutor.execute(query);
        final QueryResponse<Map<String, Object>> response = new QueryResponse<>();
        response.setStatus(Status.SUCCESS);
        response.setResults(result.getRecords());
        final Pagination pagination = query.getPagination();
        pagination.setCount(result.getRecords().size());
        pagination.setTotal(result.getTotal());
        response.setPagination(pagination);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    private ResponseEntity<Response> executeAddOrUpdate(final AddOrUpdate query) throws Json4ormException {
        LOG.debug("AddOrUpdate for: " + query.getAddOrUpdate());
        final QueryResult result = queryExecutor.execute(query);
        final QueryResponse<Map<String, Object>> response = new QueryResponse<>();
        response.setStatus(Status.SUCCESS);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    private String getAction(final Map<String, Object> request) {
        if (request.get(Constants.QUERY_FOR) != null) {
            return Constants.QUERY_FOR;
        } else if (request.get(Constants.ADD_OR_UPDATE) != null) {
            return Constants.ADD_OR_UPDATE;
        } else if (request.get(Constants.DELETE) != null) {
            return Constants.DELETE;
        }
        return null;
    }
}
