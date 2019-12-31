package com.json4orm.model.schema;

public class PropertyType {
    public static final String PTY_ID = "id";
    public static final String PTY_STRING = "string";
    public static final String PTY_BYTE = "byte";
    public static final String PTY_SHORT = "short";
    public static final String PTY_INTEGER = "integer";
    public static final String PTY_LONG = "long";
    public static final String PTY_FLOAT = "float";
    public static final String PTY_DOUBLE = "double";

    public static final String PTY_BOOLEAN = "boolean";
    public static final String PTY_DATE = "date";
    public static final String PTY_TIME = "time";
    public static final String PTY_DATETIME = "datetime";
    public static final String PTY_TIMESTAMP = "timestamp";

    public static final String PTY_LIST = "list";

    public static final String[] PROPERTY_TYPES = { PTY_ID, PTY_STRING, PTY_BYTE, PTY_SHORT, PTY_INTEGER, PTY_LONG,
            PTY_FLOAT, PTY_DOUBLE, PTY_BOOLEAN, PTY_DATE, PTY_TIME, PTY_DATETIME, PTY_TIMESTAMP, PTY_LIST };

    public static boolean isTypeValid(final String type) {
        for (final String t : PROPERTY_TYPES) {
            if (t.equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
