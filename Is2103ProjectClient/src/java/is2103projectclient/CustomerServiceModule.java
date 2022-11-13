/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103projectclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateRecordSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.Customer;
import entity.Employee;
import entity.Reservation;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeRoleEnum;
import util.enumeration.ReservationPaymentEnum;
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidEmployeeRoleException;

/**
 *
 * @author dorothyyuan
 */
public class CustomerServiceModule {

    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;

    private Employee employee;

    public CustomerServiceModule() {
    }

    public CustomerServiceModule(ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote,
            Employee employee, CategorySessionBeanRemote categorySessionBeanRemote, RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this();
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.employee = employee;
        this.categorySessionBeanRemote = categorySessionBeanRemote;
        this.rentalRateRecordSessionBeanRemote = rentalRateRecordSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    }

    public void menuCustomerServiceModule() throws InvalidEmployeeRoleException {
        if (employee.getRole() == EmployeeRoleEnum.CUSTOMER_SERVICE_EXECUTIVE) {

            Scanner scanner = new Scanner(System.in);
            Integer response = 0;

            while (true) {
                System.out.println("\n\n*** Management System :: Customer Service :: Customer Service Executive ***\n");
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

                if (response == 3) {
                    break;
                }
            }
        } else {
            throw new InvalidEmployeeRoleException("You don't have EMPLOYEE rights to access the Customer Service module.");
        }
    }

    //CUSTOMER SERVICE EXEC METHODS
    public void doPickUpCar() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Pickup Car ***\n");
        System.out.println("Enter Customer Mobile Number> ");
        // need exception
        String mobileNumber = scanner.nextLine().trim();
        try {
            System.out.println("Trying to retrive by mobile number");
            Customer customer = customerSessionBeanRemote.retrieveCustomerByMobileNumber(mobileNumber);
            System.out.println("Trying to retrive customer's reservations");

            List<Reservation> reservations = customer.getReservations();

            Reservation selectedReservation;
            // bad styling. need to change
            Car reservedCar = new Car();

            System.out.print("Choose reservation> ");
            System.out.printf("\n\n%3s%14s%30s%30s%20s%14s%14s", "S/N", "Car Plate Number", "Start Time", "End Time", "Requirements", "Pick up Location", "Return Location");

            for (Reservation r : customer.getReservations()) {
                System.out.printf("\n%3s%14s%30s%30s%20s%14s%14s", i + ". ", r.getCar().getPlateNumber(), r.getStartDateTime().toString(), r.getEndDateTime().toString(),
                        r.getRequirements().toString(), r.getPickUpLocation().getName(), r.getReturnLocation().getName());
                i++;
            }
            while (true) {
                System.out.println("> ");

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
                carSessionBeanRemote.updateCarStatusLocation(reservedCar, CarStatusEnum.RESERVED, customer.getMobileNumber()); // using mobile number to uniquely identify customer
                System.out.println("Car " + reservedCar.getPlateNumber() + " for Reservation " + selectedReservation.getId() + " has been picked up!\n");
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
            System.out.printf("\n\n%3s%14s%30s%30s%20s%14s%14s", "S/N", "Car Plate Number", "Start Time", "End Time", "Requirements", "Pick up Location", "Return Location");

            for (Reservation r : customer.getReservations()) {
                System.out.printf("\n%3s%14s%30s%30s%20s%14s%14s", i + ". ", r.getCar().getPlateNumber(), r.getStartDateTime().toString(), r.getEndDateTime().toString(),
                        r.getRequirements().toString(), r.getPickUpLocation().getName(), r.getReturnLocation().getName());
                i++;
            }
            while (true) {
                System.out.println("> ");
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
            carSessionBeanRemote.updateCarStatusLocation(reservedCar, CarStatusEnum.AVAILABLE, selectedReservation.getReturnLocation().getName()); // using mobile number to uniquely identify customer
            selectedReservation.setReservationPaymentEnum(ReservationPaymentEnum.COMPLETED);
            reservationSessionBeanRemote.merge(selectedReservation);
            System.out.println("Car " + reservedCar.getPlateNumber() + " for Reservation " + selectedReservation.getId() + " has been returned!\n");

        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
