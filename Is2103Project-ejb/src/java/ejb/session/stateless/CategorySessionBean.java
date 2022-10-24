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
    public Long createNewModelWithExistingCategory(Model model, Long categoryId) {
        em.persist(model);

        Category category = em.find(Category.class, categoryId);
        category.getModels().add(model);

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
