/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Keith test
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewReservation(Reservation reservation) {
        em.persist(reservation);
        em.flush();

        return reservation.getId();
    }

    @Override
    public List<Reservation> retrieveAllReservations() {
        Query query = em.createQuery("SELECT r FROM Reservation r");

        return query.getResultList();
    }

    @Override
    public Reservation retrieveReservationById(Long reservationId) { // throws exception
        Reservation reservation = em.find(Reservation.class, reservationId);

        if (reservation != null) {
            return reservation;
        } else {
            // throw exception
            // delete line below
            return reservation;
        }
    }

    @Override
    public void removeReservation(Long reservationId) {
        Reservation reservation = em.find(Reservation.class, reservationId);
        em.remove(reservation);
    }

}

// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method"
