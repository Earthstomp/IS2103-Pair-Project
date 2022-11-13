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
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface ReservationSessionBeanRemote {

    public void removeReservation(Long reservationId);

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException;

    public List<Reservation> retrieveAllReservations();

    public List<Reservation> retrieveReservationByDate(Date startDateTime) throws ReservationNotFoundException;

    public void merge(Reservation reservation);

    public void assignCarToReservation(Reservation reservation, Car car) throws ReservationNotFoundException, CarNotFoundException;

    public void unassignReservationFromCar(Reservation reservation);

    public List<Reservation> retrieveAllReservationsForCustomer(Customer customer) throws ReservationNotFoundException;

    public boolean checkIfRentalRateUsed(RentalRateRecord rentalRateRecord);

}
