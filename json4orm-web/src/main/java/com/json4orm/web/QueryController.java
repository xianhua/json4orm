package com.json4orm.web;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.json4orm.db.QueryExecutor;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;
import com.json4orm.parser.QueryParser;

@RestController
public class QueryController {
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

        final List<Map<String, Object>> results = queryExecutor.execute(query);
        final QueryResponse<Map<String, Object>> response = new QueryResponse<>();
        response.setStatus(Constants.STATUS_SUCCESS);
        response.setResults(results);
        return new ResponseEntity<QueryResponse<Map<String, Object>>>(response, HttpStatus.OK);
    }
}
