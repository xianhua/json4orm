package com.json4orm.model.addupdate;

import java.util.List;
import java.util.Map;

public class AddOrUpdate {
    private String addOrUpdate;
    private List<Map<String, Object>> data;
    public String getAddOrUpdate() {
        return addOrUpdate;
    }
    public void setAddOrUpdate(final String addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }
    public List<Map<String, Object>> getData() {
        return data;
    }
    public void setData(final List<Map<String, Object>> data) {
        this.data = data;
    }
    
    
}
