package is2103projectclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.Model;
import entity.Category;
import entity.Customer;
import entity.Reservation;
import java.util.List;
import java.util.Scanner;
import util.enumeration.CarStatusEnum;
import util.exception.CarNotFoundException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Keith test
 */
public class SalesManagementModule {

    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private CategorySessionBeanRemote categorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;

    public SalesManagementModule() {
    }

    public SalesManagementModule(ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote) {
        this();
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
    }

    public void menuSalesManagementModule() {// throws InvalidAccessRightException 

//        if(currentStaffEntity.getAccessRightEnum() != AccessRightEnum.MANAGER)
//        {
//            throw new InvalidAcFcessRightException("You don't have MANAGER rights to access the system administration module.");
//    }
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Management System :: Sales Management ***\n");
            System.out.println("1: Create New model");
            System.out.println("2: Pickup Car");
            System.out.println("3: Return Car");
            System.out.println("-----------------------");
            System.out.println("4: Create New Product");
            System.out.println("5: View Product Details");
            System.out.println("6: View All Products");
            System.out.println("-----------------------");
            System.out.println("7: Back\n");
            response = 0;

            while (response < 1 || response > 7) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewModel();
                } else if (response == 2) {
                    doPickUpCar();
                } else if (response == 3) {
                    doReturnCar();
                 // else if (response == 4) {
//                    doCreateNewProduct();
//                } else if (response == 5) {
//                    doViewProductDetails();
//                } else if (response == 6) {
//                    doViewAllProducts();
                } else if (response == 7) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 7) {
                break;
            }
        }

    }

    public void doCreateNewModel() {
        Scanner scanner = new Scanner(System.in);
        Model model = new Model();
        int i = 1; //  index of menu

        System.out.println("*** Management System :: Sales Management :: Create New Model ***\n");
        System.out.print("Choose category to create model in> ");
        List<Category> categories = categorySessionBeanRemote.retrieveAllCategories();
        for (Category c : categories) {
            System.out.println(i + ". " + c.getCategoryName());
            i++;
        }
        while (true) {
            Integer categoryChoice = scanner.nextInt();

            if (categoryChoice <= categories.size() && categoryChoice > 0) {
                model.setCategory(categories.get(categoryChoice - 1));
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        System.out.print("Enter Make and Model> ");
        model.setModel(scanner.nextLine().trim());

//        try { need to try and catch exception
        Long newModelId = categorySessionBeanRemote.createNewModelWithExistingCategoryClass(model, model.getCategory());
        System.out.println("New model created successfully!: " + model.getModel() + "\n");
    }

    public void doPickUpCar() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Pickup Car ***\n");
        System.out.println("Enter Customer Mobile Number> ");
        // need exception
        Integer mobileNumber = scanner.nextInt();
        Customer customer = customerSessionBeanRemote.retrieveCustomerByMobileNumber(mobileNumber);
        List<Reservation> reservations = customer.getReservations();
        Reservation selectedReservation;

        System.out.print("Choose reservation> ");
        for (Reservation r : customer.getReservations()) {
            System.out.println(i + ". " + r.getId());
            i++;
        }
        while (true) {
            Integer reservationChoice = scanner.nextInt();

            // need to check if reservation is still active
            if (reservationChoice <= reservations.size() && reservationChoice > 0) {
                selectedReservation = reservations.get(reservationChoice - 1);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        if (selectedReservation.getPaymentStatus() == "false") { // convert to boolean?
            // request for payment using credit card classes
        } else { // payment has been made
            try {
                Car reservedCar = carSessionBeanRemote.retrieveCarById(selectedReservation.getCar().getCarId());
                carSessionBeanRemote.updateCarStatusLocation(reservedCar, CarStatusEnum.RESERVED, customer.getMobileNumber()); // using mobile number to uniquely identify customer
                System.out.println("Car " + reservedCar.getPlateNumber() + " for Reservation " + selectedReservation.getId() + "has been picked up!\n");
            } catch (CarNotFoundException ex) {
                System.out.println("Car not found!");
            }
        }
    }

    public void doReturnCar() {
        Scanner scanner = new Scanner(System.in);
        int i = 1;

        System.out.println("*** Management System :: Sales Management :: Return Car ***\n");
        System.out.println("Enter Customer Mobile Number> ");
        // need exception
        Integer mobileNumber = scanner.nextInt();
        Customer customer = customerSessionBeanRemote.retrieveCustomerByMobileNumber(mobileNumber);
        List<Reservation> reservations = customer.getReservations();
        Reservation selectedReservation;

        System.out.print("Choose reservation> ");
        for (Reservation r : customer.getReservations()) {
            System.out.println(i + ". " + r.getId());
            i++;
        }
        while (true) {
            Integer reservationChoice = scanner.nextInt();

            // need to check if reservation is still active and has been picked up
            if (reservationChoice <= reservations.size() && reservationChoice > 0) {
                selectedReservation = reservations.get(reservationChoice - 1);
                break;
            } else {
                System.out.println("Invalid option, please try again!\n");
            }
        }
        try {
            Car reservedCar = carSessionBeanRemote.retrieveCarById(selectedReservation.getCar().getCarId());

            carSessionBeanRemote.updateCarStatusLocation(reservedCar, CarStatusEnum.AVAILABLE, selectedReservation.getReturnLocation().toString()); // using mobile number to uniquely identify customer
            System.out.println("Car " + reservedCar.getPlateNumber() + " for Reservation " + selectedReservation.getId() + "has been returned!\n");
        } catch (CarNotFoundException ex) {
            System.out.println("Car not found!");
        }
    }
}
