package local.exam.file.reader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import local.exam.exceptions.FileReadException;
import local.exam.file.reader.impl.TextFileReader;

public class TextFileReaderTest {

    static final Logger l = LoggerFactory.getLogger(TextFileReaderTest.class);
    TextFileReader textFileReader = new TextFileReader();
    
    @Test
    public void testValidFile() {
        try {
            String filePath = "src/test/resources/files/abcd.txt";
            List<StringBuilder> result = textFileReader.readFile(filePath);
            assertNotNull(result);
            assertTrue(result.size() > 0);
            
            for (int i = 0;i < result.size();i++) {
                l.debug("Line {}: {}", (i + 1), result.get(i));
            }
        } catch (FileReadException e) {
            fail();
        }
    }
}
