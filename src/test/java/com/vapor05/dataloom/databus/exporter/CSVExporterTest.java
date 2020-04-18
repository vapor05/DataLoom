package com.vapor05.dataloom.databus.exporter;

import com.vapor05.dataloom.databus.DataMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class CSVExporterTest {
    
    @Test
    public void testWriteString() throws Exception
    {
        File test = new File("target/exporter/csv");
        String[] keys = new String[]{"key1", "key2", "key3"};
        Object[] values = new Object[]{"value1", 2, 3.3};
        CSVExporter out;
        BufferedReader reader;
        
        delete(test);
        test.mkdirs();
        test = new File(test, "csvTest.csv");
        out = new CSVExporter(test);
        out.write(keys, values);
        out.finish();
        
        reader = new BufferedReader(new FileReader(test));
        assertEquals("\"key1\",\"key2\",\"key3\"", reader.readLine());
        assertEquals("\"value1\",2,3.3", reader.readLine());
        reader.close();
    }
    
    @Test
    public void testWriteMap() throws Exception
    {
        File test = new File("target/exporter/csv");
        DataMap data = new DataMap("{key1:value1, key2:2, key3:3.3}");
        CSVExporter out;
        BufferedReader reader;
        
        delete(test);
        test.mkdirs();
        test = new File(test, "csvTest.csv");
        out = new CSVExporter(test);
        out.write(data);
        out.finish();
        
        reader = new BufferedReader(new FileReader(test));
        assertEquals("\"key1\",\"key2\",\"key3\"", reader.readLine());
        assertEquals("\"value1\",2,3.3", reader.readLine());
        reader.close();
    }
    
    @Test
    public void testWriteMapKeys() throws Exception
    {
        File test = new File("target/exporter/csv");
        DataMap data = new DataMap("{key1:value1, key2:2, key3:3.3}");
        String[] keys = new String[]{"key1", "key3"};
        String[] names = new String[]{"column1", "column2"};
        CSVExporter out;
        BufferedReader reader;
        
        delete(test);
        test.mkdirs();
        test = new File(test, "csvTest.csv");
        out = new CSVExporter(test, keys, names);
        out.write(data);
        out.finish();
        
        reader = new BufferedReader(new FileReader(test));
        assertEquals("\"column1\",\"column2\"", reader.readLine());
        assertEquals("\"value1\",3.3", reader.readLine());
        reader.close();
    }
    
    private void delete(File dir)
    {
        if (dir.exists())
        {
            for (File file : dir.listFiles())
            {
                if (file.isDirectory()) delete(file);
                
                file.delete();
            }
            
            dir.delete();
        }
    }
}
