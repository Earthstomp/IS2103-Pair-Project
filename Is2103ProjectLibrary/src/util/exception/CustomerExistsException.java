/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author dorothyyuan
 */
public class CustomerExistsException extends Exception {

    /**
     * Creates a new instance of <code>CustomerExistsException</code> without
     * detail message.
     */
    public CustomerExistsException() {
    }

    /**
     * Constructs an instance of <code>CustomerExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerExistsException(String msg) {
        super(msg);
    }
}
