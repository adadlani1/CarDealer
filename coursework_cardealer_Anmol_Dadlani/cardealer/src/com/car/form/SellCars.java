package com.car.form;

import com.car.model.Car;
import com.car.model.Van;
import com.car.utils.Constants;
import com.car.utils.DatabaseOperations;

import javax.swing.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//ShowGUI is an interface that includes a method for making the GUI form for the class to be visible
//All of the attributes are implemented in the GUI Form
public class SellCars {
    private JPanel panelMain;
    private JRadioButton tickThisIfTheRadioButton;
    private JButton updateDetailsButton;
    private JComboBox<String> regNumComboBox;
    private JComboBox yearComboBox;
    private JComboBox monthComboBox;
    private JComboBox dayComboBox;
    private JLabel yearLabel;
    private JLabel monthLabel;
    private JLabel dayLabel;
    private static boolean checkBoxTicked;
    private static boolean updated = false;

    //constructor containing the showResults body of code with Action Listeners and accessing other methods in the class
    //and to initialize the object of a class
    private SellCars() throws FileNotFoundException {
        findNumberPlateOfUnsoldCars();
        //when a month has been selected from the month combobox, a function is called to add the number of days
        //to the day combo box. For example, february in 2019 will show 28 days and then feb in 2020 will show 29 days
        monthComboBox.addActionListener(e -> {
            String month = (String) monthComboBox.getSelectedItem();
            int year = Integer.parseInt((String) Objects.requireNonNull(yearComboBox.getSelectedItem()));
            assert month != null;
            DatabaseOperations.addDaysToComboBox(month, year, dayComboBox);
        });
        //when the checkbox has been ticked, a function calculates todays date and turns it into a string to be added
        //into the text file
        tickThisIfTheRadioButton.addActionListener(e2 -> {
            checkBoxTicked = true;

            //makes the comboboxes and labels invisible so you cannot enter a date
            dayComboBox.setVisible(false);
            monthComboBox.setVisible(false);
            yearComboBox.setVisible(false);

            dayLabel.setVisible(false);
            monthLabel.setVisible(false);
            yearLabel.setVisible(false);
        });
        //what happens when you press the button is it appends to the text file.
        updateDetailsButton.addActionListener(e -> {
            String soldCarRegNum = (String) regNumComboBox.getSelectedItem();
            try {
                getCarInfo(soldCarRegNum);
            } catch (ParseException | IOException e1) {
                e1.printStackTrace();
            }
        });
        findNumberPlateOfUnsoldCars();
    }

    public static void setVisible() throws FileNotFoundException {
        JFrame frame2 = new JFrame("Sell Cars");
        frame2.setContentPane(new SellCars().panelMain);
        frame2.setSize(900, 500);
        frame2.setVisible(true);
    }

    //function gets the current date
    private static String checkBoxTicked() {
        Date date = Calendar.getInstance().getTime();
        DateFormat currentDate = new SimpleDateFormat(Constants.CARS_DATE_FORMAT);

        return currentDate.format(date);

    }

    //car is overwritten in the database text file
    private void writeToImport(ArrayList<String> cars) throws IOException {
        BufferedWriter writeAllCars;
        writeAllCars = new BufferedWriter(new FileWriter(Constants.CARS_DATABASE_FILE));

        for (String car : cars) {
            writeAllCars.write(car);
            writeAllCars.write("\n");
        }
        writeAllCars.close();
        //if the car has been updated, a message is shown to show that it has
        if (updated) {
            JOptionPane.showMessageDialog(null, "Cars have been updated on the database.");
        } else {
            JOptionPane.showMessageDialog(null, "This car is not available. Please try again.");
        }
    }

