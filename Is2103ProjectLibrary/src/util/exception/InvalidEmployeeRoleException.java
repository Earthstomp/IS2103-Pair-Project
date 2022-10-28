package util.exception;

/**
 *
 * @author dorothyyuan
 */
public class InvalidEmployeeRoleException extends Exception {

    public InvalidEmployeeRoleException() {
    }
    public InvalidEmployeeRoleException(String msg) {
        super(msg);
    }
}
