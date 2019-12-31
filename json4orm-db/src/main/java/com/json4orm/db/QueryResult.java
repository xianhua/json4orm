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
package com.json4orm.db;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The Class QueryResult defines the query result, including total count and
 * list of records.
 *
 * @author Xianhua Liu
 */
public class QueryResult {

    /** The total. */
    private long total;

    /** The records. */
    private List<Map<String, Object>> records;

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
     * Gets the records.
     *
     * @return the records
     */
    public List<Map<String, Object>> getRecords() {
        if (records == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(records);
    }

    /**
     * Sets the records.
     *
     * @param records the records
     */
    public void setRecords(final List<Map<String, Object>> records) {
        this.records = records;
    }

}
