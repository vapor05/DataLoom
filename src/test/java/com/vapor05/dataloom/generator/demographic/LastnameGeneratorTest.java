package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.generator.demographic.LastnameGenerator;
import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class LastnameGeneratorTest {
    
    @Test
    public void testGenerate() throws Exception
    {
        LastnameGenerator generator = new LastnameGenerator();
        DataMap allNames = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/lastnames.json")));
        DataArray names;
        DataMap data;
       
        generator.setSeed(1L);
        generator.setKey("lastname");
        names = allNames.getDataMap("US").getDataArray("names");
        
        for (int i = 0; i < 1000000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            
            assertTrue(names.contains(data.getString("lastname")));
        }
        
        names = allNames.getDataMap("Mexico").getDataArray("names");
        generator.setLocalization("Mexico");
        
        for (int i = 0; i < 1000000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            assertTrue(names.contains(data.getString("lastname")));
        }
    }
    
}
