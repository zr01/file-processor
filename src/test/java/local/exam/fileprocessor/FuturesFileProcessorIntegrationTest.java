package local.exam.fileprocessor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import local.exam.file.parser.IFileParser;
import local.exam.file.parser.config.FuturesParserConfig;
import local.exam.file.parser.impl.FuturesFileContentParser;
import local.exam.file.processor.IFileProcessor;
import local.exam.file.processor.impl.DailySummaryProcessor;
import local.exam.file.reader.IFileReader;
import local.exam.file.reader.impl.TextFileReader;

public class FuturesFileProcessorIntegrationTest {

    static final Logger l = LoggerFactory.getLogger(FuturesFileProcessorIntegrationTest.class);

    private FuturesParserConfig[] retrieveConfig(IFileReader<List<StringBuilder>> reader) {
        try {
            List<StringBuilder> configFile = reader.readFile("src/main/resources/config/input/future.json");
            String content = "";
            for (StringBuilder sb : configFile) {
                content += sb.toString();
            }
            
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(content, FuturesParserConfig[].class);
        } catch (Exception e) {
            l.error("Unable to read parser config");
        }
        return null;
    }
    
    @Test
    public void testValidProcessingNoWrite() {
        try {
            // Read file
            IFileReader<List<StringBuilder>> reader = new TextFileReader();
            List<StringBuilder> fileContents = reader.readFile("src/test/resources/files/integration_input_file.txt");
            
            // Parse file
            IFileParser<List<Map<String, Object>>, List<StringBuilder>, FuturesParserConfig[]> parser = new FuturesFileContentParser();
            FuturesParserConfig[] config = retrieveConfig(reader);
            List<Map<String, Object>> parsedContents = parser.parseFile(fileContents, config);
            
            // Process file
            IFileProcessor<StringBuilder, List<Map<String, Object>>> processor = new DailySummaryProcessor();
            StringBuilder outputFileContent = processor.processContents(parsedContents);
            
            assertNotNull(outputFileContent);
            assertTrue(outputFileContent.length() > 0);
            l.info("Output File ->\n{}", outputFileContent);
        } catch (Exception e) {
            fail();
        }
    }
}
