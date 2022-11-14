/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Keith test
 */
public class InvalidTimingException extends Exception{

    /**
     * Creates a new instance of <code>InvalidTimingException</code> without
     * detail message.
     */
    public InvalidTimingException() {
    }

    /**
     * Constructs an instance of <code>InvalidTimingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidTimingException(String msg) {
        super(msg);
    }
}
