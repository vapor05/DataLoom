package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import java.util.Random;

/**
 *
 * @author NicholasBocchini
 */
public abstract class AbstractParentGenerator implements Generator {

    protected String key;
    protected long seed;
    protected Random random;
    
    @Override
    public void setSeed(long seed)
    {
        this.seed = seed;
        this.random = new Random(seed);
    }

    @Override
    public void setKey(String key)
    {
        this.key = key;
    }
    
    public abstract void setChildGeneratorsSeed(long seed);
    
    @Override
    public abstract DataMap generate(DataMap record) throws DataMapException;
}
