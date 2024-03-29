/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Keith test
 */
@Entity
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column (nullable = false)
    private String categoryName;
    
    @OneToMany (mappedBy = "category")
    private List<Model> models;
    @OneToMany (mappedBy = "category")
    private List<RentalRateRecord> rateRecords;
    
    public Category() {
        
    }
    
    public Category(String categoryName) {
        this.categoryName = categoryName;
        this.models = new ArrayList<Model>();
        this.rateRecords = new ArrayList<RentalRateRecord>();
    }

    public Category(Long categoryId, String categoryName, List<Model> models, List<RentalRateRecord> rateRecords) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.models = models;
        this.rateRecords = rateRecords;
    }
    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof Category)) {
            return false;
        }
        Category other = (Category) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Category[ id=" + categoryId + " ]";
    }

    /**
     * @return the models
     */
    public List<Model> getModels() {
        return models;
    }

    /**
     * @param models the models to set
     */
    public void setModels(List<Model> models) {
        this.models = models;
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the rateRecords
     */
    public List<RentalRateRecord> getRateRecords() {
        return rateRecords;
    }

    /**
     * @param rateRecords the rateRecords to set
     */
    public void setRateRecords(List<RentalRateRecord> rateRecords) {
        this.rateRecords = rateRecords;
    }
    
}
