package local.exam.jobs;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import local.exam.exceptions.FileParseException;
import local.exam.file.parser.IFileParser;
import local.exam.file.parser.config.FuturesParserConfig;
import local.exam.file.parser.impl.FuturesFileContentParser;
import local.exam.file.processor.IFileProcessor;
import local.exam.file.processor.impl.DailySummaryProcessor;
import local.exam.file.reader.IFileReader;
import local.exam.file.reader.impl.TextFileReader;
import local.exam.file.writer.IFileWriter;
import local.exam.file.writer.impl.StringBuilderFileWriter;

/**
 * A service that runs if the file.processor.futures.is-enabled is true.
 * This reads the input file and moves it to the correct directory for processing
 * 
 * @author Allan
 *
 */
@ConditionalOnProperty(prefix = "file.processor.futures", name = "is-enabled", matchIfMissing = false)
@Service
public class DailySummaryOutputScheduledJob {

    static final Logger l = LoggerFactory.getLogger(DailySummaryOutputScheduledJob.class);

    @Value("${file.processor.futures.input-dir:/var/spool/futures/pending}")
    String inputDir;
    
    @Value("${file.processor.futures.processed-dir:/var/spool/futures/processed}")
    String processedDir;
    
    @Value("${file.processor.futures.error-dir:/var/spool/futures/error}")
    String errorDir;
    
    @Value("${file.processor.futures.output-dir:/var/spool/futures/output}")
    String outputDir;
    
    @Value("${file.processor.futures.input-file-config:classpath:config/input/future.json}")
    String inputFileConfig;
    
    IFileReader<List<StringBuilder>> reader;
    IFileParser<List<Map<String, Object>>, List<StringBuilder>, FuturesParserConfig[]> parser;
    IFileProcessor<StringBuilder, List<Map<String, Object>>> processor;
    IFileWriter<StringBuilder> writer;
    
    @PostConstruct
    public void init() {
        // Hopefully this process runs with permissions to create a directory as a safeguard
        // Creates the directories if they don't exist
        String[] directories = {inputDir, processedDir, errorDir, outputDir};
        for (String directory : directories) {
            try {
                File dir = new File(directory);
                if (!dir.exists()) {
                    l.debug("Creating directory {}", directory);
                    dir.mkdirs();
                }
            } catch (Exception e) {
                l.warn("Unable to create directory or directory does not exist: {}", e.getMessage(), e);
            }
        }
        
        // Initialize the Objects that will be used for processing
        reader = new TextFileReader();
        parser = new FuturesFileContentParser();
        processor = new DailySummaryProcessor();
        writer = new StringBuilderFileWriter();
    }
    
    @Scheduled(fixedDelayString = "${file.processor.futures.delay:15000}")
    public void runJob() {
        try {
            l.info("!!! Running Daily Summary Output Job !!!");
            File pending = new File(inputDir);
            if (!pending.exists()) {
                throw new Exception("Invalid input directory.");
            }
            
            File[] pendingFiles = pending.listFiles();
            // Go through each file and process it
            for (File pendingFile : pendingFiles) {
                // Only process files
                if (!pendingFile.isFile()) {
                    continue;
                }
                
                try {
                    l.debug("### Reading file {}", pendingFile.getAbsolutePath());
                    // Start processing the file
                    List<StringBuilder> contents = reader.readFile(pendingFile.getAbsolutePath());
                    
                    l.debug("### Retrieving spec file from {}", inputFileConfig);
                    // Retrieve the spec file configuration
                    FuturesParserConfig[] config = retrieveSpecification(inputFileConfig);

                    l.debug("### Parsing file with total lines of {}", contents.size());
                    // Parse the data
                    List<Map<String, Object>> parsedData = parser.parseFile(contents, config);
                    
                    l.debug("### Processing data for output");
                    // Process the data
                    StringBuilder output = processor.processContents(parsedData);
                    
                    l.debug("### Writing output to file");
                    // Write the output to file
                    File outputFile = new File(outputDir, "Output.csv");
                    moveFileSafely(outputFile, outputDir);
                    writer.writeFile(outputDir, "Output.csv", output);
                    
                    l.debug("$$$ Moving file {} to processed directory in {}", pendingFile.getName(), processedDir);
                    // Move the pending file to processed directory
                    moveFileSafely(pendingFile, processedDir);
                } catch (Exception e) {
                    // Move file to error directory
                    l.error("$$$ Moving file {} to error directory in {}", pendingFile.getName(), errorDir, e);
                    moveFileSafely(pendingFile, errorDir);
                }
            }
        } catch (Exception e) {
            l.error("General error: {}", e.getMessage());
        } finally {
            l.info("!!! Completed Daily Summary Output Job !!!");
        }
    }
    
    private void moveFileSafely(File srcFile, String targetDir) throws Exception {
        File newFile = new File(targetDir, srcFile.getName());
        if (newFile.exists()) {
            Files.move(Paths.get(newFile.getAbsolutePath()), Paths.get(newFile.getAbsolutePath() + "." + System.currentTimeMillis()));
        }
        
        if (srcFile.exists()) {
            Files.move(Paths.get(srcFile.getAbsolutePath()), Paths.get(newFile.getAbsolutePath()));
        }
    }
    
    private FuturesParserConfig[] retrieveSpecification(String filePath) throws FileParseException {
        try {
            if (filePath.startsWith("classpath:")) {
                // Load from classpath
                File file = new ClassPathResource(filePath.substring(10)).getFile();
                filePath = file.getAbsolutePath();
                l.info("Loading config from classpath: {}", filePath);
            }
            // Read the config file
            List<StringBuilder> contents = reader.readFile(filePath);
            String strContent = "";
            // Compile the string into one string
            for (StringBuilder sb : contents) {
                strContent += sb.toString();
            }
            
            l.info("Mapping configuration to array object");
            // Map the config to the FuturesParserConfig[] array
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(strContent, FuturesParserConfig[].class);
        } catch (Exception e) {
            throw new FileParseException(e.getMessage());
        }
    }
}
