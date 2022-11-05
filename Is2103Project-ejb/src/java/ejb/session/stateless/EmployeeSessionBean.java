/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.TransitDriverDispatchRecord;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeExistsException;
import util.exception.EmployeeNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author Keith test
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    public EmployeeSessionBean() {
    }

    @Override
    public Long createNewEmployeeWithExistingOutlet(Employee employee, Long outletId) throws EmployeeExistsException, UnknownPersistenceException {
        em.persist(employee);

        Outlet outlet = em.find(Outlet.class, outletId);
        employee.setOutlet(outlet);
        outlet.getEmployees().add(employee);
        em.flush();
        // not setting outlet to employee cause unidirectional

        em.flush();
        // might have some error here. check with dorothy
        try {
            return employee.getEmployeeId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new EmployeeExistsException("Employee already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public Long createNewTransitDriverDispatchRecordWithExistingEmployee(TransitDriverDispatchRecord dispatchRecord, Long employeeId) {
        em.persist(dispatchRecord);

        Employee employee = em.find(Employee.class, employeeId);
        employee.getDispatches().add(dispatchRecord);

        em.flush();

        return dispatchRecord.getId();
    }

    @Override
    public List<Employee> retrieveAllEmployees() {
        Query query = em.createQuery("SELECT e FROM Employee e");

        return query.getResultList();
    }

    @Override
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException {
        Employee employee = em.find(Employee.class, employeeId);

        if (employee != null) {
            return employee;
        } else {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }
    }

    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }

    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialsException {
        try {
            Employee employee = retrieveEmployeeByUsername(username);

            if (employee.getPassword().equals(password)) {
//                employee.getSaleTransactionEntities().size();
                return employee;
            } else {
                throw new InvalidLoginCredentialsException("Username does not exist or invalid password!");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new InvalidLoginCredentialsException("Username does not exist or invalid password!");
        }
    }

}
