/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface ReservationSessionBeanRemote {

    public void removeReservation(Long reservationId);

    public Reservation retrieveReservationById(Long reservationId) throws ReservationNotFoundException ;

    public List<Reservation> retrieveAllReservations();

    public List<Reservation> retrieveReservationByDate(Date startDateTime) throws ReservationNotFoundException;
}
