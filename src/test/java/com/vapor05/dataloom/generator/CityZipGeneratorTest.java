package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import static java.time.Duration.ofMillis;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class CityZipGeneratorTest {
    
    @Test
    public void testGenerate() throws Exception
    {
        CityZipGenerator generator = new CityZipGenerator();
        final DataMap timeData = new DataMap("{state: 'NY'}");
        DataMap data = new DataMap();
        DataArray<DataMap> source = new DataArray<>(new JSONTokener(getClass().getResourceAsStream("/config/cityzip.json")));
        DataMap stateInfo = new DataMap("{zips:[], cities:[]}");
        
        generator.setKey("city");
        generator.setZipKey("zip");
        generator.setSeed(1L);
        assertEquals(new DataMap("{city:'', zip:''}"), generator.generate(data));
        
        data.put("state", "MO");
        
        for (DataMap record : source)
        {
            if(record.getString("state").equals("MO")) 
            {
                stateInfo.getDataArray("zips").add(record.getString("zipcode"));
                stateInfo.getDataArray("cities").add(record.getString("city"));
            }
        }
        
        assertTimeout(ofMillis(1), () -> {
            generator.generate(timeData);
        });
        
        for (int i = 0; i < 100000; i++)
        {
            data = generator.generate(data);
            assertTrue(stateInfo.getDataArray("zips").contains(data.getString("zip")));
            assertTrue(stateInfo.getDataArray("cities").contains(data.getString("city")));
        }
        
        data.put("state", "IL");
        stateInfo.put("zips", new DataArray());
        stateInfo.put("cities", new DataArray());
        
        for (DataMap record : source)
        {
            if(record.getString("state").equals("IL")) 
            {
                stateInfo.getDataArray("zips").add(record.getString("zipcode"));
                stateInfo.getDataArray("cities").add(record.getString("city"));
            }
        }
        
        for (int i = 0; i < 100000; i++)
        {
            data = generator.generate(data);
            assertTrue(stateInfo.getDataArray("zips").contains(data.getString("zip")));
            assertTrue(stateInfo.getDataArray("cities").contains(data.getString("city")));
        }
        
        data.put("state", "AR");
        stateInfo.put("zips", new DataArray());
        stateInfo.put("cities", new DataArray());
        
        for (DataMap record : source)
        {
            if(record.getString("state").equals("AR")) 
            {
                stateInfo.getDataArray("zips").add(record.getString("zipcode"));
                stateInfo.getDataArray("cities").add(record.getString("city"));
            }
        }
        
        for (int i = 0; i < 100000; i++)
        {
            data = generator.generate(data);
            assertTrue(stateInfo.getDataArray("zips").contains(data.getString("zip")));
            assertTrue(stateInfo.getDataArray("cities").contains(data.getString("city")));
        }
    }
}
