package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class StreetGeneratorTest  {

    @Test
    public void testGenerate() throws Exception
    {
        DataMap source = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/streets.json"))).getDataMap("US");
        DataArray commons = source.getDataMap("suffix").getDataArray("common");
        StreetGenerator generator = new StreetGenerator();
        final DataMap timeData = new DataMap();
        DataMap data = new DataMap();
        DataMap streets = new DataMap();
        int total = 0;
        int direction = 0;
        int common = 0;
        String street;
        
        
        generator.setSeed(1L);
        generator.setKey("street");
        
        assertTimeout(ofMillis(1), () -> {
            generator.generate(timeData);
        });

        assertTrue(generator.generate(data).containsKey("street"));
                
        for (int i = 0; i < 1000000; i++)
        {
            street = generator.generate(data).getString("street");
            
            if (!streets.containsKey(street)) streets.put(street, 1);
            else streets.put(street, streets.getInt(street) + 1);
            
            if (street.split(" ").length == 3) direction++;
            if (commons.contains(street.split(" ")[street.split(" ").length-1])) common++;
            
            total++;
        }

        for (String key : streets.keySet())
        {
            assertTrue((streets.getInt(key) / (double)total) <= 0.005);
        }
        
        assertEquals(0.8, common / (double)total, 0.005);
        assertEquals(0.3, direction / (double)total, 0.005);
    }
    
}
