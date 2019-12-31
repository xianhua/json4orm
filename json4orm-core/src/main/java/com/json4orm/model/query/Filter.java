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

import java.util.ArrayList;
import java.util.List;

/**
 * The Class Filter defines query filter.
 * <P>
 * A query filer defines the property name, operator, value and children
 * filters. Generally, it defines a filter like: <b>property operator value</b>,
 * such as <i>name eq 'Robert'</i>
 * </P>
 * <P>
 * Following operators are supported and defined in {@link com.json4orm.model.query.FilterOperator}
 * </P>
 * <ul>
 * <li>and: the logic AND, which performs logic AND among all the children
 * filters.</li>
 * <li>or: the logic OR, which performs logic OR among all the children
 * filters.</li>
 * <li>eq: equal, case sensitive</li>
 * <li>not_eq: not equal, case sensitive</li>
 * <li>ieq: equal, case insensitive</li>
 * <li>not_ieq: not equal, case insensitive</li>
 * <li>gt: greater than</li>
 * <li>not_gt: not greater than</li>
 * <li>lt: less than</li>
 * <li>not_lt: not less than</li>
 * <li>contains: contains, case sensitive</li>
 * <li>not_contains: not contains, case sensitive</li>
 * <li>icontains: contains, case insensitive</li>
 * <li>not_icontains: not contains, case insensitive</li>
 * <li>starts: starts with, case sensitive</li>
 * <li>not_starts: not starts with, case sensitive</li>
 * <li>istarts: starts with, case insensitive</li>
 * <li>not_istarts: not starts with, case insensitive</li>
 * <li>end: ends with, case sensitive</li>
 * <li>not_end: not ends with, case sensitive</li>
 * <li>iend:ends with, case insensitive</li>
 * <li>not_iends: not ends with, case insensitive</li>
 * <li>in: in in list of values</li>
 * <li>not_in: not in in list of values</li>
 * </ul>
 * <P>
 * The value can be a single value, string or number, or a list of values when
 * operator is 'in' or 'not_in'.
 * </P>
 * <P>Filters can be nested using the two logic operators.</P>
 * 
 * @author Xianhua Liu
 */
public class Filter {

    /** The property. */
    private String property;

    /** The operator. */
    private String operator;

    /** The value. */
    private Object value;

    /** The filters. */
    private List<Filter> filters = new ArrayList<>();

    /**
     * Instantiates a new filter.
     */
    public Filter() {
    }

    /**
     * Gets the property.
     *
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets the property.
     *
     * @param property the new property
     */
    public void setProperty(final String property) {
        this.property = property;
    }

    /**
     * Gets the operator.
     *
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Sets the operator.
     *
     * @param operator the new operator
     */
    public void setOperator(final String operator) {
        this.operator = operator;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(final Object value) {
        this.value = value;
    }

    /**
     * Gets the filters.
     *
     * @return the filters
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     * Sets the filters.
     *
     * @param filters the new filters
     */
    public void setFilters(final List<Filter> filters) {
        this.filters = filters;
    }

    /**
     * Adds the filter.
     *
     * @param filter the filter
     */
    public void addFilter(final Filter filter) {
        filters.add(filter);
    }

}
