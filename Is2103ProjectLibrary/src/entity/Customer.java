/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Keith test
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    @ManyToOne
    private Partner partner;
    private String mobileNumber;
    private String passportNumber;
    @Column(nullable = false, unique = true)
    private String email;
    @OneToMany (mappedBy = "customer")
    private List<Reservation> reservations;
//    @OneToMany(mappedBy = "customer")
//    private CreditCard creditCard;
    
    /* login credentials
    @Column(unique = true)
    private String username;
    private String password;
    */
    
    public Customer() {
        
    }

    public Customer(Long customerId, Partner partner, String mobileNumber, String passportNumber, String email, List<Reservation> reservations, CreditCard cc) {
        this.customerId = customerId;
        this.partner = partner;
        this.mobileNumber = mobileNumber;
        this.passportNumber = passportNumber;
        this.email = email;
        this.reservations = reservations;
//        this.creditCard = creditCard;
    }

    public Long getCustomerId() {
        return customerId;
        
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * @param mobileNumber the mobileNumber to set
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return the passportNumber
     */
    public String getPassportNumber() {
        return passportNumber;
    }

    /**
     * @param passportNumber the passportNumber to set
     */
    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @return the cc
     */
//    public CreditCard getCc() {
//        return creditCard;
//    }
//
//    /**
//     * @param cc the cc to set
//     */
//    public void setCc(CreditCard creditCard) {
//        this.creditCard = creditCard;
//    }
    
}
