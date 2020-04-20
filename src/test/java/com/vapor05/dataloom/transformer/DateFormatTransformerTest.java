package com.vapor05.dataloom.transformer;

import com.vapor05.dataloom.databus.DataMap;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 * @author NicholasBocchini
 */
public class DateFormatTransformerTest {
   
    @Test
    public void testTransform() throws Exception
    {
        DateFormatTransformer transformer = new DateFormatTransformer();
        DataMap data = new DataMap("{date: 1587307920000}"); // 2020/04/19 09:52:00
        
        transformer.setKey("date");
        transformer.setFormat("MM/dd/yyyy");
        transformer.setTimezone("US/Central");
        assertEquals("04/19/2020", transformer.transform(data).getString("date"));
        transformer.setFormat("MM/dd/yyyy hh:mm:ss");
        data = new DataMap("{date: 1587307920000}");
        assertEquals("04/19/2020 09:52:00", transformer.transform(data).getString("date"));
        transformer.setTimezone("US/Eastern");
        data = new DataMap("{date: 1587307920000}");
        assertEquals("04/19/2020 10:52:00", transformer.transform(data).getString("date"));
        transformer.setFormat("MM/dd/yyyy");
        data = new DataMap("{date: 1587307920000}");
        assertEquals("04/19/2020", transformer.transform(data).getString("date"));
        transformer.setFormat("yyyy");
        data = new DataMap("{date: 1587307920000}");
        assertEquals("2020", transformer.transform(data).getString("date"));
        transformer.setFormat("MMMM");
        data = new DataMap("{date: 1587307920000}");
        assertEquals("April", transformer.transform(data).getString("date"));
    }
    
}
