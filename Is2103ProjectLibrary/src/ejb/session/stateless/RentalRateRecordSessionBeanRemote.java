/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateRecord;
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

    public void updateRentalRateRecord(RentalRateRecord rentalRateRecord) throws RentalRateRecordNotFoundException;

}
