package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.json.JSONTokener;

/**
 *
 * @author NicholasBocchini
 */
public class StreetGenerator extends AbstractGenerator {

    public static final String US_LOCALIZATION = "US";
    
    private String localization = US_LOCALIZATION;
    private double commonSuffix = 0.80;
    private double directionName = 0.30;
    private final DataMap source;
    
    public StreetGenerator()
    {
        source = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/streets.json")));
    }
    
    public StreetGenerator(String key)
    {
        this();
        this.key = key;
    }

    public void setLocalization(String localization)
    {
        this.localization = localization;
    }

    public void setCommonSuffix(double commonSuffix)
    {
        this.commonSuffix = commonSuffix;
    }

    public void setDirectionName(double directionName)
    {
        this.directionName = directionName;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        DataMap source = this.source.getDataMap(localization);
        DataArray<String> names = source.getDataArray("names");
        DataArray<String> directions = source.getDataArray("direction");
        DataArray<String> suffixes = source.getDataMap("suffix").getDataArray((random.nextDouble() <= commonSuffix) ? "common" : "all");
        StringBuilder street = new StringBuilder();
        
        if (random.nextDouble() <= directionName) 
        {
            street.append(directions.getString(random.nextInt(directions.size()))).append(" ");
        }
        
        street.append(names.getString(random.nextInt(names.size()))).append(" ");
        street.append(suffixes.getString(random.nextInt(suffixes.size())));        
        record.put(key, street.toString());
        
        return record;
    }
    
}
