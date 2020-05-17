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
public class EmploymentGenerator extends AbstractGenerator {

    private DataArray<DataMap> source;
    private double unemployed = 0.05;
    private String salaryKey = "salary";
    private String birthDateKey = "birthDate";
    private DataArray<DataMap> retire;
    
    public EmploymentGenerator()
    {
        source = new DataArray(new JSONTokener(getClass().getResourceAsStream("/config/demographic/employment.json")));
        retire = new DataArray(new JSONTokener(getClass().getResourceAsStream("/config/demographic/retire_ages.json")));
    }
    
    public EmploymentGenerator(String key, String salaryKey, String birthDateKey)
    {
        this();   
        this.key = key;
        this.salaryKey = salaryKey;
        this.birthDateKey = birthDateKey;
    }

    public void setUnemployed(double unemployed)
    {
        this.unemployed = unemployed;
    }

    public void setSalaryKey(String salaryKey)
    {
        this.salaryKey = salaryKey;
    }

    public void setBirthDateKey(String birthDateKey)
    {
        this.birthDateKey = birthDateKey;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        LocalDate dob;
        DataMap employment;
        int age;
        int dollar;
       
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
            if (isRetired(age))
            {
                record.put(key, "Retired");
            }
            else if (random.nextDouble() <= unemployed)
            {
                record.put(key, "Unemployed");
            }
            else 
            {
                employment = getEmploymentInformation(random.nextDouble());
                record.put(key, employment.getString(key));
                dollar = random.nextInt(3000) * (random.nextBoolean() ? -1 : 1);
                record.put(salaryKey, employment.getInt(salaryKey) + dollar);
            }
        }
        
        return record;
    }
    
    private DataMap getEmploymentInformation(double split) throws DataMapException
    {
        DataMap employment = new DataMap();
        DataArray<String> jobNames;
        
        for (DataMap jobInfo : source)
        {
            if (jobInfo.getDouble("start") <= split && split < jobInfo.getDouble("end"))
            {
                if (jobInfo.getDataArray("employment_names").isEmpty())
                {
                    employment.put(key, jobInfo.getString("employment_group"));
                }
                else
                {
                    jobNames = jobInfo.getDataArray("employment_names");
                    employment.put(key, jobNames.get(random.nextInt(jobNames.size())));
                }
                
                employment.put(salaryKey, jobInfo.getInt("annual_median_salary"));
                break;
            }
        }
        
        return employment;
    }
    
    private boolean isRetired(int age) throws DataMapException
    {
        double split = random.nextDouble();
        
        for (DataMap record : retire)
        {
            if (age >= record.getDouble("start") && age < record.getDouble("end")) 
            {
                if (split <= record.getDouble("retire")) return true;
                else return false;
            }
        }
        
        return false;
    }
    
}
