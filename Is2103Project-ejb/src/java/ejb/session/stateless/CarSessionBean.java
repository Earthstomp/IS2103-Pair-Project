/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.Reservation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CarStatusEnum;
import util.enumeration.ReservationPaymentEnum;
import util.exception.CarNotFoundException;
import util.exception.DeleteCarException;

/**
 *
 * @author Keith test
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "Is2103Project-ejbPU")
    private EntityManager em;
    private String name;
    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    public CarSessionBean() {

    }

    // car creation method inside model session bean as it is compulsory
    @Override
    public List<Car> retrieveAllCars() {
        List<Car> cars = em.createQuery("SELECT c FROM Car C "
                + "JOIN c.model m "
                + "ORDER BY m.category, c.model").getResultList();
        for (Car car : cars) {
            car.getModel().getCategory();
        }

        return cars;
    }

    @Override
    public Car retrieveCarById(Long carId) throws CarNotFoundException {

        Car car = em.find(Car.class, carId);

        if (car != null) {
            return car;
        }
        throw new CarNotFoundException("Unable to locate car with id: " + carId);

    }

    public List<Car> retrieveAvailableCarsForReservation(Date startDateTime, Date endDateTime, List<String> requirements, Outlet pickUpOutlet, Outlet returnOutlet) throws CarNotFoundException {
        // returns the cars that can be reserved for a single reservation's request
        List<Car> carsAvailable = new ArrayList<Car>();

        // filter out unavailable cars, disabled cars, cars that cannot be returned on time
        if (requirements.size() == 1) { // requires car from specific category
            carsAvailable = categorySessionBeanLocal.retrieveAllCarsFromCategory(requirements.get(0));
        } else // require car from specific make, model
        {
            carsAvailable = modelSessionBeanLocal.retrieveAllCarsFromModel(requirements.get(0), requirements.get(1));
        }

        for (Car car : carsAvailable) {
            System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
            System.out.println("Reservations");
            for (Reservation r : car.getReservations()) {
                System.out.println("Reservation " + r.getId());
            }
        }

        Calendar calendar = Calendar.getInstance();
        // setting time if transit is possible
        calendar.setTime(startDateTime);
        calendar.add(Calendar.HOUR, -2);
        Date startTimeForTransit = calendar.getTime();

        List<Car> carsAvailableFiltered = carsAvailable; // make a copy of the list

        for (Car c : carsAvailable) {
            System.out.println("Looking at car " + c.getPlateNumber());
            if (c.getStatus() == CarStatusEnum.DISABLED || c.getStatus() == CarStatusEnum.SERVICING_OR_REPAIR) {
                System.out.println("Car is removed cause it is disabled or servicing");
                carsAvailableFiltered.remove(c);
            } else {
                // for every reservation the car has
                for (Reservation r : c.getReservations()) {
                    System.out.println(r.getStartDateTime() + " " + r.getEndDateTime());

                    // finding the time for transit for case 4
                    calendar.setTime(r.getStartDateTime());
                    calendar.add(Calendar.HOUR, -2);
                    Date rStartTimeForTransit = calendar.getTime();

                    // filter out ongoing reservations
                    if (r.getReservationPaymentEnum() != ReservationPaymentEnum.COMPLETED || r.getReservationPaymentEnum() != ReservationPaymentEnum.CANCELLED) {

                        // 1. if there is a reservation on car that will prevent car from being returned before reservation time
                        if (r.getStartDateTime().before(startDateTime) && r.getEndDateTime().after(startDateTime)) {
                            System.out.println("Car " + c.getPlateNumber() + " is removed cause case 1");

                            carsAvailableFiltered.remove(c);

                            // 2. if there is a reservation on car that returns to another outlet that will prevent car from being returned before reservation time (by accounting for 2hr transit) 
                        } else if (r.getStartDateTime().before(startDateTime) && r.getReturnLocation() != pickUpOutlet && r.getEndDateTime().after(startTimeForTransit)) {
                            System.out.println("Car is removed cause case 2");

                            carsAvailableFiltered.remove(c);

                            // 3. if there is a reservation that starts between the startDateTime and endDateTime of this reservation
                        } else if (r.getStartDateTime().after(startDateTime) && r.getStartDateTime().before(endDateTime)) {
                            System.out.println("Car is removed cause case 3");

                            carsAvailableFiltered.remove(c);
 
                            // 4. if there is a future reservation that starts at another outlet at a time less than 2hrs after this reservation end time
                        } else if (r.getPickUpLocation() != pickUpOutlet && endDateTime.after(rStartTimeForTransit)) {
                            System.out.println("Car is removed cause case 4");

                            carsAvailableFiltered.remove(c);
                        }
                    } // ignore reservations which have been completed or cancelled
                }
            }
        }

        System.out.println("Printing cars available filtered");

        for (Car car : carsAvailableFiltered) {
            System.out.println(car.getPlateNumber() + " " + car.getModel().getMake() + " " + car.getModel().getModel());
        }

        if (carsAvailableFiltered.size() == 0) {
            throw new CarNotFoundException("There are no cars available for this reservation\n");
        }

        return carsAvailableFiltered;

    }

    public Car retrieveCarByPlateNumber(String number) {
        Car car = (Car) em.createQuery("SELECT c FROM Car c WHERE c.plateNumber = :InPlateNumber")
                .setParameter("InPlateNumber", number)
                .getSingleResult();

        car.getModel().getCategory();
        car.getModel();
        return car;
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
        Car car = em.find(Car.class,
                carId);
        car = newCar;

    }

    public void updateCarStatusLocation(Car car, CarStatusEnum status, String location) {
        Car updatedCar = em.find(Car.class,
                car.getCarId());
        updatedCar.setStatus(status);
        updatedCar.setLocation(location);
    }

    public void deleteCar(Long carId) throws DeleteCarException {
        Car car = em.find(Car.class,
                carId);
        String plateNumber = car.getPlateNumber();
        // need to find a way to know if reservation  is still active, check by date? but how
        List<Reservation> reservationsUsed = em.createQuery("SELECT r FROM Reservation r JOIN r.car c WHERE c.plateNumber = :plateNumber")
                .setParameter("plateNumber", car.getPlateNumber())
                .getResultList();

        if (reservationsUsed.size() > 0) {
            car.setStatus(CarStatusEnum.DISABLED);
            em.merge(car);
            throw new DeleteCarException("Car Plate Number " + plateNumber + "is associated with existing reservation(s) and cannot be deleted!\n");

        } else { // record is not being used, can delete
            car.getModel().getCars().remove(car);
            em.remove(car);
        }
    }

    public void merge(Car car) {
        em.merge(car);
    }

}
