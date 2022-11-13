/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Keith test
 */
@Stateless
public class CreditCardSessionBean implements CreditCardSessionBeanRemote, CreditCardSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public Long createNewCard(CreditCard creditCard) {
            em.persist(creditCard);
            em.flush();
        

        return creditCard.getId();
    }
    
    public CreditCard retrieveCardById(Long id) {
        return em.find(CreditCard.class, id);
    }
    
    public void makePayment(BigDecimal payment, Long id) {
        CreditCard card = retrieveCardById(id);
        card.setAmountDue(card.getAmountDue().add(payment));
        em.merge(card);
    }
    
}
