package com.car.model;

import java.io.Serializable;

//Van extends car as it inherits the characteristics of the var
// implements serializable for the comparator
public class Van extends Car implements Serializable {
    private String vanSize;

    public Van() {
    }

    //super class in this constructor that gets the info for the van from the car class
    public Van(String regNumber, String model, String type, String vanSize, String colour, Long mileage,
               String accidentHistory, String transmissionType, Double price, String purchaseDate, String lastSoldDate) {
        super(regNumber, model, type, colour, mileage, accidentHistory, transmissionType, price, purchaseDate, lastSoldDate);
        this.vanSize = vanSize;
    }

    //getter and setter for the van attribute
    public String getVanSize() {
        return vanSize;
    }

    public void setVanSize(String vanSize) {
        this.vanSize = vanSize;
    }
}
