package com.vapor05.dataloom.cli;

import java.io.PrintStream;

/**
 *
 * @author NicholasBocchini
 */
public class HelpCommand implements Command {

    private final Command[] commands = new Command[] { 
            new GenerateFileCommand()
        };
    private PrintStream out;
    String name = "help";
    
    @Override
    public void setParameters(String[] parameters)
    {
        if (parameters.length != 0) name = parameters[0];
    }
    
    @Override
    public void setPrintStream(PrintStream out)
    {
        this.out = out;
    }
    
    @Override
    public String getName() 
    {
        return "help";
    }

    @Override
    public String getHelp() 
    {
        StringBuilder commandList = new StringBuilder();
        
        for (Command command : commands) commandList.append(command.getName() + "\n\t\t");
        
        return "Get help on how to interact with DataLoom CLI.\n" +
            "   Usage: \n" +
            "       help <command>\n" +
            "           See how to use the listed command\n" +
            "           All possible commands:\n\n" +
            "           " + commandList.toString().trim();
    }

    @Override
    public boolean execute() throws DataLoomException
    {
        Command command;
        
        if (name.equals("help"))
        {
            out.println(getHelp());
            return true;
        }
        
        command = findCommandByName(name);
        out.println(command.getHelp());
        
        return true;
    }
    
    private Command findCommandByName(String name) throws DataLoomException
    {
        for (Command command : commands)
        {
            if (command.getName().equals(name)) return command;
        }
        
        throw new DataLoomException("No Command found");
    }
        
}
