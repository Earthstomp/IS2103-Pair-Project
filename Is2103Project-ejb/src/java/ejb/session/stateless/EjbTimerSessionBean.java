package ejb.session.stateless;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import entity.Car;
import entity.Category;
import entity.Model;
import entity.Outlet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

import entity.Reservation;
import entity.TransitDriverDispatchRecord;
import java.util.Calendar;
import util.enumeration.CarStatusEnum;
import util.enumeration.ReservationPaymentEnum;
import util.exception.CarNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;

@Stateless

public class EjbTimerSessionBean implements EjbTimerSessionBeanLocal, EjbTimerSessionBeanRemote {

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    @EJB
    private TransitDriverDispatchRecordSessionBeanLocal transitDriverDispatchRecordSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;

    // For testing purpose, we are allowing the timer to trigger every 8 seconds instead of every day at 8am
    // To trigger the timer once every day at 8am instead, use the following the @Schedule annotation
    // @Schedule(dayOfWeek="*", hour = "8", info = "currentDayCarAllocationTransitDriverDispatchRecordGeneratorTimer")    
    @Schedule(hour = "*", minute = "*", second = "*/30", info = "currentDayCarAllocationTimer")
    public void currentDayCarAllocationTimer() {
        Date timeStamp = new Date(); // new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(
        System.out.println("********** EjbTimerSessionBean.currentDayCarAllocationTimer(): Timeout at " + timeStamp);
        List<Reservation> reservations = new ArrayList<Reservation>();
        List<Car> availableCars = new ArrayList<Car>();

        try {
            reservations = reservationSessionBeanLocal.retrieveReservationByDate(timeStamp);
            System.out.println("********** EjbTimerSessionBean retrieving reservations by date " + timeStamp);
            System.out.println("********** there is a total of " + reservations.size() + " on " + timeStamp);

            System.out.printf("\n%20s%20s%20s%20s%14s%14s", "Customer email", "Start Time", "End Time", "Requirements", "Pick up Location", "Return Location");
            
            for (Reservation reservation : reservations) {
                System.out.printf("\n%20s%20s%20s%20s%14s%14s", reservation.getCustomer().getEmail(), reservation.getStartDateTime().toString(), reservation.getEndDateTime().toString(),
                        reservation.getRequirements().toString(), reservation.getPickUpLocation().getName(), reservation.getReturnLocation().getName());
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println("There are no reservations today!");
        }

        // iterate through all reservations and assign cars
        for (Reservation r : reservations)
            try {
            System.out.println(r.getRequirements());
            allocateCarToReservation(r);
        } catch (CarNotFoundException | ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void allocateCarToReservation(Reservation chosenReservation) throws CarNotFoundException, ReservationNotFoundException {
        // what about cars moving by transit?
        // returns the cars that can be reserved for a single reservation's request
        Date startDateTime = chosenReservation.getStartDateTime();
        Date endDateTime = chosenReservation.getEndDateTime();
        List<String> requirements = chosenReservation.getRequirements();
        Outlet pickUpOutlet = chosenReservation.getPickUpLocation();
        Outlet returnOutlet = chosenReservation.getReturnLocation();
        List<Car> carsAvailable = new ArrayList<Car>();

        // filter out unavailable cars, disabled cars, cars that cannot be returned on time
        if (requirements.size() == 1) { // requires car from specific category
            carsAvailable = categorySessionBeanLocal.retrieveAllCarsFromCategory(requirements.get(0));
        } else // require car from specific make, model
        {
            carsAvailable = modelSessionBeanLocal.retrieveAllCarsFromModel(requirements.get(0), requirements.get(1));
        }

        for (Car car : carsAvailable) {
            System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
            System.out.println("Reservations");
            for (Reservation r : car.getReservations()) {
                System.out.println("Reservation " + r.getId());
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
        System.out.println("The latest reservation timing now is " + latestReservationTiming);
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
            System.out.println("Looking at car " + c.getPlateNumber());
            if (c.getStatus() == CarStatusEnum.DISABLED || c.getStatus() == CarStatusEnum.SERVICING_OR_REPAIR) { //  error is either here or next line
                System.out.println("Car is removed cause it is disabled or servicing");
                carsAvailableFiltered.remove(c);
            } else {
                // for every reservation the car has
                System.out.println("test reservation");

                for (Reservation r : c.getReservations()) {
                    reservationRemovedCar = false;
                    System.out.println(r.getStartDateTime() + " " + r.getEndDateTime());

                    // finding the time for transit for case 4
                    calendar.setTime(r.getStartDateTime());
                    calendar.add(Calendar.HOUR, -2);
                    Date rStartTimeForTransit = calendar.getTime();
                    System.out.println("test 2");

                    // filter out ongoing reservations
                    if (r.getReservationPaymentEnum() != ReservationPaymentEnum.COMPLETED || r.getReservationPaymentEnum() != ReservationPaymentEnum.CANCELLED) {

                        // 1. if there is a reservation on car that will prevent car from being returned before reservation time
                        if (r.getStartDateTime().before(startDateTime) && r.getEndDateTime().after(startDateTime)) {
                            System.out.println("Car " + c.getPlateNumber() + " is removed cause case 1");

                            carsAvailableFiltered.remove(c); // problem is here 
                            reservationRemovedCar = true;
                            System.out.println("removed ! 1");

                            // 2. if there is a reservation on car that returns to another outlet that will prevent car from being returned before reservation time (by accounting for 2hr transit) 
                        } else if (r.getStartDateTime().before(startDateTime) && r.getReturnLocation() != pickUpOutlet && r.getEndDateTime().after(startTimeForTransit)) {
                            System.out.println("Car is removed cause case 2");

                            carsAvailableFiltered.remove(c);
                            reservationRemovedCar = true;

                            // 3. if there is a reservation that starts between the startDateTime and endDateTime of this reservation
                        } else if (r.getStartDateTime().after(startDateTime) && r.getStartDateTime().before(endDateTime)) {
                            System.out.println("Car is removed cause case 3");

                            carsAvailableFiltered.remove(c);
                            reservationRemovedCar = true;

                            // 4. if there is a future reservation that starts at another outlet at a time less than 2hrs after this reservation end time
                        } else if (r.getPickUpLocation() != pickUpOutlet && endDateTime.after(rStartTimeForTransit)) {
                            System.out.println("Car is removed cause case 4");

                            carsAvailableFiltered.remove(c);
                            reservationRemovedCar = true;

                        } else {
                            System.out.println("this reservation " + r.getId() + " has no conflicts with the current reservation");
                        }
                    } // ignore reservations which have been completed or cancelled
                    // if this reservation did not remove the car, and its end time is the latest so far while still before the incoming reservation's start time
                    if (reservationRemovedCar = false && r.getEndDateTime().after(latestReservationTiming) && r.getEndDateTime().before(startDateTime)) {
                        latestReservation = r;
                        updatedLatestReservation = true;
                        System.out.println("updatedLatestReservation");
                    }
                }
                if (c.getReservations().size() == 0) {
                    // if the car has no reservations and is at outlet, assign 
                    if (c.getLocation().toString() == pickUpOutlet.getName().toString()) {
                        System.out.println("Car " + c.getPlateNumber() + "has no reservations and is assigned to reservation " + chosenReservation.getId());
                        reservationSessionBeanLocal.assignCarToReservation(chosenReservation, c);
                        assigned = true;
                    } else { // if car has no reservations but not at outlet, consider for transit
                        System.out.println("Car that has no reservations can be assigned with transit " + c.getPlateNumber());
                        carForAssignmentWithTransit = c;
                    }
                }

                System.out.println("Done iterating through all reservations for car " + c.getPlateNumber());

                // if there is a 
                if (updatedLatestReservation) {
                    // assign car if the it will be at the outlet already
                    if (latestReservation.getReturnLocation() == pickUpOutlet) {
                        System.out.println("Reservation is being assigned to car " + c.getPlateNumber());
                        try {
                            reservationSessionBeanLocal.assignCarToReservation(chosenReservation, c); // !!!!
                        } catch (ReservationNotFoundException | CarNotFoundException ex) {
                            System.out.println(ex.getMessage());
                        }
                        assigned = true;
                    } else { // car will not be at the outlet and can be considered for transit
                        System.out.println("Car that can be assigned with transit " + c.getPlateNumber());

                        carForAssignmentWithTransit = c;
                    }
                }
            }
        }
        System.out.println("iterated through all cars");
        if (!assigned && carsAvailableFiltered.size() != 0) {
            try {
                // create transit Dispatch record
                calendar.setTime(chosenReservation.getStartDateTime());
                calendar.add(Calendar.HOUR, -2);
                Date transitTime = calendar.getTime();
                // if car for transit has no reservations 
                System.out.println("about to create new transit record");

                if (carForAssignmentWithTransit.getReservations().size() == 0) {
                    try {
                        System.out.println("create new transit record for car without reservations");

                        generateTransitRecord(carForAssignmentWithTransit, chosenReservation.getPickUpLocation(), outletSessionBeanLocal.retrieveOutletByLocation(carForAssignmentWithTransit.getLocation()), transitTime);
                    } catch (OutletNotFoundException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else {
                    System.out.println("create new transit record for car with reservations");

                    generateTransitRecord(carForAssignmentWithTransit, chosenReservation.getPickUpLocation(), latestReservation.getReturnLocation(), transitTime);
                }
                // car with transit option assigned
                reservationSessionBeanLocal.assignCarToReservation(chosenReservation, carForAssignmentWithTransit);

            } catch (ReservationNotFoundException | CarNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }

        System.out.println("Printing cars available filtered");

        for (Car car : carsAvailableFiltered) {
            System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
        }
        if (carsAvailableFiltered.size() == 0) {
            throw new CarNotFoundException();
        }
    }

    public void generateTransitRecord(Car carToTransit, Outlet transitDestination, Outlet transitPickUpLocation, Date transitPickUpTime) {
        // need to create errors for this
        transitDriverDispatchRecordSessionBeanLocal.createNewTransitDriverDispatchRecord(new TransitDriverDispatchRecord(transitPickUpLocation, transitDestination, transitPickUpTime, carToTransit));
    }
}
