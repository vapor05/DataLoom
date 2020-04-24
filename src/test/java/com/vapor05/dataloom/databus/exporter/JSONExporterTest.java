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
public class JSONExporterTest {
    
    @Test
    public void testWrite() throws Exception
    {
        File test = new File("target/exporter/json");
        DataMap data = new DataMap("{key1:value1, key2:2, key3:{nestedKey:nestedValue}, array:[1,2,3]}");
        JSONExporter writer;
        BufferedReader reader;
        
        delete(test);
        test.mkdirs();
        test = new File(test, "jsonTest.json");
        writer = new JSONExporter(test);
        writer.write(data);
        writer.finish();
        
        reader = new BufferedReader(new FileReader(test));
        assertEquals("[{\"key1\":\"value1\",\"key2\":2,\"array\":[1,2,3],\"key3\":{\"nestedKey\":\"nestedValue\"}}]",
                    reader.readLine());
        reader.close();
        
        test = new File("target/exporter/json");
        delete(test);
        test.mkdirs();
        test = new File(test, "jsonTest.json");
        writer = new JSONExporter(test);
        writer.write(data);
        data = new DataMap("{moreKey:moreData, key:2.23}");
        writer.write(data);
        data = new DataMap("{nestedKey:[{elm1:1},{elm2:2},{elm2:{key:[1,2,3]}}]}");
        writer.write(data);
        writer.finish();
        reader = new BufferedReader(new FileReader(test));
        assertEquals("[{\"key1\":\"value1\",\"key2\":2,\"array\":[1,2,3],\"key3\":{\"nestedKey\":\"nestedValue\"}},",
                    reader.readLine());
        assertEquals("{\"moreKey\":\"moreData\",\"key\":2.23},",
                    reader.readLine());
        assertEquals("{\"nestedKey\":[{\"elm1\":1},{\"elm2\":2},{\"elm2\":{\"key\":[1,2,3]}}]}]",
                    reader.readLine());
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
