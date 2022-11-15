/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarExistsException;
import util.exception.CarNotFoundException;
import util.exception.DeleteModelException;
import util.exception.ModelNotFoundException;

/**
 *
 * @author Keith test
 */
@Local
public interface ModelSessionBeanLocal {

    public Long createNewCarWithExistingModel(Car car, Long modelId) throws ModelNotFoundException, CarExistsException;

    public List<Model> viewAllModels() throws ModelNotFoundException;

    public Model retrieveModelByName(String model, String make) throws ModelNotFoundException;

    public void merge(Model model);

    public void deleteModel(Long modelId) throws DeleteModelException, ModelNotFoundException;

    public List<Car> retrieveAllCarsFromModel(String make, String model) throws ModelNotFoundException, CarNotFoundException;

    public void updateModel(String newMake, String newModel, Long modelId) throws ModelNotFoundException;

}
