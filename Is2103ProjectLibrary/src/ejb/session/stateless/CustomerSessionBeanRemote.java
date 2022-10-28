/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import javax.ejb.Remote;

/**
 *
 * @author Keith test
 */
@Remote
public interface CustomerSessionBeanRemote {

    public Long createNewReservation(Reservation reservation, Long customerId);

    public Long createNewCustomer(Customer customer);

    public Customer retrieveCustomerByMobileNumber(Integer mobileNumber);
    
}
