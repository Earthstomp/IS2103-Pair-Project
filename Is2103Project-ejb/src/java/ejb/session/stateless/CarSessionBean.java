/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Keith test
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;

    public CarSessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    // car creation method inside model session bean as it is compulsory

    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car C");

        return query.getResultList();
    }

    @Override
    public Car retrieveCarById(Long carId) { // throws exception
        Car car = em.find(Car.class, carId);

        if (car != null) {
            return car;
        } else {
            // throw exception
            // delete line below
            return car;
        }
    }

    @Override
    public void removeCar(Long reservationId) {
        // more details need to be thought out
    }
    
}
