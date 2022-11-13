/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Customer;
import entity.RentalRateRecord;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ReservationPaymentEnum;
import util.exception.CarNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");

        return query.getResultList();
    }

    @Override
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);

        if (reservation != null) {
            reservation.getCar();
            reservation.getCustomer();
            reservation.getStartDateTime();
            reservation.getEndDateTime();
            reservation.getReturnLocation();
            reservation.getPickUpLocation();
            reservation.getRequirements();
            return reservation;
        } else {
            throw new ReservationNotFoundException("Unable to locate reservation with id: " + reservationId);
        }
    }

    @Override
    public List<Reservation> retrieveReservationByDate(Date currentDateTime) throws ReservationNotFoundException {
        // get all reservations
        List<Reservation> allReservations = em.createQuery(
                "SELECT r FROM Reservation r ORDER BY r.id ASC") // to ensure a similar sequence is achieved as when checking for availabilities
                .getResultList();

        Calendar c = Calendar.getInstance();
        // setting time to 2359
        c.setTime(currentDateTime);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 59);

        currentDateTime = c.getTime(); //  current day converted to 2359. need to change to 0159
        c.add(Calendar.DATE, -1);
        Date dayBeforeDateTime = c.getTime(); // previous day converted to 2359
        System.out.println("New current time is " + currentDateTime);
        System.out.println("Day before time is " + dayBeforeDateTime);
        List<Reservation> reservationsOnDate = new ArrayList<>();

        // only returns reservations on date that has NOT been assigned
        for (Reservation r : allReservations) {
            if (r.getStartDateTime().before(currentDateTime) && r.getStartDateTime().after(dayBeforeDateTime) && r.getCar() == null) {
                r.getCar();
                r.getStartDateTime();
                r.getEndDateTime();
                r.getPickUpLocation();
                r.getReturnLocation();
                reservationsOnDate.add(r);
            }
        }
        if (reservationsOnDate != null) {
            return reservationsOnDate;
        } else {
            throw new ReservationNotFoundException("Null / No reservations on date");
        }
    }

    @Override
    public List<Reservation> retrieveAllReservationsForCustomer(Customer customer) throws ReservationNotFoundException {
        List<Reservation> reservations = em.createQuery(
                "SELECT r FROM Reservation r WHERE r.customer = :InCustomer")
                .setParameter("InCustomer", customer)
                .getResultList();

        if (reservations != null) {
            return reservations;
        } else {
            throw new ReservationNotFoundException("No reservations found for customer");
        }
    }

    @Override
    public void assignCarToReservation(Reservation reservation, Car car) throws ReservationNotFoundException, CarNotFoundException {
        try {
            reservation = retrieveReservationById(reservation.getId());
        } catch (ReservationNotFoundException ex) {
            System.err.println(ex.getMessage());
        }

        car = carSessionBeanLocal.retrieveCarById(car.getCarId());

        // will probably need more error checking here
        System.out.println("Assigning Car " + car.getPlateNumber() + " to Reservation" + reservation.getId());

        reservation.setCar(car);
        car.getReservations().add(reservation);

        em.merge(car);
        em.merge(reservation);
    }

    public void unassignReservationFromCar(Reservation reservation) {

        Car car = new Car();
        try {
            reservation = retrieveReservationById(reservation.getId());
        } catch (ReservationNotFoundException ex) {
            System.err.println(ex.getMessage());
        }
        try {
            car = carSessionBeanLocal.retrieveCarById(reservation.getCar().getCarId());
        } catch (CarNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        reservation.setCar(null);
        car.getReservations().remove(reservation);
        em.flush();
    }

    @Override
    public void removeReservation(Long reservationId
    ) {
        Reservation reservation = em.find(Reservation.class, reservationId);

        BigDecimal rentalFee = new BigDecimal(0); // stub
        // calculate penalty
        BigDecimal penalty = new BigDecimal(0);
        Date pickupDate = reservation.getStartDateTime();

        Calendar c = Calendar.getInstance();
        c.setTime(pickupDate);
        c.add(Calendar.DATE, -3);

        Date pickupDateMinus3 = c.getTime();

        c.add(Calendar.DATE, -4);
        Date pickupDateMinus7 = c.getTime();

        c.add(Calendar.DATE, -10);
        Date pickupDateMinus14 = c.getTime();

        Date currentDate = new Date();

        if (currentDate.compareTo(pickupDateMinus14) < 0) {
            // no penalty
        } else if (currentDate.compareTo(pickupDateMinus14) > 0 && currentDate.compareTo(pickupDateMinus7) < 0) {
            // 20% penalty
            penalty.add(rentalFee.multiply(new BigDecimal(0.2)));
        } else if (currentDate.compareTo(pickupDateMinus7) > 0 && currentDate.compareTo(pickupDateMinus3) < 0) {
            // 50% penalty
            penalty.add(rentalFee.multiply(new BigDecimal(0.5)));
        } else {
            // 70% penalty
            penalty.add(rentalFee.multiply(new BigDecimal(0.7)));
        }

        if (reservation.getReservationPaymentEnum().equals("ATPICKUP")) {
            //calculate rental fee, rental fee - penalty = refund balance
            rentalFee.subtract(penalty);
        } else {
            // charge penalty to credit card
            //reservation.getCustomer().getCreditCard().setPenaltyAmount(penalty);
        }

        em.remove(reservation);
    }

    public void merge(Reservation reservation) {
        em.merge(reservation);
        em.flush();
    }

    public boolean checkIfRentalRateUsed(RentalRateRecord rentalRateRecord) {
        List<Reservation> reservations = retrieveAllReservations();
        boolean isUsed = false;
        for (Reservation r : reservations) {
            r.getCar().getModel().getCategory().getRateRecords().size();
            if (r.getReservationPaymentEnum().equals(ReservationPaymentEnum.ATPICKUP) && r.getCar().getModel().getCategory().getRateRecords().equals(rentalRateRecord)) {
                isUsed = true;
            }
        }
        return isUsed;
    }
}
