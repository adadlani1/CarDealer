package com.car.form;

import com.car.roles.Staff;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

//ShowGUI is an interface that includes a method for making the GUI form for the class to be visible
//All of the attributes are implemented in the GUI Form
//Employee is an Abstract class where the Admin and the Staff inherit most of their methods
public class StaffForm {
    private JButton searchForCarsButton;
    private JButton sellCarsButton;
    private JButton printCarsButton;
    private JButton calculateRevenueButton;
    private JButton addCarsButton;
    private JButton addACarButton;
    private JPanel panelMain;

    //constructor containing the showResults body of code with Action Listeners and accessing other methods in the class
    //and to initialize the object of a class
    private StaffForm() {
        assert false;
        Staff staff = new Staff();
        addCarsButton.addActionListener(e -> {
            staff.addCars();
        });
        addACarButton.addActionListener(e ->{
            staff.addACar();
        });
        searchForCarsButton.addActionListener(e -> {
            try {
                staff.search();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        sellCarsButton.addActionListener(e -> {
            try {
                staff.sellCars();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        printCarsButton.addActionListener(e -> staff.printCars());
        calculateRevenueButton.addActionListener(e -> staff.calculateRevenue());
    }

    public static void setVisible() {

        JFrame frame = new JFrame("Staff Menu");
        frame.setContentPane(new StaffForm().panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700,500);
        frame.setVisible(true);

    }
}