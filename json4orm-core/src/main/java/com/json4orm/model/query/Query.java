package com.json4orm.model.query;

import java.util.List;

import com.json4orm.engine.Visitable;
import com.json4orm.engine.Visitor;
import com.json4orm.exception.Json4ormException;

public class Query implements Visitable{
    private String queryFor;
    private Filter filter;
    private Result result;
    
    public Query() {
    }

    public String getQueryFor() {
        return queryFor;
    }

    public void setQueryFor(String queryFor) {
        this.queryFor = queryFor;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public void accept(Visitor visitor) throws Json4ormException {
		visitor.visit(this);
	}
}
