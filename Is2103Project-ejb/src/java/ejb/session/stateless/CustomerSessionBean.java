/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Reservation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Keith test
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCustomer(Customer customer) {
        em.persist(customer);
        em.flush();

        return customer.getCustomerId();
    }

    @Override
    public Long createNewReservation(Reservation reservation, Long customerId) {
        em.persist(reservation);

        Customer customer = em.find(Customer.class, customerId);
        customer.getReservations().add(reservation);
        em.flush();

        return reservation.getId();
    }

    public Customer retrieveCustomerByMobileNumber(Integer mobileNumber) {
        return (Customer) em.createQuery("SELECT c FROM Customer c WHERE c.mobileNumber = :mobileNumber")
                .setParameter("mobileNumber", mobileNumber)
                .getSingleResult();
    }
   
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
