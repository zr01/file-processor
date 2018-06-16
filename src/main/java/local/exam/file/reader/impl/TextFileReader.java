package local.exam.file.reader.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import local.exam.exceptions.FileReadException;
import local.exam.file.reader.IFileReader;

/**
 * Reads a text file based on the file path and produces a List<StringBuilder>.
 * Index of the list pertains to the line number starting from index 0.
 * Index 0 is line 1.
 * 
 * Using StringBuilder as it is thread safe.
 * 
 * @author Allan
 *
 */
@Component
public class TextFileReader implements IFileReader<List<StringBuilder>> {

    static final Logger l = LoggerFactory.getLogger(TextFileReader.class);
    
    public List<StringBuilder> readFile(String filePath) 
        throws FileReadException {
        try {
            l.debug("Reading from file: {}", filePath);
            Path path = Paths.get(filePath);
            List<String> fileContent = Files.readAllLines(path);
            List<StringBuilder> result = new ArrayList<>();
            
            // Just want to make sure we are thread safe by using a StringBuilder
            for (String line : fileContent) {
                result.add(new StringBuilder(line));
            }
            
            return result;
        } catch (Exception e) {
            l.error("Unable to read file due to: {}", e.getMessage());
            throw new FileReadException(e.getMessage());
        } finally {
            l.debug("Completed reading from file.");
        }
    }
}
