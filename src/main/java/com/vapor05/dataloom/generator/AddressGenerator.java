package com.vapor05.dataloom.generator;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;

/**
 *
 * @author NicholasBocchini
 */
public class AddressGenerator extends AbstractParentGenerator {

    public static final String US_LOCALIZATION = "US";
    
    private String localization = US_LOCALIZATION;
    private StateGenerator state;
    private CityZipGenerator cityzip;
    private StreetGenerator street;
    
    public AddressGenerator() throws DataMapException
    {
        state = new StateGenerator("state");
        cityzip = new CityZipGenerator("city", "zip", "state");
        street = new StreetGenerator("street");
    }
    
    public AddressGenerator(String key, String state, String city, String zip, String street) throws DataMapException
    {
        this.key = key;
        this.state = new StateGenerator(state);
        this.cityzip = new CityZipGenerator(city, zip, state);
        this.street = new StreetGenerator(street);
    }

    public void setLocalization(String localization)
    {
        this.localization = localization;
    }

    public void setState(StateGenerator state)
    {
        this.state = state;
    }

    public void setCityzip(CityZipGenerator cityzip)
    {
        this.cityzip = cityzip;
    }

    public void setStreet(StreetGenerator street)
    {
        this.street = street;
    }

    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        StringBuilder result = new StringBuilder();
        int number = random.nextInt(2500);
        DataMap address = new DataMap();
        
        result.append(number).append(" ");
        address = state.generate(address);
        address = cityzip.generate(address);
        address = street.generate(address);
        result.append(address.getString(street.getKey()));
        record.put(key, result.toString());
        record.put(state.getKey(), address.getString(state.getKey()));
        record.put(cityzip.getKey(), address.getString(cityzip.getKey()));
        record.put(cityzip.getZipKey(), address.getString(cityzip.getZipKey()));

        return record;
    }

    @Override
    public void setChildGeneratorsSeed(long seed)
    {
        state.setSeed(seed);
        cityzip.setSeed(seed);
        street.setSeed(seed);
    }
    
}
