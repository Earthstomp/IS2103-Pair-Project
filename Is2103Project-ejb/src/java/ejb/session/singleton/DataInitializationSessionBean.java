package ejb.session.singleton;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Employee;
import entity.Outlet;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeRoleEnum;
import util.exception.EmployeeExistsException;
import util.exception.OutletExistsException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dorothyyuan
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    public DataInitializationSessionBean() {

    }

    @PostConstruct
    public void postConstruct() {
        try {
            outletSessionBeanLocal.retrieveOutletByLocation("");
        } catch (OutletNotFoundException ex) {
            initializeData();
        }
        /*try
        {
            employeeSessionBeanLocal.retrieveEmployeeByUsername("administrator");
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }*/
    }

    private void initializeData() {
        try {
            // STUB time
            outletSessionBeanLocal.createNewOutlet(new Outlet("location", new Date(), new Date()));
        } catch (OutletExistsException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

        try {
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.ADMINISTRATOR, "administrator", "password"), outletSessionBeanLocal.retrieveOutletByLocation("location").getOutletId());
        } catch (EmployeeExistsException | OutletNotFoundException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

    }
}
