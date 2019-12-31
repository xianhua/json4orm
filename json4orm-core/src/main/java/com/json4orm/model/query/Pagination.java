package com.json4orm.model.query;

public class Pagination {
    private int count;
    private long total;
    private long offset = 0l;
    private int limit = 25;

    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(final long offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }
}
