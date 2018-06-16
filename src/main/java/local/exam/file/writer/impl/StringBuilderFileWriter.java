package local.exam.file.writer.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import local.exam.exceptions.FileWriteException;
import local.exam.file.writer.IFileWriter;

public class StringBuilderFileWriter implements IFileWriter<StringBuilder> {

    static final Logger l = LoggerFactory.getLogger(StringBuilderFileWriter.class);

    @Override
    public void writeFile(String directory, String filename, StringBuilder contents) throws FileWriteException {
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Path fullPath = Paths.get(directory + "/" + filename);
            Files.write(fullPath, contents.toString().getBytes());
        } catch (Exception e) {
            String msg = MessageFormat.format("Error in writing file to output {}/{} due to: {}", directory, filename,
                    e.getMessage());
            l.error(msg, e);
            throw new FileWriteException(msg);
        }
    }

}
