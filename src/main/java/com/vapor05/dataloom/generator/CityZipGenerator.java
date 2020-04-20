package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.json.JSONTokener;

/**
 *
 * @author NicholasBocchini
 */
public class CityZipGenerator extends AbstractGenerator {
    
    private String zipKey;
    private String stateKey = "state";
    private final DataArray<DataMap> source;
    private final DataMap lookup;
    
    public CityZipGenerator() throws DataMapException
    {
        String state;
        
        source = new DataArray<>(new JSONTokener(getClass().getResourceAsStream("/config/cityzip.json")));
        lookup = new DataMap();
        
        for (DataMap zip : source)
        {
            state = zip.getString("state");
            
            if (!lookup.containsKey(state)) lookup.put(state, new DataArray());
            
            lookup.getDataArray(state).add(zip);
        }
    }
    
    public CityZipGenerator(String key, String zipKey, String stateKey) throws DataMapException
    {
        this();
        this.key = key;
        this.zipKey = zipKey;
        this.stateKey = stateKey;
    }

    public void setZipKey(String zipKey)
    {
        this.zipKey = zipKey;
    }
    
    public String getZipKey()
    {
        return zipKey;
    }
    
    public void setStateKey(String stateKey)
    {
        this.stateKey = stateKey;
    }

    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        String state = record.getString(stateKey, "");
        DataArray<DataMap> stateInfo;
        int index;
        DataMap cityZip;
        
        if (state.equals(""))
        {
            record.put(key, "");
            record.put(zipKey, "");
        }
        else
        {
            stateInfo = lookup.getDataArray(state);
            index = random.nextInt(stateInfo.size());
            cityZip = stateInfo.get(index);
            record.put(key, cityZip.getString("city"));
            record.put(zipKey, cityZip.getString("zipcode"));
        }
        
        return record;
    }
    
    
}
