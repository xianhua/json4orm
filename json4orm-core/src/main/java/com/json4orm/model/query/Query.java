package com.json4orm.model.query;

import java.util.List;

public class Query {
    private String queryFor;
    private Pagination pagination;
    private List<SortBy> sortBy;
    private Filter filter;
    private Result result;

    public Query() {
    }

    public String getQueryFor() {
        return queryFor;
    }

    public void setQueryFor(final String queryFor) {
        this.queryFor = queryFor;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(final Filter filter) {
        this.filter = filter;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(final Result result) {
        this.result = result;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(final Pagination pagination) {
        this.pagination = pagination;
    }

    public List<SortBy> getSortBy() {
        return sortBy;
    }

    public void setSortBy(final List<SortBy> sortBy) {
        this.sortBy = sortBy;
    }
}
