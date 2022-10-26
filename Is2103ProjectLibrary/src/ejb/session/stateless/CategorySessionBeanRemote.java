/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Category;
import entity.Model;
import entity.RentalRateRecord;
import javax.ejb.Remote;

/**
 *
 * @author Keith test
 */
@Remote
public interface CategorySessionBeanRemote {

    public Long createNewCategory(Category category);

    public Long createNewModelWithExistingCategory(Model model, Long categoryId);

    public Long createRentalRateRecord(RentalRateRecord rateRecord, Long categoryId);
    
}
