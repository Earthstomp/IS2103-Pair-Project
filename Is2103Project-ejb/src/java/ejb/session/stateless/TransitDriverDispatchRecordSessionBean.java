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
            if (transitDriverDispatchRecord.getReturnLocation() == outlet) {
                return transitDriverDispatchRecord;
            } else {
                throw new TransitDriverDispatchRecordNotFromOutletException("Transit Record with id " + id + " is not from Outlet " + outlet);
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
                "SELECT t FROM TransitDriverDispatchRecord t JOIN t.employee e JOIN e.outlet o WHERE o.name = :outletName")
                .setParameter("outletName", outlet.getName())
                .getResultList();
        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(currentDay);
        calendar.add(GregorianCalendar.HOUR_OF_DAY, 22); //  setting end of day as 2200hours
        Date endOfCurrentDay = calendar.getTime();
        
        for (TransitDriverDispatchRecord record : list) {
            if (record.getStartDateTime().getTime() < currentDay.getTime() && record.getStartDateTime().getTime() > endOfCurrentDay.getTime()) {
                list.remove(record);
            }
        }

        return list;
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

    public void updateTransitAsCompleted(Long id) {
        TransitDriverDispatchRecord record = em.find(TransitDriverDispatchRecord.class, id);
        record.setStatus("Completed");
        record.getEmployee().setOutlet(record.getPickupLocation()); // set driver new location to that of transit destination
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
