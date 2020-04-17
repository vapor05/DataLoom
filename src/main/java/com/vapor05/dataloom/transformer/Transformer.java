package com.vapor05.dataloom.transformer;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;

/**
 *
 * @author NicholasBocchini
 */
public interface Transformer {
    
    public DataMap transform(DataMap record) throws DataMapException;
}
