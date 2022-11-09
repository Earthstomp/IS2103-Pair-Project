/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import entity.Model;
import entity.RentalRateRecord;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Keith test
 */
@Local
public interface CategorySessionBeanLocal {
    
    public Long createNewCategory(Category category) throws CategoryExistsException, UnknownPersistenceException;

    public Long createNewModelWithExistingCategory(Model model, Long categoryId);

    public Long createRentalRateRecord(RentalRateRecord rateRecord, Long categoryId);

    public List<Category> retrieveAllCategories();
    
    public Category retrieveCategoryById(Long id) throws CategoryNotFoundException;

    public Long createNewModelWithExistingCategoryClass(Model model, Category category);

    public Category retrieveCategoryByName(String name);

    public void merge(Category category);

    
}
