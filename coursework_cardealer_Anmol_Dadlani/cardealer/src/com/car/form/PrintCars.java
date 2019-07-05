package com.car.form;

import com.car.model.Car;
import com.car.model.Van;
import com.car.utils.Constants;
import com.car.utils.DatabaseOperations;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//ShowGUI is an interface that includes a method for making the GUI form for the class to be visible
//All of the attributes are implemented in the GUI Form
public class PrintCars{
    private JButton printButton;
    private JPanel panelMain;

    //constructor containing the showResults body of code with Action Listeners and accessing other methods in the class
    //and to initialize the object of a class
    private PrintCars() {
        printButton.addActionListener(e -> {
            try {
                wipeFile();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            try {
                sorting();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }


    public static void setVisible() {

        JFrame frame2 = new JFrame("Calculate Revenue");
        frame2.setContentPane(new PrintCars().panelMain);
        frame2.setSize(700, 100);
        frame2.setVisible(true);
    }

    //method reads the database file and adds each car to an array list
    private void sorting() throws IOException {
        ArrayList<Car> carList;
        carList = (ArrayList<Car>) DatabaseOperations.readCarsFromDatabase(Constants.CARS_DATABASE_FILE);
        ArrayList<Car> unsoldCarList = new ArrayList<>();
        ArrayList<Car> soldCarList = new ArrayList<>();

        //the cars are read and if it has not been sold, it gets added to the unsold list
        // if it is sold then it gets added to the sold list
        for (Car eachCars : carList){

            if (eachCars.getLastSoldDate().equals("N/A")) {
                unsoldCarList.add(eachCars);
            } else {
                soldCarList.add(eachCars);
            }
            }

        //A comparator is used to sort the list by the date
        unsoldCarList.sort(Car.CarUnsoldComparator);
        soldCarList.sort(Car.CarSoldComparator);

        //the car is added to a string array
        ArrayList<String> unsoldCarStringArray = addToStringArray(unsoldCarList);
        ArrayList<String> soldCarStringArray = addToStringArray(soldCarList);

        //now it is written to the file
        writeToFile(soldCarStringArray, "Sold Cars:");
        writeToFile(unsoldCarStringArray, "Unsold Cars:");

        //message pops up to tell the user that the list can be printed
        JOptionPane.showMessageDialog(null, "All of the vehicles have been sorted and can " +
                "now be printed.");

    }

    //it depends if the object is a car or a van as a van contains getVanSize and a car does not
    private ArrayList<String> addToStringArray(List<Car> arrayList){
        ArrayList<String> CarStringArray = new ArrayList<>();
        for (Car car : arrayList) {

            if(car instanceof Van) {

                String vanStr = car.getRegNumber() + "|"
                        + car.getModel() + "|"
                        + car.getType() + "|"
                        + ((Van) car).getVanSize() + "|"
                        + car.getColour() + "|"
                        + car.getAccidentHistory() + "|"
                        + car.getTransmissionType() + "|"
                        + car.getPrice() + "|"
                        + car.getPurchaseDate() + "|"
                        + car.getLastSoldDate();
                CarStringArray.add(vanStr);


            } else {
                String carStr = car.getRegNumber() + "|"
                        + car.getModel() + "|"
                        + car.getType() + "|" + "" + "|"
                        + car.getColour() + "|"
                        + car.getAccidentHistory() + "|"
                        + car.getTransmissionType() + "|"
                        + car.getPrice() + "|"
                        + car.getPurchaseDate() + "|"
                        + car.getLastSoldDate();
                CarStringArray.add(carStr);
            }
        }
        return CarStringArray;
    }

    //method to write the string array to the print-cars file
    private void writeToFile(ArrayList<String> carStringArray, String nameOfList) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.CARS_PRINT_FILE, true));
        writer.write(nameOfList);
        writer.newLine();
        for (String carInStringArray : carStringArray) {
            writer.write(carInStringArray);
            writer.newLine();
        }
        writer.newLine();
        writer.close();
    }

    //the file is wiped as this function is accessed
    private void wipeFile() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(Constants.CARS_PRINT_FILE);
        writer.print("");
        writer.close();
    }

}