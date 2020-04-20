package com.vapor05.dataloom.cli;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.databus.exporter.CSVExporter;
import com.vapor05.dataloom.databus.exporter.Exporter;
import com.vapor05.dataloom.generator.AbstractParentGenerator;
import com.vapor05.dataloom.generator.Generator;
import com.vapor05.dataloom.json.JSONTokener;
import com.vapor05.dataloom.transformer.Transformer;
import com.vapor05.dataloom.util.JSONReflection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author NicholasBocchini
 */
public class GenerateFileCommand implements Command {
    public static final String CSV_FILE_TYPE = "CSV";
    
    private String fileType;
    private DataMap config;
    private int records;
    private File outFile;
    private long seed;
    private PrintStream out;
    private Generator[] generators;
    private Transformer[] transformers;
    
    
    @Override
    public void setParameters(String[] parameters) throws DataLoomException
    {
        fileType = parameters[0].toUpperCase();
        try
        {
            config = new DataMap(new JSONTokener(new FileInputStream(new File(parameters[1]))));
        } catch (FileNotFoundException ex)
        {
            throw new DataLoomException(ex.getMessage());
        }
        records = Integer.parseInt(parameters[2]);
        outFile = new File(parameters[3]);
        seed = Long.parseLong(parameters[4]);
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
            "           file <type> <config file> <records> <output file> <seed>\n" +
            "               <type>: File type to create, accepted values are: csv\n" +
            "               <config file>:  Full filepath to the configuration json file\n" +
            "               <records>:      Number of records to generate\n" +
            "               <output file>:  Full filepath for the file to generate\n" +
            "               <seed>: An integer value to seed the data generation. The same seed value will produce the same data output.";
    }

    private void loadGenerators() throws DataMapException
    {
        JSONReflection<Generator> loader = new JSONReflection<Generator>();
        DataArray<DataMap> generatorConfig = config.getDataArray("generators");
        
        generators = new Generator[generatorConfig.size()];
        out.println("Loading Generators...");
        
        for (int i = 0; i < generators.length; i++)
        {
            try
            {
                generators[i] = loader.read(generatorConfig.getDataMap(i));
            } catch (ClassNotFoundException ex)
            {
                throw new DataMapException(ex.getMessage());
            }
            
            generators[i].setSeed(seed);
            
            if (generators[i] instanceof AbstractParentGenerator)
            {
                ((AbstractParentGenerator)generators[i]).setChildGeneratorsSeed(seed + i);
            }
        }
        
        out.println("Loaded " + generators.length + " Generators");
    }
    
    private void loadTransformers() throws DataMapException
    {
        JSONReflection<Transformer> loader = new JSONReflection<Transformer>();
        DataArray<DataMap> transformerConfig = config.getDataArray("transformers");
        
        transformers = new Transformer[transformerConfig.size()];
        out.println("Loading Transformers...");
        
        for (int i = 0; i < transformers.length; i++)
        {
            try
            {
                transformers[i] = loader.read(transformerConfig.getDataMap(i));
            } catch (ClassNotFoundException ex)
            {
                throw new DataMapException(ex.getMessage());
            }
        }
        
        out.println("Loaded " + transformers.length + " Transformers");
    }
    
    @Override
    public boolean execute() throws DataMapException, DataLoomException {
        Exporter writer;
        DataMap generated;

        loadGenerators();
        loadTransformers();
        writer = openWriter(outFile, config.getDataArray("columns"));        
        out.println("Generating records...");
        
        for (int row = 1; row <= records; row++)
        {
            generated = new DataMap();
            
            for (Generator generator : generators)
            {
                generated = generator.generate(generated);
            }
            
            for (Transformer transformer : transformers)
            {
                generated = transformer.transform(generated);
            }
            
            writer.write(generated);
        }
        
        try
        {
            writer.finish();
        } catch (IOException ex)
        {
            throw new DataLoomException(ex.getMessage());
        }
        
        out.println("Wrote " + records + " to file " + outFile.getPath());
        
        return true;
    }    
    
    private Exporter openWriter(File out, DataArray columnList) throws DataLoomException, DataMapException
    {
        String[] columns = new String[columnList.size()];
        
        for (int i = 0; i < columns.length; i++) columns[i] = columnList.getString(i);
        
        if (fileType.equals(CSV_FILE_TYPE)) return new CSVExporter(out, columns, columns);
        
        throw new DataLoomException("No exporter could be created");
    }
}
