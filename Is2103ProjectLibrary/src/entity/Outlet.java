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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;

/**
 *
 * @author Keith test
 */
@Entity
public class Outlet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outletId;
    private String name;
    private int openingHour;
    private int closingHour;
    @OneToMany (mappedBy = "outlet")
    private List<Employee> employees;
    @OneToMany (mappedBy = "returnLocation")
    private List<TransitDriverDispatchRecord> dispatches;

    public Outlet() {
    }

    public Outlet(String location, int openingHour, int closingHour) {
        this.name = location;
        this.openingHour = openingHour;
        this.closingHour = closingHour;
    }

    public Long getOutletId() {
        return outletId;
    }

    public void setOutletId(Long outletId) {
        this.outletId = outletId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (outletId != null ? outletId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the outletId fields are not set
        if (!(object instanceof Outlet)) {
            return false;
        }
        Outlet other = (Outlet) object;
        if ((this.outletId == null && other.outletId != null) || (this.outletId != null && !this.outletId.equals(other.outletId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Outlet[ id=" + outletId + " ]";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the openingTime
     */


    /**
     * @return the closingTime
     */


    /**
     * @return the employees
     */
    public List<Employee> getEmployees() {
        return employees;
    }

    /**
     * @param employees the employees to set
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    /**
     * @return the dispatches
     */
    public List<TransitDriverDispatchRecord> getDispatches() {
        return dispatches;
    }

    /**
     * @param dispatches the dispatches to set
     */
    public void setDispatches(List<TransitDriverDispatchRecord> dispatches) {
        this.dispatches = dispatches;
    }

    /**
     * @return the closingHour
     */
    public int getClosingHour() {
        return closingHour;
    }

    /**
     * @param closingHour the closingHour to set
     */
    public void setClosingHour(int closingHour) {
        this.closingHour = closingHour;
    }

    /**
     * @return the openingHour
     */
    public int getOpeningHour() {
        return openingHour;
    }

    /**
     * @param openingHour the openingHour to set
     */
    public void setOpeningHour(int openingHour) {
        this.openingHour = openingHour;
    }
}
