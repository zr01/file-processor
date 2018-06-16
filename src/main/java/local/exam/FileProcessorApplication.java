package local.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class, but we're running a spring-boot container and utilize a runtime
 * environment with scheduling to make things simpler.
 * 
 * Actual class calling the processor is local.exam.jobs.DailySummaryOutputScheduledJob
 * 
 * @author Allan
 *
 */
@SpringBootApplication
@EnableScheduling
public class FileProcessorApplication {
    
    // Not the actual entry point for the application
    // Using Spring-Boot just makes it easy to have an AOP framework to use
    // Also enables the application to run in a container
    public static void main(String[] args) {
        SpringApplication.run(FileProcessorApplication.class, args);
    }
}
