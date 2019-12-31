
package com.json4orm.web;

import java.util.Map;

import javax.annotation.PostConstruct;

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

@RestController
public class QueryController {
    private static final Logger LOG = LogManager.getLogger(QueryController.class);

    @Autowired
    private QueryExecutor queryExecutor;

    @Autowired
    private QueryParser queryParser;

    @PostConstruct
    private void initController() {

    }

    @PostMapping(path = "/json4orm", consumes = "text/plain", produces = "application/json")
    public ResponseEntity<QueryResponse<Map<String, Object>>> query(@RequestBody final String queryString)
            throws Json4ormException {
        final Query query = queryParser.parse(queryString);
        LOG.debug("Query for: " + query.getQueryFor());
        final QueryResult result = queryExecutor.execute(query);
        final QueryResponse<Map<String, Object>> response = new QueryResponse<>();
        response.setStatus(Constants.STATUS_SUCCESS);
        response.setResults(result.getRecords());
        final Pagination pagination = query.getPagination();
        pagination.setCount(result.getRecords().size());
        pagination.setTotal(result.getTotal());
        response.setPagination(pagination);
        return new ResponseEntity<QueryResponse<Map<String, Object>>>(response, HttpStatus.OK);
    }
}
