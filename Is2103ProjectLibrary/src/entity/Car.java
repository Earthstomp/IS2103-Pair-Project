/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import util.enumeration.CarStatusEnum;
import javax.persistence.OneToMany;

/**
 *
 * @author Keith test
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @ManyToOne
//    @JoinColumn(nullable = false)
    private Model model;
    @Enumerated(EnumType.STRING)
    private CarStatusEnum status;
    private String location;
    private String plateNumber;
    private String color;
    @OneToMany (mappedBy = "car")
    private List<Reservation> reservations;
    
    public Car() {
        this.status = CarStatusEnum.AVAILABLE;
    }

    public Car(CarStatusEnum status, String location, String plateNumber) {
        this.status = status;
        this.location = location;
        this.plateNumber = plateNumber;
    }
    
    

    public Car(Long carId, Model model, CarStatusEnum status, String location, String plateNumber, String color) {
        this.carId = carId;
        this.model = model;
        this.status = status;
        this.location = location;
        this.plateNumber = plateNumber;
        this.color = color;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCarId() != null ? getCarId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.getCarId() == null && other.getCarId() != null) || (this.getCarId() != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + getCarId() + " ]";
    }

    /**
     * @return the category
     */
    /**
     * @param category the category to set
     */
    /**
     * @return the model
     */
    public Model getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * @return the status
     */
    public CarStatusEnum getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(CarStatusEnum status) {
        this.status = status;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the plateNumber
     */
    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * @param plateNumber the plateNumber to set
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * @return the enabled
     */
}
