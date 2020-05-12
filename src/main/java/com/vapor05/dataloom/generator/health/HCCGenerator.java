package com.vapor05.dataloom.generator.health;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.generator.AbstractGenerator;
import com.vapor05.dataloom.json.JSONTokener;

/**
 *
 * 
 */
public class HCCGenerator extends AbstractGenerator {
    
    private final DataArray<DataMap> source;
    private String hccCode = "hccCode";
    
    public HCCGenerator()
    {
        source = new DataArray(new JSONTokener(getClass().getResourceAsStream("/config/hcc.json")));
    }

    public HCCGenerator(String key)
    {
        this();
        this.key = key;
    }

    public void setHCCCode(String hccCode)
    {
        this.hccCode = hccCode;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        double split = random.nextDouble();
        
        for (DataMap hcc : source)
        {
            if (split >= hcc.getDouble("start") && split < hcc.getDouble("end"))
            {
                record.put(hccCode, hcc.getString("HCC"));
                record.put(key, hcc.getString("Description"));
                break;
            }
        }
        
        return record;
    }

}
