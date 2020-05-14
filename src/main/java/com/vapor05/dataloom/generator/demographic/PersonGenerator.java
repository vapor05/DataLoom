package com.vapor05.dataloom.generator.demographic;

import com.vapor05.dataloom.databus.DataMap;
import com.vapor05.dataloom.databus.DataMapException;
import com.vapor05.dataloom.generator.AbstractGenerator;
import com.vapor05.dataloom.generator.AddressGenerator;
import com.vapor05.dataloom.generator.DateGenerator;
import com.vapor05.dataloom.generator.health.BloodTypeGenerator;
import com.vapor05.dataloom.generator.health.HCCGenerator;
import java.util.Date;

/**
 *
 * @author NicholasBocchini
 */
public class PersonGenerator extends AbstractGenerator  {
    
    private GenderGenerator gender;
    private FirstnameGenerator firstname;
    private LastnameGenerator lastname;
    private DateGenerator birthDate;
    private AddressGenerator address;
    private EducationGenerator education;
    private EmploymentGenerator employment;
    private HCCGenerator hcc;
    private BloodTypeGenerator bloodtype;
    
    public PersonGenerator() throws DataMapException
    {
        gender = new GenderGenerator("gender");
        firstname = new FirstnameGenerator("firstname");
        lastname = new LastnameGenerator("lastname");
        birthDate = new DateGenerator("birthDate", -1262282400000L, new Date().getTime());
        address = new AddressGenerator("address", "state", "city", "zip", "street");
        education = new EducationGenerator("education", "birthDate");
        employment = new EmploymentGenerator("jobTitle", "salary", "birthDate");
        hcc = new HCCGenerator("condition");
        bloodtype = new BloodTypeGenerator("bloodtype");
    }

    public void setGender(GenderGenerator gender)
    {
        this.gender = gender;
    }

    public void setFirstname(FirstnameGenerator firstname)
    {
        this.firstname = firstname;
    }

    public void setLastname(LastnameGenerator lastname)
    {
        this.lastname = lastname;
    }

    public void setBirthDate(DateGenerator birthDate)
    {
        this.birthDate = birthDate;
    }

    public void setAddress(AddressGenerator address)
    {
        this.address = address;
    }

    public void setEducation(EducationGenerator education)
    {
        this.education = education;
    }

    public void setEmployment(EmploymentGenerator employment)
    {
        this.employment = employment;
    }
    
    public void setHcc(HCCGenerator hcc)
    {
        this.hcc = hcc;
    }
    
    @Override
    public DataMap generate(DataMap record) throws DataMapException
    {
        record = gender.generate(record);
        record = firstname.generate(record);
        record = lastname.generate(record);
        record = birthDate.generate(record);
        record = address.generate(record);
        record = education.generate(record);
        record = employment.generate(record);
        record = hcc.generate(record);
        record = bloodtype.generate(record);
        
        return record;
    }

    @Override
    public void setSeed(long seed)
    {
        gender.setSeed(seed);
        firstname.setSeed(seed);
        lastname.setSeed(seed);
        birthDate.setSeed(seed);
        address.setSeed(seed);
        address.setChildGeneratorsSeed(seed);
        education.setSeed(seed);
        employment.setSeed(seed);
        hcc.setSeed(seed);
        bloodtype.setSeed(seed);
    }
    
}
