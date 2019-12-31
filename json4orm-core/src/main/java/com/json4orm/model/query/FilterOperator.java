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
 * The Class FilterOperator defines all supported operators for {@link com.json4orm.model.query.Filter}.
 *
 * @author Xianhua Liu
 */
public class FilterOperator {
    
    /** The Constant OR. */
    // logic operator
    public static final String OR = "or";
    
    /** The Constant AND. */
    public static final String AND = "and";

    /** The Constant EQUAL. */
    public static final String EQUAL = "eq";
    
    /** The Constant EQUAL_CASE_INSENSITIVE. */
    public static final String EQUAL_CASE_INSENSITIVE = "ieq";
    
    /** The Constant NOT_EQUAL. */
    public static final String NOT_EQUAL = "not_eq";
    
    /** The Constant NOT_EQUAL_CASE_INSENSITIVE. */
    public static final String NOT_EQUAL_CASE_INSENSITIVE = "not_ieq";

    /** The Constant GREATER_THAN. */
    public static final String GREATER_THAN = "gt";
    
    /** The Constant LESS_THAN. */
    public static final String LESS_THAN = "lt";
    
    /** The Constant NOT_GREATER_THAN. */
    public static final String NOT_GREATER_THAN = "not_gt";
    
    /** The Constant NOT_LESS_THAN. */
    public static final String NOT_LESS_THAN = "not_lt";

    /** The Constant CONTAINS. */
    public static final String CONTAINS = "contains";
    
    /** The Constant STARTS_WITH. */
    public static final String STARTS_WITH = "starts";
    
    /** The Constant ENDS_WITH. */
    public static final String ENDS_WITH = "ends";
    
    /** The Constant NOT_CONTAINS. */
    public static final String NOT_CONTAINS = "not_contains";
    
    /** The Constant NOT_STARTS_WITH. */
    public static final String NOT_STARTS_WITH = "not_starts";
    
    /** The Constant NOT_ENDS_WITH. */
    public static final String NOT_ENDS_WITH = "not_ends";
    
    /** The Constant CONTAINS_CASE_INSENSITIVE. */
    public static final String CONTAINS_CASE_INSENSITIVE = "icontains";
    
    /** The Constant STARTS_WITH_CASE_INSENSITIVE. */
    public static final String STARTS_WITH_CASE_INSENSITIVE = "istarts";
    
    /** The Constant ENDS_WITH_CASE_INSENSITIVE. */
    public static final String ENDS_WITH_CASE_INSENSITIVE = "iends";
    
    /** The Constant NOT_CONTAINS_CASE_INSENSITIVE. */
    public static final String NOT_CONTAINS_CASE_INSENSITIVE = "not_icontains";
    
    /** The Constant NOT_STARTS_WITH_CASE_INSENSITIVE. */
    public static final String NOT_STARTS_WITH_CASE_INSENSITIVE = "not_istarts";
    
    /** The Constant NOT_ENDS_WITH_CASE_INSENSITIVE. */
    public static final String NOT_ENDS_WITH_CASE_INSENSITIVE = "not_iends";

    /** The Constant IN. */
    public static final String IN = "in";
    
    /** The Constant NOT_IN. */
    public static final String NOT_IN = "not_in";
}
