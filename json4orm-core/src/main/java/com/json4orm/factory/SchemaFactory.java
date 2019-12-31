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
package com.json4orm.factory;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.schema.Schema;

/**
 * A factory for creating Schema objects.
 */
public interface SchemaFactory {
    
    /**
     * Creates a new Schema object.
     *
     * @return the schema
     * @throws Json4ormException
     */
    public Schema createSchema() throws Json4ormException;
}
