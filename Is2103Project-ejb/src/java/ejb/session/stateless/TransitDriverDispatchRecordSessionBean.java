/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Keith test
 */
@Stateless
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord) {
        em.persist(transitDriverDispatchRecord);
        em.flush();

        return transitDriverDispatchRecord.getId();
    }
    
    @Override
    public List<TransitDriverDispatchRecord> retrieveAllTransitDriverDispatchRecord() {
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t");

        return query.getResultList();
    }
    
    @Override
    public TransitDriverDispatchRecord retrieveReservationById(Long id) { // throws exception
        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, id);

        if (transitDriverDispatchRecord != null) {
            return transitDriverDispatchRecord;
        } else {
            // throw exception
            // delete line below
            return transitDriverDispatchRecord;
        }
    }
    
    @Override
    public void removeTransitDriverDispatchRecord(Long id) {
        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, id);
        em.remove(transitDriverDispatchRecord);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
}
