package local.exam.file.processor;

import local.exam.exceptions.FileProcessorException;

public interface IFileProcessor<OUTPUT_FORMAT, OBJECT_WRAPPER> {

    public OUTPUT_FORMAT processContents(OBJECT_WRAPPER contents) throws FileProcessorException;
}
