/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRateRecord;
import java.util.List;
import javax.ejb.Local;
import util.exception.RentalRateRecordNotFoundException;

/**
 *
 * @author Keith test
 */
@Local
public interface RentalRateRecordSessionBeanLocal {

    public void removeRentalRateRecord(Long id);

    public RentalRateRecord retrieveRentalRateRecordById(Long id) throws RentalRateRecordNotFoundException;

    public List<RentalRateRecord> retrieveAllRateRecords();
    
}
