/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.TransitDriverDispatchRecord;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeExistsException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Keith test
 */
@Remote
public interface EmployeeSessionBeanRemote {

    public Long createNewEmployeeWithExistingOutlet(Employee employee, Long outletId) throws EmployeeExistsException, UnknownPersistenceException;

    public Long createNewTransitDriverDispatchRecordWithExistingEmployee(TransitDriverDispatchRecord dispatchRecord, Long employeeId);
    
    public List<Employee> retrieveAllEmployees();
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException ;
    
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;

    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialsException;
    
}
