/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import javax.ejb.Remote;
import util.exception.CustomerExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author Keith test
 */
@Remote
public interface CustomerSessionBeanRemote {

    public Long createNewReservation(Reservation reservation, Long customerId);

    public Long createNewCustomer(Customer customer) throws CustomerExistsException;

    public Customer retrieveCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundException;

    public Customer retrieveCustomerByPassportNumber(String passportNumber) throws CustomerNotFoundException;

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    //public Customer customerLogin(String username, String password) throws InvalidLoginCredentialsException;

}
