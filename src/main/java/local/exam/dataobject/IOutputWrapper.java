package local.exam.dataobject;

import local.exam.exceptions.ContentWrapException;

/**
 * Wraps the parsed line data into the object output which will be used for the
 * output file content.
 * 
 * @author Allan
 *
 * @param <LINE_OBJECT>
 * @param <OBJECT_WRAPPER>
 * @param <PARSER_CONFIG>
 */
public interface IOutputWrapper<LINE_OBJECT, OBJECT_WRAPPER> {

    public LINE_OBJECT wrap(OBJECT_WRAPPER contents) throws ContentWrapException;
}
