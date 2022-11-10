package util.enumeration;

/**
 *
 * @author dorothyyuan
 */
public enum RentalRateEnum {
    DEFAULT("Default"),
    PROMOTION("Promotion"),
    PEAK("Peak");
    
    private String type;
    private RentalRateEnum(String type) {
        this.type = type;
    }
    
    @Override
    public String toString() {
        return type;
    }   
}