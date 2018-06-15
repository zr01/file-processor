package local.exam.exceptions;

public class FileReadException extends Exception {

    String msg;
    
    public FileReadException (String msg) {
        this.msg = msg;
    }
    
    @Override
    public String getMessage() {
        return this.msg;
    }
}
