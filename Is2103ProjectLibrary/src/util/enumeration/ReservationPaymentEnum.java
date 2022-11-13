package util.enumeration;

/**
 *
 * @author dorothyyuan
 */
public enum ReservationPaymentEnum {
    PAID("Paid"),
    ATPICKUP("At Pick Up"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");
    
    private String status;
    private ReservationPaymentEnum(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return status;
    }  
}