/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.enumeration.CarStatusEnum;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Local
public interface CarSessionBeanLocal {

    public List<Car> retrieveAllCars();

    public Car retrieveCarById(Long carId) throws CarNotFoundException;

    public void removeCar(Long reservationId);

    public void updateCarStatusLocation(Car car, CarStatusEnum status, String location);

    public Car retrieveCarByPlateNumber(String number);

    public void merge(Car car);

    public void deleteCar(Long carId) throws DeleteCarException;

    public void assignCarToReservation(Reservation chosenReservation) throws CarNotFoundException, ReservationNotFoundException;
}
