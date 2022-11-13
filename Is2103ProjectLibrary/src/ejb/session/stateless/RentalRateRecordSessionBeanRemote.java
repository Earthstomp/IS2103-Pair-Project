/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.RentalRateRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.RentalRateRecordNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface RentalRateRecordSessionBeanRemote {

    public void removeRentalRateRecord(Long id);

    public RentalRateRecord retrieveRentalRateRecordById(Long id) throws RentalRateRecordNotFoundException;

    public List<RentalRateRecord> retrieveAllRateRecords();

    public Long createRentalRateRecord(RentalRateRecord rentalRateRecord);

    public void updateRentalRateRecord(RentalRateRecord rentalRateRecord, RentalRateRecord newRentalRateRecord) throws RentalRateRecordNotFoundException;

    public List<RentalRateRecord> retrieveRentalRateRecordByName(String name) throws RentalRateRecordNotFoundException;

    public void merge(RentalRateRecord rentalRateRecord);

    public List<RentalRateRecord> retrieveAllRateRecordsByDatebyCategory(Date date, Category category);

    public RentalRateRecord chooseRateRecord(List<RentalRateRecord> rates);

}
