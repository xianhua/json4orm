package com.json4orm.parser;

import java.util.HashMap;
import java.util.Map;

import com.json4orm.util.Constants;

public class ParserFactory {
    private static final Map<String, Parser<?>> PARSERS = new HashMap<>();
    
    public static Parser<?> getParser(final String action) {
        Parser parser = PARSERS.get(action);
        if(parser!=null) {
            return parser;
        }
        
        if(action.equalsIgnoreCase(Constants.ADD_OR_UPDATE)) {
            parser = new AddOrUpdateParser();
            PARSERS.put(Constants.ADD_OR_UPDATE, parser);
            
        }else if(action.equalsIgnoreCase(Constants.QUERY) || action.equalsIgnoreCase(Constants.QUERY_FOR)) {
            parser = new QueryParser();
            PARSERS.put(Constants.QUERY, parser);
        }
        return parser;
    }
    
}
