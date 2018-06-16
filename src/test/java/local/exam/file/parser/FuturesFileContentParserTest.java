package local.exam.file.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import local.exam.exceptions.FileParseException;
import local.exam.file.parser.config.FuturesParserConfig;
import local.exam.file.parser.impl.FuturesFileContentParser;

public class FuturesFileContentParserTest {

    static final Logger l = LoggerFactory.getLogger(FuturesFileContentParserTest.class);

    IFileParser<List<Map<String, Object>>, List<StringBuilder>, FuturesParserConfig[]> parser = new FuturesFileContentParser();

    // Contents to parse
    String contents = "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200011920     687766000092600000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012100     687844000092600000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012260     687950000092550000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012280     687956000092550000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012380     688032000092500000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012400     688058000092500000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012450     688098000092500000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012530     688158000092450000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200012550     688162000092450000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200013120     689000000092250000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200013350     689458000092150000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200014270     691050000092350000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200014280     691052000092350000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200016010     765563000092050000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200016020     765565000092050000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200017320     766581000091800000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200000140     767061000091900000000             O\n"
            + "315CL  432100020001SGXDC FUSGX NK    20100910JPY01B 0000000001 0000000000000000000060DUSD000000000030DUSD000000000000DJPY201008200000250     767311000091750000000             O";

    // Configuration
    String configuration = "[{\"ref\":1,\"fieldName\":\"RECORD_CODE\",\"startIdx\":0,\"length\":3,\"type\":\"String\"},{\"ref\":2,\"fieldName\":\"CLIENT_TYPE\",\"startIdx\":3,\"length\":4,\"type\":\"String\"},{\"ref\":3,\"fieldName\":\"CLIENT_NUMBER\",\"startIdx\":7,\"length\":4,\"type\":\"Long\"},{\"ref\":4,\"fieldName\":\"ACCOUNT_NUMBER\",\"startIdx\":11,\"length\":4,\"type\":\"Long\"},{\"ref\":5,\"fieldName\":\"SUBACCOUNT_NUMBER\",\"startIdx\":15,\"length\":4,\"type\":\"Long\"},{\"ref\":6,\"fieldName\":\"OPPOSITE_PARTY_CODE\",\"startIdx\":19,\"length\":6,\"type\":\"String\"},{\"ref\":7,\"fieldName\":\"PRODCUT_GROUP_CODE\",\"startIdx\":25,\"length\":2,\"type\":\"String\"},{\"ref\":8,\"fieldName\":\"EXCHANGE_CODE\",\"startIdx\":27,\"length\":4,\"type\":\"String\"},{\"ref\":9,\"fieldName\":\"SYMBOL\",\"startIdx\":31,\"length\":6,\"type\":\"String\"},{\"ref\":11,\"fieldName\":\"EXPIRATION_DATE\",\"startIdx\":37,\"length\":8,\"type\":\"Date\",\"format\":{\"type\":\"SimpleDateFormat\",\"value\":\"yyyyMMdd\"}},{\"ref\":13,\"fieldName\":\"CURRENCY_CODE\",\"startIdx\":45,\"length\":3,\"type\":\"String\"},{\"ref\":14,\"fieldName\":\"MOVEMENT_CODE\",\"startIdx\":48,\"length\":2,\"type\":\"String\"},{\"ref\":15,\"fieldName\":\"BUY_SELL_CODE\",\"startIdx\":50,\"length\":1,\"type\":\"String\"},{\"ref\":68,\"fieldName\":\"QUANTITY_LONG_SIGN\",\"startIdx\":51,\"length\":1,\"type\":\"String\"},{\"ref\":16,\"fieldName\":\"QUANTITY_LONG\",\"startIdx\":52,\"length\":10,\"type\":\"Long\"},{\"ref\":68,\"fieldName\":\"QUANTITY_SHORT_SIGN\",\"startIdx\":62,\"length\":1,\"type\":\"String\"},{\"ref\":16,\"fieldName\":\"QUANTITY_SHORT\",\"startIdx\":63,\"length\":10,\"type\":\"Long\"},{\"ref\":17,\"fieldName\":\"EXCH/BROKER_FEE/DEC\",\"startIdx\":73,\"length\":12,\"type\":\"Double\",\"format\":{\"type\":\"Double\",\"value\":\"2\"}},{\"ref\":18,\"fieldName\":\"EXCH/BROKER_FEE_D_C\",\"startIdx\":85,\"length\":1,\"type\":\"String\"},{\"ref\":17,\"fieldName\":\"EXCH/BROKER_FEE_CUR_CODE\",\"startIdx\":86,\"length\":3,\"type\":\"String\"},{\"ref\":19,\"fieldName\":\"CLEARING_FEE/DEC\",\"startIdx\":89,\"length\":12,\"type\":\"Double\",\"format\":{\"type\":\"Double\",\"value\":\"2\"}},{\"ref\":18,\"fieldName\":\"CLEARING_FEE_D_C\",\"startIdx\":101,\"length\":1},{\"ref\":19,\"fieldName\":\"CLEARING_FEE_CUR_CODE\",\"startIdx\":102,\"length\":3},{\"ref\":86,\"fieldName\":\"COMMISSION\",\"startIdx\":105,\"length\":12,\"type\":\"Double\",\"format\":{\"type\":\"Double\",\"value\":\"2\"}},{\"ref\":18,\"fieldName\":\"COMMISSION_D_C\",\"startIdx\":117,\"length\":1},{\"ref\":86,\"fieldName\":\"COMMISION_CUR_CODE\",\"startIdx\":118,\"length\":3},{\"ref\":34,\"fieldName\":\"TRANSACTION_DATE\",\"startIdx\":121,\"length\":8,\"type\":\"Date\",\"format\":{\"type\":\"SimpleDateFormat\",\"value\":\"yyyyMMdd\"}},{\"ref\":36,\"fieldName\":\"FUTURE_REFERENCE\",\"startIdx\":129,\"length\":6,\"type\":\"Long\"},{\"ref\":37,\"fieldName\":\"TICKET_NUMBER\",\"startIdx\":135,\"length\":6},{\"ref\":38,\"fieldName\":\"EXTERNAL_NUMBER\",\"startIdx\":141,\"length\":6,\"type\":\"Long\"},{\"ref\":20,\"fieldName\":\"TRANSACTION_PRICE/DEC\",\"startIdx\":147,\"length\":15,\"type\":\"Double\",\"format\":{\"type\":\"Double\",\"value\":\"7\"}},{\"ref\":66,\"fieldName\":\"TRADER_INITIALS\",\"startIdx\":162,\"length\":6},{\"ref\":156,\"fieldName\":\"OPPOSITE_TRADER_ID\",\"startIdx\":168,\"length\":7},{\"ref\":65,\"fieldName\":\"OPEN_CLOSE_CODE\",\"startIdx\":175,\"length\":1}]";

