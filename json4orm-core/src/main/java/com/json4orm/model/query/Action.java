package com.json4orm.model.query;

public enum Action {
    SEARCH("search"),
    ADD_OR_UPDATE("addOrUpdate"),
    DELETE("delete"),
    UNKNOWN("unknown");
    
    private String actionName;
    
    private Action(final String actionName) {
       this.actionName=actionName;   
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(final String actionName) {
        this.actionName = actionName;
    }
    
    public static Action getAction(final String actionName) {
        for(final Action act: Action.values()) {
            if(act.getActionName().equalsIgnoreCase(actionName)) {
                return act;
            }
        }
        
        return UNKNOWN;
    }
}
