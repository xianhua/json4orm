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
package com.json4orm.engine;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;

/**
 * The Interface QueryBuilder defines functions for building query context.
 *
 * @author Xianhua Liu
 */
public interface QueryBuilder {

    /**
     * Builds the query context based on query object
     *
     * @param query the query object
     * @return the query context contains all required objects supporting query
     *         execution
     * @throws Json4ormException when the query is not in valid structure and format
     */
    public QueryContext build(Query query) throws Json4ormException;
}
