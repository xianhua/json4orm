package com.json4orm.model.query;

public class FilterOperator {
    // logic operator
    public static final String OR = "or";
    public static final String AND = "and";

    public static final String EQUAL = "eq";
    public static final String EQUAL_CASE_INSENSITIVE = "ieq";
    public static final String NOT_EQUAL = "!eq";
    public static final String NOT_EQUAL_CASE_INSENSITIVE = "!ieq";

    public static final String GREATER_THAN = "gt";
    public static final String LESS_THAN = "lt";
    public static final String NOT_GREATER_THAN = "!gt";
    public static final String NOT_LESS_THAN = "!lt";

    public static final String CONTAINS = "contains";
    public static final String STARTS_WITH = "starts";
    public static final String ENDS_WITH = "ends";
    public static final String NOT_CONTAINS = "!contains";
    public static final String NOT_STARTS_WITH = "!starts";
    public static final String NOT_ENDS_WITH = "!ends";
    public static final String CONTAINS_CASE_INSENSITIVE = "icontains";
    public static final String STARTS_WITH_CASE_INSENSITIVE = "istarts";
    public static final String ENDS_WITH_CASE_INSENSITIVE = "iends";
    public static final String NOT_CONTAINS_CASE_INSENSITIVE = "!icontains";
    public static final String NOT_STARTS_WITH_CASE_INSENSITIVE = "!istarts";
    public static final String NOT_ENDS_WITH_CASE_INSENSITIVE = "!iends";

    public static final String IN = "in";
    public static final String NOT_IN = "!in";
}
