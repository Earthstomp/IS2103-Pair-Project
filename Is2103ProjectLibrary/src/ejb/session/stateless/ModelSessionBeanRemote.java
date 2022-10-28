/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import javax.ejb.Remote;

/**
 *
 * @author Keith test
 */
@Remote
public interface ModelSessionBeanRemote {

    public Long createNewCarWithExistingModel(Car car, Long modelId);
    
}
