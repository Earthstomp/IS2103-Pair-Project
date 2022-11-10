/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import entity.Customer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");

        return query.getResultList();
    }

    @Override
    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);

        if (reservation != null) {
            return reservation;
        } else {
            throw new ReservationNotFoundException("Unable to locate reservation with id: " + reservationId);
        }
    }

    @Override
    public List<Reservation> retrieveReservationByDate(Date startDateTime) throws ReservationNotFoundException {
        List<Reservation> reservations = em.createQuery(
                "SELECT r FROM Reservation r WHERE r.startDateTime LIKE :startDate")
                .setParameter("startDate", startDateTime)
                .getResultList();

        if (reservations != null) {
            return reservations;
        } else {
            throw new ReservationNotFoundException("No reservations on date");
        }
    }

    public List<Reservation> retrieveAllReservationsForCustomer(Customer customer) throws ReservationNotFoundException {
        List<Reservation> reservations = em.createQuery(
                "SELECT r FROM Reservation r WHERE r.customer = :InCustomer")
                .setParameter("InCustomer", customer)
                .getResultList();

        if (reservations != null) {
            return reservations;
        } else {
            throw new ReservationNotFoundException("No reservations on date");
        }
    }

    @Override
    public void removeReservation(Long reservationId) {
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

        if (reservation.getPaymentStatus()) {
            //calculate rental fee, rental fee - penalty = refund balance
            rentalFee.subtract(penalty);
        } else {
            // charge penalty to credit card
            //reservation.getCustomer().getCreditCard().setPenaltyAmount(penalty);
        }

        em.remove(reservation);
    }

    @Override
    public Long createNewReservation(Reservation reservation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
