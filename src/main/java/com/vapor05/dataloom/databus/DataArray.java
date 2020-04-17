package com.vapor05.dataloom.databus;

import com.vapor05.dataloom.json.JSONTokener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author NicholasBocchini
 */
public class DataArray<T> extends ArrayList {
    
    public DataArray() {}
    
    public DataArray(String array)
    {
        this(new JSONTokener(array));
    }
    
    public DataArray(Collection<? extends T> collection)
    {
        super(collection);
    }
    
    public DataArray(JSONTokener tokener)
    {
        if (tokener.nextClean() != '[') throw tokener.syntaxError("A DataMapArray test must start with '['");
        
        if (tokener.nextClean() != ']')
        {
            tokener.back();
            
            for (;;)
            {
                if (tokener.nextClean() == ',')
                {
                    tokener.back();
                    add(null);
                }
                else
                {
                    tokener.back();
                    add((T)tokener.nextValue());
                }
                
                switch (tokener.nextClean())
                {
                    case ',':
                        if (tokener.nextClean() == ']') return;
                        
                        tokener.back();
                        break;
                    case ']':
                        return;
                    default:
                        throw tokener.syntaxError("Expected a ',' or ']'");
                }
            }
        }
    }
    
    public DataMap getDataMap(int index) throws DataMapException
    {
        Object value = super.get(index);
        
        if (value instanceof DataMap) return (DataMap)value;
        
        throw new DataMapException(value + " is not a valid DataObject");
    }
    
    public String getString(int index) throws DataMapException
    {
        Object value = super.get(index);
        
        if (value == null) throw new DataMapException(value + " is not a valid string");
        
        return value.toString();
    }
    
    public DataArray getDataArray(int index) 
    {
        Object value = super.get(index);
        DataArray array = new DataArray();
        
        if (value instanceof DataArray) return (DataArray) value;
        if (value instanceof List) return new DataArray((List) value);
        
        array.add(value);
        
        return array;
    }
    
    @Override
    public boolean equals(Object object)
    {
        return object instanceof DataArray && super.equals(object);
    } 
}
