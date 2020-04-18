package com.vapor05.dataloom.cli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author NicholasBocchini
 */
public class ArgumentsTest {

    @Test
    public void testParse() throws Exception
    {
        String[] args = new String[]{"help"};
        Arguments arguments;
        String[] expected;
        
        arguments = new Arguments(args);
        arguments.parse();
        assertEquals(0, arguments.getParameters().length);
        assertTrue(arguments.getCommand() instanceof HelpCommand);
        
        args = new String[]{"file", "csv", "src/test/resources/generate_file/test_config.json", "125", "/test/path/outfile.csv", "123456789"};
        arguments = new Arguments(args);
        arguments.parse();
        assertEquals(5, arguments.getParameters().length);
        assertTrue(arguments.getCommand() instanceof GenerateFileCommand);
        expected = new String[]{"csv", "src/test/resources/generate_file/test_config.json", "125", "/test/path/outfile.csv", "123456789"};
        
        for (int i=0; i<arguments.getParameters().length; i++) assertEquals(expected[i], arguments.getParameters()[i]);
    }
    
}
