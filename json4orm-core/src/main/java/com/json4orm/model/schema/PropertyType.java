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
package com.json4orm.model.schema;

/**
 * The Class PropertyType defines all supported property types and function to
 * check if a type is valid.
 *
 * @author Xianhua Liu
 */
public class PropertyType {

    /** The Constant PTY_ID. */
    public static final String PTY_ID = "id";

    /** The Constant PTY_STRING. */
    public static final String PTY_STRING = "string";

    /** The Constant PTY_BYTE. */
    public static final String PTY_BYTE = "byte";

    /** The Constant PTY_SHORT. */
    public static final String PTY_SHORT = "short";

    /** The Constant PTY_INTEGER. */
    public static final String PTY_INTEGER = "integer";

    /** The Constant PTY_LONG. */
    public static final String PTY_LONG = "long";

    /** The Constant PTY_FLOAT. */
    public static final String PTY_FLOAT = "float";

    /** The Constant PTY_DOUBLE. */
    public static final String PTY_DOUBLE = "double";

    /** The Constant PTY_BOOLEAN. */
    public static final String PTY_BOOLEAN = "boolean";

    /** The Constant PTY_DATE. */
    public static final String PTY_DATE = "date";

    /** The Constant PTY_TIME. */
    public static final String PTY_TIME = "time";

    /** The Constant PTY_DATETIME. */
    public static final String PTY_DATETIME = "datetime";

    /** The Constant PTY_TIMESTAMP. */
    public static final String PTY_TIMESTAMP = "timestamp";

    /** The Constant PTY_LIST. */
    public static final String PTY_LIST = "list";

    /** The Constant PROPERTY_TYPES. */
    public static final String[] PROPERTY_TYPES = { PTY_ID, PTY_STRING, PTY_BYTE, PTY_SHORT, PTY_INTEGER, PTY_LONG,
            PTY_FLOAT, PTY_DOUBLE, PTY_BOOLEAN, PTY_DATE, PTY_TIME, PTY_DATETIME, PTY_TIMESTAMP, PTY_LIST };

    /**
     * Checks if is type valid.
     *
     * @param type the type
     * @return true, if is type valid
     */
    public static boolean isTypeValid(final String type) {
        for (final String t : PROPERTY_TYPES) {
            if (t.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
