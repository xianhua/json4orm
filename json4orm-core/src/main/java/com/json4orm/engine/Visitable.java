package com.json4orm.engine;

import com.json4orm.exception.Json4ormException;

public interface Visitable {
	public void accept(Visitor visitor) throws Json4ormException;
}
