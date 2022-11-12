/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.TransitDriverDispatchRecordNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFromOutletException;

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
    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long id, Outlet outlet) throws TransitDriverDispatchRecordNotFoundException,  TransitDriverDispatchRecordNotFromOutletException{
        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, id);

        if (transitDriverDispatchRecord != null) {
            System.out.println("Dispatch return location is " + transitDriverDispatchRecord.getReturnLocation().getName());
            if (transitDriverDispatchRecord.getReturnLocation().getName().equals(outlet.getName())) {
                return transitDriverDispatchRecord;
            } else {
                throw new TransitDriverDispatchRecordNotFromOutletException("Transit Record with id " + id + " is not from " + outlet.getName());
            }
        } else {
            throw new TransitDriverDispatchRecordNotFoundException("Unable to locate record with id: " + id);
        }
    }

    @Override
    public void removeTransitDriverDispatchRecord(Long id
    ) {
        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, id);
        em.remove(transitDriverDispatchRecord);
    }

    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordForCurrentDay(Date currentDay, Outlet outlet) {
        List<TransitDriverDispatchRecord> list = em.createQuery(
                "SELECT t FROM TransitDriverDispatchRecord t") // JOIN t.returnLocation o WHERE o.name = :outletName"
//                .setParameter("outletName", outlet.getName())
                .getResultList();
        
        Calendar c = Calendar.getInstance();
        // setting time to 2359
        c.setTime(currentDay);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        currentDay = c.getTime(); //  current day converted to 2359. need to change to 0159
        
        System.out.println("Current day is " + currentDay);
        c.add(Calendar.DATE, -1);
        c.add(Calendar.HOUR, -2);
        Date dayBeforeDateTime = c.getTime(); // previous day converted to 2159
        
        for (TransitDriverDispatchRecord r : list) {
            System.out.println(r.getId() + " " + r.getStartDateTime());
        }
        
        for (int i = 0; i < list.size(); i++) {
            TransitDriverDispatchRecord record = list.get(i);
            if (!(record.getStartDateTime().after(dayBeforeDateTime) && record.getStartDateTime().before(currentDay))) {
                list.remove(record);
            }
        }
        
        return list;
    }

    public void assignTransitDriver(Long id, Employee driver) {
        TransitDriverDispatchRecord record = em.find(TransitDriverDispatchRecord.class, id);

        Employee managedDriver = (Employee) em.createQuery("SELECT e FROM Employee e WHERE e.username = :driverUserName")
                .setParameter("driverUserName", driver.getUsername())
                .getSingleResult();

        for (TransitDriverDispatchRecord t : managedDriver.getDispatches()) {
            if (t.getStartDateTime().equals(record.getStartDateTime())) { // only compares the day
                // throw exception
            }
        }
        record.setEmployee(driver);
        managedDriver.getDispatches().add(record);
    }

    public void updateTransitAsCompleted(Long id) {
        TransitDriverDispatchRecord record = em.find(TransitDriverDispatchRecord.class, id);
        record.setStatus("Completed");
        record.getEmployee().setOutlet(record.getPickupLocation()); // set driver new location to that of transit destination
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
