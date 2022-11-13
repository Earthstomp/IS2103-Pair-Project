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
import javax.ejb.Remote;
import util.exception.CarNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface EjbTimerSessionBeanRemote {

    public void allocateCarsManual(Date date);

    public void allocateCarToReservation(Reservation chosenReservation) throws CarNotFoundException, ReservationNotFoundException;

    public void generateTransitRecord(Car carToTransit, Outlet transitDestination, Outlet transitPickUpLocation, Date transitPickUpTime);
    
}
