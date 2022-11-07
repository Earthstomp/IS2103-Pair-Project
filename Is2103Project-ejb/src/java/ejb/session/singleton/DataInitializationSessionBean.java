package ejb.session.singleton;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Category;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CategoryExistsException;
import util.exception.EmployeeExistsException;
import util.exception.OutletExistsException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerExistsException;
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
            outletSessionBeanLocal.retrieveOutletById(1l);
        } catch (OutletNotFoundException ex) {
            initializeData();
        }
        /*try
        {
            employeeSessionBeanLocal.retrieveEmployeeById(1l);
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
        
        try {
            partnerSessionBeanLocal.retrievePartnerById(1l);
        } catch (PartnerNotFoundException ex) {
            initializeData();
        }

        try {
            categorySessionBeanLocal.retrieveCategoryById(1l);
        } catch (CategoryNotFoundException ex) {
            initializeData();
        }
        */
    }

    private void initializeData() {
        try {
            // STUB Outlet(address, opening, closing)
            outletSessionBeanLocal.createNewOutlet(new Outlet("address", new Date(), new Date()));
        } catch (OutletExistsException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

        try {
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.ADMINISTRATOR, "administrator", "password"), outletSessionBeanLocal.retrieveOutletByLocation("location").getOutletId());
        } catch (EmployeeExistsException | OutletNotFoundException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

//        try {
            partnerSessionBeanLocal.createPartner(new Partner("Holiday.com"));
//        } catch (PartnerExistsException ex) { //UnknownPersistenceException ex
//            ex.printStackTrace();
//        }

//        try {
            categorySessionBeanLocal.createNewCategory(new Category("CategoryName"));
//        } catch (CategoryExistsException | UnknownPersistenceException ex) {
//            ex.printStackTrace();
//        }

    }
}
