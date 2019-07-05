package com.car.form;

import com.car.model.Car;
import com.car.utils.Constants;
import com.car.utils.DatabaseOperations;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

//ShowGUI is an interface that includes a method for making the GUI form for the class to be visible
//All of the attributes are implemented in the GUI Form
public class CalculateRevenue {
    private static long sum;
    private JPanel panelMain;
    private JButton calculateRevenueButton;
    private JComboBox yearComboBox;
    private JComboBox monthComboBox;
    private JComboBox dayComboBox;
    private String month;
    private String year;
    private String day;
    private String date;

    //constructor containing the showResults body of code with Action Listeners and accessing other methods in the class
    //and to initialize the object of a class
    private CalculateRevenue() {
        calculateRevenueButton.addActionListener(e -> {
            //from the text fields, the information is assigned to global variables
            month = (String) monthComboBox.getSelectedItem();
            year = (String) yearComboBox.getSelectedItem();
            day = (String) dayComboBox.getSelectedItem();

            if(!(month.isEmpty() && year.isEmpty())) {

                //the date is formed with the information
                date = year + "-" + month + "-" + day;

                //checking if the fields are not empty
                assert day != null;
                readDatabase();

                //a message shows the user how much the revenue is for the month or day in question
                JOptionPane.showMessageDialog(null, "The total revenue for the date you " +
                        "provided is: £" + sum);
                sum = 0;
            } else
                JOptionPane.showMessageDialog(null, "Please fill in all of the starred details.");

        });

        monthComboBox.addActionListener(e -> {
            String month = (String) monthComboBox.getSelectedItem();
            int year = Integer.parseInt((String) Objects.requireNonNull(yearComboBox.getSelectedItem()));
            assert month != null;
            DatabaseOperations.addDaysToComboBox(month, year, dayComboBox);
        });
    }

    //if only the month and year is provided by the user, the month and year is turned into a string format
    //e.g. 2013-07 becomes 201307
    private String returnStr(String arrivalDate) {
        String year = arrivalDate.substring(0,4);
        String month = arrivalDate.substring(5,7);

        return year + month;
    }

    //method to show the GUI to the user
    public static void setVisible() {
        JFrame frame2 = new JFrame("Calculate Revenue");
        frame2.setContentPane(new CalculateRevenue().panelMain);
        frame2.setSize(500,300);
        frame2.setVisible(true);
    }

    //the cars are added to a list of objects (cars) from the database
    private void readDatabase() {
        List<Car> car;
        car = DatabaseOperations.readCarsFromDatabase(Constants.CARS_DATABASE_FILE);

        infoForEachCar(car);

    }

    //if all three fields are provided with values, this method is accessed
    //if the dates are equal then the price of the car is added to sum
    private void allFieldsUsed(String arrivalDate, double price) {
        if (date.equals(arrivalDate)) {
            sum += price;
        }
    }

    //this method gets the price and arrival date of every car for comparison later on
    private void infoForEachCar(List<Car> car) {
        for (Car eachCar : car) {
            Double price = eachCar.getPrice();
            String arrivalDate = eachCar.getLastSoldDate();
            if(!arrivalDate.equals("N/A")) {
                //if the day field is not empty, this method is accessed
                if (!day.equals(""))
                    allFieldsUsed(arrivalDate, price);

                //if the day field is empty, this method is accessed
                else {
                    monthFieldAndYearFieldUsed(arrivalDate, price);
                }
            }

        }
    }

    //when only two of the fields have been used
    private void monthFieldAndYearFieldUsed(String arrivalDate, double price) {
        String databaseDate = returnStr(arrivalDate);
        String enteredDate = returnStr(date);

        compareDates(databaseDate, enteredDate, price);
    }

    //the two dates are compared for the month and if they match then the price is added to the sum
    private void compareDates(String databaseDate, String enteredDate, double price){
        if(databaseDate.equals(enteredDate)){
            sum+=price;
        }
    }
}
