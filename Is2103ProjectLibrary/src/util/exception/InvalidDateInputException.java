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
public class InvalidDateInputException extends Exception {

    /**
     * Creates a new instance of <code>InvalidDateInputException</code> without
     * detail message.
     */
    public InvalidDateInputException() {
    }

    /**
     * Constructs an instance of <code>InvalidDateInputException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidDateInputException(String msg) {
        super(msg);
    }
}
