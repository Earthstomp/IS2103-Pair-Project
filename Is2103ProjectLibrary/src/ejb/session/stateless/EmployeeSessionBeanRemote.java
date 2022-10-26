/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.TransitDriverDispatchRecord;
import javax.ejb.Remote;

/**
 *
 * @author Keith test
 */
@Remote
public interface EmployeeSessionBeanRemote {

    public Long createNewEmployeeWithExistingOutlet(Employee employee, Long outletId);

    public TransitDriverDispatchRecord createNewTransitDriverDispatchRecordWithExistingEmployee(TransitDriverDispatchRecord dispatchRecord, Long employeeId);
    
}
