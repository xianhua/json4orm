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
import com.json4orm.model.query.Pagination;
import com.json4orm.model.query.Query;
import com.json4orm.parser.QueryParser;

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

    /** The query parser. */
    @Autowired
    private QueryParser queryParser;

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
    @PostMapping(path = "/json4orm/normalized", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> executeNormalized(@RequestBody final Query query) throws Json4ormException {
        return executeQuery(query);
    }

    private ResponseEntity<Response> executeQuery(final Query query) throws Json4ormException {
        LOG.debug("Query for: " + query.getQueryFor());
        final QueryResult result = queryExecutor.execute(query);
        final QueryResponse<Map<String, Object>> response = new QueryResponse<>();
        response.setStatus(Constants.STATUS_SUCCESS);
        response.setResults(result.getRecords());
        final Pagination pagination = query.getPagination();
        pagination.setCount(result.getRecords().size());
        pagination.setTotal(result.getTotal());
        response.setPagination(pagination);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }
}