    private List<StringBuilder> readContents() {
        String[] list = contents.split("\n");
        List<StringBuilder> result = new ArrayList<>();
        for (String str : list) {
            result.add(new StringBuilder(str));
        }

        return result;
    }

    private FuturesParserConfig[] readConfig() throws JsonMappingException, JsonParseException, IOException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(configuration, FuturesParserConfig[].class);
    }

    @Test
    public void testParseSuccess() {
        try {
            List<StringBuilder> contents = readContents();
            FuturesParserConfig[] config = readConfig();
            List<Map<String, Object>> result = parser.parseFile(contents, config);

            assertNotNull(result);
            assertEquals(contents.size(), result.size());
            
            // Print out the first item and its contents
            Map<String, Object> first = result.get(0);
            for (String key : first.keySet()) {
                l.debug("{} -> {}", key, first.get(key));
            }
        } catch (Exception e) {
            l.error("Error in testing parse success due to {}", e.getMessage(), e);
            fail();
        }
    }
    
    @Test(expected = FileParseException.class)
    public void testIncorrectInputFile() 
    throws Exception {
        try {
            List<StringBuilder> contents = readContents();
            contents.get(0).insert(0, "FAIL");
            FuturesParserConfig[] config = readConfig();
            parser.parseFile(contents, config);
        } catch (Exception e) {
            throw e;
        }
        
        fail();
    }
    
    @Test(expected = FileParseException.class)
    public void testNoConfigFile() 
    throws Exception {
        try {
            List<StringBuilder> contents = readContents();
            parser.parseFile(contents, null);
        } catch (Exception e) {
            throw e;
        }
        
        fail();
    }
}
