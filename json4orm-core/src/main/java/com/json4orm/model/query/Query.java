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
package com.json4orm.model.query;

import java.util.List;

/**
 * The Class Query defines the data structure to represent a query, including
 * query entity, filter, pagination, sorting and result template.
 *
 * @author Xianhua Liu
 */
public class Query {

    /** The query for. */
    private String queryFor;

    /** The pagination. */
    private Pagination pagination;

    /** The sort by. */
    private List<SortBy> sortBy;

    /** The filter. */
    private Filter filter;

    /** The result. */
    private Result result;

    /**
     * Instantiates a new query.
     */
    public Query() {
    }

    /**
     * Gets the query for.
     *
     * @return the query for
     */
    public String getQueryFor() {
        return queryFor;
    }

    /**
     * Sets the query for.
     *
     * @param queryFor the new query for
     */
    public void setQueryFor(final String queryFor) {
        this.queryFor = queryFor;
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * Sets the filter.
     *
     * @param filter the new filter
     */
    public void setFilter(final Filter filter) {
        this.filter = filter;
    }

    /**
     * Gets the result.
     *
     * @return the result
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the result.
     *
     * @param result the new result
     */
    public void setResult(final Result result) {
        this.result = result;
    }

    /**
     * Gets the pagination.
     *
     * @return the pagination
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Sets the pagination.
     *
     * @param pagination the new pagination
     */
    public void setPagination(final Pagination pagination) {
        this.pagination = pagination;
    }

    /**
     * Gets the sort by.
     *
     * @return the sort by
     */
    public List<SortBy> getSortBy() {
        return sortBy;
    }

    /**
     * Sets the sort by.
     *
     * @param sortBy the new sort by
     */
    public void setSortBy(final List<SortBy> sortBy) {
        this.sortBy = sortBy;
    }
}
