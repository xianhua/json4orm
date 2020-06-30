package com.json4orm.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.json4orm.model.addupdate.AddOrUpdate;
import com.json4orm.model.schema.Entity;

/**
 * @author Xianhua Liu
 *
 */
public class AddOrUpdateContext {
    private Entity entity;
    private String insertSql;
    private String updateSql;
    private AddOrUpdate addOrUpdate;
    
    private List<Map<String, Object>> insertRecords = new ArrayList<>();
    private List<Map<String, Object>> updateRecords = new ArrayList<>();
    
    
    public Entity getEntity() {
        return entity;
    }
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }
    
    public AddOrUpdate getAddOrUpdate() {
        return addOrUpdate;
    }
    public void setAddOrUpdate(final AddOrUpdate addOrUpdate) {
        this.addOrUpdate = addOrUpdate;
    }
    public String getInsertSql() {
        return insertSql;
    }
    public void setInsertSql(final String insertSql) {
        this.insertSql = insertSql;
    }
    public String getUpdateSql() {
        return updateSql;
    }
    public void setUpdateSql(final String updateSql) {
        this.updateSql = updateSql;
    }
    public List<Map<String, Object>> getInsertRecords() {
        return insertRecords;
    }
    public void setInsertRecords(final List<Map<String, Object>> insertRecords) {
        this.insertRecords = insertRecords;
    }
    public List<Map<String, Object>> getUpdateRecords() {
        return updateRecords;
    }
    public void setUpdateRecords(final List<Map<String, Object>> updateRecords) {
        this.updateRecords = updateRecords;
    }
    
    public void addInsertRecord(final Map<String, Object> record) {
        this.insertRecords.add(record);
    }
    
    public void addUpdateRecord(final Map<String, Object> record) {
        this.updateRecords.add(record);
    }
    
}
