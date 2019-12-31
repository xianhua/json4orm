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

/**
 * The Class Pagination defines properties of pagination for both query request
 * and response.
 *
 * @author Xianhua Liu
 */
public class Pagination {

    /** The count. */
    private int count;

    /** The total. */
    private long total;

    /** The offset. */
    private long offset = 0l;

    /** The limit. */
    private int limit = 25;

    /**
     * Gets the count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the count.
     *
     * @param count the new count
     */
    public void setCount(final int count) {
        this.count = count;
    }

    /**
     * Gets the total.
     *
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * Sets the total.
     *
     * @param total the new total
     */
    public void setTotal(final long total) {
        this.total = total;
    }

    /**
     * Gets the offset.
     *
     * @return the offset
     */
    public long getOffset() {
        return offset;
    }

    /**
     * Sets the offset.
     *
     * @param offset the new offset
     */
    public void setOffset(final long offset) {
        this.offset = offset;
    }

    /**
     * Gets the limit.
     *
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit.
     *
     * @param limit the new limit
     */
    public void setLimit(final int limit) {
        this.limit = limit;
    }
}
