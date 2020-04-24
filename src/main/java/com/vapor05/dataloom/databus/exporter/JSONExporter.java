package com.vapor05.dataloom.databus.exporter;

import com.vapor05.dataloom.cli.DataLoomException;
import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author NicholasBocchini
 */
public class JSONExporter implements Exporter {

    private FileWriter out;
    private boolean wroteFirst = false;
    
    public JSONExporter(File out) throws DataLoomException
    {
        try 
        {
            this.out = new FileWriter(out);
            this.out.write('[');
        } catch (IOException ex)
        {
            throw new DataLoomException(ex.getMessage());
        }
    }
    
    @Override
    public void write(DataMap record) throws DataLoomException
    { 
        try
        {
            if (!wroteFirst) wroteFirst = true;
            else out.write(",\n");
            
            writeMap(record);
            
        } catch (IOException ex)
        {
            throw new DataLoomException(ex.getMessage());
        }
    }
    
    public void write(DataArray array) throws DataLoomException
    {
        int i = 1;
        
        try
        {
            out.write("[");

            for (Object element : array)
            {   
                if (element instanceof DataMap) writeMap((DataMap) element);
                else if (element instanceof DataArray) write((DataArray) element);
                else if (element instanceof String) write((String) element);
                else out.write(element.toString());

                if (i++ != array.size()) out.write(",");
            } 
            
            out.write("]");
        } catch (IOException ex)
        {
            throw new DataLoomException(ex.getMessage());
        }
    }
    
    private void write(String value) throws IOException
    {
        out.write("\"" + value + "\"");
    }
    
    private void writeMap(DataMap map) throws IOException, DataLoomException
    {
        Object value;
        int i = 1;
        
        out.write("{");
        for(String key: map.keySet())
        {
            value = map.get(key);
            out.write("\"" + key + "\":");

            if (value instanceof DataMap) writeMap((DataMap) value);
            else if (value instanceof DataArray) write((DataArray) value);
            else if (value instanceof String) write((String) value);
            else out.write(value.toString());

            if (i++ != map.keySet().size()) out.write(",");
        }
        out.write("}");
    }

    @Override
    public void finish() throws IOException
    {
        out.write(']');
        out.close();
    }
    
}
