/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Keith test
 */
@Local
public interface TransitDriverDispatchRecordSessionBeanLocal {

    public void removeTransitDriverDispatchRecord(Long id);

    public TransitDriverDispatchRecord retrieveReservationById(Long id);

    public List<TransitDriverDispatchRecord> retrieveAllTransitDriverDispatchRecord();

    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord);
    
}
