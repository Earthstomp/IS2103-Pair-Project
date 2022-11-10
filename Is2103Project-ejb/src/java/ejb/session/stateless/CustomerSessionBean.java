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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author Keith test
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCustomer(Customer customer) throws CustomerExistsException {
        try {
            //retrieveCustomerByUsername(customer.getUsername());
            retrieveCustomerByMobileNumber(customer.getMobileNumber());
            retrieveCustomerByPassportNumber(customer.getPassportNumber());            
            throw new CustomerExistsException("Customer already exists.");
        } catch (CustomerNotFoundException ex) {
            em.persist(customer);
            em.flush();
        }

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

    @Override
    public Customer retrieveCustomerByPassportNumber(String passportNumber) throws CustomerNotFoundException {
        try {
            return (Customer) em.createQuery("SELECT c FROM Customer c WHERE c.passportNumber = :passportNumber")
                    .setParameter("passportNumber", passportNumber)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer does not exist!");
        }
    }

    @Override
    public Customer retrieveCustomerByMobileNumber(String mobileNumber) throws CustomerNotFoundException {
        try {
            return (Customer) em.createQuery("SELECT c FROM Customer c WHERE c.mobileNumber = :mobileNumber")
                    .setParameter("mobileNumber", mobileNumber)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer does not exist!");
        }
    }

    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Customer) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }

    /*public Customer customerLogin(String username, String password) throws InvalidLoginCredentialsException {
        try {
            Customer customer = retrieveCustomerByUsername(username);

            if (customer.getPassword().equals(password)) {
                return customer;
            } else {
                throw new InvalidLoginCredentialsException("Username does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginCredentialsException("Username does not exist or invalid password!");
        }
    }*/

}
