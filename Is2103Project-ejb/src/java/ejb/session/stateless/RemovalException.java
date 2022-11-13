/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

/**
 *
 * @author dorothyyuan
 */
public class RemovalException extends Exception {

    /**
     * Creates a new instance of <code>RemovalException</code> without detail
     * message.
     */
    public RemovalException() {
    }

    /**
     * Constructs an instance of <code>RemovalException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RemovalException(String msg) {
        super(msg);
    }
}
