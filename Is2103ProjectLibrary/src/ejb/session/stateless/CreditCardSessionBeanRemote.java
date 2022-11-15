/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import java.math.BigDecimal;
import javax.ejb.Remote;
import util.exception.CreditCardExistsException;

/**
 *
 * @author Keith test
 */
@Remote
public interface CreditCardSessionBeanRemote {

        public Long createNewCard(CreditCard creditCard) throws CreditCardExistsException;

    public void makePayment(BigDecimal payment, Long id);

    public CreditCard retrieveCardById(Long id);

}
