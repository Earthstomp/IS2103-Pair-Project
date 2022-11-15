/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import entity.Reservation;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialsException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface PartnerSessionBeanRemote {

    public void removePartner(Long id) throws PartnerNotFoundException;

    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException;

    public List<Partner> retrieveAllPartners() throws PartnerNotFoundException;

    public Long createPartner(Partner partner) throws PartnerExistsException;

    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialsException;

    public Partner retrievePartnerByUsername(String username) throws PartnerNotFoundException;

    public List<Reservation> retrieveAllPartnerReservations(Long partnerId) throws ReservationNotFoundException, CustomerNotFoundException, PartnerNotFoundException;

}
