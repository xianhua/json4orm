package com.json4orm.util;

import java.util.HashMap;
import java.util.Map;

import com.json4orm.model.query.FilterOperator;

public class EngineUtil {
	public static Map<String, Integer> ALIAS_COUNTS = new HashMap<>();
	public static Map<String, Integer> PLACEHOLDER_COUNTS = new HashMap<>();

	public static String getAlias(String entity) {
		int count = 0;
		if (ALIAS_COUNTS.containsKey(entity)) {
			count = ALIAS_COUNTS.get(entity) + 1;
		}

		ALIAS_COUNTS.put(entity, count);
		return entity + "_" + count;
	}

	public static String getPlaceHolder(String property) {
		int count = 0;
		if (PLACEHOLDER_COUNTS.containsKey(property)) {
			count = PLACEHOLDER_COUNTS.get(property) + 1;
		}

		PLACEHOLDER_COUNTS.put(property, count);
		return property + "_" + count;
	}

	public static String getLast(String propertyChain) {
		int pos = propertyChain.lastIndexOf(".");
		if (pos == -1) {
			return propertyChain;
		}

		return propertyChain.substring(pos+1);

	}

	public static String getEntityChain(String propertyChain) {
		int pos = propertyChain.lastIndexOf(".");
		if (pos == -1) {
			return null;
		}

		return propertyChain.substring(0, pos);

	}

	public static boolean isLogicOperator(String operator) {
		return FilterOperator.OR.equalsIgnoreCase(operator) || FilterOperator.AND.equalsIgnoreCase(operator);
	}
}
