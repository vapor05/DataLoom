package com.vapor05.dataloom.databus;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
/**
 *
 * @author NicholasBocchini
 */
public class DataMapTest {
    
    @Test
    public void testParse() throws Exception
    {
        DataMap dm = new DataMap("{boolean: true, integer: 456, double: 98.76, " +
                "string1: \"value\", string2: \'value2\', array: [1,2,3,4]}");
        
        assertTrue(dm.get("boolean").equals(Boolean.TRUE));
        assertTrue(dm.get("integer").equals(456l));
        assertTrue(dm.get("double").equals(98.76));
        assertTrue(dm.get("string1").equals("value"));
        assertTrue(dm.get("string2").equals("value2"));
        assertTrue(dm.get("array").equals(new DataArray("[1,2,3,4]")));
    }
    
    @Test
    public void testEquals()
    {
        DataMap data1;
        DataMap data2;
        DataMap data3;
        
        data1 = new DataMap("{key: 4, column: value}");
        data2 = new DataMap("{key: 4, column: value}");
        data3 = new DataMap("{key: 2, column: diffvalue}");
        
        assertTrue(data1.equals(data2));
        assertFalse(data1.equals(data3));
        
        data2.put("newkey", 78);
        assertFalse(data1.equals(data2));
        data1 = new DataMap();
        data2 = new DataMap();
        assertTrue(data1.equals(data2));
    }
}
