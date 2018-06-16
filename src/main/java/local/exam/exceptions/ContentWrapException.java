package local.exam.exceptions;

public class ContentWrapException extends Exception {

    String msg;
    
    public ContentWrapException (String msg) {
        this.msg = msg;
    }
    
    @Override
    public String getMessage() {
        return this.msg;
    }
}
