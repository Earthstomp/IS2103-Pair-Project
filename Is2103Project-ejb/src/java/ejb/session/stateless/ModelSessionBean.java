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
        return em.createQuery("SELECT m FROM Model m"
                + "ORDER BY m.category, m.model")
                .getResultList();
    }

    public void updateModel(String newModel, Long modelId) {
        Model model = em.find(Model.class, modelId);
        model.setModel(newModel);
    }

    public void deleteModel(Long modelId) {
        Model model = em.find(Model.class, modelId);
        String modelName = model.getModel();

        List<Car> carsUsed = em.createQuery(
                "SELECT c FROM Car c WHERE c.model = modelName").getResultList();
        
        if (carsUsed.size() > 0) {
            System.out.print("There are cars that use this model, model is disabled instead of deleted");
            model.setEnabled(false);
        } else {
            model.getCategory().getModels().remove(model);
            em.remove(model);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
