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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import util.exception.CreditCardExistsException;
import util.exception.CreditCardNotFoundException;

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
    public Long createNewCard(CreditCard creditCard) throws CreditCardExistsException {
        CreditCard cardExists = null;

        try {
            cardExists = retrieveCreditCardBySerialNumber(creditCard.getSerialNumber());
        } catch (CreditCardNotFoundException ex) {
            if (cardExists != null) {
                throw new CreditCardExistsException("Credit Card with this number already exists.");
            }

            em.persist(creditCard);
            em.flush();
        }
        return creditCard.getId();
    }

    public CreditCard retrieveCardById(Long id) {
        return em.find(CreditCard.class, id);
    }

    @Override
    public CreditCard retrieveCreditCardBySerialNumber(String number) throws CreditCardNotFoundException {
        try {
            return (CreditCard) em.createQuery("SELECT c FROM CreditCard c WHERE c.serialNumber = :inSerialNumber")
                    .setParameter("inSerialNumber", number)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CreditCardNotFoundException("Credit Card does not exist!");
        }
    }

    public void makePayment(BigDecimal payment, Long id) {
        CreditCard card = retrieveCardById(id);
        card.setAmountDue(card.getAmountDue().add(payment));
        em.merge(card);
    }

}
