package local.exam.exceptions;

public class FileParseException extends Exception {

    String msg;
    
    public FileParseException (String msg) {
        this.msg = msg;
    }
    
    @Override
    public String getMessage() {
        return this.msg;
    }
}
