package com.vapor05.dataloom.databus;

import com.vapor05.dataloom.json.JSONTokener;
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
    
    public String getString(String key, String defaultValue) throws DataMapException
    {
        Object value = super.get(key);
        
        if (value == null) return defaultValue;
        
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
    
    public String printJSON()
    {
        StringBuilder json = new StringBuilder();
        int indent = 0;
        Object value;
        
        json.append("{\n");
        
        for (String key : keySet())
        {
            JSONPrintKey(key, json);
            value = get(key);
            
            if (value instanceof DataMap) 
            {
                indent += 4;
                json = JSONPrintDataMap((DataMap) value, indent, json);
                indent -= 4;
            }
            else if (value instanceof DataArray)
            {
                indent += 4;
                JSONPrintDataArray((DataArray)value, indent, json);
                indent -= 4;
            }
             else if (value instanceof String)
            {
                json = JSONPrintString((String)value, json);
            }
            else if (value == null)
            {
                json.append("null");
            }
            else
            {
                json.append(value.toString());
            }
            
            json.append(",\n");
        }
        
        if (!keySet().isEmpty()) json.deleteCharAt(json.length()-2);
        
        json.append("}");
        
        return json.toString();
    }

    private StringBuilder JSONPrintDataMap(DataMap data, int indent, StringBuilder json)
    {
        StringBuilder indentSpace = new StringBuilder();
        Object value;
        
        for(int i = 0; i < indent; i++) indentSpace.append(" ");

        json.append("{\n");
        
        for (String key : data.keySet())
        {
            json.append(indentSpace.toString());
            json = JSONPrintKey(key, json);
            value = data.get(key);
            
            if (value instanceof DataMap) 
            {
                indent += 4;
                json = JSONPrintDataMap((DataMap) value, indent, json);
                indent -= 4;
            }
            else if (value instanceof DataArray)
            {
                indent += 4;
                json = JSONPrintDataArray((DataArray)value, indent, json);
                indent -= 4;
            }
            else if (value instanceof String)
            {
                json = JSONPrintString((String)value, json);
            }
            else if (value == null)
            {
                json.append("null");
            }
            else
            {
                json.append(value.toString());
            }
            
            json.append(",\n");
        }
        
        if (!data.keySet().isEmpty()) json.deleteCharAt(json.length()-2);
        
        json.append(indentSpace.delete(indent-4, indentSpace.length())).append("}");
        
        return json;
    }
    
    private StringBuilder JSONPrintDataArray(DataArray array, int indent, StringBuilder json)
    {
        StringBuilder indentSpace = new StringBuilder();
        Object value;
        
        for(int i = 0; i < indent; i++) indentSpace.append(" ");

        json.append("[\n");
        
        for (Object element : array)
        {
            json.append(indentSpace.toString());
            
            if (element instanceof DataMap) 
            {
                indent += 4;
                json = JSONPrintDataMap((DataMap) element, indent, json);
                indent -= 4;
            }
            else if (element instanceof DataArray)
            {
                indent += 4;
                json = JSONPrintDataArray((DataArray)element, indent, json);
                indent -= 4;
            }
            else if (element instanceof String)
            {
                json = JSONPrintString((String)element, json);
            }
            else if (element == null)
            {
                json.append("null");
            }
            else
            {
                json.append(element.toString());
            }
            
            json.append(",\n");
        }
        
        if (!array.isEmpty()) json.deleteCharAt(json.length()-2);
        
        json.append(indentSpace.delete(indent-4, indentSpace.length()).toString()).append("]");
        
        return json;
    }
    
    private StringBuilder JSONPrintKey(String key, StringBuilder json)
    {
        json.append("\"").append(key).append("\": ");
        
        return json;
    }
    
    private StringBuilder JSONPrintString(String value, StringBuilder json)
    {
        json.append("\"").append(value).append("\"");
        
        return json;
    }
    
    @Override
    public boolean equals(Object object)
    {
        return object instanceof DataMap ? super.equals(object) : false;
    }
}
