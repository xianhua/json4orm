package com.json4orm.model.query;

public class FilterOperator {
    // logic operator
    public static final String OR = "or";
    public static final String AND = "and";

    public static final String EQUAL = "eq";
    public static final String EQUAL_CASE_INSENSITIVE = "ieq";
    public static final String GREATER_THAN = "gt";
    public static final String LESS_THAN = "lt";
    public static final String NOT_EQUAL = "neq";
    public static final String NOT_EQUAL_CASE_INSENSITIVE = "nieq";
    public static final String NOT_GREATER_THAN = "ngt";
    public static final String NOT_LESS_THAN = "nlt";

    public static final String CONTAINS = "contains";
    public static final String STARTS_WITH = "starts";
    public static final String END_WITH = "ends";
    public static final String CONTAINS_CASE_INSENSITIVE = "icontain";
    public static final String STARTS_WITH_CASE_INSENSITIVE = "istart";
    public static final String END_WITH_CASE_INSENSITIVE = "iend";
    
    public static final String IN = "in";
}
