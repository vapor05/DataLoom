package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataMap;
import static java.time.Duration.ofMillis;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author NicholasBocchini
 */
public class AddressGeneratorTest {

    @Test
    public void testGenerate() throws Exception
    {
        AddressGenerator generator = new AddressGenerator();
        DataMap data = new DataMap();
        DataMap timeData = new DataMap();
        long start;
        long elapsed;
        
        generator.setKey("address1");
        generator.setSeed(1L);
        generator.setChildGeneratorsSeed(1L);
        data = generator.generate(data);
        
        assertTrue(data.containsKey("address1"));
        assertTrue(data.containsKey("state"));
        assertTrue(data.containsKey("city"));
        assertTrue(data.containsKey("zip"));
        
        assertTimeout(ofMillis(1), () -> {
            generator.generate(timeData);
        });
        
        start = System.nanoTime();
        generator.generate(timeData);
        elapsed = System.nanoTime() - start;
        System.out.println("AddressGenerator execution time (ms): " + elapsed/(float)1000000);
    }
    
}
