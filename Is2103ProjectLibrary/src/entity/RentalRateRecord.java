/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import util.enumeration.RentalRateEnum;

/**
 *
 * @author Keith
 */
@Entity
public class RentalRateRecord implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 64)
    private String recordName;
    private RentalRateEnum type;
    @ManyToOne
    private Category category;
    @Column(precision = 11, scale = 2)
    private Double rate;
    @Column(nullable = false, length = 64)
    private List<Date> validityPeriod;
    private boolean enabled;

    public RentalRateRecord() {
    }

    public RentalRateRecord(String recordName, RentalRateEnum type, Double rate, Date startDateTime, Date endDateTime) {
        this.recordName = recordName;
        this.type = type;
        this.rate = rate;
        ArrayList<Date> validity = new ArrayList<Date>();
        validity.add(startDateTime);
        validity.add(endDateTime);
        this.validityPeriod = validity;
    }   
    
    
    

    public RentalRateRecord(String recordName, Double rate, List<Date> validityPeriod) {
        this.recordName = recordName;
        this.rate = rate;
        this.validityPeriod = validityPeriod;
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
        if (!(object instanceof RentalRateRecord)) {
            return false;
        }
        RentalRateRecord other = (RentalRateRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRateRecord[ id=" + id + " ]";
    }

    /**
     * @return the recordName
     */
    public String getRecordName() {
        return recordName;
    }

    /**
     * @param recordName the recordName to set
     */
    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    /**
     * @return the rate
     */
    public Double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(Double rate) {
        this.rate = rate;
    }

    /**
     * @return the validityPeriod
     */
    public List<Date> getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(List<Date> validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the enabled
     */
    public boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the type
     */
    public RentalRateEnum getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(RentalRateEnum type) {
        this.type = type;
    }
    
}
