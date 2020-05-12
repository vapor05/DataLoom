package com.vapor05.dataloom.cli;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.databus.exporter.JSONExporter;
import com.vapor05.dataloom.generator.demographic.PersonGenerator;
import com.vapor05.dataloom.transformer.DateFormatTransformer;
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
        DateFormatTransformer formatDate = new DateFormatTransformer();
        JSONExporter writer;
        
        generator.setSeed(seed);
        person = generator.generate(person);
        person = moveKey.transform("state", "location.state", person);
        person = moveKey.transform("city", "location.city", person);
        person = moveKey.transform("zip", "location.zip", person);
        person = moveKey.transform("address", "location.address", person);
        person = moveKey.transform("condition", "health.condition", person);
        person = moveKey.transform("hccCode", "health.hccCode", person);
        person = formatDate.transform("birthDate", "yyyy-MM-dd", null, person);
        
        if (person.containsKey("jobTitle"))
        {
            moveKey.setSourceKey("jobTitle");
            moveKey.setDestinationKey("employment.jobTitle");
            person = moveKey.transform("jobTitle", "employment.jobTitle", person);
            
            if (person.containsKey("salary"))
            {
                moveKey.setSourceKey("salary");
                person = moveKey.transform("salary", "employment.salary", person);
            }
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
