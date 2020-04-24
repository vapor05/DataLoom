package com.vapor05.dataloom.transformer;

import com.vapor05.dataloom.databus.DataMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class MoveKeyTransformerTest {
    
    @Test
    public void testTransform() throws Exception
    {
        MoveKeyTransformer transformer = new MoveKeyTransformer();
        DataMap data;
        
        transformer.setSourceKey("key");
        transformer.setDestinationKey("newKey");
        data = transformer.transform(new DataMap("{key:dataValue}"));
        assertEquals(new DataMap("{newKey:dataValue}"), data);
        transformer.setSourceKey("key");
        transformer.setDestinationKey("newMap.newKey");
        data = transformer.transform(new DataMap("{key:dataValue}"));
        assertEquals(new DataMap("{newMap:{newKey:dataValue}}"), data);   
        transformer.setSourceKey("nestedKey.key");
        transformer.setDestinationKey("newKey");
        data = transformer.transform(new DataMap("{nestedKey:{key: nestedValue}}"));
        assertEquals(new DataMap("{newKey:nestedValue, nestedKey:{}}"), data);   
        transformer.setSourceKey("key");
        transformer.setDestinationKey("newKey");
        data = transformer.transform(new DataMap("{key:null}"));
        assertEquals(new DataMap("{newKey:null}"), data);       
    }
    
}
