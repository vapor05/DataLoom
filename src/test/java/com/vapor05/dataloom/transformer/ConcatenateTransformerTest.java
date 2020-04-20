package com.vapor05.dataloom.transformer;

import com.vapor05.dataloom.databus.DataMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author NicholasBocchini
 */
public class ConcatenateTransformerTest {

    @Test
    public void testTransform() throws Exception
    {
        DataMap data = new DataMap("{key1: Hello, key2: World}");
        ConcatenateTransformer transformer = new ConcatenateTransformer();
        
        transformer.setConcatKeys(new String[]{"key1", "key2"});
        transformer.setOutputKey("concatKey");
        transformer.setDelimeter(" ");
        assertEquals("Hello World", transformer.transform(data).getString("concatKey"));
        transformer.setDelimeter(",");
        assertEquals("Hello,World", transformer.transform(data).getString("concatKey"));
        transformer.setDelimeter("");
        assertEquals("HelloWorld", transformer.transform(data).getString("concatKey"));
        transformer.setConcatKeys(new String[]{"key2", "key1"});
        transformer.setDelimeter(",");
        assertEquals("World,Hello", transformer.transform(data).getString("concatKey"));
        transformer.setConcatKeys(new String[]{"key1", "key3"});
        transformer.setDelimeter(",");
        assertEquals("Hello,", transformer.transform(data).getString("concatKey"));
    }
    
}
