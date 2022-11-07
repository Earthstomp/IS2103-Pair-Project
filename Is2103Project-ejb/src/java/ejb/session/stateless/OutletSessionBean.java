/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.OutletExistsException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Keith test
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewOutlet(Outlet outlet) throws OutletExistsException, UnknownPersistenceException  {
        try {
            em.persist(outlet);
            em.flush();

            return outlet.getOutletId();
        } catch (PersistenceException ex) {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new OutletExistsException("Outlet already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public List<Outlet> retrieveAllOutlets() {
        Query query = em.createQuery("SELECT o FROM Outlet o");

        return query.getResultList();
    }

    @Override
    public Outlet retrieveOutletById(Long outletId) throws OutletNotFoundException{
        Outlet outlet = em.find(Outlet.class, outletId);

        if (outlet != null) {
            return outlet;
        } else {
             throw new OutletNotFoundException("Unable to locate outlet with id: " + outletId);
        }
    }
    
    @Override
    public Outlet retrieveOutletByLocation(String location) throws OutletNotFoundException {
        Query query = em.createQuery("SELECT o FROM Outlet o WHERE o.location = :inLocation");
        query.setParameter("inLocation", location);

        try {
            return (Outlet) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new OutletNotFoundException("Outlet at Location " + location + " does not exist!");
        }
    }
}
