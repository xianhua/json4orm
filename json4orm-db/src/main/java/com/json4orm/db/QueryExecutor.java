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

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.addupdate.AddOrUpdate;
import com.json4orm.model.query.Query;

/**
 * The Interface QueryExecutor defines function to execute query.
 *
 * @author Xianhua Liu
 */
public interface QueryExecutor {

    /**
     * Execute search query.
     *
     * @param query the query
     * @return the query result
     * @throws Json4ormException the json 4 orm exception
     */
    public QueryResult execute(final Query query) throws Json4ormException;
    
    /**
     * Execute add or update query.
     *
     * @param addOrUpdate the add or update
     * @return the query result
     * @throws Json4ormException the json 4 orm exception
     */
    public QueryResult execute(final AddOrUpdate addOrUpdate) throws Json4ormException;
}
