package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;

/**
 *
 * @author NicholasBocchini
 */
public class DateGenerator extends AbstractGenerator {

    private String minKey;
    private String maxKey;
    private long earliest;
    private long latest;
    
    public DateGenerator()
    {
        earliest = -2208967200000L; // 1900/1/1
        latest = 2556079200000L; // 2050/12/31
    }
    
    public DateGenerator(String key, long earliest, long latest)
    {
        this.key = key;
        this.earliest = earliest;
        this.latest = latest;
    }

    public void setMinKey(String minKey)
    {
        this.minKey = minKey;
    }

    public void setMaxKey(String maxKey)
    {
        this.maxKey = maxKey;
    }
    
    public void setEarliest(long earliest)
    {
        this.earliest = earliest;
    }

    public void setLatest(long latest)
    {
        this.latest = latest;
    }
    
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        long diff;
        long date;
        
        if (minKey != null) setEarliest(record.getLong(minKey));
        if (maxKey != null) setLatest(record.getLong(maxKey));
        
        diff = latest - earliest;
        date = (Math.abs(random.nextLong()) % diff) + earliest;
        record.put(key, date);
        
        return record;
    }
    
    
}
