package com.vapor05.dataloom.databus.exporter;

import com.vapor05.dataloom.databus.DataMap;

/**
 *
 * @author NicholasBocchini
 */
public interface Exporter {
    
    public void write(DataMap record);
}
