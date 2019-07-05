package com.car.roles;

import com.car.form.*;

import java.io.FileNotFoundException;
import java.io.IOException;

//This is a staff class that extends the employee class and implements the contactForms interface
//the employee contains all of the functions admin and staff have in common
// the contactForms interface is used to load up the GUI for the staff
public class Staff extends Employee implements contactForms {

    @Override
    public void sellCars() throws FileNotFoundException {
        SellCars.setVisible();
    }

    @Override
    public void addCars() {
        AddCars.setVisible();
    }

    @Override
    public void addACar() {
        AddACar.setVisible();
    }

    @Override
    public void calculateRevenue() {
        CalculateRevenue.setVisible();
    }

    @Override
    public void printCars() {
        PrintCars.setVisible();
    }

    @Override
    public void search() throws IOException {
        Search.setVisible();

    }

    @Override
    public void contactForm() {
        StaffForm.setVisible();
    }
}
