package util.exception;

/**
 *
 * @author dorothyyuan
 */
public class InvalidLoginCredentialsException extends Exception {

    public InvalidLoginCredentialsException() {
    }
    
    public InvalidLoginCredentialsException(String msg) {
        super(msg);
    }
}
