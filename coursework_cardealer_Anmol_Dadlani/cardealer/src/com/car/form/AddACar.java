package com.car.form;

import com.car.model.Car;
import com.car.utils.Constants;
import com.car.utils.DatabaseOperations;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

//ShowGUI is an interface that includes a method for making the GUI form for the class to be visible
//All of the attributes are implemented in the GUI Form
public class AddACar {
    private JPanel panelMain;
    private JTextField regNumField;
    private JTextField carNameField;
    private JTextField colourField;
    private JTextField mileageField;
    private JTextField accHistField;
    private JTextField priceField;
    private JLabel regNum;
    private JLabel carName;
    private JLabel type;
    private JLabel vanSize;
    private JLabel colour;
    private JLabel mileage;
    private JLabel accidentHistory;
    private JLabel price;
    private JLabel datePurchased;
    private JLabel dateSold;
    private JButton addCarButton;
    private JComboBox yearSoldComboBox;
    private JComboBox monthSoldComboBox;
    private JComboBox daySoldComboBox;
    private JComboBox yearArrivedComboBox;
    private JComboBox monthArrivedComboBox;
    private JComboBox dayArrivedComboBox;
    private JComboBox typeComboBox;
    private JComboBox vanSizeComboBox;
    private JComboBox transmissionComboBox;
    private String regNumber;
    private String nameOfCar;
    private String carType;
    private String sizeOfVan;
    private String carColour;
    private Long carMileage;
    private String carAccHistory;
    private Double carPrice;
    private String carDateArrived;
    private String carDateSold;
    private String transmissionType;
    private boolean notDuplicate;
    private boolean noErrors = true;

    //This Constructor gets all off the text in the text fields in the GUI and assigns them to a variable
    private AddACar() {
        assert false;
        addCarButton.addActionListener(e -> {
            //checks if all of the details have been filled
            if (!(yearArrivedComboBox.getSelectedItem().equals("") || monthArrivedComboBox.getSelectedItem().equals("")
                    || dayArrivedComboBox.getSelectedItem().equals("") && regNumField.getText().isEmpty()
                    && carNameField.getText().isEmpty() && colourField.getText().isEmpty()
                    && mileageField.getText().isEmpty() && accHistField.getText().isEmpty()
                    && priceField.getText().isEmpty())) {
                getCarDetails();

                notDuplicate = checkIfCarAlreadyPresent();

                carDateArrived = enteredDateToUsableFormat(yearArrivedComboBox, monthArrivedComboBox, dayArrivedComboBox);

                if (yearSoldComboBox.getSelectedItem().equals("") || monthSoldComboBox.getSelectedItem().equals("") ||
                        daySoldComboBox.getSelectedItem().equals("")) {

                    //If any of the comboBoxes are left blank as it is still on sale, N/A will be filled in automatically
                    carDateSold = "N/A";
                } else {

                    carDateSold = enteredDateToUsableFormat(yearSoldComboBox, monthSoldComboBox, daySoldComboBox);
                }
                //Details for the new car are put together in the format of the other cars in the database
                String newCarDetails = regNumber + "|" + nameOfCar + "|" + carType + "|" + sizeOfVan + "|" + carColour + "|" +
                        carMileage + "|" + carAccHistory + "|" + transmissionType + "|" + carPrice + "|"
                        + carDateArrived + "|" + carDateSold;

                writeToFile(newCarDetails);
            } else
                JOptionPane.showMessageDialog(null, "Please fill in all of the starred details.");

        });
        monthArrivedComboBox.addActionListener(e -> {

            String monthArrived = (String) monthArrivedComboBox.getSelectedItem();
            int year = Integer.parseInt((String) yearArrivedComboBox.getSelectedItem());
            DatabaseOperations.addDaysToComboBox(monthArrived, year, dayArrivedComboBox);
        });
        monthSoldComboBox.addActionListener(e -> {

            String monthSold = (String) monthSoldComboBox.getSelectedItem();
            int year = Integer.parseInt((String) yearArrivedComboBox.getSelectedItem());
            DatabaseOperations.addDaysToComboBox(monthSold, year, daySoldComboBox);
        });
        typeComboBox.addActionListener(e -> {

            if (typeComboBox.getSelectedItem().equals("Van"))
                vanSizeComboBox.setEnabled(true);
        });
    }

    public static void setVisible() {
        JFrame frame2 = new JFrame("Add A Car");
        frame2.setContentPane(new AddACar().panelMain);
        frame2.setSize(500, 700);
        frame2.setVisible(true);

    }

    private boolean checkIfCarAlreadyPresent() {
        ArrayList<Car> carList = (ArrayList<Car>) DatabaseOperations.readCarsFromDatabase
                (Constants.CARS_DATABASE_FILE);
        for (Car car : carList) {
            if (car.getRegNumber().equals(regNumField.getText())) {
                JOptionPane.showMessageDialog(null, "This Car is already in the database.");
                return false;
            }
        }
        return true;
    }

    //function gets all of details of the new car that has been entered in the GUI.
    private void getCarDetails() {
        regNumber = regNumField.getText().toUpperCase().trim();
        nameOfCar = capitaliseWord(carNameField.getText().toLowerCase());
        carType = (String) typeComboBox.getSelectedItem();
        sizeOfVan = ((String) vanSizeComboBox.getSelectedItem()).toLowerCase();
        carColour = colourField.getText().toLowerCase();
        transmissionType = ((String) transmissionComboBox.getSelectedItem()).toLowerCase();
        try {
            carMileage = Long.valueOf(mileageField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter an integer for the mileage.");
            noErrors = false;

        }
        carAccHistory = accHistField.getText().toLowerCase();
        try {
            carPrice = Double.valueOf(priceField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please enter an integer for the price.");
            noErrors = false;
        }

        if (!(carType.equals("Van"))) {
            sizeOfVan = "";
        }
    }

    private void writeToFile(String newCarDetails) {
        if (notDuplicate && noErrors) {
            //The new car is appended to the database using BufferedWriter
            try (FileWriter fileWriter = new FileWriter("cars-database.txt", true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
                printWriter.println(newCarDetails);
            } catch (IOException ignored) {
            }
            //A dialog message pops up telling the user that the car has been added to the database

            JOptionPane.showMessageDialog(null, "The car has been added to the database.");

        }
    }

    private String enteredDateToUsableFormat(JComboBox yearComboBox, JComboBox monthComboBox, JComboBox dayComboBox) {
        String year = (String) yearComboBox.getSelectedItem();
        String month = (String) monthComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();

        return year + "-" + month + "-" + day;
    }

    //when certain phrases are entered by the user, the first letter of each word is capitalised to keep it formal
    // in the database.
    private static String capitaliseWord(String str) {
        String[] carDetails = str.split("\\s");
        StringBuilder capitalisedPhrase = new StringBuilder();
        for (String carDetail : carDetails) {
            String firstChar = carDetail.substring(0, 1);
            String secCharOnwards = carDetail.substring(1);
            capitalisedPhrase.append(firstChar.toUpperCase()).append(secCharOnwards).append(" ");
        }
        return capitalisedPhrase.toString().trim();
    }
}