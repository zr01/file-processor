package local.exam.file.processor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import local.exam.dataobject.dailysummary.DailySummaryOutput;
import local.exam.file.processor.impl.DailySummaryProcessor;

public class DailySummaryProcessorTest {

    static final Logger l = LoggerFactory.getLogger(DailySummaryProcessorTest.class);

    @Test
    public void testValidProcessing() {
        try {
            // Initialize mock output
            DailySummaryOutput mockOutput = new DailySummaryOutput();
            ReflectionTestUtils.setField(mockOutput, "clientInformation", "Test Client Information,1");
            ReflectionTestUtils.setField(mockOutput, "productInformation", "Test Product Information,2");
            ReflectionTestUtils.setField(mockOutput, "quantityLong", 4L);
            ReflectionTestUtils.setField(mockOutput, "quantityShort", 1L);

            // Initialize wrapper mock
            DailySummaryOutput mockWrapper = Mockito.mock(DailySummaryOutput.class);
            when(mockWrapper.wrap(Mockito.any(Map.class)))
                .thenReturn(mockOutput);
            
            // Initialize processor
            DailySummaryProcessor processor = new DailySummaryProcessor();
            ReflectionTestUtils.setField(processor, "wrapper", mockWrapper);
            
            // Initialize parameters to be passed
            // Take note that unit test does not actually process the passed parameters
            // The mock wrapper is tested separately via DailySummaryOutputTest
            // Unit test only makes sure that we can process at least 1 record successfully here
            List<Map<String, Object>> contents = new ArrayList<>();
            for (int i = 0; i < 10;i++) {
                contents.add(new HashMap<>());
            }
            
            StringBuilder output = processor.processContents(contents);
            assertNotNull(output);
            assertTrue(output.length() > 0);
            l.debug("Output file: {}", output);
        } catch (Exception e) {
            fail();
        }
    }
}
