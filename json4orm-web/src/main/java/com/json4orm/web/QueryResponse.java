package com.json4orm.web;

import java.util.List;

import com.json4orm.model.query.Pagination;

public class QueryResponse<T> {
    private String status;
    private String error;
    private Pagination pagination;
    private List<T> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(final Pagination pagination) {
        this.pagination = pagination;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(final List<T> results) {
        this.results = results;
    }
}
