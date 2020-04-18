package com.vapor05.dataloom.databus.exporter;

import com.vapor05.dataloom.cli.DataLoomException;
import com.vapor05.dataloom.databus.DataMap;
import java.io.IOException;

/**
 *
 * @author NicholasBocchini
 */
public interface Exporter {
    
    public void write(DataMap record) throws DataLoomException;
    
    public void finish() throws IOException;
}
