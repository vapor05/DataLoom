/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vapor05.dataloom.databus.exporter;

import com.vapor05.dataloom.cli.DataLoomException;
import com.vapor05.dataloom.databus.DataMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author NicholasBocchini
 */
public class CSVExporter implements Exporter {
    
    private FileWriter out;
    private boolean headerWritten;
    private String[] keys;
    private String[] names;
    
    public CSVExporter(File out) throws DataLoomException
    {
        this(out, false);
    }
    
    public CSVExporter(File out, boolean append) throws DataLoomException
    {
        headerWritten = append;
        
        try { this.out = new FileWriter(out, append); }
        catch (IOException ex) { throw new DataLoomException(ex.getMessage()); }
    }
    
    public CSVExporter(File out, String[] keys, String[] names) throws DataLoomException
    {
        this.keys = keys;
        this.names = names;
        
        try { this.out = new FileWriter(out);  } 
        catch (IOException ex) { throw new DataLoomException(ex.getMessage()); }
    }
    
    private void writeHeader(String[] keys) throws IOException
    {
        writeRow(keys);
        headerWritten = true;
    }
    
    public void write(String[] keys, Object[] values) throws IOException
    {
        if (!headerWritten) writeRow(keys);
        
        writeRow(values);
    }
    
    @Override
    public void write(DataMap object) throws DataLoomException
    {
        Object[] values;
        
        if (this.keys == null) detectHeader(object);
        
        values = new Object[keys.length];
        
        for (int i = 0; i < keys.length; i++) values[i] = object.get(keys[i]);
        
        try
        {
            if (!headerWritten) writeHeader(names);

            writeRow(values);
        }
        catch (IOException ex)
        {
            throw new DataLoomException(ex.getMessage());
        }
        
    }
    
    private void writeRow(Object[] values) throws IOException
    {
        for (int i=0; i < values.length; i++)
        {
            if (i > 0) out.write(",");

            if (values[i] != null)
            {
                if (values[i] instanceof String)
                {
                    out.write("\"");
                    out.write(values[i].toString().replaceAll("\"", "\"\""));
                    out.write("\"");
                }
                else out.write(values[i].toString());
            }
        }
        out.write("\n");
    }
    
    private void detectHeader(DataMap row)
    {
        Set<String> keySet = row.keySet();
        Iterator<String> iter = keySet.iterator();

        names = keys = new String[keySet.size()];

        for (int i = 0; i < keys.length; i++) names[i] = keys[i] = iter.next();
    }
    
    @Override
    public void finish() throws IOException
    {
        out.close();
    }
}
