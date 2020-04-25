package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataMap;
import static java.time.Duration.ofMillis;
import java.util.regex.Pattern;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author NicholasBocchini
 */
public class PersonGeneratorTest {

    @Test
    public void testGenerate() throws Exception
    {
        PersonGenerator generator = new PersonGenerator();
        DataMap data = new DataMap();
        DataMap timeData = new DataMap();
        Pattern numPattern = Pattern.compile("\\d\\d\\d\\d\\d");
        long start;
        long elapsed;
        
        generator.setSeed(1L);
        generator.setKey("person");
        generator.setChildGeneratorsSeed(1L);
        data = generator.generate(data);
        assertTrue(data.containsKey("gender"));
        assertTrue(data.containsKey("firstname"));
        assertTrue(data.containsKey("lastname"));
        assertTrue(data.containsKey("birthDate"));
        assertTrue(data.containsKey("state"));
        assertTrue(data.containsKey("zip"));
        assertTrue(data.containsKey("city"));
        assertTrue(data.containsKey("address"));
        assertTrue(numPattern.matcher(data.getString("zip")).matches());
        
        assertTimeout(ofMillis(1), () -> {
            generator.generate(timeData);
        });
        
        start = System.nanoTime();
        generator.generate(timeData);
        elapsed = System.nanoTime() - start;
        System.out.println("PersonGenerator execution time (ms): " + elapsed/(float)1000000);
    }
    
}
