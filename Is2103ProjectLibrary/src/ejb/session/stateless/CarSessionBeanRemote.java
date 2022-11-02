/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Remote;
import util.enumeration.CarStatusEnum;
import util.exception.CarNotFoundException;

/**
 *
 * @author Keith test
 */
@Remote
public interface CarSessionBeanRemote {

    public List<Car> retrieveAllCars();

    public Car retrieveCarById(Long carId) throws CarNotFoundException;

    public void removeCar(Long reservationId);

    public void updateCarStatusLocation(Car car, CarStatusEnum status, String location);
    
}
