package com.vapor05.dataloom.util;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author NicholasBocchini
 */
public class JSONReflectionTest {
    
    @Test
    public void testLoadClass() throws Exception
    {
        JSONReflection<ExampleClass> reflect = new JSONReflection();
        Class clazz = reflect.loadClass(new DataMap("{<class>: "+ ExampleClass.class.getName() +"}"));
        
        assertEquals(ExampleClass.class, clazz);
        
    }
    
    @Test
    public void testInsertData() throws Exception
    {
        JSONReflection<ExampleClass> reflect = new JSONReflection();
        DataMap dm = new DataMap("{string:TestValue, int:15, double:87.12, " + 
                "dataMap:{key:value}, example:{<class>:"+ExampleClass.class.getName()+"}}");
        DataMap expected = new DataMap("{key:value}");
        
        dm.put("long", 100L);
        
        assertEquals("TestValue", reflect.insertData(String.class, dm, "string"));
        assertEquals(15, reflect.insertData(Integer.class, dm, "int"));
        assertEquals(87.12, reflect.insertData(Double.class, dm, "double"));
        assertEquals(100L, reflect.insertData(Long.class, dm, "long"));
        assertEquals(expected, reflect.insertData(DataMap.class, dm, "dataMap"));
        assertEquals(ExampleClass.class, reflect.insertData(ExampleClass.class, dm, "example").getClass());
        
    }
    
    @Test
    public void testReadDescription() throws Exception
    {
        JSONReflection<ExampleClass> reflect = new JSONReflection();
        DataMap dm = new DataMap("{<class>:"+ExampleClass.class.getName()+"}");
        ExampleClass test = (ExampleClass) reflect.readDescription(dm);

        assertNull(test.getString());
        assertEquals(0, test.getIntValue());
        assertEquals(0L, test.getLongValue());
        assertEquals(0.0, test.getDoubleValue());
        assertNull(test.getExample());
        assertNull(test.getStringArray());
        assertNull(test.getList());
        assertNull(test.getDataMap());
        assertNull(test.getDataArray());

        dm = new DataMap(new JSONTokener(getClass().getResourceAsStream("/reflection/exampleClass.json")));
        test = (ExampleClass) reflect.readDescription(dm);
        String[] expSArray = new String[]{"elm1", "elm2", "elm3"};
        String[] interList = new String[]{"list1", "list2", "list3"};
        List expList = Arrays.asList(interList);
        DataMap expDM = new DataMap("{key:value1, key2:value2}");
        DataArray expArray = new DataArray("[da1, da2, da3]");
        
        assertEquals("stringValue", test.getString());
        assertEquals(105, test.getIntValue());
        assertEquals(1005L, test.getLongValue());
        assertEquals(1.05, test.getDoubleValue());
        assertEquals("stringValue2", test.getExample().getString());
        assertEquals(205, test.getExample().getIntValue());
        assertEquals("elm1", test.getStringArray()[0]);
        assertEquals("elm2", test.getStringArray()[1]);
        assertEquals("elm3", test.getStringArray()[2]);
        assertEquals(expList, test.getList());
        assertEquals(expDM, test.getDataMap());
        assertEquals(expArray, test.getDataArray());
    }
    
    public static class ExampleClass
    {
        private String string;
        private int intValue;
        private long longValue;
        private double doubleValue;
        private ExampleClass example;
        private String[] stringArray;
        private List list;
        private DataMap dataMap;
        private DataArray dataArray;
        
        public void setString(String string)
        {
            this.string = string;
        }
        
        public String getString()
        {
            return string;
        }
        
        public int getIntValue() 
        {
            return intValue;
        }

        public void setIntValue(int intValue) 
        {
            this.intValue = intValue;
        }
        
        public long getLongValue() 
        {
            return longValue;
        }

        public void setLongValue(long longValue) 
        {
            this.longValue = longValue;
        }

        public double getDoubleValue() 
        {
            return doubleValue;
        }

        public void setDoubleValue(double doubleValue) 
        {
            this.doubleValue = doubleValue;
        }
        
        public void setExample(ExampleClass example)
        {
            this.example = example;
        }
        
        public ExampleClass getExample()
        {
            return example;
        }
                
        public void setStringArray(String[] stringArray) 
        {
            this.stringArray = stringArray;
        }

        public void setList(List list) 
        {
            this.list = list;
        }

        public void setDataMap(DataMap dataMap) 
        {
            this.dataMap = dataMap;
        }

        public void setDataArray(DataArray dataArray) 
        {
            this.dataArray = dataArray;
        }

        public String[] getStringArray() 
        {
            return stringArray;
        }

        public List getList() 
        {
            return list;
        }

        public DataMap getDataMap() 
        {
            return dataMap;
        }

        public DataArray getDataArray() 
        {
            return dataArray;
        }
    }
}