    //the info for each car is obtained and depending on this different functions are called
    private void getCarInfo(String regNum) throws ParseException, IOException {
        ArrayList<String> updatedCarDetails = new ArrayList<>();
        String dateSold;
        List<Car> car = DatabaseOperations.readCarsFromDatabase(Constants.CARS_DATABASE_FILE);

        //for each car, it gets the details
        for (Car eachCars : car) {
            String regNumber = eachCars.getRegNumber();
            String soldDate = eachCars.getLastSoldDate();
            String arrivalDate = eachCars.getPurchaseDate();

            if (regNum.equals(regNumber) && soldDate.equals("N/A")) {
                updated = true;
                if (checkBoxTicked) {
                    dateSold = checkBoxTicked();
                } else {
                    dateSold = getUserInputSoldDate();
                }

                boolean dateValid = compareDates(arrivalDate, dateSold);
                if (dateValid) {
                    eachCars.setLastSoldDate(dateSold);
                } else {
                    JOptionPane.showMessageDialog(null, "The date you have entered cannot be " +
                            "before the date of arrival. Please re-enter the information");
                }
            }
            String newCarDetails = updateCarDetails(eachCars);

            updatedCarDetails.add(newCarDetails);
        }
        writeToImport(updatedCarDetails);
    }

    //this gets the data from the user and forms it into a date
    private String getUserInputSoldDate() {
        String year = (String) yearComboBox.getSelectedItem();
        String month = (String) monthComboBox.getSelectedItem();
        String day = (String) dayComboBox.getSelectedItem();

        return year + "-" + month + "-" + day;
    }

    //the dates are compared to see if the sold date is after the arrival date
    //this function takes an assumption that when the car arrives it doesnt go on sale immediately, it gets serviced
    private boolean compareDates(String arrivalDate, String soldDate) throws ParseException {
        Date dateArrivalDate = new SimpleDateFormat(Constants.CARS_DATE_FORMAT).parse(arrivalDate);
        Date dateSoldDate = new SimpleDateFormat(Constants.CARS_DATE_FORMAT).parse(soldDate);

        return dateSoldDate.compareTo(dateArrivalDate) > 0;
    }

    //the details of the sold car is updated
    private String updateCarDetails(Car eachCars) {
        String model = eachCars.getModel();
        String registrationNum = eachCars.getRegNumber();
        String type = eachCars.getType();
        String vanSize;
        if (eachCars instanceof Van) {
            vanSize = ((Van) eachCars).getVanSize();
        } else {
            vanSize = "";
        }
        String colour = eachCars.getColour();
        String price = String.valueOf(eachCars.getPrice());
        String accHistory = eachCars.getAccidentHistory();
        String dateOfArrival = eachCars.getPurchaseDate();
        String sellDate = eachCars.getLastSoldDate();
        String mileage = String.valueOf(eachCars.getMileage());
        String transmissionType = eachCars.getTransmissionType();

        return registrationNum + "|"
                + model + "|"
                + type + "|"
                + vanSize + "|"
                + colour + "|"
                + mileage + "|"
                + accHistory + "|"
                + transmissionType + "|"
                + price + "|"
                + dateOfArrival + "|"
                + sellDate;

    }

    //this finds the number plates of the cars that are unsold
    private void findNumberPlateOfUnsoldCars() throws FileNotFoundException {
        BufferedReader carReader = new BufferedReader(new FileReader(Constants.CARS_DATABASE_FILE));
        String[] lineArr;
        ArrayList<String> listOfUnsoldRegNums = new ArrayList<>();
        String line;
        try {
            while ((line = carReader.readLine()) != null) {
                lineArr = line.split("\\|");
                if (lineArr[10].equals("N/A")) {

                    //if the car is unsold, it is added to a list.
                    listOfUnsoldRegNums.add(lineArr[0]);
                }
            }

            if (!(listOfUnsoldRegNums.size() == 0))
                addItemsToRegNumCmBox(listOfUnsoldRegNums, listOfUnsoldRegNums.size());
            else {
                //when the cars-database.txt is empty, this is added to the comboBox
                regNumComboBox.addItem("Please add cars into the database using 'Add Cars'.");
                updateDetailsButton.setEnabled(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this adds whatever has been added to the arraylist into the combobox as an option
    private void addItemsToRegNumCmBox(ArrayList<String> sortedUnsoldList, int x) {
        regNumComboBox.removeAllItems();
        regNumComboBox.addItem("");
        for (int element = 0; element < x; element++) {
            regNumComboBox.addItem(sortedUnsoldList.get(element));
        }
    }
}
