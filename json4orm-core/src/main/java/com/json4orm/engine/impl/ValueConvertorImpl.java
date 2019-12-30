package com.json4orm.engine.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.json4orm.engine.ValueConvertor;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Property;
import com.json4orm.model.entity.PropertyType;

public class ValueConvertorImpl implements ValueConvertor {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public Object convert(final Property property, final Object value) throws Json4ormException {

        if (value == null) {
            return null;
        }

        final String type = property.getEntityType();
        if (PropertyType.PTY_BOOLEAN.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Boolean> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Boolean.valueOf(o.toString()));
                }
                return list;
            } else {
                return Boolean.valueOf(value.toString());
            }
        } else if (PropertyType.PTY_BYTE.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Byte> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Byte.valueOf(o.toString()));
                }
                return list;
            } else {
                return Byte.valueOf(value.toString());
            }
        } else if (PropertyType.PTY_SHORT.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Short> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Short.valueOf(o.toString()));
                }
                return list;
            } else {
                return Short.valueOf(value.toString());
            }
        } else if (PropertyType.PTY_INTEGER.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Integer> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Integer.valueOf(o.toString()));
                }
                return list;
            } else {
                return Integer.valueOf(value.toString());
            }
        } else if (PropertyType.PTY_LONG.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Long> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Long.valueOf(o.toString()));
                }
                return list;
            } else {
                return Long.valueOf(value.toString());
            }
        } else if (PropertyType.PTY_FLOAT.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Float> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Float.valueOf(o.toString()));
                }
                return list;
            } else {
                return Float.valueOf(value.toString());
            }
        } else if (PropertyType.PTY_DOUBLE.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Double> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Double.valueOf(o.toString()));
                }
                return list;
            } else {
                return Double.valueOf(value.toString());
            }
        } else if (PropertyType.PTY_STRING.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<String> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(String.valueOf(o));
                }
                return list;
            } else {
                return String.valueOf(value);
            }
        } else if (PropertyType.PTY_DATE.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Date> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(convertToDate(o));
                }
                return list;
            } else {
                return convertToDate(value);
            }
        } else if (PropertyType.PTY_DATETIME.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Date> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(convertToDatetime(o));
                }
                return list;
            } else {
                return convertToDatetime(value);
            }
        } else if (PropertyType.PTY_TIMESTAMP.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Timestamp> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(convertToTimestamp(o));
                }
                return list;
            } else {
                return convertToTimestamp(value);
            }
        } else if (PropertyType.PTY_ID.equalsIgnoreCase(type)) {
            if (value instanceof List) {
                final List<Integer> list = new ArrayList<>();
                for (final Object o : ((List<?>) value)) {
                    list.add(Integer.valueOf(o.toString()));
                }
                return list;
            } else {
                return Integer.valueOf(value.toString());
            }
        } else {
            throw new Json4ormException("Not supported property type: " + type);
        }
    }

    public java.sql.Date convertToDate(final Object value) throws Json4ormException {
        try {
            final java.util.Date date = DATE_FORMATTER.parse(value.toString());
            return new java.sql.Date(date.getTime());
        } catch (final ParseException e) {
            throw new Json4ormException("Invalid date value: " + value.toString());
        }

    }

    public java.sql.Date convertToDatetime(final Object value) throws Json4ormException {
        try {
            final java.util.Date date = DATETIME_FORMATTER.parse(value.toString());
            return new java.sql.Date(date.getTime());
        } catch (final ParseException e) {
            throw new Json4ormException("Invalid date value: " + value.toString());
        }
    }

    public java.sql.Timestamp convertToTimestamp(final Object value) throws Json4ormException {
        try {
            final java.util.Date date = TIMESTAMP_FORMATTER.parse(value.toString());
            return new java.sql.Timestamp(date.getTime());
        } catch (final ParseException e) {
            throw new Json4ormException("Invalid date value: " + value.toString());
        }
    }
}
