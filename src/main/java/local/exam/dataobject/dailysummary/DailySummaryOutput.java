package local.exam.dataobject.dailysummary;

import java.text.MessageFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import local.exam.dataobject.IOutputWrapper;
import local.exam.exceptions.ContentWrapException;
import local.exam.file.parser.config.FuturesParserConfig;

/**
 * Wraps the parsed data from the Futures file into the DailySummaryOutput as
 * defined for the Output.csv file.<br/>
 * The parsing ensures that the line data is complete and has been validated
 * prior to being processed here.<br/>
 * Assume that all the contents are valid for processing.<br/>
 * <br/>
 * The Daily summary report should be in CSV format (called Output.csv) with the
 * following specifications<br/>
 * <br/>
 * The CSV has the following Headers<br/>
 * - Client_Information<br/>
 * - Product_Information<br/>
 * - Total_Transaction_Amount<br/>
 * <br/>
 * Client_Information should be a combination of the CLIENT TYPE, CLIENT NUMBER,
 * ACCOUNT NUMBER, SUBACCOUNT NUMBER fields from Input file<br/>
 * Product_Information should be a combination of the EXCHANGE CODE, PRODUCT
 * GROUP CODE, SYMBOL, EXPIRATION DATE<br/>
 * Total_Transaction_Amount should be a Net Total of the (QUANTITY LONG -
 * QUANTITY SHORT) values for each client per product
 * 
 * @author Allan
 *
 */
public class DailySummaryOutput
        implements IOutputWrapper<DailySummaryOutput, Map<String, Object>, FuturesParserConfig[]> {

    static final Logger l = LoggerFactory.getLogger(DailySummaryOutput.class);

    String clientInformation;
    String productInformation;
    Long quantityLong;
    Long quantityShort;

    public String getClientInformation() {
        return clientInformation;
    }

    public String getProductInformation() {
        return productInformation;
    }

    public Long getQuantityLong() {
        return quantityLong;
    }

    public Long getQuantityShort() {
        return quantityShort;
    }

    @Override
    public DailySummaryOutput wrap(Map<String, Object> contents, FuturesParserConfig[] config)
            throws ContentWrapException {
        try {
            if (contents == null || contents.isEmpty()) {
                throw new Exception("Invalid parsed data");
            }

            if (config == null || config.length == 0) {
                throw new Exception("Invalid configuration");
            }

            l.trace("Extracting parsed data into daily summary output");
            DailySummaryOutput wrapper = new DailySummaryOutput();
            wrapper.clientInformation = String.valueOf(contents.get("CLIENT_TYPE")).trim() + ',' 
                    + String.valueOf(contents.get("CLIENT_NUMBER")).trim() + ',' 
                    + String.valueOf(contents.get("ACCOUNT_NUMBER")).trim() + ',' 
                    + String.valueOf(contents.get("SUBACCOUNT_NUMBER")).trim();
            
            wrapper.productInformation = String.valueOf(contents.get("EXCHANGE_CODE")).trim() + ','
                    + String.valueOf(contents.get("PRODUCT_GROUP_CODE")).trim() + ','
                    + String.valueOf(contents.get("SYMBOL")).trim() + ','
                    + String.valueOf(contents.get("EXPIRATION_DATE")).trim();
            
            wrapper.quantityLong = (Long)contents.get("QUANTITY_LONG");
            wrapper.quantityShort = (Long)contents.get("QUANTITY_SHORT");
            
            l.debug("Content wrapped to: {}|{}|{}|{}", wrapper.clientInformation, wrapper.productInformation, 
                    wrapper.quantityLong, wrapper.quantityShort);
            return wrapper;
        } catch (Exception e) {
            String msg = MessageFormat.format("Error in wrapping content due to: {0}", e.getMessage());
            l.error(msg, e);
            throw new ContentWrapException(msg);
        }
    }

}
