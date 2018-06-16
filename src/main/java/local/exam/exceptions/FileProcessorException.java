package local.exam.exceptions;

public class FileProcessorException extends Exception {

    String msg;
    
    public FileProcessorException (String msg) {
        this.msg = msg;
    }
    
    @Override
    public String getMessage() {
        return this.msg;
    }
}
