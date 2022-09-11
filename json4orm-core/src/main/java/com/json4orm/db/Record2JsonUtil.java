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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class Record2JsonUtil offers utility function to convert a list of object
 * of {@link com.json4orm.db.Record} into a JSON-serialisable Map.
 *
 * @author Xianhua Liu
 */
public class Record2JsonUtil {

    /**
     * Convert a list of object of {@link com.json4orm.db.Record} into a list of
     * JSON-serialisable Map
     *
     * @param records a list of records
     * @return the list of Map object
     */
    public static List<Map<String, Object>> toJsonObject(final List<Record> records) {
        final List<Map<String, Object>> result = new ArrayList<>();
        for (final Record record : records) {
            result.add(toJsonObject(record));
        }
        return result;
    }

    /**
     * Convert an object of {@link com.json4orm.db.Record} into a JSON-serialisable
     * Map
     *
     * @param record the record
     * @return the map
     */
    public static Map<String, Object> toJsonObject(final Record record) {
        final Map<String, Object> result = new LinkedHashMap<>();
        for (final String key : record.getValuesMap().keySet()) {
            final Object obj = record.getValuesMap().get(key);
            if (obj instanceof Record) {
                result.put(key, toJsonObject((Record) obj));
            } else if (obj instanceof List) {
                result.put(key, toJsonObject((List<Record>) obj));
            } else {
                result.put(key, obj);
            }
        }

        return result;
    }
}
