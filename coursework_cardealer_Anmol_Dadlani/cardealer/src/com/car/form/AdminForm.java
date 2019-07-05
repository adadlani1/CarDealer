
package com.car.form;

import com.car.roles.Admin;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

//ShowGUI is an interface that includes a method for making the GUI form for the class to be visible
//All of the attributes are implemented in the GUI Form
//Employee is an Abstract class where the Admin and the Staff inherit most of their methods
public class AdminForm {
    private JButton createALoginButton;
    private JLabel menuTitle;
    private JButton addCarsButton;
    private JButton addACarButton;
    private JButton sellCarsButton;
    private JButton printCarsButton;
    private JButton searchForCarsButton;
    private JButton calculateRevenueButton;
    private JPanel panelMain;

    //constructor which contains all of the Action Listeners
    private AdminForm() {
        Admin admin = new Admin();
        addCarsButton.addActionListener(e -> admin.addCars());
        searchForCarsButton.addActionListener(e -> {
            try {
                admin.search();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        //Event Listeners are created for each button and each button leads to a different method
        addACarButton.addActionListener(e -> admin.addACar());
        createALoginButton.addActionListener(e -> admin.createLoginDetails());
        calculateRevenueButton.addActionListener(e -> admin.calculateRevenue());
        sellCarsButton.addActionListener(e -> {
            try {
                admin.sellCars();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        printCarsButton.addActionListener(e -> admin.printCars());
    }

    //Set the Admin Form GUI Form visible
    public static void setVisible() {
        JFrame frame = new JFrame("Admin Menu");
        frame.setContentPane(new AdminForm().panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700,500);
        frame.setVisible(true);
    }
}