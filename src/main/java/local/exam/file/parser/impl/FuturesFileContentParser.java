package local.exam.file.parser.impl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import local.exam.exceptions.FileParseException;
import local.exam.file.parser.IFileParser;
import local.exam.file.parser.ParserDataType;
import local.exam.file.parser.config.FuturesParserConfig;

/**
 * Parses the file content that was read by the TextFileReader.
 * It uses the configuration passed for each line that needs to be parsed.
 * Each config item in the array is a column specification.
 * 
 * @author Allan
 *
 */
public class FuturesFileContentParser implements IFileParser<List<Map<String, Object>>, List<StringBuilder>, FuturesParserConfig[]> {

    static final Logger l = LoggerFactory.getLogger(FuturesFileContentParser.class);

    @Override
    public List<Map<String, Object>> parseFile(List<StringBuilder> contents, FuturesParserConfig[] config)
            throws FileParseException {
        int idx = 0;
        try {
            l.debug("Validating file content if empty");
            if (contents == null || contents.isEmpty()) {
                throw new Exception("File content is invalid for parsing");
            }
            
            l.debug("Validating file config");
            if (config == null || config.length == 0) {
                throw new Exception("File configuration is invalid for parsing");
            }
            
            // Let's start going through the file contents
            // Initialize the resulting list
            List<Map<String, Object>> result = new ArrayList<>();
            for (idx = 0; idx < contents.size();idx++) {
                l.debug("Processing line {}", (idx + 1));
                StringBuilder line = contents.get(idx);
                Map<String, Object> lineData = new LinkedHashMap<>();
                for (FuturesParserConfig colDefinition : config) {
                    // Extract the column data
                    l.trace("Extracting {}:{}", colDefinition.startIdx, colDefinition.startIdx + colDefinition.length);
                    String colData = line.substring(colDefinition.startIdx, colDefinition.startIdx + colDefinition.length);
                    
                    // Let's see what kind of column this is by using the column definition
                    // Get the type
                    ParserDataType type = ParserDataType.getDataType(colDefinition.type);
                    switch (type) {
                        case String:
                            // Save as-is
                            lineData.put(colDefinition.fieldName, colData);
                            break;
                        case Long:
                            // Parse the data as a Long value, parse exceptions will be caught by the exception
                            lineData.put(colDefinition.fieldName, Long.valueOf(colData));
                            break;
                        case Double:
                            // Double types require the optional format field in the column definition
                            // We need to know the decimal places of the current column
                            int decimalPlaces = Integer.valueOf(colDefinition.format.value);
                            Double colValue = Double.valueOf(colData);
                            colValue /= Math.pow(10, decimalPlaces);
                            lineData.put(colDefinition.fieldName, colValue);
                            break;
                        case Date:
                            // Date types require the optional format field in the column definition
                            // We need to know the SimpleDateFormat format string
                            SimpleDateFormat sdf = new SimpleDateFormat(colDefinition.format.value);
                            lineData.put(colDefinition.fieldName, sdf.parse(colData));
                            break;
                    }
                }
                
                l.debug("Successfully parsed line {}", (idx + 1));
                // No problem in parsing the line, add to the resulting list
                result.add(lineData);
            }
            return result;
        } catch (Exception e) {
            String msg = MessageFormat.format("Error encountered while parsing line {0} due to {1}", idx,
                    e.getMessage());
            l.error(msg, e);
            throw new FileParseException(msg);
        }
    }

}
