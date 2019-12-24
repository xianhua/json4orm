package com.json4orm.model.query;

import java.util.ArrayList;
import java.util.List;

public class Result {
	
    private String entity;
    private List<String> properties;
    private List<Result> associates = new ArrayList<>();
    
    public Result() {
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public List<Result> getAssociates() {
		return associates;
	}

	public void setAssociates(List<Result> associates) {
		this.associates = associates;
	}

}
