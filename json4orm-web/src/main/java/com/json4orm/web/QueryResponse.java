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

import java.util.List;

import com.json4orm.model.query.Pagination;

/**
 * The Class QueryResponse.
 *
 * @author Xianhua Liu
 * @param <T> the generic type
 */
public class QueryResponse<T> extends Response {

    /** The pagination. */
    private Pagination pagination;
    
    /** The results. */
    private List<T> results;

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
     * Gets the results.
     *
     * @return the results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Sets the results.
     *
     * @param results the new results
     */
    public void setResults(final List<T> results) {
        this.results = results;
    }
}
