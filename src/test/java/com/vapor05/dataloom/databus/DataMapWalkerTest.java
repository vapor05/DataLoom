package com.vapor05.dataloom.databus;

import com.vapor05.dataloom.json.JSONTokener;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class DataMapWalkerTest {
    
    @Test
    public void testGet() throws Exception
    {
        DataMap data = new DataMap(new JSONTokener(getClass().getResourceAsStream("/walker/testWalker.json")));
        DataMapWalker walker = new DataMapWalker(data);
        
        assertEquals("value1", walker.get("key1"));
        assertEquals("value11", walker.get("key2.key11"));
        assertEquals("nestedValue", walker.get("key2.key22.nestedKey"));
        
        try 
        {
            walker.get("array");
            fail("Array navigation not supported, should throw a DataMapException");
        } catch (DataMapException ex) {
            assertEquals("Array navigation is not supported by get method", ex.getMessage());
        }
        
        try 
        {
            walker.get("array2.elm1Key");
            fail("Array navigation not supported, should throw a DataMapException");
        } catch (DataMapException ex) {
            assertEquals("Array navigation is not supported by get method", ex.getMessage());
        }
    }
    
    @Test
    public void testUpdate() throws Exception
    {
        DataMap data = new DataMap(new JSONTokener(getClass().getResourceAsStream("/walker/testWalker.json")));
        DataMapWalker walker = new DataMapWalker(data);
        
        walker.update("key1", new Action() {
            @Override
            public Object replace(Object input)
            {
                return "newValue1";
            }
        });
        assertEquals("newValue1", walker.getRecord().getString("key1"));
        walker.update("newKey", new Action() {
            @Override
            public Object replace(Object input)
            {
                return "addedValue";
            }
        });
        assertEquals("addedValue", walker.getRecord().getString("newKey"));
        walker.update("key2.key11", new Action() {
            @Override
            public Object replace(Object input)
            {
                return "newValue11";
            }
        });
        assertEquals("newValue11", walker.getRecord().getDataMap("key2").getString("key11"));
        walker.update("array.", new Action() {
            @Override
            public Object replace(Object input)
            {
                return (long)input + 1;
            }
        });
        assertEquals(2L, walker.getRecord().getDataArray("array").get(0));
        assertEquals(3L, walker.getRecord().getDataArray("array").get(1));
        assertEquals(4L, walker.getRecord().getDataArray("array").get(2));
        
        walker.update("array2.elm1Key", new Action() {
            @Override
            public Object replace(Object input)
            {
                return "newElm1Value";
            }
        });
        assertEquals("newElm1Value", walker.getRecord().getDataArray("array2").getDataMap(0).getString("elm1Key"));
        assertEquals("newElm1Value", walker.getRecord().getDataArray("array2").getDataMap(1).getString("elm1Key"));
        
        walker.update("nested.nested.nested", new Action() {
            @Override
            public Object replace(Object input)
            {
                return "newNestedValue";
            }
        });
        assertEquals("newNestedValue", walker.getRecord().getDataMap("nested").getDataMap("nested").getString("nested"));
    }
    
    @Test
    public void testPut() throws Exception
    {
        DataMap data = new DataMap();
        DataMapWalker walker = new DataMapWalker(data);
        
        walker.put("stringKey", "stringValue");
        assertEquals("stringValue", walker.getRecord().getString("stringKey"));
        
        walker.put("intKey", 88);
        assertEquals(88, walker.getRecord().getInt("intKey"));
        
        walker.put("longKey", 54L);
        assertEquals(54L, walker.getRecord().getLong("longKey"));
        
        walker.put("dataMapKey", new DataMap("{nestedKey: nestedValue}"));
        assertEquals(new DataMap("{nestedKey: nestedValue}"), walker.getRecord().getDataMap("dataMapKey"));
        
        walker.put("arrayKey", new DataArray("[e1,e2,e3]"));
        assertEquals(new DataArray("[e1,e2,e3]"), walker.getRecord().getDataArray("arrayKey"));
    }
    
    @Test
    public void testRemove() throws Exception
    {
        DataMap data = new DataMap("{key:1, keyRemove:valueRemove}");
        DataMapWalker walker = new DataMapWalker(data);
        
        walker.remove("keyRemove");
        assertEquals(new DataMap("{key:1}"), walker.getRecord());
        data = new DataMap("{key:1, obj:{keyRemove:valueRemove}}");
        walker = new DataMapWalker(data);
        walker.remove("obj.keyRemove");
        assertEquals(new DataMap("{key:1, obj:{}}"), walker.getRecord());
        walker.remove("obj");
        assertEquals(new DataMap("{key:1}"), walker.getRecord());
        
        data = new DataMap("{key:1, obj:{obj:{keyRemove:valueRemove}, key:8}}");
        walker = new DataMapWalker(data);
        walker.remove("obj.obj.keyRemove");
        assertEquals(new DataMap("{key:1, obj:{obj:{},key:8}}"), walker.getRecord());
        walker.remove("notAKey");
        assertEquals(new DataMap("{key:1, obj:{obj:{},key:8}}"), walker.getRecord());
        data = new DataMap("{key:1, array:[1,2,3]}");
        walker = new DataMapWalker(data);
        walker.remove("array");
        assertEquals(new DataMap("{key:1}"), walker.getRecord());
        data = new DataMap("{key:1, array:[{elm1:val},{elm1:val},{elm1:val,elm2:12}]}");
        walker = new DataMapWalker(data);
        walker.remove("array.elm1");
        assertEquals(new DataMap("{key:1, array:[{},{},{elm2:12}]}"), walker.getRecord());
        data = new DataMap("{key:1, obj:{array:[{elm1:val},{elm1:val},{elm1:val,elm2:12}]}}");
        walker = new DataMapWalker(data);
        walker.remove("obj.array.elm1");
        assertEquals(new DataMap("{key:1, obj:{array:[{},{},{elm2:12}]}}"), walker.getRecord());
        data = new DataMap("{key:1, array:[{elm1:[{elm1:val}]},{elm1:[{elm1:val},{elm1:val, elm2:val}]},{elm1:[{elm1:val}],elm2:12}]}");
        walker = new DataMapWalker(data);
        walker.remove("array.elm1.elm1");
        assertEquals(new DataMap("{key:1, array:[{elm1:[{}]},{elm1:[{},{elm2:val}]},{elm1:[{}],elm2:12}]}"), walker.getRecord());
    }
    
}
