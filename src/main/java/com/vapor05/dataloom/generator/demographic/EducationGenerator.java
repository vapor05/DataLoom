package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataArray;
import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.generator.AbstractGenerator;
import com.vapor05.dataloom.json.JSONTokener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

/**
 *
 * @author NicholasBocchini
 */
public class EducationGenerator extends AbstractGenerator {

    private String birthDateKey = "birthDate";
    private DataArray<DataMap> source;
    
    public EducationGenerator()
    {
        source = new DataMap(new JSONTokener(getClass().getResourceAsStream("/config/education.json"))).getDataArray("over18");
    }
    
    public EducationGenerator(String key, String birthDateKey)
    {
        this();
        this.key = key;
        this.birthDateKey = birthDateKey;
    }

    public void setBirthDateKey(String birthDateKey)
    {
        this.birthDateKey = birthDateKey;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        LocalDate dob;
        int age;
        double split;
       
        if (!record.containsKey(birthDateKey))
        {
            age = 18;
        }
        else
        {
            dob = LocalDate.ofInstant(Instant.ofEpochMilli(record.getLong(birthDateKey)), ZoneId.of("Z"));
            age = Period.between(dob, LocalDate.now()).getYears();
        }

        if (age >= 18)
        {
            split = random.nextDouble();
            
            for (DataMap code : source)
            {
                if (code.getDouble("start") <= split && split < code.getDouble("end"))
                {
                    record.put(key, code.getString("code"));
                    break;
                }
            }
        }
        
        return record;
    }
    
}
