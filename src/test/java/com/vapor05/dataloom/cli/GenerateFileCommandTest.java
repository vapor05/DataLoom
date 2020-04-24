package com.vapor05.dataloom.cli;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class GenerateFileCommandTest {
    
    @Test
    public void testExecute() throws Exception
    {
        GenerateFileCommand generateFile = new GenerateFileCommand();
        String[] args = new String[]{"csv", "src/test/resources/generate_file/test_config.json", "0", "target/generateFile/csvTest.csv", "123456789"};
        ByteArrayOutputStream outTest = new ByteArrayOutputStream();
        File test = new File("target/generateFile");
        
        delete(test);
        test.mkdirs();
        generateFile.setParameters(args);
        generateFile.setPrintStream(new PrintStream(outTest));
        generateFile.execute();
        
        test = new File(test, "csvTest.csv");
        assertTrue(test.exists());
        assertEquals("Loading Generators...\nLoaded 0 Generators\nLoading Transformers...\n"
                + "Loaded 0 Transformers\nGenerating records...\nWrote 0 to file target/generateFile/csvTest.csv\n",
                    outTest.toString());
        
        args[0] = "json";
        args[3] = "target/generateFile/jsonTest.json";
        outTest = new ByteArrayOutputStream();
        test = new File("target/generateFile/jsonTest.json");
        generateFile.setParameters(args);
        generateFile.setPrintStream(new PrintStream(outTest));
        generateFile.execute();
        assertTrue(test.exists());
        assertEquals("Loading Generators...\nLoaded 0 Generators\nLoading Transformers...\n"
                + "Loaded 0 Transformers\nGenerating records...\nWrote 0 to file target/generateFile/jsonTest.json\n",
                    outTest.toString());
    }
    
    @Test
    public void testGetHelp()
    {
        GenerateFileCommand generateFile = new GenerateFileCommand();
        
        assertEquals("Generate a file of randomized data using the provided configuration json file.\n" +
            "       Usage:\n" +
            "           file <type> <config file> <records> <output file> <seed>\n" +
            "               <type>: File type to create, accepted values are: csv, json\n" +
            "               <config file>:  Full filepath to the configuration json file\n" +
            "               <records>:      Number of records to generate\n" +
            "               <output file>:  Full filepath for the file to generate\n" +
            "               <seed>: An integer value to seed the data generation. The same seed value will produce the same data output.",
                generateFile.getHelp());
    }
    
    private void delete(File dir)
    {
        if (dir.exists())
        {
            for (File file : dir.listFiles())
            {
                if (file.isDirectory()) delete(file);
                
                file.delete();
            }
            
            dir.delete();
        }
    }
}
