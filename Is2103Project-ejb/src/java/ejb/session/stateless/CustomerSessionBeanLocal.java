/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import javax.ejb.Local;
import util.exception.CustomerExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author Keith test
 */
@Local
public interface CustomerSessionBeanLocal {

    public Long createNewReservation(Reservation reservation, Long customerId);

    public Long createNewCustomer(Customer customer) throws CustomerExistsException;

    public Customer retrieveCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundException;

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    //public Customer customerLogin(String username, String password) throws InvalidLoginCredentialsException;

    public Customer retrieveCustomerByPassportNumber(String passportNumber) throws CustomerNotFoundException;

}
