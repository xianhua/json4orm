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

import com.json4orm.util.Constants;

/**
 * The Class SortBy defines the data structure for sorting, include sorting by
 * property and sorting order. The default sorting order is ASC.
 *
 * @author Xianhua Liu
 */
public class SortBy {

    /** The property. */
    private String property;

    /** The order. */
    private String order = Constants.ORDER_ASC;

    /**
     * Instantiates a new sort by.
     *
     * @param property the property
     */
    public SortBy(final String property) {
        super();
        this.property = property;
    }

    /**
     * Instantiates a new sort by.
     *
     * @param property the property
     * @param order    the order
     */
    public SortBy(final String property, final String order) {
        super();
        this.property = property;
        this.order = order;
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
     * Gets the order.
     *
     * @return the order
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the order.
     *
     * @param order the new order
     */
    public void setOrder(final String order) {
        this.order = order;
    }

}
