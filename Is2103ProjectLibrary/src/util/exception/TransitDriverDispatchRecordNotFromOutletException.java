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
public class TransitDriverDispatchRecordNotFromOutletException extends Exception{

    /**
     * Creates a new instance of
     * <code>TransitDriverDispatchRecordNotFromOutletException</code> without
     * detail message.
     */
    public TransitDriverDispatchRecordNotFromOutletException() {
    }

    /**
     * Constructs an instance of
     * <code>TransitDriverDispatchRecordNotFromOutletException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TransitDriverDispatchRecordNotFromOutletException(String msg) {
        super(msg);
    }
}
