package com.json4orm.db;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QueryResult {
    private long total;
    private List<Map<String, Object>> records;

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public List<Map<String, Object>> getRecords() {
        if(records==null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(records);
    }

    public void setRecords(final List<Map<String, Object>> records) {
        this.records = records;
    }

}
