/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRateRecord;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.RentalRateEnum;
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
        List<RentalRateRecord> rentalRateRecords = em.createQuery("SELECT r FROM RentalRateRecord r"
                + " ORDER BY r.category, r.validityPeriod")
                .getResultList();
        for (RentalRateRecord r : rentalRateRecords) {
            r.getCategory();
        }
        return rentalRateRecords;
    }

    @Override
    public List<RentalRateRecord> retrieveAllRateRecordsByDatebyCategory(Date date, Category category) {
        List<RentalRateRecord> rentalRateRecords = em.createQuery("SELECT r FROM RentalRateRecord r WHERE r.category.categoryName = :categoryName")
                .setParameter("categoryName", category.getCategoryName())
                .getResultList();

        List<RentalRateRecord> ratesAvailableOnDate = new ArrayList<RentalRateRecord>();
        for (RentalRateRecord r : rentalRateRecords) {
            List<Date> validityPeriod = r.getValidityPeriod();
//            r.getValidityPeriod();
            System.out.println("Rental rate record is ID " + r.getId() + " name is " + r.getRecordName());
            Date rateStartTime = validityPeriod.get(0);
            System.out.println("rate start time is " + rateStartTime);
            Date rateEndTime = validityPeriod.get(1);
            System.out.println("rate end time is " + rateEndTime);

            // if the rentalDate is within the rate period
            if (rateStartTime == null) {
                                ratesAvailableOnDate.add(r);
            } else if (date.after(rateStartTime) && date.before(rateEndTime)) {
                ratesAvailableOnDate.add(r);
            } else {
                
            }
        }
        return ratesAvailableOnDate;
    }

    public RentalRateRecord chooseRateRecord(List<RentalRateRecord> rates) {

        List<RentalRateRecord> promotionRates = new ArrayList<RentalRateRecord>();
        List<RentalRateRecord> peakRates = new ArrayList<RentalRateRecord>();
        List<RentalRateRecord> defaultRates = new ArrayList<RentalRateRecord>();

        for (RentalRateRecord r : rates) {
            if (r.getType() == (RentalRateEnum.PROMOTION)) {
                promotionRates.add(r);
            } else if (r.getType() == (RentalRateEnum.PEAK)) {
                peakRates.add(r);
            } else { //  default
                defaultRates.add(r);
            }
        }
        
        System.out.println(promotionRates.size() + " " +  peakRates.size() + " " + defaultRates.size());

        if (promotionRates.size() > 0) {
            RentalRateRecord lowestRate = promotionRates.get(0);
            for (RentalRateRecord promotionRate : promotionRates) {
                if (promotionRate.getRate() < lowestRate.getRate()) {
                    lowestRate = promotionRate;
                }
            }

            return lowestRate;
        } else if (peakRates.size() > 0) {
            RentalRateRecord lowestRate = peakRates.get(0);
            for (RentalRateRecord peakRate : peakRates) {
                if (peakRate.getRate() < lowestRate.getRate()) {
                    lowestRate = peakRate;
                }
            }
            return lowestRate;
        } else { // for default rates
            RentalRateRecord lowestRate = defaultRates.get(0);
            for (RentalRateRecord defaultRate : defaultRates) {
                if (defaultRate.getRate() < lowestRate.getRate()) {
                    lowestRate = defaultRate;
                }
            }
            return lowestRate;
        }

    }

    @Override
    public void updateRentalRateRecord(RentalRateRecord rentalRateRecord, RentalRateRecord newRentalRateRecord) throws RentalRateRecordNotFoundException {
        if (rentalRateRecord != null && rentalRateRecord.getId() != null) {
            try {
                RentalRateRecord updateRentalRateRecord = retrieveRentalRateRecordById(rentalRateRecord.getId());
                updateRentalRateRecord.setRecordName(newRentalRateRecord.getRecordName());
                updateRentalRateRecord.setRate(newRentalRateRecord.getRate());
                updateRentalRateRecord.setValidityPeriod(newRentalRateRecord.getValidityPeriod());
                updateRentalRateRecord.setCategory(newRentalRateRecord.getCategory());
                updateRentalRateRecord.setEnabled(newRentalRateRecord.getEnabled());
            } catch (RentalRateRecordNotFoundException ex) {
                throw new RentalRateRecordNotFoundException("Rental Rate Record ID not provided for record to be updated");
            }
        } else {
            throw new RentalRateRecordNotFoundException("Rental Rate Record ID not provided for record to be updated");
        }
    }

    @Override
    public RentalRateRecord retrieveRentalRateRecordById(Long id) throws RentalRateRecordNotFoundException {

        RentalRateRecord rentalRateRecord = em.find(RentalRateRecord.class, id);

        if (rentalRateRecord != null) {
            return rentalRateRecord;
        } else {
            throw new RentalRateRecordNotFoundException("Unable to locate record with id: " + id);
        }
    }

    @Override
    public List<RentalRateRecord> retrieveRentalRateRecordByName(String name) throws RentalRateRecordNotFoundException {
        List<RentalRateRecord> rentalRateRecords = new ArrayList<>();
        try {
            rentalRateRecords = em.createQuery("SELECT r FROM RentalRateRecord r WHERE r.recordName = :InName")
                    .setParameter("InName", name)            
                    .getResultList();

            for (RentalRateRecord r : rentalRateRecords) {
                r.getValidityPeriod().size();
            }
            
            return rentalRateRecords;

        } catch (NullPointerException ex) {
            throw new RentalRateRecordNotFoundException("Unable to locate record.");
        }
    }

    @Override
    public void removeRentalRateRecord(Long id, boolean isUsed) {
        RentalRateRecord rentalRateRecord = em.find(RentalRateRecord.class, id);

        if (isUsed) {
            System.out.print("There are reservations that use this rental rate record. Record is disabled instead of deleted");
            rentalRateRecord.setEnabled(false);

        } else { // record is not being used, can delete
            rentalRateRecord.getCategory().getRateRecords().remove(rentalRateRecord);
            em.remove(rentalRateRecord);
        }
      

    }

    public void merge(RentalRateRecord rentalRateRecord) {
        em.merge(rentalRateRecord);
    }
}

// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method"
