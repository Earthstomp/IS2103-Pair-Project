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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Keith test
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCategory(Category category) {
        em.persist(category);
        em.flush();

        return category.getCategoryId();
    }
    
    @Override
    public List<Category> retrieveAllCategories() {
        return em.createQuery("SELECT c FROM Category c").getResultList();
        
    }

    @Override
    public Long createNewModelWithExistingCategory(Model model, Long categoryId) {
        em.persist(model);

        Category category = em.find(Category.class, categoryId);
        category.getModels().add(model);
        model.setCategory(category);

        em.flush();

        return model.getModelId();
    }
    
    public Long createNewModelWithExistingCategoryClass(Model model, Category category) { //  need to account for exception
        em.persist(model);
        
        // need to account for exception
        Category managedCategory = (Category) em.createQuery("SELECT c FROM Category c WHERE c = :category")
                .setParameter("category", category)
                .getSingleResult();
        
        managedCategory.getModels().add(model);
        model.setCategory(managedCategory);

        em.flush();

        return model.getModelId();
    }

    @Override
    public Long createRentalRateRecord(RentalRateRecord rateRecord, Long categoryId) {
        em.persist(rateRecord);
        Category category = em.find(Category.class, categoryId);
        category.getRateRecords().add(rateRecord);

        em.flush();

        return rateRecord.getId();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
