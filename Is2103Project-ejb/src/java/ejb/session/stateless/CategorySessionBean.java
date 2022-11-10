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
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CategoryExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author Keith test
 */
@Stateless
public class CategorySessionBean implements CategorySessionBeanRemote, CategorySessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCategory(Category category) throws CategoryExistsException, UnknownPersistenceException {
        try {
            em.persist(category);
            em.flush();

            return category.getCategoryId();
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CategoryExistsException("Category already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    public List<Car> retrieveAllCarsFromCategory(String categoryName) {
        
        return em.createQuery("SELECT c FROM Car c JOIN c.model m JOIN m.category cat WHERE cat.categoryName = :categoryName")
                .setParameter("categoryName", categoryName)
                .getResultList();
    }

    @Override
    public List<Category> retrieveAllCategories() {
        return em.createQuery("SELECT c FROM Category c").getResultList();

    }

    @Override
    public Category retrieveCategoryById(Long id) throws CategoryNotFoundException {
        Category category = em.find(Category.class, id);

        if (category != null) {
            return category;
        } else {
            throw new CategoryNotFoundException("Unable to locate category with id: " + id);
        }
    }

    public Category retrieveCategoryByName(String name) {
        Category category = (Category) em.createQuery("SELECT c FROM Category c WHERE c.categoryName = :InName")
                .setParameter("InName", name)
                .getSingleResult();

        category.getModels().size();
        return category;
    }

    @Override
    public Long createNewModelWithExistingCategory(Model model, Long categoryId) {
        em.persist(model);

        Category category = em.find(Category.class,
                categoryId);
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
        Category category = em.find(Category.class,
                categoryId);
        category.getRateRecords().add(rateRecord);
        rateRecord.setCategory(category);
        em.flush();

        return rateRecord.getId();
    }

    public void merge(Category category) {
        em.merge(category);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
