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
public class FirstnameGenerator extends AbstractGenerator {

    public static final String US_LOCALIZATION = "US";
    public static final String MEXICO_LOCALIZATION = "Mexico";
    
    private DataMap source;
    private String genderKey = "gender";
    private String localization = US_LOCALIZATION;
    
    public void setGenderKey(String genderKey)
    {
        this.genderKey = genderKey;
    }
    
    public void setLocalization(String localization)
    {
        this.localization = localization;
    }
    
    public FirstnameGenerator()
    {
        source = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/firstnames.json")));
    }
    
    public FirstnameGenerator(String key)
    {
        this();
        this.key = key;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        DataMap names = source.getDataMap(localization);
        String type = "";
        DataArray<String> list;
        int index;
        
        if (record.containsKey(genderKey)) type = record.getString(genderKey, "").toLowerCase();
        if (type.equals("unknown") || type.equals("")) type = random.nextBoolean() ? "male" : "female";

        list = names.getDataArray(type);
        index = random.nextInt(list.size());
        record.put(key, list.getString(index));
        
        return record;
    }
    
}
