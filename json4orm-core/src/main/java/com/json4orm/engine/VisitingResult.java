package com.json4orm.engine;

import java.util.Map;

public class VisitingResult {
	private String sql;
	private Map<String, Object> values;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public Map<String, Object> getValues() {
		return values;
	}
	public void setValues(Map<String, Object> values) {
		this.values = values;
	}
	
	
}
