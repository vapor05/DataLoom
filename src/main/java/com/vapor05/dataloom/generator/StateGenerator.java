package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.json.JSONTokener;

/**
 *
 * @author NicholasBocchini
 */
public class StateGenerator extends AbstractGenerator {

    public static final String US_LOCALIZATION = "US";
    public static final String TYPE_CODE = "code";
    public static final String TYPE_NAME = "name";
    
    private String localization = US_LOCALIZATION;
    private String type = TYPE_CODE;
    private final DataMap source;
    
    public StateGenerator()
    {
        source = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/states.json")));
    }

    public StateGenerator(String key)
    {
        this();
        this.key = key;
    }
    
    public void setLocalization(String localization)
    {
        this.localization = localization;
    }

    public void setType(String type)
    {
        this.type = type;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        DataMap localizedMap;
        DataArray<DataMap> stateInfo;
        int index;
        
        localizedMap = source.getDataMap(localization);
        stateInfo = localizedMap.getDataArray("codes");
        index = random.nextInt(stateInfo.size());
        record.put(key, stateInfo.getDataMap(index).getString(type));
        
        return record;
    }
    
}
