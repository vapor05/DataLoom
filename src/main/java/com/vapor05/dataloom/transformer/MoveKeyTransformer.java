package com.vapor05.dataloom.transformer;

import com.vapor05.dataloom.cli.DataLoomException;
import com.vapor05.dataloom.databus.Action;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.databus.DataMapWalker;

/**
 *
 * @author NicholasBocchini
 */
public class MoveKeyTransformer implements Transformer {
    private String sourceKey;
    private String destinationKey;

    public void setSourceKey(String sourceKey)
    {
        this.sourceKey = sourceKey;
    }

    public void setDestinationKey(String destinationKey)
    {
        this.destinationKey = destinationKey;
    }
    
    public DataMap transform(String sourceKey, String destinationKey, DataMap record) throws DataMapException
    {
        String origSourceKey = this.sourceKey;
        String orgDestKey = this.destinationKey;
        
        this.sourceKey = sourceKey;
        this.destinationKey = destinationKey;
        transform(record);
        this.sourceKey = origSourceKey;
        this.destinationKey = orgDestKey;
        
        return record;
    }

    @Override
    public DataMap transform(DataMap record) throws DataMapException
    {
        DataMapWalker walker = new DataMapWalker(record);
        
        walker.update(sourceKey, new Action() {
            @Override
            public Object replace(Object input) throws DataMapException
            {
                new DataMapWalker(record).update(destinationKey, new Action() {
                    @Override
                    public Object replace(Object destintion) throws DataMapException
                    {
                        return input;
                    }
                });
                
                return null;
            }
        });
        
        try
        {
            walker.remove(sourceKey);
        } catch (DataLoomException ex)
        {
            throw new DataMapException(ex.getMessage());
        }
        
        return walker.getRecord();
    }
}
