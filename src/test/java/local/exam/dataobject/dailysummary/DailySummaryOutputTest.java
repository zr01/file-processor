package local.exam.dataobject.dailysummary;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import local.exam.exceptions.ContentWrapException;

public class DailySummaryOutputTest {

    static final Logger l = LoggerFactory.getLogger(DailySummaryOutputTest.class);
    
    @Test
    public void testValidOutput() {
        DailySummaryOutput wrapper = new DailySummaryOutput();
        Map<String, Object> contents = new HashMap<>();
        
        // Prep contents that are valid for the wrapper
        /**
         * Client_Information should be a combination of the CLIENT TYPE, CLIENT NUMBER, ACCOUNT NUMBER, SUBACCOUNT NUMBER fields from Input file
         * Product_Information should be a combination of the EXCHANGE CODE, PRODUCT GROUP CODE, SYMBOL, EXPIRATION DATE
         * Total_Transaction_Amount should be a Net Total of the (QUANTITY LONG - QUANTITY SHORT) values for each client per product
         */
        // Client Information
        contents.put("CLIENT_TYPE", "TEST CLIENT");
        contents.put("CLIENT_NUMBER", "TEST CLIENT NUMBER");
        contents.put("ACCOUNT_NUMBER", "TEST ACCOUNT NUMBER");
        contents.put("SUBACCOUNT_NUMBER", "TEST SUBACCOUNT NUMBER");
        
        // Product Information
        contents.put("EXCHANGE_CODE", "TEST EXCHANGE CODE");
        contents.put("PRODUCT_GROUP_CODE", "TEST PRODUCT GROUP CODE");
        contents.put("SYMBOL", "TEST SYMBOL");
        contents.put("EXPIRATION_DATE", "TEST EXPIRATION DATE");
        
        // Quantity Information
        contents.put("QUANTITY_LONG", 4L);
        contents.put("QUANTITY_SHORT", 2L);
        
        try {
            DailySummaryOutput output = wrapper.wrap(contents);
            
            assertNotNull(output.getProductInformation());
            assertNotNull(output.getClientInformation());
            assertNotNull(output.getQuantityLong());
            assertNotNull(output.getQuantityShort());
        } catch (ContentWrapException e) {
            fail();
        }
    }
    
    /**
     * Test with missing QUANTITY
     * 
     * @throws ContentWrapException
     */
    @Test(expected = ContentWrapException.class)
    public void testMissingField() 
        throws ContentWrapException {
        DailySummaryOutput wrapper = new DailySummaryOutput();
        Map<String, Object> contents = new HashMap<>();
        
        // Prep contents that are valid for the wrapper
        /**
         * Client_Information should be a combination of the CLIENT TYPE, CLIENT NUMBER, ACCOUNT NUMBER, SUBACCOUNT NUMBER fields from Input file
         * Product_Information should be a combination of the EXCHANGE CODE, PRODUCT GROUP CODE, SYMBOL, EXPIRATION DATE
         * Total_Transaction_Amount should be a Net Total of the (QUANTITY LONG - QUANTITY SHORT) values for each client per product
         */
        // Client Information
        contents.put("CLIENT_TYPE", "TEST CLIENT");
        contents.put("CLIENT_NUMBER", "TEST CLIENT NUMBER");
        contents.put("ACCOUNT_NUMBER", "TEST ACCOUNT NUMBER");
        contents.put("SUBACCOUNT_NUMBER", "TEST SUBACCOUNT NUMBER");
        
        // Product Information
        contents.put("EXCHANGE_CODE", "TEST EXCHANGE CODE");
        contents.put("PRODUCT_GROUP_CODE", "TEST PRODUCT GROUP CODE");
        contents.put("SYMBOL", "TEST SYMBOL");
        contents.put("EXPIRATION_DATE", "TEST EXPIRATION DATE");
        
        // Quantity Information
//        contents.put("QUANTITY_LONG", 4L);
//        contents.put("QUANTITY_SHORT", 2L);
        
        wrapper.wrap(contents);
        fail();
    }
}
