package com.vapor05.dataloom.cli;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.databus.exporter.JSONExporter;
import com.vapor05.dataloom.generator.demographic.PersonGenerator;
import com.vapor05.dataloom.transformer.MoveKeyTransformer;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author NicholasBocchini
 */
public class GeneratePersonCommand implements Command {

    private PrintStream out;
    private long seed;
    private File outFile;
    
    @Override
    public void setParameters(String[] parameters) throws DataLoomException
    {
        seed = Long.parseLong(parameters[0]);
        
        if (parameters.length != 1) outFile = new File(parameters[1]);
    }

    @Override
    public void setPrintStream(PrintStream out)
    {
        this.out = out;
    }

    @Override
    public String getName()
    {
        return "person";
    }

    @Override
    public String getHelp()
    {
        return "Generate randomized person information.\n" +
            "       Usage:\n" +
            "           person <seed> [output File]\n" +
            "               <seed>: An integer value to seed the data generation. The same seed value will produce the same data output.\n" +
            "           Optional:\n" +
            "               [output file]:  Full filepath for person data to be written to.";
    }

    @Override
    public boolean execute() throws DataLoomException, DataMapException
    {
        DataMap person = new DataMap();
        PersonGenerator generator = new PersonGenerator();
        MoveKeyTransformer moveKey = new MoveKeyTransformer();
        JSONExporter writer;
        
        generator.setSeed(seed);
        person = generator.generate(person);
        moveKey.setSourceKey("state");
        moveKey.setDestinationKey("location.state");
        person = moveKey.transform(person);
        moveKey.setSourceKey("city");
        moveKey.setDestinationKey("location.city");
        person = moveKey.transform(person);
        moveKey.setSourceKey("zip");
        moveKey.setDestinationKey("location.zip");
        person = moveKey.transform(person);
        moveKey.setSourceKey("address");
        moveKey.setDestinationKey("location.address");
        person = moveKey.transform(person);
        
        if (person.containsKey("jobTitle"))
        {
            moveKey.setSourceKey("jobTitle");
            moveKey.setDestinationKey("employment.jobTitle");
            person = moveKey.transform(person);
            moveKey.setSourceKey("salary");
            moveKey.setDestinationKey("employment.salary");
            person = moveKey.transform(person);
        }
        
        if (outFile == null) 
        {
            out.println(person.printJSON());
        }
        else 
        {
            writer = new JSONExporter(outFile);
            writer.write(person);
            
            try
            {
                writer.finish();
            } catch (IOException ex)
            {
                throw new DataLoomException(ex.getMessage());
            }
        }
        
        return true;
    }
    
}
