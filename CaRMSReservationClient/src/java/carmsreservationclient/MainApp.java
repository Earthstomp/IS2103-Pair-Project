/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CreditCardSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateRecordSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.Outlet;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialsException;
import util.exception.OutletNotFoundException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import entity.Category;
import entity.CreditCard;
import entity.Customer;
import entity.RentalRateRecord;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.InputMismatchException;
import util.enumeration.CarStatusEnum;
import util.enumeration.ReservationPaymentEnum;
import util.exception.CarNotFoundException;
import util.exception.CustomerExistsException;
import util.exception.ModelNotFoundException;
import util.exception.ReservationNotFoundException;

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
    private CreditCardSessionBeanRemote creditCardSessionBeanRemote;

    private Customer customer;
    private List<Reservation> reservationsToUnassign;

    public MainApp() {
        reservationsToUnassign = new ArrayList<>();
    }

    public MainApp(ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote,
            CategorySessionBeanRemote categorySessionBeanRemote, RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote,
            CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CreditCardSessionBeanRemote creditCardSessionBeanRemote) {
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
        this.creditCardSessionBeanRemote = creditCardSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("\n\n*** Welcome to CaRMS Reservation Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register as Customer");
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doLogin();
                    System.out.println("Login successful!\n");
                    menuMain();
                } else if (response == 2) {
                    doRegistration();
                } else if (response == 3) {
                    doSearchCar();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }
    }

    private void doLogin() {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CaRMS Reservation Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        try {
            customer = customerSessionBeanRemote.customerLogin(username, password);
        } catch (InvalidLoginCredentialsException | InputMismatchException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void doRegistration() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter mobile number> ");
        String mobileNumber = scanner.nextLine().trim();

        System.out.print("Enter passport number> ");
        String passportNumber = scanner.nextLine().trim();

        System.out.print("Enter email> ");
        String email = scanner.nextLine().trim();

        System.out.print("Credit card number>");
        String num = scanner.nextLine().trim();

        Customer customer = new Customer(mobileNumber, passportNumber, email, username, password);

        try {
            Long cardId = creditCardSessionBeanRemote.createNewCard(new CreditCard(num));
            CreditCard card = creditCardSessionBeanRemote.retrieveCardById(cardId);
            Long customerId = customerSessionBeanRemote.createNewCustomer(customer, card);
        } catch (CustomerExistsException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void doSearchCar() {
        Scanner scanner = new Scanner(System.in);

        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            Date pickUpDate;
            Date returnDate;
            Outlet pickUpOutlet = new Outlet();
            Outlet returnOutlet = new Outlet();

            System.out.println("*** Reservation Client :: Search Car ***\n");
            System.out.println("Enter Pickup Outlet> ");
            try {
                pickUpOutlet = outletSessionBeanRemote.retrieveOutletByLocation(scanner.nextLine().trim());
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Enter Return Outlet> ");
            try {
                returnOutlet = outletSessionBeanRemote.retrieveOutletByLocation(scanner.nextLine().trim());
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
            }

            System.out.println("Enter Pickup Date and Time (dd/mm/yyyy hh:mm a)> ");
            pickUpDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.println(pickUpDate);

            System.out.println("Enter Return Date and Time (dd/mm/yyyy hh:mm a)> ");
            returnDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.println(returnDate);

            testReservationValidity(pickUpOutlet, returnOutlet, pickUpDate, returnDate);

        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Reservation Client ***\n");
            System.out.println("You are logged in as " + customer.getUsername());
            System.out.println("1: Search Car");
            System.out.println("2: Cancel Reservation");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Log Out\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchCar();

                } else if (response == 2) {
                    doCancelReservation();
                } else if (response == 3) {
                    doViewReservationDetails();
                } else if (response == 4) {
                    doViewAllMyReservations();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 5) {
                break;
            }
        }
    }

    private void doCancelReservation() {
        try {
            List<Reservation> reservations = reservationSessionBeanRemote.retrieveAllReservationsForCustomer(customer);
            System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s%15s", "S/N", "Start Date", "Pick Up Location", "End Date", "Return Location", "Payment Status", "Make", "Model");
            int index = 1;
            for (Reservation reservation : reservations) {
                System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s%15s", index + ".", reservation.getStartDateTime(), reservation.getPickUpLocation().getName(), reservation.getEndDateTime(), reservation.getReturnLocation().getName(), reservation.getReservationPaymentEnum(), reservation.getRequirements().get(0), reservation.getRequirements().get(1));
                index++;
            }
            System.out.println("\n");
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nSelect Reservation to cancel by S/N > ");
            Long reservationId = reservations.get(scanner.nextInt() - 1).getId();
            System.out.println("\nEnter \'Y' to confirm cancellation> ");
            if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                reservationSessionBeanRemote.removeReservation(reservationId);
            } else {
                System.out.println("Cancellation revoked.");
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doViewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nSelect Reservation to view by S/N > ");

        try {
            List<Reservation> reservations = reservationSessionBeanRemote.retrieveAllReservationsForCustomer(customer);
            for (Reservation r : reservations) {
                System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s%15s", "S/N", "Start Date", "Pick Up Location", "End Date", "Return Location", "Payment Status", "Make", "Model");
                System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s%15s", 1 + ".", r.getStartDateTime(), r.getPickUpLocation().getName(), r.getEndDateTime(), r.getReturnLocation().getName(), r.getReservationPaymentEnum(), r.getRequirements().get(0), r.getRequirements().get(1));

                System.out.println("\n");
            }
            int sn = scanner.nextInt();
            Long reservationId = reservations.get(sn - 1).getId();
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationById(reservationId);

            System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s%15s", "S/N", "Start Date", "Pick Up Location", "End Date", "Return Location", "Payment Status", "Make", "Model");
            System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s%15s", sn + ".", reservation.getStartDateTime(), reservation.getPickUpLocation().getName(), reservation.getEndDateTime(), reservation.getReturnLocation().getName(), reservation.getReservationPaymentEnum(), reservation.getRequirements().get(0), reservation.getRequirements().get(1));

            System.out.println("\n");
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doViewAllMyReservations() {
        try {
            List<Reservation> reservations = reservationSessionBeanRemote.retrieveAllReservationsForCustomer(customer);
            System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s", "S/N", "Start Date", "Pick Up Location", "End Date", "Return Location", "Payment Status", "Category");
            int index = 1;
            for (Reservation reservation : reservations) {
                System.out.printf("\n%3s%35s%20s%35s%20s%15s%15s", index + ".", reservation.getStartDateTime(), reservation.getPickUpLocation().getName(), reservation.getEndDateTime(), reservation.getReturnLocation().getName(), reservation.getReservationPaymentEnum(), reservation.getRequirements().get(0));
                index++;
            }
            System.out.println("\n");
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void testReservationValidity(Outlet pickUpOutlet, Outlet returnOutlet, Date pickUpDate, Date returnDate) {
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        List<Boolean> categoryAvailabilities = new ArrayList<Boolean>(); // not sure why small b does not work
        List<BigDecimal> rentalFees = new ArrayList<BigDecimal>();

        // assign currently unassigned reservations to system so system can check for most updated availabiliry
        assignReservations();
        int index = 1;
        System.out.printf("\n%3s%16s%15s%15s", "S/N", "Car Category", "Available?", "Rental Fee");
        for (Category category : categories) {
            // was an error here need to check
            boolean availability = assignReservationByCategory(category, pickUpOutlet, returnOutlet, pickUpDate, returnDate);
            BigDecimal rentalFee = calculateRentalFee(category, pickUpDate, returnDate);
            System.out.printf("\n%3s%16s%15s%15s", category.getCategoryId(), category.getCategoryName(), availability, rentalFee);
            categoryAvailabilities.add(availability);
            rentalFees.add(rentalFee);
            index++;

        }
        unassignReservations();
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            response = 0;

            System.out.println("\n1: Reserve a Car");
            System.out.println("2: Back");

            while (response < 1 || response > 2) {
                System.out.print("> ");
                response = 0;
                response = scanner.nextInt();

                if (response == 1) {
                    if (customer == null) {
                        System.out.println("You need to log in to reserve a car> ");
                        break;
                    }
                    scanner.nextLine();
                    System.out.println("Enter S/N of Category of Car you would like to reserve> ");
                    int chosenCategorySn = scanner.nextInt();

                    if (categoryAvailabilities.get(chosenCategorySn - 1) == false) {
                        System.out.println("Cars of chosen Category cannot be reserved> ");
                        break;
                    } else {
                        System.out.println("1: Pay Online");
                        System.out.println("2: Pay At Pick up ");

                        int paymentMethod = scanner.nextInt();

                        if (paymentMethod == 1) {
//                            System.out.println("getting rental fee " + rentalFees.get(chosenCategorySn - 1));
                            System.out.println("Reservation paid for! " + rentalFees.get(chosenCategorySn - 1) + " charged to your credit card " + customer.getCreditCard().getSerialNumber());
                            creditCardSessionBeanRemote.makePayment(rentalFees.get(chosenCategorySn - 1), customer.getCreditCard().getId());
                            customerSessionBeanRemote.createNewReservation(new Reservation(pickUpDate, returnDate, pickUpOutlet, returnOutlet, ReservationPaymentEnum.PAID, customer, categories.get(chosenCategorySn - 1).getCategoryName()), customer.getCustomerId());
                            break;
                        } else { // 2
                            System.out.println("Reservation paid at Pick up! Saving your credit card: " + customer.getCreditCard().getSerialNumber());
                            customerSessionBeanRemote.createNewReservation(new Reservation(pickUpDate, returnDate, pickUpOutlet, returnOutlet, ReservationPaymentEnum.ATPICKUP, customer, categories.get(chosenCategorySn - 1).getCategoryName()), customer.getCustomerId());
                            break;
                        }
                    }
                    //doReserveCar();
                    // need more code here
                }

                if (response == 2) {
                    break;
                }
            }
            break;

        }
    }
    // assign any unassigned reservations to update current system. should be successful for all

    private void assignReservations() {
        //System.out.println("Starting assigning reservations");
        List<Reservation> reservations = new ArrayList<>();
        // retrieve all reservations on todays date
        try {
            reservations = reservationSessionBeanRemote.retrieveReservationByDate(new Date());
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        // copying list of reservations to be unassigned later
        for (Reservation reservation : reservations) {
            reservationsToUnassign.add(reservation);
            // car has not been assigned to reservation yet, so car is null
            //System.out.println("Added reservation id " + reservation.getId() + " to unassign");
        }

        for (Reservation chosenReservation : reservations) {
            Date startDateTime = chosenReservation.getStartDateTime();
            Date endDateTime = chosenReservation.getEndDateTime();
            List<String> requirements = chosenReservation.getRequirements();
            Outlet pickUpOutlet = chosenReservation.getPickUpLocation();
            Outlet returnOutlet = chosenReservation.getReturnLocation();
            List<Car> carsAvailable = new ArrayList<Car>();

            // filter out unavailable cars, disabled cars, cars that cannot be returned on time
            if (requirements.size() == 1) { // requires car from specific category
                carsAvailable = categorySessionBeanRemote.retrieveAllCarsFromCategory(requirements.get(0));
            } else // require car from specific make, model
            {
                try {
                    carsAvailable = modelSessionBeanRemote.retrieveAllCarsFromModel(requirements.get(0), requirements.get(1));
                } catch (ModelNotFoundException | CarNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            }

            /*for (Car car : carsAvailable) {
                System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
//                System.out.println("Reservations");
                for (Reservation r : car.getReservations()) {
//                    System.out.println("Reservation " + r.getId());
                }
            }*/
            Calendar calendar = Calendar.getInstance();
            // setting time if transit is possible
            calendar.setTime(startDateTime);
            calendar.add(Calendar.HOUR, -2);
            Date startTimeForTransit = calendar.getTime();
            List<Car> carsAvailableFiltered = new ArrayList<Car>();
            // setting a boolean to check if the reservation led to the car being removed
            boolean reservationRemovedCar;
            // setting a date for initial comparison
            calendar.add(Calendar.DAY_OF_YEAR, -300);
            Date latestReservationTiming = calendar.getTime();
            //System.out.println("The latest reservation timing now is " + latestReservationTiming);
            // keeping the latest reservation
            Reservation latestReservation = new Reservation();
            // to track if the latestReservation was updated
            boolean updatedLatestReservation = false;
            // to track if the input reservation was assigned
            boolean assigned = false;
            // a car that can be assigned with a transit, when there is no car at the outlet to be picked up at
            Car carForAssignmentWithTransit = new Car();
            // true if reservation requires a transit dispatch record

            for (Car c : carsAvailable) {
                carsAvailableFiltered.add(c);
            }

            for (int i = 0; i < carsAvailable.size(); i++) {
                Car c = carsAvailable.get(i);
                //System.out.println("Looking at car " + c.getPlateNumber());
                if (c.getStatus() == CarStatusEnum.DISABLED || c.getStatus() == CarStatusEnum.SERVICING_OR_REPAIR) { //  error is either here or next line
                    //System.out.println("Car is removed cause it is disabled or servicing");
                    carsAvailableFiltered.remove(c);
                } else {
                    // for every reservation the car has
                    //System.out.println("test reservation");

                    for (Reservation r : c.getReservations()) {
                        reservationRemovedCar = false;
                        //System.out.println(r.getStartDateTime() + " " + r.getEndDateTime());

                        // finding the time for transit for case 4
                        calendar.setTime(r.getStartDateTime());
                        calendar.add(Calendar.HOUR, -2);
                        Date rStartTimeForTransit = calendar.getTime();
                        //System.out.println("test 2");

                        // filter out ongoing reservations
                        if (r.getReservationPaymentEnum() != ReservationPaymentEnum.COMPLETED || r.getReservationPaymentEnum() != ReservationPaymentEnum.CANCELLED) {

                            // 1. if there is a reservation on car that will prevent car from being returned before reservation time
                            if (r.getStartDateTime().before(startDateTime) && r.getEndDateTime().after(startDateTime)) {
                                //System.out.println("Car " + c.getPlateNumber() + " is removed cause case 1");

                                carsAvailableFiltered.remove(c); // problem is here 
                                reservationRemovedCar = true;
                                //System.out.println("removed ! 1");

                                // 2. if there is a reservation on car that returns to another outlet that will prevent car from being returned before reservation time (by accounting for 2hr transit) 
                            } else if (r.getStartDateTime().before(startDateTime) && r.getReturnLocation() != pickUpOutlet && r.getEndDateTime().after(startTimeForTransit)) {
                                //System.out.println("Car is removed cause case 2");

                                carsAvailableFiltered.remove(c);
                                reservationRemovedCar = true;

                                // 3. if there is a reservation that starts between the startDateTime and endDateTime of this reservation
                            } else if (r.getStartDateTime().after(startDateTime) && r.getStartDateTime().before(endDateTime)) {
                                // System.out.println("Car is removed cause case 3");

                                carsAvailableFiltered.remove(c);
                                reservationRemovedCar = true;

                                // 4. if there is a future reservation that starts at another outlet at a time less than 2hrs after this reservation end time
                            } else if (r.getPickUpLocation() != pickUpOutlet && endDateTime.after(rStartTimeForTransit) && (r.getStartDateTime().after(endDateTime))) {
                                //System.out.println("Car is removed cause case 4");

                                carsAvailableFiltered.remove(c);
                                reservationRemovedCar = true;

                            } else {
                                //System.out.println("this reservation " + r.getId() + " has no conflicts with the current reservation");
                            }
                        } // ignore reservations which have been completed or cancelled
                        // if this reservation did not remove the car, and its end time is the latest so far while still before the incoming reservation's start time
                        if (reservationRemovedCar = false && r.getEndDateTime().after(latestReservationTiming) && r.getEndDateTime().before(startDateTime)) {
                            latestReservation = r;
                            updatedLatestReservation = true;
                            //System.out.println("updatedLatestReservation");
                        }
                    }
                    if (c.getReservations().size() == 0) {
                        // if the car has no reservations and is at outlet, assign 
                        if (c.getLocation().toString() == pickUpOutlet.getName().toString()) {
                            //System.out.println("Car " + c.getPlateNumber() + "has no reservations and is assigned to reservation " + chosenReservation.getId());
                            try {
                                reservationSessionBeanRemote.assignCarToReservation(chosenReservation, c);
                            } catch (CarNotFoundException | ReservationNotFoundException ex) {
                                System.out.println(ex.getMessage());
                            }
                            assigned = true;
                        } else { // if car has no reservations but not at outlet, consider for transit
                            //System.out.println("Car that has no reservations can be assigned with transit " + c.getPlateNumber());
                            carForAssignmentWithTransit = c;
                        }
                    }

                    //System.out.println("Done iterating through all reservations for car " + c.getPlateNumber());
                    // if there is a 
                    if (updatedLatestReservation) {
                        // assign car if the it will be at the outlet already
                        if (latestReservation.getReturnLocation() == pickUpOutlet) {
                            //System.out.println("Reservation is being assigned to car " + c.getPlateNumber());

                            try {
                                reservationSessionBeanRemote.assignCarToReservation(chosenReservation, c);
                            } catch (CarNotFoundException | ReservationNotFoundException ex) {
                                System.out.println(ex.getMessage());
                            }
                            assigned = true;
                        } else { // car will not be at the outlet and can be considered for transit
                            //System.out.println("Car that can be assigned with transit " + c.getPlateNumber());

                            carForAssignmentWithTransit = c;
                        }
                    }
                }
            }
            //System.out.println("iterated through all cars");
            if (!assigned && carsAvailableFiltered.size() != 0) {

                // create transit Dispatch record
                calendar.setTime(chosenReservation.getStartDateTime());
                calendar.add(Calendar.HOUR, -2);
                Date transitTime = calendar.getTime();

                try {
                    //System.out.println("assigning car " + carForAssignmentWithTransit.getPlateNumber());
                    reservationSessionBeanRemote.assignCarToReservation(chosenReservation, carForAssignmentWithTransit);
                } catch (CarNotFoundException | ReservationNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }

            }

//            System.out.println("Printing cars available filtered");
//
//            for (Car car : carsAvailableFiltered) {
//                System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
//            }
//            if (carsAvailableFiltered.size() == 0) {
//                System.out.println("No cars found");
//            }
        }

        //System.out.println("Finished assigning reservations");
        // iterated through all reservations and all were able to be assigned
    }

    private boolean assignReservationByCategory(Category category, Outlet pickUpOutlet, Outlet returnOutlet, Date startDateTime, Date endDateTime) {
        //System.out.println("assigning cars of category " + category.getCategoryName());
        String categoryName = category.getCategoryName();
        List<Car> carsAvailable = new ArrayList<Car>();

        carsAvailable = categorySessionBeanRemote.retrieveAllCarsFromCategory(categoryName);

        for (Car car : carsAvailable) {
//            System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
//            System.out.println("Reservations");
            for (Reservation r : car.getReservations()) {
//                System.out.println("Reservation " + r.getId());
            }
        }

        Calendar calendar = Calendar.getInstance();
        // setting time if transit is possible
        calendar.setTime(startDateTime);
        calendar.add(Calendar.HOUR, -2);
        Date startTimeForTransit = calendar.getTime();
        List<Car> carsAvailableFiltered = new ArrayList<Car>();
        // setting a boolean to check if the reservation led to the car being removed
        boolean reservationRemovedCar;
        // setting a date for initial comparison
        calendar.add(Calendar.DAY_OF_YEAR, -300);
        Date latestReservationTiming = calendar.getTime();
        //System.out.println("The latest reservation timing now is " + latestReservationTiming);
        // keeping the latest reservation
        Reservation latestReservation = new Reservation();
        // to track if the latestReservation was updated
        boolean updatedLatestReservation = false;
        // to track if the input reservation was assigned
        boolean assigned = false;
        // a car that can be assigned with a transit, when there is no car at the outlet to be picked up at
        Car carForAssignmentWithTransit = new Car();
        // true if reservation requires a transit dispatch record

        for (Car c : carsAvailable) {
            carsAvailableFiltered.add(c);
        }

        for (int i = 0; i < carsAvailable.size(); i++) {
            Car c = carsAvailable.get(i);
            //System.out.println("Looking at car " + c.getPlateNumber());
            // filter out unavailable cars, disabled cars, cars that cannot be returned on time

            if (c.getStatus() == CarStatusEnum.DISABLED || c.getStatus() == CarStatusEnum.SERVICING_OR_REPAIR) { //  error is either here or next line
                //System.out.println("Car is removed cause it is disabled or servicing");
                carsAvailableFiltered.remove(c);
            } else {
                // for every reservation the car has
                //System.out.println("test reservation");

                for (Reservation r : c.getReservations()) {
                    reservationRemovedCar = false;
                    //System.out.println(r.getStartDateTime() + " " + r.getEndDateTime());

                    // finding the time for transit for case 4
                    calendar.setTime(r.getStartDateTime());
                    calendar.add(Calendar.HOUR, -2);
                    Date rStartTimeForTransit = calendar.getTime();
                    //System.out.println("test 2");

                    // filter out ongoing reservations
                    if (r.getReservationPaymentEnum() != ReservationPaymentEnum.COMPLETED || r.getReservationPaymentEnum() != ReservationPaymentEnum.CANCELLED) {

                        // 1. if there is a reservation on car that will prevent car from being returned before reservation time
                        if (r.getStartDateTime().before(startDateTime) && r.getEndDateTime().after(startDateTime)) {
                            //System.out.println("Car " + c.getPlateNumber() + " is removed cause case 1");

                            carsAvailableFiltered.remove(c); // problem is here 
                            reservationRemovedCar = true;
                            //System.out.println("removed ! 1");

                            // 2. if there is a reservation on car that returns to another outlet that will prevent car from being returned before reservation time (by accounting for 2hr transit) 
                        } else if (r.getStartDateTime().before(startDateTime) && r.getReturnLocation() != pickUpOutlet && r.getEndDateTime().after(startTimeForTransit)) {
                            //System.out.println("Car is removed cause case 2");

                            carsAvailableFiltered.remove(c);
                            reservationRemovedCar = true;

                            // 3. if there is a reservation that starts between the startDateTime and endDateTime of this reservation
                        } else if (r.getStartDateTime().after(startDateTime) && r.getStartDateTime().before(endDateTime)) {
                            //System.out.println("Car is removed cause case 3");

                            carsAvailableFiltered.remove(c);
                            reservationRemovedCar = true;

                            // 4. if there is a future reservation that starts at another outlet at a time less than 2hrs after this reservation end time
                        } else if (r.getPickUpLocation() != pickUpOutlet && endDateTime.after(rStartTimeForTransit) && (r.getStartDateTime().after(endDateTime))) {
                            //System.out.println("Car is removed cause case 4");

                            carsAvailableFiltered.remove(c);
                            reservationRemovedCar = true;

                        } else {
                            //System.out.println("this reservation " + r.getId() + " has no conflicts with the current reservation");
                        }
                    } // ignore reservations which have been completed or cancelled
                    // if this reservation did not remove the car, and its end time is the latest so far while still before the incoming reservation's start time
                    if (reservationRemovedCar = false && r.getEndDateTime().after(latestReservationTiming) && r.getEndDateTime().before(startDateTime)) {
                        latestReservation = r;
                        updatedLatestReservation = true;
                        //System.out.println("updatedLatestReservation");
                    }
                }
                if (c.getReservations().size() == 0) {
                    // if the car has no reservations and is at outlet, assign 
                    if (c.getLocation().toString() == pickUpOutlet.getName().toString()) {
                        //System.out.println("Car " + c.getPlateNumber() + "has no reservations and can be assigned to reservation");
                        // reservationSessionBeanRemote.assignCarToReservation(chosenReservation, c);
                        assigned = true;
                        return true;
                    } else { // if car has no reservations but not at outlet, consider for transit
                        //System.out.println("Car that has no reservations can be assigned with transit " + c.getPlateNumber());
                        carForAssignmentWithTransit = c;
                    }
                }

//                System.out.println("Done iterating through all reservations for car " + c.getPlateNumber());
                // if there is a 
                if (updatedLatestReservation) {
                    // assign car if the it will be at the outlet already
                    if (latestReservation.getReturnLocation() == pickUpOutlet) {
                        //System.out.println("Reservation is being assigned to car " + c.getPlateNumber());

//                        reservationSessionBeanRemote.assignCarToReservation(chosenReservation, c); // !!!!
                        assigned = true;

                        return true;
                    } else { // car will not be at the outlet and can be considered for transit
                        //System.out.println("Car that can be assigned with transit " + c.getPlateNumber());

                        carForAssignmentWithTransit = c;
                    }
                }
            }
        }
        //System.out.println("iterated through all cars");
        if (!assigned && carsAvailableFiltered.size() != 0) {
            return true;
        }

        //System.out.println("Printing cars available filtered");
        for (Car car : carsAvailableFiltered) {
            //System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
        }
        if (carsAvailableFiltered.size() == 0) {
            //System.out.println("No cars found for this timing and outlet");
            return false;
        }

        return true;
    }

    private void unassignReservations() {
        for (Reservation r : reservationsToUnassign) {
//            System.out.println("Unassigning Reservation with ID " + r.getId());
//            System.out.println("Unassigned reservation from car " + r.getCar());
            reservationSessionBeanRemote.unassignReservationFromCar(r);

        }
    }

    public BigDecimal calculateRentalFee(Category category, Date pickUpDate, Date returnDate) {
        Date rentalDate = pickUpDate;
        double rentalFee = 0.0;
        Calendar c = Calendar.getInstance();
        //System.out.println("Calculating fee for category " + category.getCategoryName());

        // loop while date of rental calculation fee is before return date
        while (rentalDate.before(returnDate)) {
            //System.out.println("Rental fee is " + rentalFee);

            List<RentalRateRecord> ratesAvailableOnDate = rentalRateRecordSessionBeanRemote.retrieveAllRateRecordsByDatebyCategory(rentalDate, category);
            if (category.getRateRecords().size() > 0) { // need more errors here to check if record is enabled
                rentalFee += rentalRateRecordSessionBeanRemote.chooseRateRecord(ratesAvailableOnDate).getRate();
            }
            c.setTime(rentalDate);
            c.add(Calendar.DATE, 1);
            rentalDate = c.getTime();
        }
        return new BigDecimal(rentalFee);
    }
}
