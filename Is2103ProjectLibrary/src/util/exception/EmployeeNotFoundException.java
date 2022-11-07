package util.exception;

/**
 *
 * @author dorothyyuan
 */
public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException() {
    }

    public EmployeeNotFoundException(String msg) {
        super(msg);
    }
}
