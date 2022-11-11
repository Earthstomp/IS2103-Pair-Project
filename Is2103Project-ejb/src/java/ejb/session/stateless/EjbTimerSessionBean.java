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

    // For testing purpose, we are allowing the timer to trigger every 8 seconds instead of every day at 8am
    // To trigger the timer once every day at 8am instead, use the following the @Schedule annotation
    // @Schedule(dayOfWeek="*", hour = "8", info = "currentDayCarAllocationTransitDriverDispatchRecordGeneratorTimer")    
    @Schedule(hour = "*", minute = "*", second = "*/3", info = "currentDayCarAllocationTimer")
    public void currentDayCarAllocationTimer() {
        Date timeStamp = new Date(); // new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(
        System.out.println("********** EjbTimerSessionBean.currentDayCarAllocationTimer(): Timeout at " + timeStamp);
        List<Reservation> reservations = new ArrayList<Reservation>();
        List<Car> availableCars = new ArrayList<Car>();

        try {
            reservations = reservationSessionBeanLocal.retrieveReservationByDate(timeStamp);
            System.out.println("********** EjbTimerSessionBean retrieving reservations by date " + timeStamp);
            System.out.println("********** there is a total of " + reservations.size() + " on " + timeStamp);

            for (Reservation reservation : reservations) {
                System.out.println(reservation.getCustomer().getEmail() + " " + reservation.getStartDateTime().toString() + " " + reservation.getEndDateTime().toString()
                        + " " + reservation.getRequirements().toString() + " " + reservation.getPickUpLocation().getName() + " " + reservation.getReturnLocation().getName());
            }
        } catch (ReservationNotFoundException ex) {
            System.out.println("There are no reservations today!");
        }

        for (Reservation r : reservations)
            try {
            System.out.println(r.getRequirements());
            availableCars = carSessionBeanLocal.retrieveAvailableCarsForReservation(r.getStartDateTime(), r.getEndDateTime(), r.getRequirements(), r.getPickUpLocation(), r.getReturnLocation());
        } catch (CarNotFoundException ex) {
            System.out.println("There are no cars available for this reservation\n");
        }
    }

//    for (Reservation reservation : reservations
//
//    
//        ) { // *** CONSIDER CREATING MODEL ATTRIBUTE IN CAR
//            Model model = reservation.getCar().getModel();
//        Category category = reservation.getCar().getModel().getCategory();
//
//        // unidirectional reservation -> car, only need to set reservation
//        // NOTE: Assumes that reservation can only be made if car is available
//        while (reservation.getCar().getPlateNumber() == null) // no car allocated yet
//        {
//            if (model != null && category != null) { // required model and category specified
//                for (Car car : availableCars) {
//                    if (car.getModel().equals(model) && car.getModel().getCategory().equals(category)) {
//                        reservation.setCar(car);
//                    }
//                }
//            } else if (model != null && category == null) { // only required model specified
//                for (Car car : availableCars) {
//                    if (car.getModel().equals(model)) {
//                        reservation.setCar(car);
//                    }
//                }
//                // IF MODEL ATTRIBUTE IN CAR IS CREATED, AND CATEGORY CAN BE SPECIFIED WITHOUT MODEL SPECIFICATION (i.e. only required category specified)                
//                /*} else if (model == null && category != null) { 
//                    for(Car car : availableCars) {
//                        if (car.getModel().getCategory().equals(category)) {
//                            reservation.setCar(car);
//                        }
//                    }*/
//            } else { // no requirements
//                for (Car car : availableCars) {
//                    reservation.setCar(car);
//                }
//            }
//        }
//    }
//}
    public void transitDriverDispatchRecordGeneratorTimer() {
        Date timeStamp = new Date();  // new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(
        System.out.println("********** EjbTimerSessionBean.currentDayCarAllocationTimer(): Timeout at " + timeStamp);
        List<Reservation> reservations = new ArrayList<Reservation>();
        Outlet previousReturnLocation = new Outlet();
        try {
            reservations = reservationSessionBeanLocal.retrieveReservationByDate(timeStamp);

        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        List<Car> requireMovement = new ArrayList<Car>();

        for (Reservation reservation : reservations) {
            try {
                previousReturnLocation = outletSessionBeanLocal.retrieveOutletByLocation(reservation.getCar().getLocation());
            } catch (OutletNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
            Outlet nextPickupLocation = reservation.getPickUpLocation();

            if (!previousReturnLocation.equals(nextPickupLocation)) {
                requireMovement.add(reservation.getCar());
                TransitDriverDispatchRecord transitDriverDispatchRecord = new TransitDriverDispatchRecord(nextPickupLocation, previousReturnLocation, reservation.getEndDateTime());
                transitDriverDispatchRecordSessionBeanLocal.createNewTransitDriverDispatchRecord(transitDriverDispatchRecord);
                nextPickupLocation.getDispatches().add(transitDriverDispatchRecord);
            }
        }
    }
}
