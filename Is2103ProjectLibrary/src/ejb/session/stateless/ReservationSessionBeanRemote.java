/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Keith test
 */
@Remote
public interface ReservationSessionBeanRemote {

    public void removeReservation(Long reservationId);

    public Reservation retrieveReservationById(Long reservationId);

    public List<Reservation> retrieveAllReservations();
}
