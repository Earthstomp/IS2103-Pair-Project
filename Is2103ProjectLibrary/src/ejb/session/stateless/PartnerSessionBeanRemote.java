/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import java.util.List;
import javax.ejb.Remote;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface PartnerSessionBeanRemote {

    public void removePartner(Long id);

    public Partner retrievePartnerById(Long id) throws PartnerNotFoundException;

    public List<Partner> retrieveAllPartners();

    public Long createPartner(Partner partner);
    
}