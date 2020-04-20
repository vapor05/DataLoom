package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class FirstnameGeneratorTest {
    
    @Test
    public void testGenerate() throws Exception
    {
        FirstnameGenerator generator = new FirstnameGenerator();
        DataMap allNames = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/firstnames.json")));
        DataMap names;
        DataMap data;
       
        generator.setSeed(1L);
        generator.setKey("name");
        names = allNames.getDataMap("US");
        
        for (int i = 0; i < 1000000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            assertTrue(names.getDataArray("male").contains(data.getString("name")) || names.getDataArray("female").contains(data.getString("name")));
        }
        
        generator.setGenderKey("gender");
        
        for (int i = 0; i < 1000000; i++)
        {
            data = new DataMap("{gender: male}");
            data = generator.generate(data);
            assertTrue(names.getDataArray("male").contains(data.getString("name")));
            data = new DataMap("{gender: female}");
            data = generator.generate(data);
            assertTrue(names.getDataArray("female").contains(data.getString("name")));
        }
        
        generator.setLocalization("Mexico");
        names = allNames.getDataMap("Mexico");
        for (int i = 0; i < 1000000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            assertTrue(names.getDataArray("male").contains(data.getString("name")) || names.getDataArray("female").contains(data.getString("name")));
        }
    }
    
}
