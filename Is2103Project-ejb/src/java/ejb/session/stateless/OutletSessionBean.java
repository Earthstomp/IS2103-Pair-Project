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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.OutletNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewOutlet(Outlet outlet) {
        em.persist(outlet);
        em.flush();

        return outlet.getOutletId();
    }
        

    @Override
    public List<Outlet> retrieveAllOutlets() {
        Query query = em.createQuery("SELECT o FROM Outlet o");

        return query.getResultList();
    }

    @Override
    public Outlet retrieveReservationById(Long outletId) throws OutletNotFoundException {
        Outlet outlet = em.find(Outlet.class, outletId);

        if (outlet != null) {
            return outlet;
        } else {
             throw new OutletNotFoundException("Unable to locate outlet with id: " + outletId);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
