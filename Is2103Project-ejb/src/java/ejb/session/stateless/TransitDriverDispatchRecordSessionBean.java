/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import java.util.Date;
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
    
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordForCurrentDay(Date currentDay, Outlet outlet) {
        return em.createQuery(
                "SELECT t FROM TransitDriverDispatchRecord t "
                + "JOIN t.employee e"
                + "WHERE e.outlet = outlet"
                + "AND t.startDateTime.equals(:currentDay)") // not sure how to compare dates
                .setParameter("currentDay", currentDay)
                .getResultList();
        
    }
    
    public void assignTransitDriver(Long id, Employee driver) {
        TransitDriverDispatchRecord record = em.find(TransitDriverDispatchRecord.class, id);
        
        Employee managedDriver = (Employee) em.createQuery("SELECT e FROM Employee e"
                + "WHERE e = :driver")
                .setParameter("driver", driver)
                .getSingleResult();
        
        for (TransitDriverDispatchRecord t : managedDriver.getDispatches()) {
            if (t.getStartDateTime().equals(record.getStartDateTime())) { // only compares the day
                // throw exception
            }
        }        
        record.setEmployee(driver);
        managedDriver.getDispatches().add(record);
    }
    
    private void updateTransitAsCompleted(Long id) {
        TransitDriverDispatchRecord record = em.find(TransitDriverDispatchRecord.class, id);
        record.setStatus("Completed");
        record.getEmployee().setOutlet(record.getPickupLocation()); // set driver new location to that of transit destination
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}
