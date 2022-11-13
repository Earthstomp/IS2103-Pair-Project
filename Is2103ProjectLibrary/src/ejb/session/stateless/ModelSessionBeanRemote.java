/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import java.util.List;
import javax.ejb.Remote;
import util.exception.DeleteModelException;

/**
 *
 * @author Keith test
 */
@Remote
public interface ModelSessionBeanRemote {

    public Long createNewCarWithExistingModel(Car car, Long modelId);

    public List<Model> viewAllModels();

    public Model retrieveModelByName(String model, String make);

    public void merge(Model model);

    public void deleteModel(Long modelId) throws DeleteModelException;

    public List<Car> retrieveAllCarsFromModel(String make, String model);

    }
