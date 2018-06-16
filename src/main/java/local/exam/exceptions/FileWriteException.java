package local.exam.exceptions;

public class FileWriteException extends Exception {

    String msg;
    
    public FileWriteException (String msg) {
        this.msg = msg;
    }
    
    @Override
    public String getMessage() {
        return this.msg;
    }
}
