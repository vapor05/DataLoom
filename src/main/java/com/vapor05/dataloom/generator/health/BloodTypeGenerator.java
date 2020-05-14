package com.vapor05.dataloom.generator.health;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.generator.AbstractGenerator;
import com.vapor05.dataloom.json.JSONTokener;

/**
 *
 * @author NicholasBocchini
 */
public class BloodTypeGenerator extends AbstractGenerator {
    
    private final DataArray<DataMap> source;
    
    public BloodTypeGenerator()
    {
        source = new DataArray(new JSONTokener(getClass().getResourceAsStream("/config/health/bloodtype.json")));
    }
    
    public BloodTypeGenerator(String key)
    {
        this();
        this.key = key;
    }

    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        double split = random.nextDouble();

        for (DataMap type : source)
        {
            if (split >= type.getDouble("start") && split < type.getDouble("end"))
            {
                record.put(key, type.getString("code"));
                break;
            }
        }
        
        return record;
    }
    
}
