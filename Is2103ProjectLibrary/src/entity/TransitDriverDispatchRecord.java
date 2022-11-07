/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Keith test
 */
@Entity
public class TransitDriverDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Employee employee;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Outlet pickupLocation;
    @JoinColumn(nullable = false)
    @ManyToOne
    private Outlet returnLocation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDateTime;
    private String status;

    public TransitDriverDispatchRecord() {
    }

    public TransitDriverDispatchRecord(Outlet pickupLocation, Outlet returnLocation, Date startDateTime) {
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.startDateTime = startDateTime;
    }
    
    public TransitDriverDispatchRecord(Outlet pickupLocation, Outlet returnLocation, Date startDateTime, Date endDateTime, Employee employee) {
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.employee = employee;
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
        if (!(object instanceof TransitDriverDispatchRecord)) {
            return false;
        }
        TransitDriverDispatchRecord other = (TransitDriverDispatchRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatchRecord[ id=" + id + " ]";
    }

    /**
     * @return the pickupLocation
     */
    public Outlet getPickupLocation() {
        return pickupLocation;
    }

    /**
     * @param pickupLocation the pickupLocation to set
     */
    public void setPickupLocation(Outlet pickupLocation) {
        this.pickupLocation = pickupLocation;
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
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

}
