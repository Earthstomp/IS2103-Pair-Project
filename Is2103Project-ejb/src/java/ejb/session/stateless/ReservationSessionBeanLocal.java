/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Customer;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Local
public interface ReservationSessionBeanLocal {

    public void removeReservation(Long reservationId);

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public List<Reservation> retrieveAllReservations();

    public List<Reservation> retrieveReservationByDate(Date startDateTime) throws ReservationNotFoundException;

    public void assignCarToReservation(Reservation reservation, Car car) throws ReservationNotFoundException, CarNotFoundException;

    public void merge(Reservation reservation);

    public List<Reservation> retrieveAllReservationsForCustomer(Customer customer) throws ReservationNotFoundException;
}
