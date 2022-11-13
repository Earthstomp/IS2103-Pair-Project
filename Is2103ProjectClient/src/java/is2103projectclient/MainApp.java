/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103projectclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateRecordSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Employee;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import util.exception.InvalidEmployeeRoleException;
import util.exception.InvalidLoginCredentialsException;

/**
 *
 * @author Keith test
 */
public class MainApp {

    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;

    private SalesManagementModule salesManagementModule;
    private CustomerServiceModule customerServiceModule;
    private Employee employee;
    private EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;

    public MainApp() {
    }

    public MainApp(ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote,
            CategorySessionBeanRemote categorySessionBeanRemote, RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote,
            CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote) {
        this();
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.rentalRateRecordSessionBeanRemote = rentalRateRecordSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.ejbTimerSessionBeanRemote = ejbTimerSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to CaRMS Management Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        
                        salesManagementModule = new SalesManagementModule(modelSessionBeanRemote, carSessionBeanRemote,
                                transitDriverDispatchRecordSessionBeanRemote, outletSessionBeanRemote, employeeSessionBeanRemote,
                                employee, categorySessionBeanRemote, rentalRateRecordSessionBeanRemote, customerSessionBeanRemote, reservationSessionBeanRemote);
                        
                        customerServiceModule = new CustomerServiceModule(modelSessionBeanRemote, carSessionBeanRemote,
                                transitDriverDispatchRecordSessionBeanRemote, outletSessionBeanRemote, employeeSessionBeanRemote,
                                employee, categorySessionBeanRemote, rentalRateRecordSessionBeanRemote, customerSessionBeanRemote, reservationSessionBeanRemote);
                        
                        menuMain();
                    } catch (InvalidLoginCredentialsException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialsException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMS Management Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            employee = employeeSessionBeanRemote.employeeLogin(username, password);
        } else {
            throw new InvalidLoginCredentialsException("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Management Client ***\n");
            System.out.println("You are logged in as " + employee.getUsername() + " with " + employee.getRole().toString() + " rights\n");
            System.out.println("1: Sales Management Module");
            System.out.println("2: Customer Management Module");
            System.out.println("3: Trigger Car Allocation");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        salesManagementModule.menuSalesManagementModule();
                    } catch (InvalidEmployeeRoleException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    try {
                        customerServiceModule.menuCustomerServiceModule();
                    } catch (InvalidEmployeeRoleException ex) {
                        System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                    }
                } else if (response == 3) {

                    try {
                        System.out.println("Enter Date and Time to trigger allocation (dd/mm/yyyy hh:mm a)> ");
                        scanner.nextLine(); 
                        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                        Date date = inputDateFormat.parse(scanner.nextLine().trim());
                        ejbTimerSessionBeanRemote.allocateCarsManual(date);
                    } catch (ParseException ex) {
                        System.out.println("Invalid date input!\n");
                    }
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }
}
