package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.generator.AbstractGenerator;

/**
 *
 * @author NicholasBocchini
 */
public class GenderGenerator extends AbstractGenerator {

    private double split = 0.49;
    private double unknown = 0.01;
    
    public GenderGenerator() {}
    
    public GenderGenerator(String key)
    {
        this.key = key;
    }
    
    public void setSplit(double split)
    {
        this.split = split;
    }

    public void setUnknown(double unknown)
    {
        this.unknown = unknown;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        double split = (this.split * (1.0 - unknown)) + unknown;
        double test = random.nextDouble();
        String value;
        
        if (test <= unknown) value = "Unknown";
        else if (test <= split) value = "Male";
        else value = "Female";
        
        record.put(key, value);
        
        return record;
    }
}
