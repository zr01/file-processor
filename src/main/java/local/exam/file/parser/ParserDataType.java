package local.exam.file.parser;

/**
 * Default data type of parsed column is always String. If the getDataType's parameter is null then it is resolved as a String.
 * 
 * @author Allan
 *
 */
public enum ParserDataType {
    String, Double, Long, Date;
    
    public static ParserDataType getDataType(String type) {
        if (type == null || type.isEmpty()) {
            return String;
        }
        
        for (ParserDataType pdt : values()) {
            if (pdt.name().equals(type)) {
                return pdt;
            }
        }
        
        return String;
    }
}
