package local.exam.file.reader;

import local.exam.exceptions.FileReadException;

public interface IFileReader<T> {

    public T readFile(String filePath) throws FileReadException;
}
