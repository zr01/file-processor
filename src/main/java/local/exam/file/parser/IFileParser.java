package local.exam.file.parser;

import local.exam.exceptions.FileParseException;

/**
 * Interface that we can use to implement different parsers which may have
 * different Object wrappers as a result.
 * 
 * @author Allan
 *
 * @param <OBJECT_WRAPPER> The Object wrapper to the file that was parsed
 */
public interface IFileParser<OBJECT_WRAPPER, CONTENT_TYPE, PARSER_CONFIG> {

    public OBJECT_WRAPPER parseFile(CONTENT_TYPE contents, PARSER_CONFIG config) throws FileParseException;
}
