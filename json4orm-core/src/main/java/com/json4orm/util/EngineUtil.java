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
package com.json4orm.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.json4orm.model.query.FilterOperator;

/**
 * The Class EngineUtil contains utility functions commonly used.
 *
 * @author Xianhua Liu
 */
public class EngineUtil {

    /** The alias counts. */
    public static Map<String, Integer> ALIAS_COUNTS = new HashMap<>();

    /** The placeholder counts. */
    public static Map<String, Integer> PLACEHOLDER_COUNTS = new HashMap<>();

    /**
     * Gets the alias.
     *
     * @param entity the entity
     * @return the alias
     */
    public static String getAlias(final String entity) {
        int count = 0;
        if (ALIAS_COUNTS.containsKey(entity)) {
            count = ALIAS_COUNTS.get(entity) + 1;
        }

        ALIAS_COUNTS.put(entity, count);
        return entity + "_" + count;
    }

    /**
     * Gets the place holder.
     *
     * @param property the property
     * @return the place holder
     */
    public static String getPlaceHolder(final String property) {
        int count = 0;
        if (PLACEHOLDER_COUNTS.containsKey(property)) {
            count = PLACEHOLDER_COUNTS.get(property) + 1;
        }

        PLACEHOLDER_COUNTS.put(property, count);
        return property + "_" + count;
    }

    /**
     * Reset alias place holder counts.
     */
    public static void resetAliasPlaceHolderCounts() {
        ALIAS_COUNTS.clear();
        PLACEHOLDER_COUNTS.clear();
    }

    /**
     * Gets the last.
     *
     * @param propertyChain the property chain
     * @return the last
     */
    public static String getLast(final String propertyChain) {
        final int pos = propertyChain.lastIndexOf(".");
        if (pos == -1) {
            return propertyChain;
        }

        return propertyChain.substring(pos + 1);

    }

    /**
     * Gets the entity chain.
     *
     * @param propertyChain the property chain
     * @return the entity chain
     */
    public static String getEntityChain(final String propertyChain) {
        final int pos = propertyChain.lastIndexOf(".");
        if (pos == -1) {
            return null;
        }

        return propertyChain.substring(0, pos);

    }

    /**
     * Checks if is logic operator.
     *
     * @param operator the operator
     * @return true, if is logic operator
     */
    public static boolean isLogicOperator(final String operator) {
        return FilterOperator.OR.equalsIgnoreCase(operator) || FilterOperator.AND.equalsIgnoreCase(operator);
    }

    /**
     * Checks if is equal.
     *
     * @param values1 the values 1
     * @param values2 the values 2
     * @return true, if is equal
     */
    public static boolean isEqual(final List<Object> values1, final List<Object> values2) {
        if (values1 == null || values2 == null) {
            return false;
        }
        if (values1.size() != values2.size()) {
            return false;
        }

        for (int i = 0; i < values1.size(); i++) {
            if (!values1.get(i).equals(values2.get(i))) {
                return false;
            }
        }

        return true;

    }

    /**
     * Checks if is empty.
     *
     * @param obj the obj
     * @return true, if is empty
     */
    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof List) {
            return ((List) obj).isEmpty();
        }

        return false;
    }
}
