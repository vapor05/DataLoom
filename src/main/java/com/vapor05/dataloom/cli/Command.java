package com.vapor05.dataloom.cli;

import com.vapor05.dataloom.databus.DataMapException;
import java.io.PrintStream;

/**
 *
 * @author NicholasBocchini
 */
public interface Command {
    
    public void setParameters(String[] parameters) throws DataLoomException;
    
    public void setPrintStream(PrintStream out);
    
    public String getName();
    
    public String getHelp();
    
    public boolean execute() throws DataLoomException, DataMapException;
}
