/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createPartner(Partner partner) {
        em.persist(partner);
        em.flush();

        return partner.getId();
    }
    
    @Override
    public List<Partner> retrieveAllPartners() {
        Query query = em.createQuery("SELECT p FROM Partner p");

        return query.getResultList();
    }
    
    @Override
    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, id);

        if (partner != null) {
            return partner;
        } else {
            throw new PartnerNotFoundException("Unable to locate partner with id: " + id);
        }
    }

    @Override
    public void removePartner(Long id) {
        Partner partner = em.find(Partner.class, id);
        em.remove(partner);
        // need to check for dependencies / if other cars are using this rental rate record
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
