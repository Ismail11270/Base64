package pl.polsl.ismoil.atajanov.model;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test class for Base64 model
 * @author Ismoil Atajanov
 * @version 1.0
 */
public class TestBase64 {
    /**
     * Tested model object
     */
    Base64 base64 = new Base64();


    @Test
    public void testDecode(){
        String encoded = "YmFzZTY0IGRlY29kZXI=";
        String wellDecoded = "base64 decoder";
        try {
            Assert.assertEquals("Decoded string is not correct!",wellDecoded,base64.decode(encoded));
        }catch(pl.polsl.ismoil.atajanov.model.base64exceptions.EncodedStringInvalidException e){

        }
        encoded = "4513/432.$#*54#$*#*$(";
        try{
            base64.decode(encoded);
            fail("Wrong encoded string, exception was expected");

        }catch(pl.polsl.ismoil.atajanov.model.base64exceptions.EncodedStringInvalidException e){

        }
    }

    @Test
    public void testEncode(){
        String decoded = "base64 decoder";
        String wellEncoded = "YmFzZTY0IGRlY29kZXI=";
        assertEquals("Encoded string is wrong",base64.encode(decoded),wellEncoded);
    }
}
