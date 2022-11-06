package is2103projectclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.Model;
import entity.Category;
import entity.Customer;
import entity.Employee;
import entity.Outlet;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.text.ParseException;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.DeleteModelException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidEmployeeRoleException;
import util.exception.OutletNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFromOutletException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Keith test
 */
public class SalesManagementModule {

    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;

    private Employee employee;

    public SalesManagementModule() {
    }

    public SalesManagementModule(ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote, Employee employee) {
        this();
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.employee = employee;
    }

    public void menuSalesManagementModule() throws InvalidEmployeeRoleException {

        if (employee.getRole() != EmployeeRoleEnum.SALES_MANAGER && employee.getRole() != EmployeeRoleEnum.ADMINISTRATOR) {
            throw new InvalidEmployeeRoleException("You don't have SALES MANAGER rights to access the Sales Management module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Management System :: Sales Management :: Operations Manager***\n");
            System.out.println("1: Create New model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model;");
            System.out.println("4: Delete Model");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("10: View Transit Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign  Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("13: Pickup Car");
            System.out.println("14: Return Car");
            System.out.println("15: Back\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewModel();
                } else if (response == 2) {
                    doViewAllModels();
                } else if (response == 3) {
                    doUpdateModel();
                } else if (response == 4) {
                    doDeleteModel();
                } else if (response == 5) {
                    doCreateNewCar();
                } else if (response == 6) {
                    doViewAllCars();
                } else if (response == 7) {
                    doViewCarDetails();
                } else if (response == 8) {
                    doUpdateCar();
                } else if (response == 9) {
                    doDeleteCar();
                } else if (response == 10) {
                    doViewTransitDriverDispatchRecordsForCurrentDayReservations();
                } else if (response == 11) {
                    doAssignDriver();
                } else if (response == 12) {
                    doUpdateTransitAsCompleted();
                } else if (response == 13) {
                    doPickUpCar();
                } else if (response == 14) {
                    doReturnCar();
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 15) {
                break;
            }
        }

    }

    public void doCreateNewModel() {
        Scanner scanner = new Scanner(System.in);
        Model model = new Model();
        int i = 1; //  index of menu

        System.out.println("*** Management System :: Sales Management :: Create New Model ***\n");
        System.out.print("Choose category to create model in> ");
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        for (Category c : categories) {
            System.out.println(i + ". " + c.getCategoryName());
            i++;
        }
        while (true) {
            Integer categoryChoice = scanner.nextInt();

            if (categoryChoice <= categories.size() && categoryChoice > 0) {
                model.setCategory(categories.get(categoryChoice - 1));
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        System.out.print("Enter Make and Model> ");
        model.setModel(scanner.nextLine().trim());

//        try { need to try and catch exception
        Long newModelId = categorySessionBeanRemote.createNewModelWithExistingCategoryClass(model, model.getCategory());
        System.out.println("New model created successfully!: " + model.getModel() + "\n");
    }

    public void doViewAllModels() {
        List<Model> models = modelSessionBeanRemote.viewAllModels();
        System.out.printf("\n%3s%12s%14s", "S/N", "Car Category", "Make and Model");
        int index = 1;
        for (Model model : models) {
            System.out.printf("\n%3s%20s%20s", index + ".", model.getCategory(), model.getModel());
            index++;
        }
    }

    public void doUpdateModel() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Update Model ***\n");
        System.out.println("Enter Model Name> ");
        Model model = modelSessionBeanRemote.retrieveModelByName(scanner.nextLine().trim());
        System.out.printf("\n%12s%14s", "Car Category", "Make and Model");
        System.out.printf("\n%3s%20s%20s", model.getCategory(), model.getModel());

        System.out.println("Enter Model's New Category> ");
        Category category = categorySessionBeanRemote.retrieveCategoryByName(scanner.nextLine().trim());

        model.setCategory(category);
        System.out.println("Enter Model's New Make and Model> ");
        model.setModel(scanner.nextLine().trim());
        modelSessionBeanRemote.merge(model);
        category.getModels().add(model);
        categorySessionBeanRemote.merge(category);
        System.out.println("Model updated successfully!");
        System.out.printf("\n%12s%14s", "Car Category", "Make and Model");
        System.out.printf("\n%3s%20s%20s", model.getCategory(), model.getModel());
    }

    public void doDeleteModel() {
        Scanner scanner = new Scanner(System.in);
        String confirmDelete = "";

        System.out.println("*** Management System :: Sales Management :: Delete Model ***\n");
        System.out.println("Enter Model Name> ");
        Model model = modelSessionBeanRemote.retrieveModelByName(scanner.nextLine().trim());
        System.out.printf("\n%12s%14s", "Car Category", "Make and Model");
        System.out.printf("\n%3s%20s%20s", model.getCategory(), model.getModel());

        System.out.print("Confirm delete this Model? (Enter 'Y' to delete)> ");
        confirmDelete = scanner.nextLine().trim();

        if (confirmDelete.equals("Y")) {
            try {
                modelSessionBeanRemote.deleteModel(model.getModelId());
                System.out.println("Model " + model.getModel() + " deleted successfully!");
            } catch (DeleteModelException ex) {
                System.out.println("An error has occurred while deleting the model: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Delete cancelled!\n");
        }
    }

    public void doCreateNewCar() {
        Scanner scanner = new Scanner(System.in);
        Car car = new Car();

        System.out.println("*** Management System :: Sales Management :: Create New Car ***\n");
        System.out.println("Enter Car Plate Number> ");
        car.setPlateNumber(scanner.nextLine().trim());
        System.out.println("Enter Car Colour> ");
        car.setPlateNumber(scanner.nextLine().trim());
        System.out.println("Enter Car Model> ");
        Model model = modelSessionBeanRemote.retrieveModelByName(scanner.nextLine().trim());
        car.setModel(model);
        System.out.println("Enter Car Location> ");
        car.setLocation(scanner.nextLine().trim());

        Long carId = modelSessionBeanRemote.createNewCarWithExistingModel(car, model.getModelId());
        System.out.println("New Car created successfully!: " + car.getPlateNumber() + "\n");
    }

    public void doViewAllCars() {
        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("\n%3s%12s%14s", "S/N", "Car Category", "Make and Model");
        int index = 1;
        for (Car car : cars) {
            System.out.printf("\n%3s%20s%20s%15s", index + ".", car.getModel().getCategory(), car.getModel(), car.getPlateNumber());
            index++;
        }
    }

    public void doViewCarDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Management System :: Sales Management :: View Car Details ***\n");
        System.out.println("Enter Car Plate Number> ");
        Car car = carSessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        System.out.printf("\n%12s%14s%20s%15s%20s%10s", "Car Category", "Make and Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%20s%20s%15s%15s%15s", car.getModel().getCategory(), car.getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());

    }

    public void doUpdateCar() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Management System :: Sales Management :: Update Car ***\n");
        System.out.println("Enter Car Plate Number> ");
        Car car = carSessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        System.out.printf("\n%12s%14s%20s%15s%20s%10s", "Car Category", "Make and Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%20s%20s%15s%15s%15s", car.getModel().getCategory(), car.getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());

        System.out.println("Enter Car's New Plate Number> ");
        car.setPlateNumber(scanner.nextLine().trim());
        System.out.println("Enter Car's Model> ");
        Model model = modelSessionBeanRemote.retrieveModelByName(scanner.nextLine().trim());
        // need to set
        Model oldModel = car.getModel(); // finding old model
        oldModel.getCars().remove(car);
        car.setModel(model); //  setting new model

        System.out.println("Enter Car's New Color> ");
        car.setColor(scanner.nextLine().trim());
        System.out.println("Enter Car's New Location> ");
        car.setLocation(scanner.nextLine().trim());
        System.out.println("Enter Car's status ");
        car.setEnabled(scanner.nextBoolean());

        // unsure if it is adding managed instance or not
        carSessionBeanRemote.merge(car);
        model.getCars().add(car);
        modelSessionBeanRemote.merge(model);
        System.out.println("Car updated successfully!");
        System.out.printf("\n%12s%14s%20s%15s%20s%10s", "Car Category", "Make and Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%20s%20s%15s%15s%15s", car.getModel().getCategory(), car.getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());
    }

    public void doDeleteCar() {
        Scanner scanner = new Scanner(System.in);
        String confirmDelete = "";

        System.out.println("*** Management System :: Sales Management :: Delete Car ***\n");
        System.out.println("Enter Car Plate Number> ");
        Car car = carSessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        System.out.printf("\n%12s%14s%20s%15s%20s%10s", "Car Category", "Make and Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%20s%20s%15s%15s%15s", car.getModel().getCategory(), car.getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());

        System.out.print("Confirm delete this Car? (Enter 'Y' to delete)> ");
        confirmDelete = scanner.nextLine().trim();

        if (confirmDelete.equals("Y")) {
            try {
                carSessionBeanRemote.deleteCar(car.getCarId());
                System.out.println("Car " + car.getPlateNumber() + " deleted successfully!");
            } catch (DeleteCarException ex) {
                System.out.println("An error has occurred while deleting the model: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Delete cancelled!\n");
        }
    }

    public void doViewTransitDriverDispatchRecordsForCurrentDayReservations() {
        Scanner scanner = new Scanner(System.in);
        // may remove in the future
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        Date currentDate;
        Outlet outlet = new Outlet();
        int index = 1;

        try {
            System.out.println("*** Management System :: Sales Management :: View Transit Driver Dispatch Records For Current Day Reservations ***\n");
            // temporary solution: will probably retrieve date from the system in the future. Singleton...?
            System.out.println("Enter Today's Date (dd/mm/yyyy)> ");
            currentDate = inputDateFormat.parse(scanner.nextLine().trim());
            // unsure if outlet is an input or take from employee 
            System.out.println("Enter Outlet> ");
            // need to improve error handling here
            try {
                outlet = outletSessionBeanRemote.retrieveOutletByLocation(scanner.nextLine().trim());
            } catch (OutletNotFoundException ex) {
                System.out.println("An error has occurred while finding the Outlet: " + ex.getMessage() + "\n");
            }
            List<TransitDriverDispatchRecord> transits = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitDriverDispatchRecordForCurrentDay(currentDate, outlet);

            for (TransitDriverDispatchRecord transit : transits) {
                System.out.printf("\n%3s%10s%20s%15s%20s%15s%20s$15s", index + ".", transit.getId(),
                        transit.getEmployee(), transit.getStartDateTime(), transit.getPickupLocation(),
                        transit.getEndDateTime(), transit.getReturnLocation(), transit.getStatus());
                index++;
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

    public void doAssignDriver() {
        Scanner scanner = new Scanner(System.in);
        // may remove in the future
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        Date currentDate;
        Outlet outlet = new Outlet();
        TransitDriverDispatchRecord transit = new TransitDriverDispatchRecord();
        Employee employee = new Employee();
        int index = 1;

        try {
            System.out.println("*** Management System :: Sales Management :: View Transit Driver Dispatch Records For Current Day Reservations ***\n");
            // temporary solution: will probably retrieve date from the system in the future. Singleton...?
            System.out.println("Enter Today's Date (dd/mm/yyyy)> ");
            currentDate = inputDateFormat.parse(scanner.nextLine().trim());
            // unsure if outlet is an input or take from employee 
            System.out.println("Enter Outlet> ");
            // need to improve error handling here
            try {
                outlet = outletSessionBeanRemote.retrieveOutletByLocation(scanner.nextLine().trim());
            } catch (OutletNotFoundException ex) {
                System.out.println("An error has occurred while finding the Outlet: " + ex.getMessage() + "\n");
            }

            // need check if record is from outlet
            System.out.println("Enter Transit Record Id> ");
            try {
                transit = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitDriverDispatchRecordById(scanner.nextLong(), outlet);
            } catch (TransitDriverDispatchRecordNotFromOutletException ex) {
                System.out.println("An error has occurred while finding the Transit Record: " + ex.getMessage() + "\n");
            } catch (TransitDriverDispatchRecordNotFoundException ex) {
                System.out.println("An error has occurred while finding the Transit Record: " + ex.getMessage() + "\n");
            }

            System.out.println("Enter Employee Id> ");
            try {
                employee = employeeSessionBeanRemote.retrieveEmployeeByEmployeeId(scanner.nextLong());
            } catch (EmployeeNotFoundException ex) {
                System.out.println("An error has occurred while finding the Employee: " + ex.getMessage() + "\n");
            }

            transitDriverDispatchRecordSessionBeanRemote.assignTransitDriver(transit.getId(), employee);
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

    public void doUpdateTransitAsCompleted() {
        Scanner scanner = new Scanner(System.in);
        TransitDriverDispatchRecord transit = new TransitDriverDispatchRecord();
        Outlet outlet = new Outlet();

        System.out.println("*** Management System :: Sales Management :: Update Transit as Completed ***\n");
        System.out.println("Enter Outlet> ");
        // need to improve error handling here
        try {
            outlet = outletSessionBeanRemote.retrieveOutletByLocation(scanner.nextLine().trim());
        } catch (OutletNotFoundException ex) {
            System.out.println("An error has occurred while finding the Outlet: " + ex.getMessage() + "\n");
        }
        System.out.println("Enter Transit Record Id> ");
        try {
            transit = transitDriverDispatchRecordSessionBeanRemote.retrieveTransitDriverDispatchRecordById(scanner.nextLong(), outlet);
        } catch (TransitDriverDispatchRecordNotFromOutletException ex) {
            System.out.println("An error has occurred while finding the Transit Record: " + ex.getMessage() + "\n");
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            System.out.println("An error has occurred while finding the Transit Record: " + ex.getMessage() + "\n");
        }

        transitDriverDispatchRecordSessionBeanRemote.updateTransitAsCompleted(transit.getId());
    }

    public void doPickUpCar() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Pickup Car ***\n");
        System.out.println("Enter Customer Mobile Number> ");
        // need exception
        Integer mobileNumber = scanner.nextInt();
        Customer customer = customerSessionBeanRemote.retrieveCustomerByMobileNumber(mobileNumber);
        List<Reservation> reservations = customer.getReservations();
        Reservation selectedReservation;
        // bad styling. need to change
        Car reservedCar = new Car();

        System.out.print("Choose reservation> ");
        for (Reservation r : customer.getReservations()) {
            System.out.println(i + ". " + r.getId());
            i++;
        }
        while (true) {
            Integer reservationChoice = scanner.nextInt();

            // need to check if reservation is still active
            if (reservationChoice <= reservations.size() && reservationChoice > 0) {
                selectedReservation = reservations.get(reservationChoice - 1);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        if (selectedReservation.getPaymentStatus() == "false") { // convert to boolean?
            // request for payment using credit card classes
        } else { // payment has been made

            try {
                reservedCar = carSessionBeanRemote.retrieveCarById(selectedReservation.getCar().getCarId());
            } catch (CarNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            // check car status enum. should be "on rental"
            carSessionBeanRemote.updateCarStatusLocation(reservedCar, CarStatusEnum.DISABLED, customer.getMobileNumber()); // using mobile number to uniquely identify customer
            System.out.println("Car " + reservedCar.getPlateNumber() + " for Reservation " + selectedReservation.getId() + "has been picked up!\n");
        }
    }

    public void doReturnCar() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Return Car ***\n");
        System.out.println("Enter Customer Mobile Number> ");
        // need exception
        Integer mobileNumber = scanner.nextInt();
        Customer customer = customerSessionBeanRemote.retrieveCustomerByMobileNumber(mobileNumber);
        List<Reservation> reservations = customer.getReservations();
        Reservation selectedReservation;// bad styling. need to change
        Car reservedCar = new Car();

        System.out.print("Choose reservation> ");
        for (Reservation r : customer.getReservations()) {
            System.out.println(i + ". " + r.getId());
            i++;
        }
        while (true) {
            Integer reservationChoice = scanner.nextInt();

            // need to check if reservation is still active and has been picked up
            if (reservationChoice <= reservations.size() && reservationChoice > 0) {
                selectedReservation = reservations.get(reservationChoice - 1);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        try {
            reservedCar = carSessionBeanRemote.retrieveCarById(selectedReservation.getCar().getCarId());
        } catch (CarNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
// need to check CarStatusEnum. Supposed to be "in outlet"
        carSessionBeanRemote.updateCarStatusLocation(reservedCar, CarStatusEnum.TRANSIT, selectedReservation.getReturnLocation().toString()); // using mobile number to uniquely identify customer
        System.out.println("Car " + reservedCar.getPlateNumber() + " for Reservation " + selectedReservation.getId() + "has been returned!\n");

    }
}
