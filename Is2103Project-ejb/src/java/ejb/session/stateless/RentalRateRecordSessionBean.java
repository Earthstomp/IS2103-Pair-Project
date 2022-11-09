/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateRecord;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RentalRateRecordNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class RentalRateRecordSessionBean implements RentalRateRecordSessionBeanRemote, RentalRateRecordSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createRentalRateRecord(RentalRateRecord rentalRateRecord) {
        em.persist(rentalRateRecord);
        em.flush();

        return rentalRateRecord.getId();
    }

    @Override
    public List<RentalRateRecord> retrieveAllRateRecords() {
        Query query = em.createQuery("SELECT r FROM RentalRateRecord r ORDER BY r.carCategory, r.validityPeriod");
        return query.getResultList();
    }

    
    @Override
    public void updateRentalRateRecord(RentalRateRecord rentalRateRecord) throws RentalRateRecordNotFoundException
    {
        if(rentalRateRecord != null && rentalRateRecord.getId()!= null)
        {
            try {
                RentalRateRecord updateRentalRateRecord = retrieveRentalRateRecordById(rentalRateRecord.getId());
                updateRentalRateRecord.setRecordName(rentalRateRecord.getRecordName());
                updateRentalRateRecord.setRate(rentalRateRecord.getRate());
                updateRentalRateRecord.setValidityPeriod(rentalRateRecord.getValidityPeriod());
                updateRentalRateRecord.setCategory(rentalRateRecord.getCategory());
                updateRentalRateRecord.setEnabled(rentalRateRecord.getEnabled());
            } catch (RentalRateRecordNotFoundException ex) {
                throw new RentalRateRecordNotFoundException("Rental Rate Record ID not provided for record to be updated");
            }
        } else {
            throw new RentalRateRecordNotFoundException("Rental Rate Record ID not provided for record to be updated");
        }
    }
    
    @Override
    public RentalRateRecord retrieveRentalRateRecordById(Long id) throws RentalRateRecordNotFoundException{

        RentalRateRecord rentalRateRecord = em.find(RentalRateRecord.class, id);
        
        if (rentalRateRecord != null) {
            return rentalRateRecord;
        } else {
            throw new RentalRateRecordNotFoundException("Unable to locate record with id: " + id);
        }
    }

    @Override
    public void removeRentalRateRecord(Long id) {
        RentalRateRecord rentalRateRecord = em.find(RentalRateRecord.class, id);

        List<RentalRateRecord> recordsUsed = em.createQuery(
                // check query and how to compare dates 
                "SELECT r "
                + "FROM RentalRateRecord r JOIN (r.carCategory) category"
                + "JOIN (category.models) m"
                + "JOIN (m.cars) c"
                + "JOIN (c.reservations) reservation"
                + "WHERE reservation.startDateTime <= r.validityPeriod && reservation.endDateTime >= r.validityPeriod").getResultList();

        if (recordsUsed.size() > 0) {
            System.out.print("There are reservations that use this rental rate record, record is disabled instead of deleted");
            rentalRateRecord.setEnabled(false);

        } else { // record is not being used, can delete
            rentalRateRecord.getCategory().getRateRecords().remove(rentalRateRecord);
            em.remove(rentalRateRecord);
        }

    }

}

// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method"
