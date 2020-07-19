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
package com.json4orm.engine.impl;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.json4orm.engine.ValueConvertor;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.schema.Property;
import com.json4orm.model.schema.PropertyType;

/**
 * The Class ValueConvertorImpl implements function to convert values to SQL
 * types.
 *
 * @author Xianhua Liu
 */
public class ValueConvertorImpl implements ValueConvertor {

	/** The Constant DATE_FORMATTER. */
	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

	/** The Constant DATETIME_FORMATTER. */
	private static final SimpleDateFormat DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

	private static final SimpleDateFormat DATETIME_FORMATTER_NO_TIMEZONE = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSSSS");

	/** The Constant TIMESTAMP_FORMATTER. */
	private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
	private static final SimpleDateFormat TIMESTAMP_FORMATTER_NO_TIMEZONE = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSSSS");

	/**
	 * Convert.
	 *
	 * @param property the property
	 * @param value    the value
	 * @return the object
	 * @throws Json4ormException the json 4 orm exception
	 */
	@Override
	public Object convertToDB(final Property property, final Object value) throws Json4ormException {

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
		} else if (PropertyType.PTY_TIME.equalsIgnoreCase(type)) {
			if (value instanceof List) {
				final List<Time> list = new ArrayList<>();
				for (final Object o : ((List<?>) value)) {
					list.add(convertToTime(o));
				}
				return list;
			} else {
				return convertToTime(value);
			}
		} else {
			throw new Json4ormException("Not supported property type: " + type);
		}
	}

	/**
	 * Convert to time.
	 *
	 * @param value the value in format hh:mm:ss
	 * @return the time
	 */
	private Time convertToTime(final Object value) {
		return Time.valueOf(value.toString());
	}

	/**
	 * Convert to date.
	 *
	 * @param value the value in format: yyyy-MM-dd
	 * @return the object of java.sql.date
	 * @throws Json4ormException when the value is not in the required format:
	 *                           yyyy-MM-dd
	 */
	public java.sql.Date convertToDate(final Object value) throws Json4ormException {
		try {
			final java.util.Date date = DATE_FORMATTER.parse(value.toString());
			return new java.sql.Date(date.getTime());
		} catch (final ParseException e) {
			throw new Json4ormException("Invalid date value: " + value.toString()
					+ ". Expecting format: yyyy-MM-dd. For example: 2019-12-30", e);
		}

	}

	/**
	 * Convert to datetime.
	 *
	 * @param value the value in format: yyyy-MM-dd'T'HH:mm:ss.SSSX, such as
	 *              2019-12-30T23:42:13.056+1000
	 * @return object of java.sql.date
	 * @throws Json4ormException the value is not in the required format:
	 *                           yyyy-MM-dd'T'HH:mm:ss.SSSX
	 */
	public java.sql.Date convertToDatetime(final Object value) throws Json4ormException {
		try {
			final java.util.Date date = DATETIME_FORMATTER_NO_TIMEZONE.parse(value.toString());
			return new java.sql.Date(date.getTime());
		} catch (final ParseException e) {
			try {
				final java.util.Date date = DATETIME_FORMATTER.parse(value.toString());
				return new java.sql.Date(date.getTime());
			} catch (final ParseException e1) {

				throw new Json4ormException("Invalid date value: " + value.toString() + ". Expecting format: "
						+ DATETIME_FORMATTER_NO_TIMEZONE + " or " + DATETIME_FORMATTER
						+ ". For example: 2019-12-30 23:42:13.05633 or 2019-12-30T23:42:13.056+1000.", e);
			}
		}
	}

	/**
	 * Convert to timestamp.
	 *
	 * @param value the value in format: yyyy-MM-dd'T'HH:mm:ss.SSSX, such as
	 *              2019-12-30T23:42:13.056+1000
	 * @return object of the java.sql.timestamp
	 * @throws Json4ormException the value is not in the required format:
	 *                           yyyy-MM-dd'T'HH:mm:ss.SSSX
	 */
	public java.sql.Timestamp convertToTimestamp(final Object value) throws Json4ormException {
		try {
			final java.util.Date date = TIMESTAMP_FORMATTER_NO_TIMEZONE.parse(value.toString());
			return new java.sql.Timestamp(date.getTime());
		} catch (final ParseException e) {
			try {
				final java.util.Date date = TIMESTAMP_FORMATTER.parse(value.toString());
				return new java.sql.Timestamp(date.getTime());
			} catch (final ParseException e1) {

				throw new Json4ormException("Invalid date value: " + value.toString() + ". Expecting format: "
						+ TIMESTAMP_FORMATTER_NO_TIMEZONE + " or " + TIMESTAMP_FORMATTER
						+ ". For example: 2019-12-30 23:42:13.05633 or 2019-12-30T23:42:13.056+1000.", e1);
			}
		}
	}

	@Override
	public Object convertFromDB(Property property, Object value) throws Json4ormException {
		return convertFromDB(property.getEntityType(), value);
	}

	@Override
	public Object convertFromDB(String type, Object value) throws Json4ormException {
		if (value == null) {
			return null;
		}

		if (PropertyType.PTY_DATETIME.equalsIgnoreCase(type)) {
			return convertFromDatetime(value);
		} else if (PropertyType.PTY_TIMESTAMP.equalsIgnoreCase(type)) {
			return convertFromDatetime(value);
		} else {
			return value;
		}
	}

	public String convertFromDatetime(final Object value) throws Json4ormException {
		if (value instanceof Date) {
			Date d = (Date) value;
			return DATETIME_FORMATTER.format(d);
		} else if (value instanceof Timestamp) {
			Timestamp d = (Timestamp) value;
			return DATETIME_FORMATTER.format(d);
		} else {
			throw new Json4ormException("Could not convert to datetime: " + value.getClass().getCanonicalName());
		}
	}
}
