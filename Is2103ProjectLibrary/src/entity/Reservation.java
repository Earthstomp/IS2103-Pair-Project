/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Keith test
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    private Car car;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Outlet pickUpLocation;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Outlet returnLocation;
    @ManyToMany
//    @JoinColumn(nullable = false) // not sure if correct
    private List<RentalRateRecord> rentalRates;
    @Column(nullable = false, length = 32)
    private String paymentStatus;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    public Reservation() {
    }

    public Reservation(Date startDateTime, Date endDateTime, Outlet pickUpLocation, Outlet returnLocation, String paymentStatus, Customer customer) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.pickUpLocation = pickUpLocation;
        this.returnLocation = returnLocation;
        this.paymentStatus = paymentStatus;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + id + " ]";
    }

    /**
     * @return the startDateTime
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public Date getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the pickUpLocation
     */
    public Outlet getPickUpLocation() {
        return pickUpLocation;
    }

    /**
     * @param pickUpLocation the pickUpLocation to set
     */
    public void setPickUpLocation(Outlet pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    /**
     * @return the returnLocation
     */
    public Outlet getReturnLocation() {
        return returnLocation;
    }

    /**
     * @param returnLocation the returnLocation to set
     */
    public void setReturnLocation(Outlet returnLocation) {
        this.returnLocation = returnLocation;
    }

    /**
     * @return the paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus the paymentStatus to set
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return the rentalRates
     */
    public List<RentalRateRecord> getRentalRates() {
        return rentalRates;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRateRecord> rentalRates) {
        this.rentalRates = rentalRates;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
