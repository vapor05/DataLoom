package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author NicholasBocchini
 */
public class EducationGeneratorTest {

    @Test
    public void testGenerate()throws Exception
    {
        EducationGenerator generator = new EducationGenerator();
        DataArray<DataMap> source = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/education.json"))).getDataArray("over18");
        DataArray<String> codes = new DataArray();
        DataMap timeData = new DataMap();
        DataMap data;
        long start;
        long elapsed;
        
        for (DataMap code : source) codes.add(code.getString("code"));
        
        generator.setKey("education");
        generator.setSeed(1L);
        
        for (int i = 0; i < 1000; i++)
        {
            data = new DataMap();
            data = generator.generate(data);
            assertTrue(data.containsKey("education"));
            assertTrue(codes.contains(data.getString("education")));
        }
        
        generator.setBirthDateKey("dob");
        data = new DataMap("{dob:1420092000000}"); //5years old
        data = generator.generate(data);
        assertFalse(data.containsKey("education"));
        data = new DataMap("{dob:1009864800000}"); //18years old
        data = generator.generate(data);
        assertTrue(data.containsKey("education"));
        assertTrue(codes.contains(data.getString("education")));
        data = new DataMap("{dob:1262325600000}"); //10years old
        data = generator.generate(data);
        assertFalse(data.containsKey("education"));
        data = new DataMap("{dob:473407200000}"); //35years old
        data = generator.generate(data);
        assertTrue(data.containsKey("education"));
        assertTrue(codes.contains(data.getString("education")));
        data = new DataMap("{dob:1514786400000}"); //2years old
        data = generator.generate(data);
        assertFalse(data.containsKey("education"));
        
        data = new DataMap("{dob:473407200000}"); //35years old
        assertTimeout(ofMillis(1), () -> {
            generator.generate(timeData);
        });
        
        start = System.nanoTime();
        generator.generate(timeData);
        elapsed = System.nanoTime() - start;
        System.out.println("EducationGenerator execution time (ms): " + elapsed/(float)1000000);
    }
    
}
