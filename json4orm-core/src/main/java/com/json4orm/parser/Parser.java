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
package com.json4orm.parser;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;

/**
 * The Interface Parser.
 *
 * @author Xianhua Liu
 */
public interface Parser {
    
    /**
     * Parses inputStream into Query object.
     *
     * @param inputStream the input stream
     * @return the query
     * @throws Json4ormException the json 4 orm exception
     */
    public Query parse(final InputStream inputStream) throws Json4ormException;
    
    /**
     * Parses contents= of File into Query object.
     *
     * @param file the file
     * @return the query
     * @throws Json4ormException the json 4 orm exception
     */
    public Query parse(final File file) throws Json4ormException;
    
    /**
     * Parses String into Query object.
     *
     * @param string the string
     * @return the query
     * @throws Json4ormException the json 4 orm exception
     */
    public Query parse(final String string) throws Json4ormException;
    
    /**
     * Parses JSON Map into Query object.
     *
     * @param jsonMap the json map
     * @return the query
     * @throws Json4ormException the json 4 orm exception
     */
    public Query parse(final Map<String, Object> jsonMap) throws Json4ormException;   
}
