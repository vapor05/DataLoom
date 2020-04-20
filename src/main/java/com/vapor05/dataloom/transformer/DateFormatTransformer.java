package com.vapor05.dataloom.transformer;

import com.vapor05.dataloom.databus.Action;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.databus.DataMapWalker;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author NicholasBocchini
 */
public class DateFormatTransformer implements Transformer {
    
    private String key;
    private String format;
    private String timezone;

    public void setKey(String key)
    {
        this.key = key;
    }

    public void setFormat(String format)
    {
        this.format = format;
    }

    public void setTimezone(String timezone)
    {
        this.timezone = timezone;
    }

    @Override
    public DataMap transform(DataMap record) throws DataMapException
    {
        DataMapWalker walker = new DataMapWalker(record);
        final SimpleDateFormat formatter = new SimpleDateFormat(format);
        
        if (timezone != null) formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        
        walker.update(key, new Action<Long, String>()
        {
            @Override
            public String replace(Long input) throws DataMapException
            {
                if (input == null) return null;
                else return formatter.format(new Date(input));
            }            
        });
        
        return record;
    }
    
    
}
