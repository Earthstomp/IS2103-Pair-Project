/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package is2103projectclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Employee;
import javax.ejb.EJB;

/**
 *
 * @author Keith test
 */
public class Main {

    /**
     * @param args the command line arguments
     */
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


    public static void main(String[] args) {
        // TODO code application logic here

        MainApp mainApp = new MainApp(modelSessionBeanRemote, carSessionBeanRemote, transitDriverDispatchRecordSessionBeanRemote, outletSessionBeanRemote, employeeSessionBeanRemote, categorySessionBeanRemote);
        mainApp.runApp();
    }

}
