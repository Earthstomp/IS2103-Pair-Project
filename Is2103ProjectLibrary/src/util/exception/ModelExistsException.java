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
public class ModelExistsException extends Exception {

    /**
     * Creates a new instance of <code>ModelExistsException</code> without
     * detail message.
     */
    public ModelExistsException() {
    }

    /**
     * Constructs an instance of <code>ModelExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ModelExistsException(String msg) {
        super(msg);
    }
}
