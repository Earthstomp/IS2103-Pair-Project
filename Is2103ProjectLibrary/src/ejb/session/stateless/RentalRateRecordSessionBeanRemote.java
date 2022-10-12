/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateRecord;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Keith test
 */
@Remote
public interface RentalRateRecordSessionBeanRemote {

    public void removeRentalRateRecord(Long id);

    public RentalRateRecord retrieveRentalRateRecordById(Long id);

    public List<RentalRateRecord> retrieveAllRateRecords();

    public Long createRentalRateRecord(RentalRateRecord rentalRateRecord);
}
