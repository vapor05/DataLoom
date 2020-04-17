package com.vapor05.dataloom;

import com.vapor05.dataloom.cli.Arguments;
import com.vapor05.dataloom.cli.Command;
import com.vapor05.dataloom.cli.DataLoomException;
import com.vapor05.dataloom.cli.HelpCommand;
import com.vapor05.dataloom.databus.DataMapException;

/**
 *
 * @author NicholasBocchini
 */
public class Main {
    
    public static void main(String[] args) throws DataLoomException, DataMapException
    {
        HelpCommand help = new HelpCommand();
        Arguments arguments;        
        Command command;
        
        arguments = new Arguments(args);
        
        try
        {
            arguments.parse();
        } 
        catch (DataLoomException ex)
        {
            System.out.println(ex.getMessage());
            help.setPrintStream(System.out);
            help.getHelp();
            System.exit(1);
        }
        
        command = arguments.getCommand();
        command.setPrintStream(System.out);
        command.execute();        
    }
}
