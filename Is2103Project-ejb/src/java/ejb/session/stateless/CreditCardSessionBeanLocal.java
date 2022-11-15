/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import javax.ejb.Local;
import util.exception.CreditCardExistsException;
import util.exception.CreditCardNotFoundException;

/**
 *
 * @author Keith test
 */
@Local
public interface CreditCardSessionBeanLocal {

    public Long createNewCard(CreditCard creditCard) throws CreditCardExistsException;

    public CreditCard retrieveCardById(Long id);

    public CreditCard retrieveCreditCardBySerialNumber(String number) throws CreditCardNotFoundException;
}
