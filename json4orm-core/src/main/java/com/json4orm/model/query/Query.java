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
import java.util.Map;

/**
 * The Class Query defines the data structure to represent a query. Following types of query are supported:
 * <ul>
 * <li>Search: to search data with filter, pagination, sorting and result template.</li>
 * <li>AddOrUpdate: to add or update a list of entities.</li>
 * <li>Delete: to delete an entity by its primary key value.</li>
 * </ul>
 *
 * @author Xianhua Liu
 */
public class Query {

    private Action action;
    
    private String entityName;

    /*
     * Following properties are for Search action
     */
    /** The pagination. */
    private Pagination pagination;

    /** The sort by. */
    private List<SortBy> sortBy;

    /** The filter. */
    private Filter filter;

    /** The result. */
    private Result result;

    /*
     * Following properties are for addOrUpdate action
     */
    private List<Map<String, Object>> data;
    
    /*
     * Following properties are for Delete action
     */
    private Object id;
    
    /**
     * Instantiates a new query.
     */
    public Query() {
    }



    public Action getAction() {
        return action;
    }



    public void setAction(final Action action) {
        this.action = action;
    }



    public String getEntityName() {
        return entityName;
    }



    public void setEntityName(final String entityName) {
        this.entityName = entityName;
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



    public List<Map<String, Object>> getData() {
        return data;
    }



    public void setData(final List<Map<String, Object>> data) {
        this.data = data;
    }



    public Object getId() {
        return id;
    }



    public void setId(final Object id) {
        this.id = id;
    }

}
