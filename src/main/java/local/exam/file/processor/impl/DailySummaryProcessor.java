package local.exam.file.processor.impl;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import local.exam.dataobject.dailysummary.DailySummaryOutput;
import local.exam.exceptions.FileProcessorException;
import local.exam.file.processor.IFileProcessor;

/**
 * Processes the parsed content to the StringBuilder which will be written to the file.
 * 
 * @author Allan
 *
 */
public class DailySummaryProcessor implements IFileProcessor<StringBuilder, List<Map<String, Object>>> {

    static final Logger l = LoggerFactory.getLogger(DailySummaryProcessor.class);
    
    static final String NEWLINE = System.getProperty("line.separator");
    
    DailySummaryOutput wrapper = new DailySummaryOutput();
    
    @Override
    public StringBuilder processContents(List<Map<String, Object>> contents)
            throws FileProcessorException {
        try {
            Map<String, Map<String, Long>> output = new LinkedHashMap<>();
            // Go through the parsed contents one by one
            for (Map<String, Object> content : contents) {
                // Wrap the content into an object which we can easily use to group data
                DailySummaryOutput dsoOutput = wrapper.wrap(content);
                
                // Create the clientInformation header group
                if (!output.containsKey(dsoOutput.getClientInformation())) {
                    // Create new entry of client information
                    output.put(dsoOutput.getClientInformation(), new LinkedHashMap<>());
                }

                // Create the productInformation header group for each clientInformation
                Map<String, Long> clientInformation = output.get(dsoOutput.getClientInformation());
                Long delta = dsoOutput.getQuantityLong() - dsoOutput.getQuantityShort(); // We end up always calculating anyway
                
                // Summation of quantityLong - quantityShort per productInformation for each clientInformation
                if (!clientInformation.containsKey(dsoOutput.getProductInformation())) {
                    // Create new entry of product information
                    clientInformation.put(dsoOutput.getProductInformation(), delta);
                } else {
                    // Add to the product information in the clientInformation
                    clientInformation.put(dsoOutput.getProductInformation(), 
                            clientInformation.get(dsoOutput.getProductInformation()) + delta);
                }
            }

            // We've aggreggated the information, now we need to push the data into a CSV formatted String
            StringBuilder sbOutput = new StringBuilder();
            
            // Go through the output map
            boolean isFirstLine = true;
            for (String clientInformation : output.keySet()) {
                // Each client information contains has a product information and its quantity
                // Each header has clientInformation<br/>productInformation</br>totalQuantity
                Map<String, Long> clientInfoData = output.get(clientInformation);
                
                // Create record for every productInformation
                for (String productInformation : clientInfoData.keySet()) {
                    sbOutput = sbOutput.append(!isFirstLine ? NEWLINE : "").append(clientInformation).append(NEWLINE)
                            .append(productInformation).append(NEWLINE)
                            .append(clientInfoData.get(productInformation));
                    
                    // Inserts NEWLINE to every record after the first line 
                    isFirstLine = false;
                }
            }
            return sbOutput;
        } catch (Exception e) {
            String msg = MessageFormat.format("Error encountered while processing file content due to: {0}", e.getMessage());
            l.error(msg, e);
            throw new FileProcessorException(msg);
        }
    }

}
