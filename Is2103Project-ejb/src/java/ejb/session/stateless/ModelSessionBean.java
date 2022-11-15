/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CarExistsException;
import util.exception.CarNotFoundException;
import util.exception.DeleteModelException;
import util.exception.ModelNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;

    @Override
    public Long createNewCarWithExistingModel(Car car, Long modelId) throws ModelNotFoundException, CarExistsException {

        if (carSessionBeanLocal.retrieveCarByPlateNumber(car.getPlateNumber()) != null) {
            throw new CarExistsException("Car with this plate number already exists.");
        } else {
            em.persist(car);
        }

        Model model = em.find(Model.class, modelId);

        if (model != null) {
            model.getCars().add(car);
            car.setModel(model);

            em.flush();

            return car.getCarId();
        } else {
            throw new ModelNotFoundException("Model not found.");
        }
    }

    @Override
    public List<Car> retrieveAllCarsFromModel(String make, String model) throws ModelNotFoundException, CarNotFoundException {
        System.out.println("Finding cars from model");

        if (retrieveModelByName(make, model) != null) {
            List<Car> cars = em.createQuery("SELECT c FROM Car c"
                    + " JOIN c.model m WHERE m.make = :make AND m.model = :model")
                    .setParameter("make", make)
                    .setParameter("model", model)
                    .getResultList();

            if (cars.size() < 1) {
                throw new CarNotFoundException("No Cars with this model found.");
            } else {

                for (Car car : cars) {
                    car.getModel();
                    car.getReservations().size();
                    car.getTransitRecords().size();
                }

                return cars;
            }
        } else {
            throw new ModelNotFoundException("No model with this name found.");
        }
    }

    @Override
    public List<Model> viewAllModels() throws ModelNotFoundException {
        List<Model> models = em.createQuery("SELECT m FROM Model m "
                + "ORDER BY m.category, m.model, m.make ASC")
                .getResultList();

        if (models.size() < 1) {
            throw new ModelNotFoundException("No models found.");
        } else {

            for (Model model : models) {
                model.getCategory();
                model.getModel();
                model.getMake();
            }

            return models;
        }
    }

    @Override
    public Model retrieveModelByName(String make, String model) throws ModelNotFoundException {
        Model retrievedModel = (Model) em.createQuery("SELECT m FROM Model m WHERE m.model = :InModelName AND m.make = :InMakeName")
                .setParameter("InMakeName", make)
                .setParameter("InModelName", model)
                .getSingleResult();

        if (retrievedModel == null) {
            throw new ModelNotFoundException("No models found.");
        } else {
            retrievedModel.getCategory();
            retrievedModel.getCars().size();
            return retrievedModel;
        }
    }

    public void updateModel(String newMake, String newModel, Long modelId) throws ModelNotFoundException {
        Model model = em.find(Model.class,
                modelId);
        if (model == null) {
            throw new ModelNotFoundException("No models found.");
        } else {
            model.setModel(newModel);
            model.setMake(newMake);
        }
    }

    public void deleteModel(Long modelId) throws DeleteModelException, ModelNotFoundException {
        Model model = em.find(Model.class,
                modelId);

        if (model == null) {
            throw new ModelNotFoundException("No model with this id found.");
        } else {
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
    }

    public void merge(Model model) {
        em.merge(model);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
