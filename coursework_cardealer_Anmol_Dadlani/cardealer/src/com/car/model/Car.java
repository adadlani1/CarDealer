package com.car.model;

import com.car.utils.Constants;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

//the car class turned into a class that returns the info for each car when called
public class Car implements Serializable {
    private String regNumber;
    private String model;
    private String type;
    private String colour;
    private Long mileage;
    private String accidentHistory;
    private String transmissionType;
    private Double price;
    private String purchaseDate;
    private String lastSoldDate;

    public Car() {
    }

    //constructor which saves the input data into a global variable
    public Car(String regNumber, String model, String type, String colour, Long mileage,
               String accidentHistory, String transmissionType, Double price, String purchaseDate,
               String lastSoldDate) {
        this.regNumber = regNumber;
        this.model = model;
        this.type = type;
        this.colour = colour;
        this.mileage = mileage;
        this.accidentHistory = accidentHistory;
        this.transmissionType = transmissionType;
        this.price = price;
        this.purchaseDate = purchaseDate;
        this.lastSoldDate = lastSoldDate;
    }

    //setters and getters for each of the attributes of the car
    //does not include van size as it is specific to a van not a car
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Long getMileage() {
        return mileage;
    }

    public void setMileage(Long mileage) {
        this.mileage = mileage;
    }

    public String getAccidentHistory() {
        return accidentHistory;
    }

    public void setAccidentHistory(String accidentHistory) {
        this.accidentHistory = accidentHistory;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getLastSoldDate() {
        return lastSoldDate;
    }

    public void setLastSoldDate(String lastSoldDate) {
        this.lastSoldDate = lastSoldDate;
    }

    //comparators that compares the arrival date for the car that is not sold and the sold date for the cars that are
    public static Comparator<Car> CarUnsoldComparator = (c1, c2) -> {
        LocalDate d1 = LocalDate.parse(c1.getPurchaseDate(), DateTimeFormatter.ofPattern(Constants.CARS_DATE_FORMAT));
        LocalDate d2 = LocalDate.parse(c2.getPurchaseDate(), DateTimeFormatter.ofPattern(Constants.CARS_DATE_FORMAT));

        return d1.compareTo(d2);
    };

    public static Comparator<Car> CarSoldComparator = (c1, c2) -> {
        LocalDate d1 = LocalDate.parse(c1.getLastSoldDate(), DateTimeFormatter.ofPattern(Constants.CARS_DATE_FORMAT));
        LocalDate d2 = LocalDate.parse(c2.getLastSoldDate(), DateTimeFormatter.ofPattern(Constants.CARS_DATE_FORMAT));

        return d1.compareTo(d2);

    };
}