package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.generator.demographic.GenderGenerator;
import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class GenderGeneratorTest {
    
    @Test
    public void testGenerate() throws Exception
    {
        DataArray expected = new DataArray("[Female, Male, Unknown]");
        GenderGenerator generator = new GenderGenerator();
        DataMap data;
        
        generator.setKey("gender");
        generator.setSeed(1L);
        
        for (int i = 0; i < 10000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            assertTrue(expected.contains(data.getString("gender")));            
        }
    }
    
    @Test
    public void testProbabilities() throws Exception
    {
        GenderGenerator generator = new GenderGenerator();
        DataMap data;
        long male = 0;
        long female = 0;
        long unknown = 0;
        
        generator.setKey("gender");
        generator.setSeed(1L);
        
        for (int i = 0; i < 10000000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            
            if (data.getString("gender").equals("Male")) male++;
            else if (data.getString("gender").equals("Female")) female++;
            else if (data.getString("gender").equals("Unknown")) unknown++;
        }
        
        assertEquals(10000000, male + female + unknown);
        assertEquals(0.49, (male / (double)(male+female)), 0.005);
        assertEquals(0.51, (female / (double)(male+female)), 0.005);
        assertEquals(0.01, (unknown / (double)(unknown+male+female)), 0.005);
        
        generator.setSplit(0.6);
        generator.setUnknown(0.5);
        male = female = unknown = 0;
        
        for (int i = 0; i < 10000000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            
            if (data.getString("gender").equals("Male")) male++;
            else if (data.getString("gender").equals("Female")) female++;
            else if (data.getString("gender").equals("Unknown")) unknown++;
        }
        
        assertEquals(10000000, male + female + unknown);
        assertEquals(0.6, (male / (double)(male+female)), 0.005);
        assertEquals(0.4, (female / (double)(male+female)), 0.005);
        assertEquals(0.5, (unknown / (double)(unknown+male+female)), 0.005);
    }
}
