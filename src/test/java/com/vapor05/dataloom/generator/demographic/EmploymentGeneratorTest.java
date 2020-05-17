package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataMap;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author NicholasBocchini
 */
public class EmploymentGeneratorTest {


    @Test
    public void testSomeMethod() throws Exception
    {
        EmploymentGenerator generator = new EmploymentGenerator();
        DataMap data = new DataMap();
        DataMap timeData = new DataMap();
        long start;
        long elapsed;
        long unemployed = 0;
        
        generator.setSeed(1L);
        generator.setKey("job");
        data = generator.generate(data);
        assertTrue(data.containsKey("job"));
        assertTrue(data.containsKey("salary"));
        assertTrue(data.get("job") instanceof String);
        assertFalse(data.getString("job").isEmpty());        
        
        generator.setBirthDateKey("dob");
        data = new DataMap("{dob:1420092000000}"); //5years old
        data = generator.generate(data);
        assertFalse(data.containsKey("job"));
        data = new DataMap("{dob:1009864800000}"); //18years old
        data = generator.generate(data);
        assertTrue(data.containsKey("job"));
        data = new DataMap("{dob:1262325600000}"); //10years old
        data = generator.generate(data);
        assertFalse(data.containsKey("job"));
        data = new DataMap("{dob:473407200000}"); //35years old
        data = generator.generate(data);
        assertTrue(data.containsKey("job"));
        data = new DataMap("{dob:1514786400000}"); //2years old
        data = generator.generate(data);
        assertFalse(data.containsKey("job"));
        data = new DataMap("{dob:-1577901600000}"); //100years old
        data = generator.generate(data);
        assertTrue(data.containsKey("job"));    
        assertFalse(data.containsKey("salary"));
        assertEquals("Retired", data.getString("job"));
        
        for (int i=0; i<100000; i++) 
        {
            data = generator.generate(new DataMap());
            assertTrue(data.containsKey("job"));
            
            if (data.getString("job").equals("Unemployed")) unemployed++;
        }

        assertEquals(0.05, (unemployed/(float)100000), 0.005);
        assertTimeout(ofMillis(1), () -> {
            generator.generate(timeData);
        });
        
        start = System.nanoTime();
        generator.generate(timeData);
        elapsed = System.nanoTime() - start;
        System.out.println("EmploymentGenerator execution time (ms): " + elapsed/(float)1000000);
    }
}
