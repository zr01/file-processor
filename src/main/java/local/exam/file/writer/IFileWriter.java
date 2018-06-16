package local.exam.file.writer;

import local.exam.exceptions.FileWriteException;

/**
 * Interface that may implement different file writers for different kidns of input determined by FILE_CONTENT
 * 
 * @author Allan
 *
 * @param <FILE_CONTENT>
 */
public interface IFileWriter<FILE_CONTENT> {
    
    public void writeFile(String directory, String filename, FILE_CONTENT contents) throws FileWriteException;
}
