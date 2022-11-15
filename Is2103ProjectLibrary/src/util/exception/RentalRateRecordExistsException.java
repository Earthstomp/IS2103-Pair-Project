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
public class RentalRateRecordExistsException extends Exception {

    /**
     * Creates a new instance of <code>RentalRateRecordExistsException</code>
     * without detail message.
     */
    public RentalRateRecordExistsException() {
    }

    /**
     * Constructs an instance of <code>RentalRateRecordExistsException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalRateRecordExistsException(String msg) {
        super(msg);
    }
}
