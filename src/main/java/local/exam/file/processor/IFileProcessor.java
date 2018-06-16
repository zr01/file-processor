package local.exam.file.processor;

import java.util.List;

import local.exam.exceptions.FileProcessorException;

public interface IFileProcessor<OUTPUT_FORMAT, OBJECT_WRAPPER, PARSER_CONFIG> {

    public OUTPUT_FORMAT processContents(OBJECT_WRAPPER contents, PARSER_CONFIG config) throws FileProcessorException;
}
