/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vapor05.dataloom.cli;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author NicholasBocchini
 */
public class HelpCommandTest {

    @Test
    public void testSetParameters()
    {
        HelpCommand help = new HelpCommand();
        String[] args = new String[]{"file"};
        ByteArrayOutputStream outTest = new ByteArrayOutputStream();
        
        help.setParameters(args);
        help.setPrintStream(new PrintStream(outTest));
        assertEquals("file", help.name);
    }
    
    @Test 
    public void testGetHelp()
    {
        HelpCommand help = new HelpCommand();
        
        help.setParameters(new String[]{"file"});
        assertEquals("Get help on how to interact with DataLoom CLI.\n" +
            "   Usage: \n" +
            "       help <command>\n" +
            "           See how to use the listed command\n" +
            "           All possible commands:\n\n" +
            "           file", help.getHelp());
    }
    
    @Test
    public void testExecute() throws Exception
    {
        HelpCommand help = new HelpCommand();
        ByteArrayOutputStream outTest = new ByteArrayOutputStream();
        
        help.setParameters(new String[]{"file"});
        help.setPrintStream(new PrintStream(outTest));
        help.execute();
        
        assertEquals("Generate a file of randomized data using the provided configuration json file.\n" +
            "       Usage:\n" +
            "           file <type> <config file> <records> <output file> <seed>\n" +
            "               <type>: File type to create, accepted values are: csv, json\n" +
            "               <config file>:  Full filepath to the configuration json file\n" +
            "               <records>:      Number of records to generate\n" +
            "               <output file>:  Full filepath for the file to generate\n" +
            "               <seed>: An integer value to seed the data generation. The same seed value will produce the same data output.\n",
                outTest.toString());
        
        outTest = new ByteArrayOutputStream();
        help.setParameters(new String[]{"help"});
        help.setPrintStream(new PrintStream(outTest));
        help.execute();
        assertEquals("Get help on how to interact with DataLoom CLI.\n" +
            "   Usage: \n" +
            "       help <command>\n" +
            "           See how to use the listed command\n" +
            "           All possible commands:\n\n" +
            "           file\n", outTest.toString());
    }
    
}
