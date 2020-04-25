package com.vapor05.dataloom.cli;

import com.vapor05.dataloom.databus.DataMapException;
import java.io.PrintStream;
import java.util.List;

/**
 * A discrete piece of the DataLoom tool's functionality. A command is requested
 * by the user and is called to execute its function.
 * 
 *
 */
public interface Command {

    /**
     * Sets the {@code Command} parameters.
     * 
     * @param parameters A String array of the parameters the user passed
     * @throws DataLoomException if the parameters are incorrectly formatted
     */
    public void setParameters(String[] parameters) throws DataLoomException;
    
    /**
     * Sets the {@code Command} PrintStream to send output messages
     * 
     * @param out
     */
    public void setPrintStream(PrintStream out);
    
    /**
     * Provides the name used to identify this command.
     * 
     * @return the {@code String} value of the command name
     */
    public String getName();
    
    /**
     * Provides the user a text message on how to properly call this command.
     * 
     * @return the {@code String} help message
     */
    public String getHelp();
    
    /**
     * Executes this {@code Command} function.
     * 
     * @return {@code true} if the method completes its function successfully and
     * {@code false} otherwise.
     * @throws DataLoomException if there is an error while trying to execute its function.
     * @throws DataMapException  if there is a data processing error
     */
    public boolean execute() throws DataLoomException, DataMapException;
}
