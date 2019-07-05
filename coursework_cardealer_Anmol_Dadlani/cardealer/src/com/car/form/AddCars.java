package com.car.form;

import com.car.model.Car;
import com.car.utils.Constants;
import com.car.utils.DatabaseOperations;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//All of the attributes are implemented in the GUI Form
public class AddCars {
    private JButton addCarsButton;
    private JLabel titleMain;
    private JPanel panelMain;

    private AddCars() {
        addCarsButton.addActionListener(e -> {
            try {
                addObjectListToDatabase();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Cars have been added to the database.");
        });
    }

    //method to ensure the GUI form appears on the user's screen
    public static void setVisible() {
        JFrame frame2 = new JFrame("Add Cars");
        frame2.setContentPane(new AddCars().panelMain);
        frame2.setSize(500, 100);
        frame2.setVisible(true);
    }

    //method that ensures all of the cars (objects) are added to the database
    private void addObjectListToDatabase() throws IOException {
        List<String> carStringList = readFromCarsImport();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream
                (new File(Constants.CARS_DATABASE_FILE), true), StandardCharsets.UTF_8));

        ArrayList<Car> carList = (ArrayList<Car>) DatabaseOperations.readCarsFromDatabase
                (Constants.CARS_DATABASE_FILE);

        //if there are no cars in the database file, then all of the cars in cars-import are added
        if(carList.size() == 0){
            for (String car: carStringList) {
                writer.write(car);
                writer.write("\n");
            }
            //if not, then the program finds any cars that are not present in the database but are in cars-import
            //program then adds the cars not present
        } else {
            List<String> carNumPlates = getListOfRegNumInCarsImport();
            List<String> carNumPlatesInDatabase = getListOfRegNumInCarsDatabase(carList);
            for (String plate: carNumPlates) {
                boolean duplicates = checkForDuplicates(carNumPlatesInDatabase, plate);
                //if it is not a duplicate, add to cars-database.txt
                if (!duplicates){
                    for (String car: carStringList) {
                        String[] carSplit = car.split("\\|");
                        if (carSplit[0].equals(plate)){
                            writer.append(car);
                            writer.write("\n");
                        }
                    }
                }
            }
        }

        writer.close();
    }

    //the file cars-import.txt is read by this method
    private List<String> readFromCarsImport() throws IOException {
        List<String> carStringList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat(Constants.CARS_DATE_FORMAT);

        //current date is used to add to any cars that are missing the details.
        Date date = new Date();
        BufferedReader reader = new BufferedReader(new FileReader(Constants.CARS_IMPORT_FILE));
        String[] lineStrArr;
        String line;

        while ((line = reader.readLine()) != null) {
            lineStrArr = line.split("\\|");

            //length of the string array is checked and if it is below the standard 10 elements, the dates are added
            if (lineStrArr.length < 11) {

                if (lineStrArr.length == 9) {
                    String currentDate = dateFormat.format(date);
                    String bar = "|";
                    String notApplicable = "N/A";

                    line += bar + currentDate + bar + notApplicable;
                } else if (lineStrArr.length == 10) {
                    String bar = "|";
                    String notApplicable = "N/A";

                    line += bar + notApplicable;
                }
            }
            carStringList.add(line);
        }

        return carStringList;
    }

    private static boolean checkForDuplicates(List<String> listOfNumberPlates, String numberPlate) {
        for(String eachPlate: listOfNumberPlates){
            if(eachPlate.equals(numberPlate))
                return true;
        }
        return false;
    }

    private List<String> getListOfRegNumInCarsImport() throws IOException {
        List<String> carStringList = readFromCarsImport();
        List<String> carNumPlates = new ArrayList<>();

        for (String car: carStringList) {
            String[] carSplit = car.split("\\|");
            carNumPlates.add(carSplit[0]);
        }

        return carNumPlates;
    }

    private List<String> getListOfRegNumInCarsDatabase(List<Car> carList) {
        List<String> carNumPlates = new ArrayList<>();

        for (Car car: carList) {
            String plate = car.getRegNumber();
            carNumPlates.add(plate);
        }

        return carNumPlates;
    }
}
