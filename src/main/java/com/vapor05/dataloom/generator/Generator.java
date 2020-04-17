package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;

/**
 *
 * @author NicholasBocchini
 */
public interface Generator {
    
    public DataMap generate(DataMap record) throws DataMapException;
    
    public void setSeed(long seed);
    
    public void setKey(String key);
    
}
