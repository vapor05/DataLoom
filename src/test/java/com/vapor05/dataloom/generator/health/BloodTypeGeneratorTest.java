package com.vapor05.dataloom.generator.health;

import com.vapor05.dataloom.databus.DataMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author NicholasBocchini
 */
public class BloodTypeGeneratorTest {
    
    @Test
    public void testGenerate() throws Exception
    {
        BloodTypeGenerator generator = new BloodTypeGenerator();
        DataMap types = new DataMap("{O+:0,A+:0,B+:0,AB+:0,O-:0,A-:0,B-:0,AB-:0}");
        DataMap data;
        
        generator.setKey("bloodType");
        generator.setSeed(1L);
        data = generator.generate(new DataMap());
        assertTrue(data.containsKey("bloodType"));
        
        for (int i = 0; i < 1000000; i++)
        {
            data = generator.generate(new DataMap());
            types.put(data.getString("bloodType"), types.getLong(data.getString("bloodType")) + 1);
        }
        System.out.println(types);
        assertEquals(0.374, (types.getLong("O+"))/1000000.0, 0.005);
        assertEquals(0.357, (types.getLong("A+"))/1000000.0, 0.005);
        assertEquals(0.085, (types.getLong("B+"))/1000000.0, 0.005);
        assertEquals(0.034, (types.getLong("AB+"))/1000000.0, 0.005);
        assertEquals(0.066, (types.getLong("O-"))/1000000.0, 0.005);
        assertEquals(0.063, (types.getLong("A-"))/1000000.0, 0.005);
        assertEquals(0.015, (types.getLong("B-"))/1000000.0, 0.005);
        assertEquals(0.006, (types.getLong("AB-"))/1000000.0, 0.005);
    }
    
}
