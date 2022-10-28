/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Keith test
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewEmployeeWithExistingOutlet(Employee employee, Long outletId) {
        em.persist(employee);
        
        Outlet outlet = em.find(Outlet.class, outletId);
        employee.setOutlet(outlet); 
        // not setting outlet to employee cause unidirectional
        
        em.flush();

        return employee.getEmployeeId();
    }
    
    @Override
    public TransitDriverDispatchRecord createNewTransitDriverDispatchRecordWithExistingEmployee(TransitDriverDispatchRecord dispatchRecord, Long employeeId) {
        em.persist(dispatchRecord);
        
        Employee employee = em.find(Employee.class, employeeId);
        employee.getDispatches().add(dispatchRecord);
        
        em.flush();

        return dispatchRecord;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    
}
