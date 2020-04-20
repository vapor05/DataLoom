package com.vapor05.dataloom.generator;


import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;


/**
 *
 * @author NicholasBocchini
 */
public class StateGeneratorTest {
    
    @Test
    public void testGenerate() throws Exception
    {
        StateGenerator generator = new StateGenerator();
        DataMap data;
        DataArray<DataMap> stateInfo = (new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/states.json")))).getDataMap("US").getDataArray("codes");
        DataArray<String> codes = new DataArray();
        DataArray<String> names = new DataArray();
        
        for (Object state : stateInfo)
        {
            codes.add(((DataMap)state).getString("code"));
            names.add(((DataMap)state).getString("name"));
        }
        
        generator.setSeed(1L);
        generator.setKey("state");        
        
        for (int i = 0; i < 1000; i++)
        {
            data = new DataMap();
            assertTrue(codes.contains(generator.generate(data).getString("state")));
        }
        
        generator.setType("name");
        
        for (int i = 0; i < 1000; i++)
        {
            data = new DataMap();
            assertTrue(names.contains(generator.generate(data).getString("state")));
        }
    }
}
