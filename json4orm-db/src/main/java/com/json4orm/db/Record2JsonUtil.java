package com.json4orm.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Record2JsonUtil {

    public static List<Map<String, Object>> toJsonObject(final List<Record> records) {
        final List<Map<String, Object>> result = new ArrayList<>();
        for (final Record record : records) {
            result.add(toJsonObject(record));
        }
        return result;
    }

    public static Map<String, Object> toJsonObject(final Record record) {
        final Map<String, Object> result = new LinkedHashMap<>();
        for (final String key : record.getValuesMap().keySet()) {
            final Object obj = record.getValuesMap().get(key);
            if (obj instanceof Record) {
                result.put(key, toJsonObject((Record) obj));
            } else if (obj instanceof List) {
                result.put(key, toJsonObject((List<Record>) obj));
            } else {
                result.put(key, obj);
            }
        }

        return result;
    }
}
