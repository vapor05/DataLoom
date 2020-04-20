package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataMap;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class DateGeneratorTest {

    @Test
    public void testGenerate() throws Exception
    {
        DateGenerator generator = new DateGenerator();
        DataMap data;

        generator.setSeed(1L);
        generator.setKey("date");

        for (int i = 0; i < 10000000; i++)
        {
            data = generator.generate(new DataMap());
            // verify date is between default range 1900/1/1 - 2050/12/31
            assertTrue(-2208967200000L <= data.getLong("date") && data.getLong("date") <= 2556079200000L);
        }

        generator.setMinKey("minDate");
        generator.setMaxKey("maxDate");

        for (int i = 0; i < 10000000; i++)
        {
            data = generator.generate(new DataMap("{minDate:1546322400000, maxDate:1577772000000}"));
            // verify date is between range 2019/1/1 - 2019/12/31
            assertTrue(1546322400000L <= data.getLong("date") && data.getLong("date") <= 1577772000000L);
        }
    }
}
