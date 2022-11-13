/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateRecordSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author Keith test
 */
public class Main {
    
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;
    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    @EJB
    private static TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    @EJB
    private static CategorySessionBeanRemote categorySessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;
    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static RentalRateRecordSessionBeanRemote rentalRateRecordSessionBeanRemote;
    @EJB    
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;


    public static void main(String[] args) {
        // TODO code application logic here

        MainApp mainApp = new MainApp(modelSessionBeanRemote, carSessionBeanRemote,
                transitDriverDispatchRecordSessionBeanRemote, outletSessionBeanRemote,
                employeeSessionBeanRemote, categorySessionBeanRemote, rentalRateRecordSessionBeanRemote,
                customerSessionBeanRemote, reservationSessionBeanRemote);
        mainApp.runApp();
    }

}
