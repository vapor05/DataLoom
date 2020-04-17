
package com.vapor05.dataloom.databus;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
/**
 *
 * @author NicholasBocchini
 */
public class DataArrayTest {
    
    @Test
    public void testParse()
    {
        DataArray array = new DataArray("[true, 762, 74.12, \"string1\", \'string2\']");
        assertTrue(array.get(0).equals(Boolean.TRUE));
        assertTrue(array.get(1).equals(762l));
        assertTrue(array.get(2).equals(74.12));
        assertTrue(array.get(3).equals("string1"));
        assertTrue(array.get(4).equals("string2"));
    }
       
}
