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
public class InvalidDateTimeCombinationException extends Exception {

    /**
     * Creates a new instance of
     * <code>InvalidDateTimeCombinationException</code> without detail message.
     */
    public InvalidDateTimeCombinationException() {
    }

    /**
     * Constructs an instance of
     * <code>InvalidDateTimeCombinationException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public InvalidDateTimeCombinationException(String msg) {
        super(msg);
    }
}
