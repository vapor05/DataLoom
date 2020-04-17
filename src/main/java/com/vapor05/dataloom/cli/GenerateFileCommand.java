package com.vapor05.dataloom.cli;

import java.io.File;
import java.io.PrintStream;

/**
 *
 * @author NicholasBocchini
 */
public class GenerateFileCommand implements Command {
    private File config;
    private int records;
    private File outFile;
    private long seed;
    private PrintStream out;
    
    
    @Override
    public void setParameters(String[] parameters)
    {
        config = new File(parameters[0]);
        records = Integer.parseInt(parameters[1]);
        outFile = new File(parameters[2]);
        seed = Long.parseLong(parameters[3]);
    }
    
    @Override
    public void setPrintStream(PrintStream out)
    {
        this.out = out;
    }
    
    @Override
    public String getName() {
        return "file";
    }

    @Override
    public String getHelp() {
        return "Generate a file of randomized data using the provided configuration json file.\n" +
            "       Usage:\n" +
            "           file <config file> <records> <output file> <seed>\n" +
            "               <config file>:  Full filepath to the configuration json file\n" +
            "               <records>:      Number of records to generate\n" +
            "               <output file>:  Full filepath for the file to generate\n" +
            "               <seed>: An integer value to seed the data generation. The same seed value will produce the same data output.";
    }

    @Override
    public boolean execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }    
}
