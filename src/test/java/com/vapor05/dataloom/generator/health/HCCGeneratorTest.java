package com.vapor05.dataloom.generator.health;

import com.vapor05.dataloom.databus.DataMap;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * 
 */
public class HCCGeneratorTest {

    @Test
    public void testGenerate() throws Exception
    {
        HCCGenerator generator = new HCCGenerator();
        DataMap hcc = new DataMap();
        DataMap timeData = new DataMap();
        DataMap data;
        long start;
        long elapsed;
        
        generator.setKey("condition");
        generator.setSeed(1L);
        
        data = generator.generate(new DataMap());
        assertTrue(data.containsKey("condition") && data.containsKey("hccCode"));
        

        for (int i = 0; i < 1000000; i++)
        {
            data = generator.generate(new DataMap());
            
            if (!hcc.containsKey(data.getString("hccCode"))) 
            {
                hcc.put(data.getString("hccCode"), 1);
            }
            else 
            {
                hcc.put(data.getString("hccCode"), hcc.getLong(data.getString("hccCode")) + 1);
            }
        }
        
        for (String key : hcc.keySet())
        {
            assertEquals(0.0078125, (hcc.getLong(key) / 1000000.0), 0.0005);
        }
        
        assertTimeout(ofMillis(1), () -> {
            generator.generate(timeData);
        });
        
        start = System.nanoTime();
        generator.generate(timeData);
        elapsed = System.nanoTime() - start;
        System.out.println("EducationGenerator execution time (ms): " + elapsed/(float)1000000);
    }
    
}
