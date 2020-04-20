package com.vapor05.dataloom.transformer;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.databus.DataMapWalker;

/**
 *
 * @author NicholasBocchini
 */
public class ConcatenateTransformer implements Transformer {

    private String[] concatKeys;
    private String outputKey;
    private String delimiter;

    public void setConcatKeys(String[] concatKeys)
    {
        this.concatKeys = concatKeys;
    }

    public void setOutputKey(String outputKey)
    {
        this.outputKey = outputKey;
    }

    public void setDelimeter(String delimiter)
    {
        this.delimiter = delimiter;
    }
    
    @Override
    public DataMap transform(DataMap record) throws DataMapException
    {
        DataMapWalker walker = new DataMapWalker(record);
        StringBuilder result = new StringBuilder();
        
        for (String key : concatKeys)
        {
            result.append(delimiter);
            result.append(walker.getString(key, ""));
        }
        
        if (!result.toString().isEmpty()) 
        {
            walker.put(outputKey, result.toString().substring(delimiter.length()));
        }
        
        return walker.getRecord();
    }
    
}
