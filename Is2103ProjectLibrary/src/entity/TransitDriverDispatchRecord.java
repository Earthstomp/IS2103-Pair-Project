/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    // private Driver driver;
    private String pickupLocation;
    private String returnLocation;
    private Date startDate;
    private Date endDate;

    public TransitDriverDispatchRecord() {
    }

    public TransitDriverDispatchRecord(String pickupLocation, String returnLocation, Date startDate, Date endDate) {
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.startDate = startDate;
        this.endDate = endDate;
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
    public String getPickupLocation() {
        return pickupLocation;
    }

    /**
     * @param pickupLocation the pickupLocation to set
     */
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * @return the returnLocation
     */
    public String getReturnLocation() {
        return returnLocation;
    }

    /**
     * @param returnLocation the returnLocation to set
     */
    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
