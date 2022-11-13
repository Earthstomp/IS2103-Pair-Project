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
        car.setModel(model);

        em.flush();

        return car.getCarId();
    }
    
    public List<Car> retrieveAllCarsFromModel(String make, String model) {
        System.out.println("Finding cars from model");
        List<Car> cars = em.createQuery("SELECT c FROM Car c"
                + " JOIN c.model m WHERE m.make = :make AND m.model = :model")
                .setParameter("make", make)
                .setParameter("model", model)
                .getResultList();
        
        for (Car car : cars) {
            car.getModel();
            car.getReservations().size();
            car.getTransitRecords().size();
        }
        
        return cars;
    }

    public List<Model> viewAllModels() {
        List<Model> models = em.createQuery("SELECT m FROM Model m "
                + "ORDER BY m.category, m.model, m.make ASC")
                .getResultList();

        for (Model model : models) {
            model.getCategory();
            model.getModel();
            model.getMake();
        }

        return models;
    }

    public Model retrieveModelByName(String make, String model) {
        Model retrievedModel = (Model) em.createQuery("SELECT m FROM Model m WHERE m.model = :InModelName AND m.make = :InMakeName")
                .setParameter("InMakeName", make)
                .setParameter("InModelName", model)
                .getSingleResult();

        retrievedModel.getCategory();
        retrievedModel.getCars().size();
        return retrievedModel;
    }

    public void updateModel(String newMake, String newModel, Long modelId) {
        Model model = em.find(Model.class, modelId);
        model.setModel(newModel);
        model.setMake(newMake);
    }

    public void deleteModel(Long modelId) throws DeleteModelException {
        Model model = em.find(Model.class, modelId);
        String modelName = model.getModel();
        String makeName = model.getMake();

        List<Car> carsUsed = em.createQuery(
                "SELECT c FROM Car c JOIN c.model m WHERE m.model = :modelName AND m.make = :makeName")
                .setParameter("modelName", modelName)
                .setParameter("makeName", makeName)
                .getResultList();

        if (carsUsed.size() > 0) {
            model.setEnabled(false);
            em.merge(model);
            throw new DeleteModelException("Model " + modelName + " " + makeName + " is associated with existing car(s) and cannot be deleted!\n");
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
