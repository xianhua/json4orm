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
import com.json4orm.model.schema.Property;

/**
 * The Interface ValueConvertor defines function for converting data form
 * request to corresponding SQL types based on the property type .
 */
public interface ValueConvertor {

    /**
     * Converts value for property based on the property type for database operation.
     *
     * @param property the property to convert value for
     * @param value    the value
     * @return the object converted value to the property
     * @throws Json4ormException when the value is invalid or the type of the
     *                           property is invalid
     */
    Object convertToDB(Property property, Object value) throws Json4ormException;
    
    
    /**
     * Convert from DB type to JSON type.
     *
     * @param property the property
     * @param value the value
     * @return the object
     * @throws Json4ormException the json 4 orm exception
     */
    Object convertFromDB(Property property, Object value) throws Json4ormException;
    Object convertFromDB(String type, Object value) throws Json4ormException;
}
