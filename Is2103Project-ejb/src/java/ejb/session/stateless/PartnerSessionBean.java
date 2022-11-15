/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Partner;
import entity.Reservation;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createPartner(Partner partner) throws PartnerExistsException {
        Partner partnerExists = null;
        try {
            partnerExists = retrievePartnerByUsername(partner.getUsername());
        } catch (PartnerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        if (partnerExists != null) {
            throw new PartnerExistsException("Partner already has account.");
        } else {

            em.persist(partner);
            em.flush();

            return partner.getId();
        }

    }

    @Override
    public List<Partner> retrieveAllPartners() throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p");

        List<Partner> partners = query.getResultList();

        if (partners.size() < 1) {
            throw new PartnerNotFoundException("No partners found.");
        } else {
            return partners;
        }
    }

    @Override
    public List<Reservation> retrieveAllPartnerReservations(Long partnerId) throws ReservationNotFoundException, CustomerNotFoundException, PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.partnerId = :inPartnerId")
                .setParameter("inPartnerId", partnerId);

        Partner partner = (Partner) query.getSingleResult();

        if (partner == null) {
            throw new PartnerNotFoundException("No partners found.");
        } else {
            List<Customer> customers = partner.getCustomers();

            if (customers.size() < 1) {
                throw new CustomerNotFoundException("No customers from partner found.");
            } else {
                List<Reservation> allReservations = new ArrayList<Reservation>();

                for (Customer c : customers) {
                    for (Reservation r : c.getReservations()) {
                        allReservations.add(r);
                    }
                }

                if (allReservations.size() < 1) {
                    throw new ReservationNotFoundException("No reservations from partner found.");
                } else {

                    return allReservations;
                }
            }
        }
    }

    @Override
    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, id);

        if (partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Unable to locate partner with id: " + id);
        }
    }

    @Override
    public void removePartner(Long id) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, id);

        if (partner != null) {
            em.remove(partner);
        } else {
            throw new PartnerNotFoundException("Unable to locate partner with id: " + id);
        }
        // need to check for dependencies / if other cars are using this rental rate record
    }

    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialsException {
        try {
            Partner partner = retrievePartnerByUsername(username);

            if (partner.getPassword().equals(password)) {
                partner.getCustomers();
                return partner;
            } else {
                throw new InvalidLoginCredentialsException("Username does not exist or invalid password!");
            }
        } catch (PartnerNotFoundException ex) {
            throw new InvalidLoginCredentialsException("Username does not exist or invalid password!");
        }
    }

    @Override
    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Partner) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Username does not exist.");
        }
    }
}
