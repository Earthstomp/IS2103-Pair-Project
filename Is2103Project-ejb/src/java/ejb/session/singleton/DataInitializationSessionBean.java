package ejb.session.singleton;

import ejb.session.stateless.CategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Car;
import entity.Category;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.Partner;
import entity.RentalRateRecord;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeRoleEnum;
import util.enumeration.RentalRateEnum;
import util.exception.CategoryExistsException;
import util.exception.EmployeeExistsException;
import util.exception.OutletExistsException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;

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

        Long standardSedanId = 0L;
        Long familySedanId = 0L;
        Long luxurySedanId = 0L;
        Long suvAndMinivanId = 0L;

        Long toyotaCorollaId = 0L;
        Long hondaCividId = 0L;
        Long nissanSunnyId = 0L;
        Long mercedesEClass = 0L;
        Long bmw5Series = 0L;
        Long audiA6 = 0L;

        // loading Outlet        
        try {
            // for testing purposes
            outletSessionBeanLocal.createNewOutlet(new Outlet("location", null, null));

            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet A", null, null));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet B", null, null));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Outlet C", new Date(), new Date())); //  supposed to be String 10:00 and 22:00
        } catch (OutletExistsException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

        try {

            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.ADMINISTRATOR, "admin", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("location").getOutletId());

            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.SALES_MANAGER, "Employee A1", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet A").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.OPERATIONS_MANAGER, "Employee A2", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet A").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.CUSTOMER_SERVICE_EXECUTIVE, "Employee A3", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet A").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.EMPLOYEE, "Employee A4", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet A").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.EMPLOYEE, "Employee A5", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet A").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.SALES_MANAGER, "Employee B1", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet B").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.OPERATIONS_MANAGER, "Employee B2", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet B").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.CUSTOMER_SERVICE_EXECUTIVE, "Employee B3", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet B").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.SALES_MANAGER, "Employee C1", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet C").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.OPERATIONS_MANAGER, "Employee C2", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet C").getOutletId());
            employeeSessionBeanLocal.createNewEmployeeWithExistingOutlet(new Employee(EmployeeRoleEnum.CUSTOMER_SERVICE_EXECUTIVE, "Employee C3", "pw"), outletSessionBeanLocal.retrieveOutletByLocation("Outlet C").getOutletId());

        } catch (EmployeeExistsException | OutletNotFoundException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

        try {
            standardSedanId = categorySessionBeanLocal.createNewCategory(new Category("Standard Sedan"));
            familySedanId = categorySessionBeanLocal.createNewCategory(new Category("Family Sedan"));
            luxurySedanId = categorySessionBeanLocal.createNewCategory(new Category("Luxury Sedan"));
            suvAndMinivanId = categorySessionBeanLocal.createNewCategory(new Category("SUV and Minivan"));

        } catch (CategoryExistsException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }

//        try { no exception created yet
        toyotaCorollaId = categorySessionBeanLocal.createNewModelWithExistingCategory(new Model("Toyota", "Corolla"), standardSedanId);
        hondaCividId = categorySessionBeanLocal.createNewModelWithExistingCategory(new Model("Honda", "Civic"), standardSedanId);
        nissanSunnyId = categorySessionBeanLocal.createNewModelWithExistingCategory(new Model("Nissan", "Sunny"), standardSedanId);
        mercedesEClass = categorySessionBeanLocal.createNewModelWithExistingCategory(new Model("Mercedes", "E Class"), luxurySedanId);
        bmw5Series = categorySessionBeanLocal.createNewModelWithExistingCategory(new Model("BMW", "5 Series"), luxurySedanId);
        audiA6 = categorySessionBeanLocal.createNewModelWithExistingCategory(new Model("Audi", "A6"), luxurySedanId);

//        try {  no exception created yet
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet A", "SS00A1TC"), toyotaCorollaId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet A", "SS00A2TC"), toyotaCorollaId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet A", "SS00A3TC"), toyotaCorollaId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet B", "SS00B1HC"), hondaCividId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet B", "SS00B2HC"), hondaCividId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet B", "SS00B3HC"), hondaCividId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet C", "SS00C1NS"), nissanSunnyId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet C", "SS00C2NS"), nissanSunnyId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.SERVICING_OR_REPAIR, "Outlet C", "SS00C3NS"), nissanSunnyId);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet A", "LS00A4ME"), mercedesEClass);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet B", "LS00B4B5"), bmw5Series);
        modelSessionBeanLocal.createNewCarWithExistingModel(new Car(CarStatusEnum.AVAILABLE, "Outlet C", "LS00C4A6"), audiA6);

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        Date rate2start = new Date();
        Date rate2end = new Date();
        Date rate5start = new Date();
        Date rate5end = new Date();
        Date rate6start = new Date();
        Date rate6end = new Date();
        Date rate7start = new Date();
        Date rate7end = new Date();
        Date rate8start = new Date();
        Date rate8end = new Date();

        try {
            rate2start = inputDateFormat.parse("09/12/2022");
            rate2start.setHours(12);
            rate2end = inputDateFormat.parse("11/12/2022");
            rate2end.setHours(0);

            rate5start = inputDateFormat.parse("05/12/2022");
            rate5start.setHours(0);
            rate5end = inputDateFormat.parse("05/12/2022");
            rate5end.setHours(23);
            rate5end.setMinutes(59);

            rate6start = inputDateFormat.parse("06/12/2022");
            rate6start.setHours(0);
            rate6end = inputDateFormat.parse("06/12/2022");
            rate6end.setHours(23);
            rate6end.setMinutes(59);

            rate7start = inputDateFormat.parse("07/12/2022");
            rate7start.setHours(0);
            rate7end = inputDateFormat.parse("07/12/2022");
            rate7end.setHours(23);
            rate7end.setMinutes(59);

            rate8start = inputDateFormat.parse("07/12/2022");
            rate8start.setHours(12);
            rate8end = inputDateFormat.parse("08/12/2022");
            rate8end.setHours(12);
            rate8end.setMinutes(59);

        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }

        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Default", RentalRateEnum.DEFAULT, 100.0, null, null), standardSedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Weekend Promo", RentalRateEnum.PROMOTION, 80.0, rate2start, rate2end), standardSedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Default", RentalRateEnum.DEFAULT, 200.0, null, null), standardSedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Default", RentalRateEnum.DEFAULT, 300.0, null, null), luxurySedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Monday", RentalRateEnum.PEAK, 310.0, rate5start, rate5end), luxurySedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Tuesday", RentalRateEnum.PEAK, 320.0, rate6start, rate6end), luxurySedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Wednesday", RentalRateEnum.PEAK, 330.0, rate7start, rate7end), luxurySedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Weekday Promo", RentalRateEnum.PROMOTION, 250.0, rate8start, rate8end), luxurySedanId);
        categorySessionBeanLocal.createRentalRateRecord(new RentalRateRecord("Default", RentalRateEnum.DEFAULT, 400.0, null, null), luxurySedanId);

        partnerSessionBeanLocal.createPartner(new Partner("Holiday.com"));

    }
//        } catch (PartnerExistsException ex) { //UnknownPersistenceException ex
//            ex.printStackTrace();
//        }

}
