package com.json4orm.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.json4orm.model.query.FilterOperator;

public class EngineUtil {
    public static Map<String, Integer> ALIAS_COUNTS = new HashMap<>();
    public static Map<String, Integer> PLACEHOLDER_COUNTS = new HashMap<>();

    public static String getAlias(final String entity) {
        int count = 0;
        if (ALIAS_COUNTS.containsKey(entity)) {
            count = ALIAS_COUNTS.get(entity) + 1;
        }

        ALIAS_COUNTS.put(entity, count);
        return entity + "_" + count;
    }

    public static String getPlaceHolder(final String property) {
        int count = 0;
        if (PLACEHOLDER_COUNTS.containsKey(property)) {
            count = PLACEHOLDER_COUNTS.get(property) + 1;
        }

        PLACEHOLDER_COUNTS.put(property, count);
        return property + "_" + count;
    }

    public static void resetAliasPlaceHolderCounts() {
        ALIAS_COUNTS.clear();
        PLACEHOLDER_COUNTS.clear();
    }

    public static String getLast(final String propertyChain) {
        final int pos = propertyChain.lastIndexOf(".");
        if (pos == -1) {
            return propertyChain;
        }

        return propertyChain.substring(pos + 1);

    }

    public static String getEntityChain(final String propertyChain) {
        final int pos = propertyChain.lastIndexOf(".");
        if (pos == -1) {
            return null;
        }

        return propertyChain.substring(0, pos);

    }

    public static boolean isLogicOperator(final String operator) {
        return FilterOperator.OR.equalsIgnoreCase(operator) || FilterOperator.AND.equalsIgnoreCase(operator);
    }

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
