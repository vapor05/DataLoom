package com.vapor05.dataloom.util;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMapException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author NicholasBocchini
 */
public class JSONReflection<T> {
    
    private ClassLoader loader;
    
    public T read(DataMap description) throws DataMapException, ClassNotFoundException
    {
        try
        {
            return readDescription(description);
        }
        catch (IntrospectionException ex) { throw new DataMapException(ex.getMessage()); }
    }
    
    T readDescription(DataMap description) throws DataMapException, ClassNotFoundException, IntrospectionException
    {
        Class<T> clazz = loadClass(description);
        BeanInfo info = Introspector.getBeanInfo(clazz);
        PropertyDescriptor[] properties = info.getPropertyDescriptors();
        T result;
        
        try
        {
            result = clazz.getDeclaredConstructor().newInstance();
            
            for (PropertyDescriptor property : properties)
            {
                if (description.containsKey(property.getName()))
                {
                    property.getWriteMethod().invoke(result, insertData(property.getPropertyType(), description, property.getName()));
                }
            }   
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) 
        { 
            throw new DataMapException(ex.getMessage()); 
        }
        
        return result;
    }
    
    Class loadClass(DataMap description) throws DataMapException, ClassNotFoundException
    {
        String name;
        
        loader = getClass().getClassLoader();
        name = description.getString("<class>");
        
        return loader.loadClass(name);
    }
    
    Object insertData(Class type, DataMap description, String key) throws DataMapException, ClassNotFoundException, IntrospectionException
    {
        if (type.equals(Integer.class) || type.equals(int.class)) return description.getInt(key);
        if (type.equals(String.class)) return description.getString(key);
        if (type.equals(Long.class) || type.equals(long.class)) return description.getLong(key);
        if (type.equals(Double.class) || type.equals(double.class)) return description.getDouble(key);
        if (type.equals(String[].class) && description.get(key) instanceof DataArray) return arrayToStringArray(description.getDataArray(key));
        
        if (type.equals(DataMap.class)) return description.getDataMap(key);
        if (type.equals(DataArray.class)) return description.getDataArray(key);
        
        if (type.equals(List.class) || description.get(key) instanceof DataArray) return arrayToList(description.getDataArray(key));
        if (type.equals(Map.class) || description.get(key) instanceof DataMap) return dataMapToObject(description.getDataMap(key));
        
        return description.get(key);
    }
    
    private Object dataMapToObject(DataMap object) throws DataMapException, ClassNotFoundException, IntrospectionException
    {
        if (object.containsKey("<class>")) return readDescription(object);

        return object;
    }

    private Object arrayToStringArray(DataArray dataArray) throws DataMapException 
    {
        String[] result = new String[dataArray.size()];
        
        for (int i=0; i < result.length; i++)
        {
            if (dataArray.get(i) != null) result[i] = dataArray.getString(i);
        }
        
        return result;
    }

    private Object arrayToList(DataArray dataArray) throws DataMapException, ClassNotFoundException, IntrospectionException 
    {
        DataArray result = new DataArray();

        for (int i = 0; i < dataArray.size(); i++)
        {
            if (dataArray.get(i) instanceof DataMap) result.add(dataMapToObject(dataArray.getDataMap(i)));
            else if (dataArray.get(i) instanceof DataArray) result.add(arrayToList(dataArray.getDataArray(i)));
            else result.add(dataArray.get(i));                        
        }

        return result;
    }
    
}
