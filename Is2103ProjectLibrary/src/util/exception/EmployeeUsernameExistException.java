package util.exception;

/**
 *
 * @author dorothyyuan
 */
public class EmployeeUsernameExistException extends Exception {

    public EmployeeUsernameExistException() {
    }

    public EmployeeUsernameExistException(String msg) {
        super(msg);
    }
}
