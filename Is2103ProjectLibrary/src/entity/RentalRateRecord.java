/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Keith test
 */
@Entity
public class RentalRateRecord implements Serializable {

    private static long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recordName;
    // private CarCategory category;
    private double rate;
    private List<Date> validityPeriod;

    public RentalRateRecord() {
    }

    public RentalRateRecord(String recordName, double rate, List<Date> validityPeriod) {
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
    public double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(double rate) {
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
    
}
