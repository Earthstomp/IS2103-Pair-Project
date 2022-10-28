/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import java.util.List;
import javax.ejb.Remote;
import util.exception.TransitDriverDispatchRecordNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface TransitDriverDispatchRecordSessionBeanRemote {
    
    public void removeTransitDriverDispatchRecord(Long id);

    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordById(Long id) throws TransitDriverDispatchRecordNotFoundException ;

    public List<TransitDriverDispatchRecord> retrieveAllTransitDriverDispatchRecord();

    public Long createNewTransitDriverDispatchRecord(TransitDriverDispatchRecord transitDriverDispatchRecord);
    
}
