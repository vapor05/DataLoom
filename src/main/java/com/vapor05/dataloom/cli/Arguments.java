package com.vapor05.dataloom.cli;

/**
 *
 * @author NicholasBocchini
 */
public class Arguments {
    
    private final String[] cliArgs;
    private String[] parameters;
    private final Command[] commands = new Command[] { 
            new HelpCommand(),
            new GenerateFileCommand()
        };
    private Command command;
        
    public Arguments(String[] args)
    {
        if (args.length == 0) command = commands[0];
        cliArgs = new String[args.length];
        
        for (int i=0; i<args.length; i++) cliArgs[i] = args[i];
    }
    
    public String[] getParameters()
    {
        return parameters;
    }
    
    public void parse() throws DataLoomException
    {
        if (cliArgs.length == 0) return;

        parameters = new String[cliArgs.length-1];
        command = findCommandByName(cliArgs[0]);
        
        for (int i=1; i<cliArgs.length; i++) parameters[i-1] = cliArgs[i];
        
        command.setParameters(parameters);
    }
    
    public Command getCommand()
    {
        return command;
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
