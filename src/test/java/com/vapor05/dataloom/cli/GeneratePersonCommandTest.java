package com.vapor05.dataloom.cli;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.json.JSONTokener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author NicholasBocchini
 */
public class GeneratePersonCommandTest {

    @Test
    public void testGetName()
    {
        GeneratePersonCommand command = new GeneratePersonCommand();
        
        assertEquals("person", command.getName());
    }
    
    @Test 
    public void testGetHelp()
    {
        GeneratePersonCommand command = new GeneratePersonCommand();
        
        assertEquals("Generate randomized person information.\n" +
            "       Usage:\n" +
            "           person <seed> [output File]\n" +
            "               <seed>: An integer value to seed the data generation. The same seed value will produce the same data output.\n" +
            "           Optional:\n" +
            "               [output file]:  Full filepath for person data to be written to.",
                command.getHelp());
    }
    
    @Test
    public void testExecutePrint() throws Exception
    {
        GeneratePersonCommand command = new GeneratePersonCommand();
        ByteArrayOutputStream outTest = new ByteArrayOutputStream();
        Pattern numPattern = Pattern.compile("\\d\\d\\d\\d\\d");
        DataMap data;
        
        command.setParameters(new String[]{"1"});
        command.setPrintStream(new PrintStream(outTest));
        command.execute();
        
        data = new DataMap(new JSONTokener(new ByteArrayInputStream(outTest.toByteArray())));
        assertTrue(data.containsKey("gender"));
        assertTrue(data.containsKey("firstname"));
        assertTrue(data.containsKey("lastname"));
        assertTrue(data.containsKey("birthDate"));
        assertTrue(data.containsKey("location"));
        assertTrue(data.getDataMap("location").containsKey("state"));
        assertTrue(data.getDataMap("location").containsKey("zip"));
        assertTrue(data.getDataMap("location").containsKey("city"));
        assertTrue(data.getDataMap("location").containsKey("address"));
        assertTrue(numPattern.matcher(data.getDataMap("location").getString("zip")).matches());
    }
    
    @Test
    public void testExecuteWrite() throws Exception
    {
        GeneratePersonCommand command = new GeneratePersonCommand();
        ByteArrayOutputStream outTest = new ByteArrayOutputStream();
        File test = new File("target/generatePerson");
        
        delete(test);
        test.mkdirs();
        command.setParameters(new String[]{"1", "target/generatePerson/person.json"});
        command.setPrintStream(new PrintStream(outTest));
        command.execute();
        test = new File(test, "person.json");
        assertTrue(test.exists());
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
