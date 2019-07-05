package com.car.utils;

import com.car.model.Car;
import com.car.model.Van;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

//class that includes methods for basic database manipulation
public class DatabaseOperations {

    //this function reads cars from the database and puts it into a list of objects
    public static List<Car> readCarsFromDatabase(String databaseFileName) {
        ArrayList<Car> carList = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(databaseFileName));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] carDetails = line.split("\\|");

                Car newCar;

                //checks if the 4th element in car info string array is empty
                // if it is empty, it is a car and not a van, vice versa
                if (carDetails[3].length() == 0) {
                    newCar = new Car();
                } else {
                    newCar = new Van();
                    ((Van) newCar).setVanSize(carDetails[3]);
                }

                //sets the value of each element in the setters in the car class
                newCar.setRegNumber(carDetails[0]);
                newCar.setModel(carDetails[1]);
                newCar.setType(carDetails[2]);
                newCar.setColour(carDetails[4]);
                newCar.setMileage(Long.valueOf(carDetails[5]));
                newCar.setAccidentHistory(carDetails[6]);
                newCar.setTransmissionType(carDetails[7]);
                newCar.setPrice(Double.valueOf(carDetails[8]));
                newCar.setPurchaseDate(carDetails[9]);
                newCar.setLastSoldDate(carDetails[10]);

                carList.add(newCar);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return carList;
    }

   public static void addDaysToComboBox(String month, int year, JComboBox comboBoxName) {

        if (month.equals("01") || month.equals("03") || month.equals("05") || month.equals("07")
                || month.equals("08") || month.equals("10") || month.equals("12")) {
            comboBoxName.removeAllItems();
            comboBoxName.addItem("");
            for (int i = 1; i <= 31; i++) {
                comboBoxName.addItem(String.format("%02d", i));
            }
        } else if (month.equals("04") || month.equals("06") || month.equals("09")
                || month.equals("11")) {
            comboBoxName.removeAllItems();
            comboBoxName.addItem("");
            for (int i = 1; i <= 30; i++) {
                comboBoxName.addItem(String.format("%02d", i));
            }
        } else if (year % 4 == 0) {
            comboBoxName.removeAllItems();
            comboBoxName.addItem("");
            for (int i = 1; i <= 29; i++) {
                comboBoxName.addItem(String.format("%02d", i));
            }
        } else {
            comboBoxName.removeAllItems();
            comboBoxName.addItem("");
            for (int i = 1; i <= 28; i++) {
                comboBoxName.addItem(String.format("%02d", i));
            }
        }
    }

}
