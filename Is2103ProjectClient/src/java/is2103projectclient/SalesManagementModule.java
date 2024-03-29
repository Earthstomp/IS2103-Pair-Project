package is2103projectclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateRecordSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.Model;
import entity.Category;
import entity.Customer;
import entity.Employee;
import entity.Outlet;
import entity.RentalRateRecord;
import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeRoleEnum;
import util.enumeration.RentalRateEnum;
import util.enumeration.ReservationPaymentEnum;
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCarException;
import util.exception.DeleteModelException;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidEmployeeRoleException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRateRecordNotFoundException;
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
    private RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote;

    private Employee employee;

    public SalesManagementModule() {
    }

    public SalesManagementModule(ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote,
            Employee employee, CategorySessionBeanRemote categorySessionBeanRemote, RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote) {
        this();
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.employee = employee;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.rentalRateRecordSessionBeanRemote = rentalRateRecordSessionBeanRemote;
    }

    public void menuSalesManagementModule() throws InvalidEmployeeRoleException {

        if (employee.getRole() == EmployeeRoleEnum.OPERATIONS_MANAGER) {

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;

            while (true) {
                System.out.println("\n\n*** Management System :: Sales Management :: Operations Manager***\n");
                System.out.println("1: Create New model");
                System.out.println("2: View All Models");
                System.out.println("3: Update Model");
                System.out.println("4: Delete Model");
                System.out.println("5: Create New Car");
                System.out.println("6: View All Cars");
                System.out.println("7: View Car Details");
                System.out.println("8: Update Car");
                System.out.println("9: Delete Car");
                System.out.println("10: View Transit Dispatch Records for Current Day Reservations");
                System.out.println("11: Assign Transit Driver");
                System.out.println("12: Update Transit As Completed");
                System.out.println("13: Pickup Car");
                System.out.println("14: Return Car");
                System.out.println("15: Back\n");
                response = 0;

                while (response < 1 || response > 14) {
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
                    } else if (response == 15) {
                        break;

                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 15) {
                    break;
                }
            }
        } else if (employee.getRole() == EmployeeRoleEnum.SALES_MANAGER) {

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;

            while (true) {
                System.out.println("\n\n*** Management System :: Sales Management :: Sales Manager***\n");
                System.out.println("1: Create Rental Rate");
                System.out.println("2: View All Rental Rates");
                System.out.println("3: View Rental Rate Details");
                System.out.println("4: Update Rental Rate");
                System.out.println("5: Delete Rental Rate");
                System.out.println("6: Back\n");
                response = 0;

                while (response < 1 || response > 6) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doCreateRentalRate();
                    } else if (response == 2) {
                        doViewAllRentalRates();
                    } else if (response == 3) {
                        doViewRentalRateDetails();
                    } else if (response == 4) {
                        doUpdateRentalRate();
                    } else if (response == 5) {
                        doDeleteRentalRate();
                    } else if (response == 6) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 6) {
                    break;
                }

            }
        } else if (employee.getRole() == EmployeeRoleEnum.CUSTOMER_SERVICE_EXECUTIVE) {

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;

            while (true) {
                System.out.println("\n\n*** Management System :: Sales Management :: Sales Manager ***\n");
                System.out.println("1: Pick Up Car");
                System.out.println("2: Return Car");
                System.out.println("3: Back\n");
                response = 0;

                while (response < 1 || response > 3) {
                    System.out.print("> ");

                    response = scanner.nextInt();

                    if (response == 1) {
                        doPickUpCar();
                    } else if (response == 2) {
                        doReturnCar();
                    } else if (response == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 6) {
                    break;
                }
            }
        } else {
            throw new InvalidEmployeeRoleException("You don't have EMPLOYEE rights to access the Sales Management module.");
        }
    }

    public void doCreateNewModel() {
        Scanner scanner = new Scanner(System.in);
        Model model = new Model();
        boolean categorySet = false;
        int i = 1; //  index of menu

        System.out.println("*** Management System :: Sales Management :: Create New Model ***\n");
        System.out.print("Choose category to create model in> \n");
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        for (Category c : categories) {
            System.out.println(i + ". " + c.getCategoryName());
            i++;
        }
        do {
            System.out.print("> ");
            Integer categoryChoice = scanner.nextInt();

            if (categoryChoice <= categories.size() && categoryChoice > 0) {
                model.setCategory(categories.get(categoryChoice - 1));
                categorySet = true;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        } while (!categorySet);

        scanner.nextLine();
        System.out.println("Enter Make: ");
        model.setMake(scanner.nextLine().trim());

        System.out.println("Enter Model: ");
        model.setModel(scanner.nextLine().trim());

//        try { need to try and catch exception
        Long newModelId = categorySessionBeanRemote.createNewModelWithExistingCategoryClass(model, model.getCategory());
        System.out.println("New model created successfully!: " + model.getMake() + " " + model.getModel() + "\n");
    }

    public void doViewAllModels() {
        List<Model> models = modelSessionBeanRemote.viewAllModels();
        System.out.printf("\n%3s%16s%15s%15s", "S/N", "Car Category", "Make", "Model");
        int index = 1;
        for (Model model : models) {
            System.out.printf("\n%3s%16s%15s%15s", index + ".", model.getCategory().getCategoryName(), model.getMake(), model.getModel());
            index++;
        }
        System.out.println("\n");
    }

    public void doUpdateModel() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Update Model ***\n");
        System.out.println("Enter Make Name> ");
        String makeName = scanner.nextLine().trim();

        System.out.println("Enter Model Name> ");
        String modelName = scanner.nextLine().trim();
        Model model = modelSessionBeanRemote.retrieveModelByName(makeName, modelName);
        System.out.printf("\n%12s%15s%15s", "Car Category", "Make", "Model");
        System.out.printf("\n%12s%15s%15s", model.getCategory().getCategoryName(), model.getMake(), model.getModel());

        System.out.println("\nEnter Model's New Category> ");
        System.out.print("> ");
        Category category = categorySessionBeanRemote.retrieveCategoryByName(scanner.nextLine().trim());

        model.setCategory(category);
        System.out.println("Enter New Make Name> ");
        model.setMake(scanner.nextLine().trim());

        System.out.println("Enter New Model Name> ");
        model.setModel(scanner.nextLine().trim());

        modelSessionBeanRemote.merge(model);
        category.getModels().add(model);
        categorySessionBeanRemote.merge(category);

        System.out.println("Model updated successfully!");
        System.out.printf("\n%20s%15s%15s", "Car Category", "Make", "Model");
        System.out.printf("\n%20s%15s%15s\n", model.getCategory().getCategoryName(), model.getMake(), model.getModel());
    }

    public void doDeleteModel() {
        Scanner scanner = new Scanner(System.in);
        String confirmDelete = "";

        System.out.println("*** Management System :: Sales Management :: Delete Model ***\n");
        System.out.println("Enter Make: ");
        String make = scanner.nextLine().trim();

        System.out.println("Enter Model: ");
        String model = scanner.nextLine().trim();
//        try { need to try and catch exception
        Model modelToDelete = modelSessionBeanRemote.retrieveModelByName(make, model);
        System.out.printf("\n%12s%15s%15s", "Car Category", "Make", "Model");
        System.out.printf("\n%12s%15s%15s", modelToDelete.getCategory().getCategoryName(), modelToDelete.getMake(), modelToDelete.getModel());

        System.out.print("\nConfirm delete this Model? (Enter 'Y' to delete)> ");
        confirmDelete = scanner.nextLine().trim();

        if (confirmDelete.equals("Y")) {
            try {
                modelSessionBeanRemote.deleteModel(modelToDelete.getModelId());
                System.out.println("Model " + modelToDelete.getMake() + " " + modelToDelete.getModel() + " deleted successfully!");
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
        car.setColor(scanner.nextLine().trim());
        System.out.println("Enter Car Make> ");
        String make = scanner.nextLine().trim();
        System.out.println("Enter Car Model> ");
        String model = scanner.nextLine().trim();

        Model retrievedModel = modelSessionBeanRemote.retrieveModelByName(make, model);
        car.setModel(retrievedModel);
        System.out.println("Enter Car Location> ");
        car.setLocation(scanner.nextLine().trim());

        Long carId = modelSessionBeanRemote.createNewCarWithExistingModel(car, retrievedModel.getModelId());
        System.out.println("New Car created successfully!: " + car.getPlateNumber() + "\n");
    }

    public void doViewAllCars() {
        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("\n%3s%20s%14s%14s%14s", "S/N", "Car Category", "Make", "Model", "Plate Number");
        int index = 1;
        for (Car car : cars) {
            System.out.printf("\n%3s%20s%14s%14s%14s", index + ".", car.getModel().getCategory().getCategoryName(), car.getModel().getMake(), car.getModel().getModel(), car.getPlateNumber());
            index++;
        }
    }

    public void doViewCarDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Management System :: Sales Management :: View Car Details ***\n");
        System.out.println("Enter Car Plate Number> ");
        Car car = carSessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        System.out.printf("\n%15s%14s%14s%20s%15s%15s%20s", "Car Category", "Make", "Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%15s%14s%14s%20s%15s%15s%20s", car.getModel().getCategory().getCategoryName(), car.getModel().getMake(), car.getModel().getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());

    }

    public void doUpdateCar() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Management System :: Sales Management :: Update Car ***\n");
        System.out.println("Enter Car Plate Number> ");
        Car car = carSessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        System.out.printf("\n%12s%14s%14s%20s%15s%20s%10s", "Car Category", "Make", "Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%12s%14s%14s%20s%15s%20s%10s", car.getModel().getCategory().getCategoryName(), car.getModel().getMake(), car.getModel().getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());

        System.out.println("\nEnter Car's New Plate Number> ");
        car.setPlateNumber(scanner.nextLine().trim());
        System.out.println("Enter Car Make> ");
        String make = scanner.nextLine().trim();
        System.out.println("Enter Car Model> ");
        String model = scanner.nextLine().trim();

        Model retrievedModel = modelSessionBeanRemote.retrieveModelByName(make, model);
        System.out.println("found model");
        // need to set
        Model oldModel = car.getModel(); // finding old model
        System.out.println("found model from car");

        oldModel = modelSessionBeanRemote.retrieveModelByName(oldModel.getMake(), oldModel.getModel());

        oldModel.getCars().remove(car);

        System.out.println("removed car");

        car.setModel(retrievedModel); //  setting new model
        System.out.println("set model");

        System.out.println("Enter Car's New Color> ");
        car.setColor(scanner.nextLine().trim());
        System.out.println("Enter Car's New Location> ");
        car.setLocation(scanner.nextLine().trim());
        System.out.println("Enter Car's status (1: enabled or 2: disabled) ");

        boolean statusChosen = false;
        do {
            int status = scanner.nextInt();
            if (status == 1) {
                car.setStatus(CarStatusEnum.AVAILABLE);
                statusChosen = true;
            } else if (status == 2) {
                car.setStatus(CarStatusEnum.DISABLED);
                statusChosen = true;
            } 
        } while (!statusChosen);
                
        // unsure if it is adding managed instance or not
        carSessionBeanRemote.merge(car);
        System.out.println("merged car ");

        retrievedModel.getCars().add(carSessionBeanRemote.retrieveCarByPlateNumber(car.getPlateNumber()));
        modelSessionBeanRemote.merge(retrievedModel);
        System.out.println("Car updated successfully!");
        System.out.printf("\n%12s%14s%14s%20s%15s%20s%10s", "Car Category", "Make", "Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%12s%14s%14s%20s%15s%20s%10s", car.getModel().getCategory().getCategoryName(), car.getModel().getMake(), car.getModel().getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());
    }

    public void doDeleteCar() {
        Scanner scanner = new Scanner(System.in);
        String confirmDelete = "";

        System.out.println("*** Management System :: Sales Management :: Delete Car ***\n");
        System.out.println("Enter Car Plate Number> ");
        Car car = carSessionBeanRemote.retrieveCarByPlateNumber(scanner.nextLine().trim());
        System.out.printf("\n%12s%14s%14s%20s%15s%20s%10s", "Car Category", "Make", "Model", "Plate Number", "Color", "Location", "Enabled Status");
        System.out.printf("\n%12s%14s%14s%20s%15s%20s%10s", car.getModel().getCategory().getCategoryName(), car.getModel().getMake(), car.getModel().getModel(), car.getPlateNumber(),
                car.getColor(), car.getLocation(), car.getStatus());

        System.out.print("\n\nConfirm delete this Car? (Enter 'Y' to delete)> ");
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
                //
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

    //SALES MANAGER METHODS
    public void doCreateRentalRate() {
        Scanner scanner = new Scanner(System.in);
        RentalRateRecord rentalRate = new RentalRateRecord();
        System.out.println("*** Management System :: Sales Management :: Create New Car ***\n");
        System.out.print("Enter Record Name> ");
        rentalRate.setRecordName(scanner.nextLine().trim());
        System.out.println("Choose Record Type> ");

        Integer response = 0;

        System.out.println("1: Default");
        System.out.println("2: Promotion");
        System.out.println("3: Peak");
        response = 0;

        while (response < 1 || response > 3) {
            System.out.print("> ");
            response = scanner.nextInt();
            switch (response) {
                case 1:
                    rentalRate.setType(RentalRateEnum.DEFAULT);
                    break;
                case 2:
                    rentalRate.setType(RentalRateEnum.PROMOTION);
                    break;
                case 3:
                    rentalRate.setType(RentalRateEnum.PEAK);
                    break;
                default:
                    System.out.println("Invalid option, please try again!\n");
                    break;
            }
        }

        System.out.print("Enter Rate > ");
        Double rate = scanner.nextDouble();
        System.out.print("Enter Start Date and Time in DD/MM/YYYY hh:mm > ");
        scanner.nextLine(); // added to consume \n
        String startDateString = scanner.nextLine().trim();
        System.out.print("Enter End Date and Time in DD/MM/YYYY hh:mm > ");
        String endDateString = scanner.nextLine().trim();

        Date startDate = new Date();
        SimpleDateFormat dateF = new SimpleDateFormat("d/M/y h:m");
        try {
            startDate = dateF.parse(startDateString);
            System.out.println("Entered Start Date is: " + startDate.toString());
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }

        Date endDate = new Date();
        try {
            endDate = dateF.parse(endDateString);
            System.out.println("Entered End Date is: " + endDate.toString());
        } catch (Exception e) {
            System.out.println("Exception: " + e.toString());
        }

        List<Date> validityPeriod = Arrays.asList(startDate, endDate);
        rentalRate.setValidityPeriod(validityPeriod);

        Long rentalRateRecordId = rentalRateRecordSessionBeanRemote.createRentalRateRecord(rentalRate);
        System.out.println("New Rental Rate Record created successfully!: " + rentalRate.getRecordName() + "\n");
    }

    public void doViewAllRentalRates() {
        List<RentalRateRecord> rentalRateRecords = rentalRateRecordSessionBeanRemote.retrieveAllRateRecords();
        System.out.printf("\n%3s%20s%14s%14s%14s", "S/N", "Record Name", "Type", "Category", "Start", "End");
        int index = 1;
        for (RentalRateRecord r : rentalRateRecords) {
            System.out.printf("\n%3s%20s%14s%14s%14s%14s", index + ".", r.getRecordName(), r.getType().toString(), r.getCategory(), r.getValidityPeriod().get(0), r.getValidityPeriod().get(1));
            index++;
        }
    }

    public void doViewRentalRateDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Management System :: Sales Management :: View Rental Rate Details ***\n");
        System.out.println("Enter Rental Rate Name > ");
        try {
            RentalRateRecord r = rentalRateRecordSessionBeanRemote.retrieveRentalRateRecordByName(scanner.nextLine().trim());
            System.out.printf("\n%20s%14s%14s%14s", "Record Name", "Type", "Category", "Start", "End");
            System.out.printf("\n%3s%20s%14s%14s%14s%14s", r.getRecordName(), r.getType().toString(), r.getCategory(), r.getValidityPeriod().get(0), r.getValidityPeriod().get(1));
        } catch (RentalRateRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void doUpdateRentalRate() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** Management System :: Sales Management :: Update Rental Rate ***\n");

        try {
            RentalRateRecord rentalRateRecord = rentalRateRecordSessionBeanRemote.retrieveRentalRateRecordByName(scanner.nextLine().trim());
            System.out.printf("\n%20s%14s%14s%14s", "Record Name", "Type", "Category", "Start", "End");
            System.out.printf("\n%3s%20s%14s%14s%14s%14s", rentalRateRecord.getRecordName(), rentalRateRecord.getType().toString(), rentalRateRecord.getCategory(), rentalRateRecord.getValidityPeriod().get(0), rentalRateRecord.getValidityPeriod().get(1));

            System.out.println("\nEnter Rental Rate Record Name> ");
            rentalRateRecord.setRecordName(scanner.nextLine().trim());
            System.out.println("Enter Rental Rate Record Rate> ");
            rentalRateRecord.setRate(scanner.nextDouble());
            System.out.print("Enter new Start Date and Time in DD/MM/YYYY hh:mm > ");
            scanner.nextLine(); // added to consume \n
            String startDateString = scanner.nextLine().trim();
            System.out.print("Enter new End Date and Time in DD/MM/YYYY hh:mm > ");
            String endDateString = scanner.nextLine().trim();

            Date startDate = new Date();
            SimpleDateFormat dateF = new SimpleDateFormat("d/M/y h:m");
            try {
                startDate = dateF.parse(startDateString);
                System.out.println("Entered Start Date is: " + startDate.toString());
            } catch (Exception e) {
                System.out.println("Exception: " + e.toString());
            }

            Date endDate = new Date();
            try {
                endDate = dateF.parse(endDateString);
                System.out.println("Entered End Date is: " + endDate.toString());
            } catch (Exception e) {
                System.out.println("Exception: " + e.toString());
            }

            List<Date> newValidityPeriod = Arrays.asList(startDate, endDate);
            rentalRateRecord.setValidityPeriod(newValidityPeriod);

            System.out.println("Enter Rental Rate Record Category> ");
            String newCategory = scanner.nextLine().trim();
            Category retrievedCategory = categorySessionBeanRemote.retrieveCategoryByName(newCategory);
            Category oldCategory = rentalRateRecord.getCategory();
            oldCategory = categorySessionBeanRemote.retrieveCategoryByName(oldCategory.getCategoryName());
            oldCategory.getRateRecords().remove(rentalRateRecord);
            rentalRateRecord.setCategory(retrievedCategory);
            System.out.println("Enter Rental Rate Record's status (true: enabled or false: disabled) ");
            rentalRateRecord.setEnabled(scanner.nextBoolean());

            // unsure if it is adding managed instance or not
            rentalRateRecordSessionBeanRemote.merge(rentalRateRecord);

            retrievedCategory.getRateRecords().add(rentalRateRecordSessionBeanRemote.retrieveRentalRateRecordByName(rentalRateRecord.getRecordName()));
            categorySessionBeanRemote.merge(retrievedCategory);
            System.out.println("Rental Rate Record updated successfully!");
            System.out.printf("\n%20s%14s%14s%14s", "Record Name", "Type", "Category", "Start", "End");
            System.out.printf("\n%3s%20s%14s%14s%14s%14s", rentalRateRecord.getRecordName(), rentalRateRecord.getType().toString(), rentalRateRecord.getCategory(), rentalRateRecord.getValidityPeriod().get(0), rentalRateRecord.getValidityPeriod().get(1));

        } catch (RentalRateRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void doDeleteRentalRate() {
        Scanner scanner = new Scanner(System.in);
        String confirmDelete = "";

        System.out.println("*** Management System :: Sales Management :: Delete Rental Rate ***\n");
        System.out.println("Enter Rental Rate Name> ");
        try {
            RentalRateRecord rentalRateRecord = rentalRateRecordSessionBeanRemote.retrieveRentalRateRecordByName(scanner.nextLine().trim());
            System.out.printf("\n%20s%14s%14s%14s", "Record Name", "Type", "Category", "Start", "End");
            System.out.printf("\n%3s%20s%14s%14s%14s%14s", rentalRateRecord.getRecordName(), rentalRateRecord.getType().toString(), rentalRateRecord.getCategory(), rentalRateRecord.getValidityPeriod().get(0), rentalRateRecord.getValidityPeriod().get(1));

            System.out.print("\n\nConfirm delete this Record? (Enter 'Y' to delete)> ");
            confirmDelete = scanner.nextLine().trim();

            if (confirmDelete.equals("Y")) {
                // try { add DeleteRecordException
                rentalRateRecordSessionBeanRemote.removeRentalRateRecord(rentalRateRecord.getId());
                System.out.println("Rental Rate Record " + rentalRateRecord.getRecordName() + " deleted successfully!");
            } else {
                System.out.println("Delete cancelled!\n");
            }
        } catch (RentalRateRecordNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //CUSTOMER SERVICE EXEC METHODS
    public void doPickUpCar() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Pickup Car ***\n");
        System.out.println("Enter Customer Mobile Number> ");
        // need exception
        String mobileNumber = scanner.nextLine();
        try {
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
            if (selectedReservation.getReservationPaymentEnum().equals("ATPICKUP")) {
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
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void doReturnCar() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Return Car ***\n");
        System.out.println("Enter Customer Mobile Number> ");
        // need exception
        String mobileNumber = scanner.nextLine();
        try {
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
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
