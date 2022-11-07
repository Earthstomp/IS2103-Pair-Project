/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.DeleteModelException;

/**
 *
 * @author Keith test
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @Override
    public Long createNewCarWithExistingModel(Car car, Long modelId) {
        em.persist(car);

        Model model = em.find(Model.class, modelId);
        model.getCars().add(car);

        em.flush();

        return car.getCarId();
    }

    public List<Model> viewAllModels() {
        List<Model> models = em.createQuery("SELECT m FROM Model m"
                + "ORDER BY m.category, m.model")
                .getResultList();

        for (Model model : models) {
            model.getCategory();
            model.getModel();
        }

        return models;
    }

    public Model retrieveModelByName(String name) {
        Model model = (Model) em.createQuery("SELECT m FROM Model m WHERE m.model = :InModelName")
                .setParameter("InModelName", name)
                .getSingleResult();

        model.getCategory();
        model.getCars();
        return model;
    }

    public void updateModel(String newModel, Long modelId) {
        Model model = em.find(Model.class, modelId);
        model.setModel(newModel);
    }

    public void deleteModel(Long modelId) throws DeleteModelException {
        Model model = em.find(Model.class, modelId);
        String modelName = model.getModel();

        List<Car> carsUsed = em.createQuery(
                "SELECT c FROM Car c WHERE c.model = modelName").getResultList();

        if (carsUsed.size() > 0) {
            model.setEnabled(false);
            em.merge(model);
            throw new DeleteModelException("Model Name " + modelName + "is associated with existing car(s) and cannot be deleted!\n");
        } else {
            model.getCategory().getModels().remove(model);
            em.remove(model);
        }
    }

    public void merge(Model model) {
        em.merge(model);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
