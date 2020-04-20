package com.vapor05.dataloom.databus;

import java.util.Arrays;

/**
 *
 * @author NicholasBocchini
 */
public class DataMapWalker {
    
    private DataMap record;
    
    public DataMapWalker(DataMap record)
    {
        this.record = record;
    }
    
    public DataMap getRecord()
    {
        return record;
    }
    
    public Object get(String key) throws DataMapException
    {
        DataMap parent = record;
        Object child = null;
        String[] keys;
        int count = 0;
        
        keys = key.split("\\.");
        
        while (count < keys.length)
        {
            if (parent.get(keys[count]) instanceof DataArray)
            {
                throw new DataMapException("Array navigation is not supported by get method");
            }
            
            if (parent.get(keys[count]) instanceof DataMap)
            {
                parent = parent.getDataMap(keys[count]);
                child = parent;
                count++;
                continue;
            }
            
            child = parent.get(keys[count]);
            break;
        }
        
        return child;
    }
    
    public String getString(String key) throws DataMapException
    {
        return (String) get(key);
    }
    
    public String getString(String key, String defaultValue) throws DataMapException
    {
        String value = getString(key);
        
        return value == null ? defaultValue : value;
    }
    
    public void update(DataMap parent, String key, Action action) throws DataMapException
    {
        String[] keys;
        DataMap previous = null;
        int count= 0;
        int index;
        Object child = parent;
        
        if (key == null)
        {
            this.record = (DataMap) action.replace(parent);
            return;
        }
        
        keys = key.split("\\.");
        
        if (key.charAt(key.length()-1) == '.')
        {
            keys = Arrays.copyOf(keys, keys.length+1);
            keys[keys.length-1] = "";
        }
        
        while (count < keys.length)
        {
            previous = parent;
            
            if (count < keys.length-1 && parent.get(keys[count]) == null)
            {
                parent.put(keys[count], new DataMap());
            }
            
            if (parent.get(keys[count]) instanceof DataMap)
            {
                parent = parent.getDataMap(keys[count]);
                child = parent;
                count++;
                continue;
            }
            
            child = parent.get(keys[count]);
            break;
        }
        
        if (child instanceof DataArray && count < keys.length-1)
        {
            Object map;
            index = 0;
            count = count +1;
            
            for (int i = 0; i < count; i++) index = index + keys[i].length() + 1;
            
            while (count < keys.length)
            {
                for (int j = 0; j < ((DataArray)child).size(); j++)
                {
                    map = ((DataArray)child).get(j);
                    
                    if (key.substring(index).equals(""))
                    {
                        ((DataArray)child).set(j, action.replace(map));
                    }
                    else
                    {
                        update((DataMap)map, key.substring(index), action);
                    }
                    
                    count++;
                }
                
                return;
            }
            
            return;            
        }
        
        if (child instanceof DataMap) parent = previous;
        
        parent.put(keys[keys.length-1], action.replace(child));
    }
    
    public void update(String key, Action action) throws DataMapException
    {
        update(record, key, action);
    }
    
    public void put(String key, final Object value) throws DataMapException
    {
        update(key, new Action() {
            @Override
            public Object replace(Object input)
            {
                return value;
            }
        });
    }
}
