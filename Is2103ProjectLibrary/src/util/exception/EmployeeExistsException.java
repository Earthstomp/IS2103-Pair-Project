package util.exception;

/**
 *
 * @author dorothyyuan
 */
public class EmployeeExistsException extends Exception {

    public EmployeeExistsException() {
    }
    
    public EmployeeExistsException(String msg) {
        super(msg);
    }
}
