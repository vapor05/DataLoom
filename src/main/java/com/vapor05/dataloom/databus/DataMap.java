package com.vapor05.dataloom.databus;

import com.vapor05.dataloom.json.JSONTokener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author NicholasBocchini
 */
public class DataMap extends HashMap<String, Object> {
    
    public DataMap() {}
    
    public DataMap(String json)
    {
        this(new JSONTokener(json));
    }
    
    public DataMap(JSONTokener tokener)
    {
        char c;
        String key;
        
        if (tokener.nextClean() != '{') throw tokener.syntaxError("A JSON text must start with '{'");
        
        for (;;)
        {
            c = tokener.nextClean();
            
            switch (c)
            {
                case 0:
                    throw tokener.syntaxError("A JSON text must end with '}'");
                case '}':
                    return;
                default:
                    tokener.back();
                    key = tokener.nextValue().toString();
            }
            
            c = tokener.nextClean();
            
            if (c != ':') throw tokener.syntaxError("Expected a ':' after a key");
            
            if (!this.containsKey(key)) this.put(key, tokener.nextValue());
            
            switch (tokener.nextClean())
            {
                case ';':
                case ',':
                    if (tokener.nextClean() == '}') return;
                    
                    tokener.back();
                    
                    break;
                case '}':
                    return;
                default:
                    throw tokener.syntaxError("Expected a ',' or '}'");
            }
        }
    }
            
    
    public Object put(String key, Object value)
    {
        return super.put(key, value);
    } 
    
    public String getString(String key) throws DataMapException
    {
        Object value = super.get(key);
        
        if (value == null) throw new DataMapException(value + " is not a valid string");
        
        return value.toString();
    }
    
    public int getInt(String key) throws DataMapException
    {
        Object value = super.get(key);
        
        if (value instanceof Number) return ((Number)value).intValue();
        if (value instanceof String) return Integer.parseInt((String)value);
        
        throw new DataMapException(value + " is not a valid integer");
    }
    
    public long getLong(String key) throws DataMapException
    {
        Object value = super.get(key);
        
        if (value instanceof Number) return ((Number)value).longValue();
        if (value instanceof String) return Long.parseLong((String)value);
        
        throw new DataMapException(value + " is not a valid integer");
    }
    
    public DataMap getDataMap(String key) throws DataMapException
    {
        Object value = super.get(key);
        
        if (value instanceof DataMap) return (DataMap) value;
        
        throw new DataMapException(value + " is not a valid DataObject");
    }
    
    public double getDouble(String key) throws DataMapException
    {
        Object value = super.get(key);
        
        if (value instanceof Number) return ((Number)value).doubleValue();
        if (value instanceof String) return Double.parseDouble((String)value);
        
        throw new DataMapException(value + " is not a valid double");
    }
    
    public DataArray getDataArray(String key)
    {
        Object value = super.get(key);
        DataArray array = new DataArray();
        
        if (value instanceof DataArray) return (DataArray) value;
        if (value instanceof List) return new DataArray((List) value);
        
        array.add(value);
        
        return array;
    }
    
    @Override
    public boolean equals(Object object)
    {
        return object instanceof DataMap ? super.equals(object) : false;
    }
}
