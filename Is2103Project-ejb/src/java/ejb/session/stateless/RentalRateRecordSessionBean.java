/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateRecord;
import entity.Reservation;
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
public class RentalRateRecordSessionBean implements RentalRateRecordSessionBeanRemote, RentalRateRecordSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public List<RentalRateRecord> retrieveAllRateRecords() {
        Query query = em.createQuery("SELECT r FROM RentalRateRecord r");

        return query.getResultList();
    }

    @Override
    public RentalRateRecord retrieveRentalRateRecordById(Long id) { // throws exception
        RentalRateRecord rentalRateRecord = em.find(RentalRateRecord.class, id);

        if (rentalRateRecord != null) {
            return rentalRateRecord;
        } else {
            // throw exception
            // delete line below
            return rentalRateRecord;
        }
    }

    @Override
    public void removeRentalRateRecord(Long id) {
        RentalRateRecord rentalRateRecord = em.find(RentalRateRecord.class, id);
        em.remove(rentalRateRecord);
        // need to check for dependencies / if other cars are using this rental rate record
    }

}

// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method"
