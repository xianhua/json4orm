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

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.json4orm.engine.QueryContext;
import com.json4orm.exception.Json4ormException;

/**
 * The Interface RecordBuilder defines function to build record from data
 * returned in ResultSet.
 *
 * @author Xianhua Liu
 */
public interface RecordBuilder {

    /**
     * Builds the record.
     *
     * @param rs      the rs
     * @param context the context
     * @return the list
     * @throws Json4ormException the json 4 orm exception
     */
    public List<Map<String, Object>> buildRecord(final ResultSet rs, final QueryContext context)
            throws Json4ormException;
}
