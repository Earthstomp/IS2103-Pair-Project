/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Reservation;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CarNotFoundException;

/**
 *
 * @author Keith test
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    private String name;

    public CarSessionBean() {
    }

    // car creation method inside model session bean as it is compulsory
    

    @Override
    public List<Car> retrieveAllCars() {
        Query query = em.createQuery("SELECT c FROM Car C");

        return query.getResultList();
    }

    @Override
    public Car retrieveCarById(Long carId) throws CarNotFoundException {
        
        Car car = em.find(Car.class, carId);

        if (car != null) {
            return car;
        } 
            throw new CarNotFoundException("Unable to locate car with id: " + carId);

    }

    @Override
    public void removeCar(Long reservationId) {
        // more details need to be thought out
    }


    public List<Car> viewAllCars() {
        return em.createQuery("SELECT c FROM Car c"
                + "ORDER BY c.category, c.model, c.plateNumber")
                .getResultList();
    }

    // not sure
    public void updateCar(Car newCar, Long carId) {
        Car car = em.find(Car.class, carId);
        car = newCar;

    }

    public void updateCarStatusLocation(Car car, String status, String location) {
        Car updatedCar = em.find(Car.class, car.getCarId());
        updatedCar.setStatus(status);
        updatedCar.setLocation(location);
    }

    public void deleteCar(Long carId) {
        Car car = em.find(Car.class, carId);
        // need to find a way to know if reservation  is still active, check by date? but how
        List<Reservation> reservationsUsed = em.createQuery("SELECT r FROM Reservation r WHERE r.car = car").getResultList();

        if (reservationsUsed.size() > 0) {
            System.out.print("There are reservations that use this car, car is disabled instead of deleted");
            car.setEnabled(false);

        } else { // record is not being used, can delete
            car.getModel().getCars().remove(car);
            em.remove(car);
        }

    }

}
