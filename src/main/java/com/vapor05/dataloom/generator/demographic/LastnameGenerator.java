package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.generator.AbstractGenerator;
import com.vapor05.dataloom.json.JSONTokener;

/**
 *
 * @author NicholasBocchini
 */
public class LastnameGenerator extends AbstractGenerator {
    
    public static final String US_LOCALIZATION = "US";
    public static final String MEXICO_LOCALIZATION = "Mexico";
    
    private DataMap source;
    private String localization = US_LOCALIZATION;
    
    public void setLocalization(String localization)
    {
        this.localization = localization;
    }
    
    public LastnameGenerator()
    {
        source = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/lastnames.json")));
    }
    
    public LastnameGenerator(String key)
    {
        this();
        this.key = key;
    }

    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        DataMap names = source.getDataMap(localization);
        DataArray<String> list;
        
        list = names.getDataArray("names");
        record.put(key, list.getString(random.nextInt(list.size())));
        
        return record;
    }   
    
}
