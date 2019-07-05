package com.car.form;

import com.car.model.Car;
import com.car.model.Van;
import com.car.utils.Constants;
import com.car.utils.DatabaseOperations;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import static java.lang.Integer.parseInt;

//ShowGUI is an interface that includes a method for making the GUI form for the class to be visible
//All of the attributes are implemented in the GUI Form and some global variables
public class Search {
    private JLabel searchForCarsLabel;
    private JPanel panelMain;
    private JComboBox maxSeatsComboBox;
    private JComboBox minSeatsComboBox;
    private JButton searchButton;
    private JComboBox<String> modelComboBox;
    private JComboBox<String> colourComboBox;
    private JComboBox transmissionComboBox;
    private JComboBox vanSizeComboBox;
    private JCheckBox colourCheckBox;
    private JCheckBox transmissionTypeAndModelCheckBox;
    private JCheckBox vanSizeCheckBox;
    private JCheckBox numberOfSeatsCheckBox;
    private JLabel modelJLabel;
    private JLabel colourJLabel;
    private JLabel transmissionJLabel;
    private JLabel maxSeatsJLabel;
    private JLabel vanSizeJLabel;
    private JLabel minSeatsJLabel;
    private JLabel addCarsPleaseLabel;
    private Object maxSeats;
    private Object minSeats;
    private Object model;
    private Object colour;
    private Object transmission;
    private Object vanSize;
    private String regNum;
    private ImageIcon modelPic;
    private Long mileage;
    private String accHistory;
    private String arrivalDate;
    private String carType;
    private String userTypeNew;
    private List<Car> searchedCars;
    private String priceReformatted;
    private boolean shownCars;

    //constructor containing the showResults body of code with Action Listeners and accessing other methods in the class
    //and to initialize the object of a class
    public Search() throws IOException {
        listOfUnsoldNames();
        searchButton.addActionListener(e -> {
            getUserType();
            getValuesInCmBoxes();
            //depending on which comboBoxes are used, a different search takes place
            if (!Objects.equals(modelComboBox.getSelectedItem(), "")
                    && !Objects.equals(transmissionComboBox.getSelectedItem(), "")) {

                //shownCars is used to see if any cars are outputted, if not an error message shows that there are no
                //cars that match the entered input
                shownCars = false;
                givenModelAndTransmission();
            }

            else if (!Objects.equals(minSeatsComboBox.getSelectedItem(), "")
                    && !Objects.equals(maxSeatsComboBox.getSelectedItem(), "")) {
                shownCars = false;
                searchedCars = givenNumberOfSeats();
                carSeatSearch(searchedCars);
            }

            else if (!Objects.equals(colourComboBox.getSelectedItem(), "")) {
                shownCars = false;
                givenColour();
            }

            else if (!Objects.equals(vanSizeComboBox.getSelectedItem(), "")) {
                shownCars = false;
                givenVanSize();
            }

            resetSelectedItems();
            if(!shownCars){
                JOptionPane.showMessageDialog(null, "No vehicles found. Please try again.");
            }
        });
        transmissionTypeAndModelCheckBox.addActionListener(e -> {
            /*depending on the input of the check boxes, certain search comboBoxes appear*/
            vanSizeComboBox.setVisible(false);
            minSeatsComboBox.setVisible(false);
            maxSeatsComboBox.setVisible(false);
            modelComboBox.setVisible(true);
            colourComboBox.setVisible(false);
            transmissionComboBox.setVisible(true);

            vanSizeJLabel.setVisible(false);
            minSeatsJLabel.setVisible(false);
            maxSeatsJLabel.setVisible(false);
            modelJLabel.setVisible(true);
            colourJLabel.setVisible(false);
            transmissionJLabel.setVisible(true);

        });
        numberOfSeatsCheckBox.addActionListener(e -> {
            vanSizeComboBox.setVisible(false);
            minSeatsComboBox.setVisible(true);
            maxSeatsComboBox.setVisible(true);
            modelComboBox.setVisible(false);
            colourComboBox.setVisible(false);
            transmissionComboBox.setVisible(false);

            vanSizeJLabel.setVisible(false);
            minSeatsJLabel.setVisible(true);
            maxSeatsJLabel.setVisible(true);
            modelJLabel.setVisible(false);
            colourJLabel.setVisible(false);
            transmissionJLabel.setVisible(false);
        });
        colourCheckBox.addActionListener(e -> {
            vanSizeComboBox.setVisible(false);
            minSeatsComboBox.setVisible(false);
            maxSeatsComboBox.setVisible(false);
            modelComboBox.setVisible(false);
            colourComboBox.setVisible(true);
            transmissionComboBox.setVisible(false);

            vanSizeJLabel.setVisible(false);
            minSeatsJLabel.setVisible(false);
            maxSeatsJLabel.setVisible(false);
            modelJLabel.setVisible(false);
            colourJLabel.setVisible(true);
            transmissionJLabel.setVisible(false);
        });
        vanSizeCheckBox.addActionListener(e -> {
            vanSizeComboBox.setVisible(true);
            minSeatsComboBox.setVisible(false);
            maxSeatsComboBox.setVisible(false);
            modelComboBox.setVisible(false);
            colourComboBox.setVisible(false);
            transmissionComboBox.setVisible(false);

            vanSizeJLabel.setVisible(true);
            minSeatsJLabel.setVisible(false);
            maxSeatsJLabel.setVisible(false);
            modelJLabel.setVisible(false);
            colourJLabel.setVisible(false);
            transmissionJLabel.setVisible(false);
        });
    }

    public static void setVisible() throws IOException {
        JFrame frame = new JFrame("Search");
        frame.setContentPane(new Search().panelMain);
        frame.setSize(700,500);
        frame.setVisible(true);
    }

    //method that removes any duplicate values
    //e.g. if two cars with the same name are present, only one is shown in the drop down menu
    private void removeDuplicates(ArrayList namesList, String comboBox) {
        java.util.Collections.sort(namesList);
        for (int i = 0; i < namesList.size(); i++) {
            for (int j = 0; j < namesList.size(); j++) {
                if (namesList.get(i).equals(namesList.get(j)) && j != i) {
                    namesList.remove(i);
                }
            }
        }

        if (comboBox.equals("model"))
            addItemsToCarModelCmBox(namesList, namesList.size());
        else
            addItemsToCarColourCmBox(namesList, namesList.size());
    }

    //a list of unsold cars is formed then sent to the removeDuplicates method
    private void listOfUnsoldNames() throws IOException {

        BufferedReader carReader = new BufferedReader(new FileReader(Constants.CARS_DATABASE_FILE));
        String[] lineArr;
        ArrayList<String> listOfModelNames = new ArrayList<>();
        ArrayList<String> listOfColourNames = new ArrayList<>();
        String line;
        try {
            while ((line = carReader.readLine()) != null) {
                lineArr = line.split("\\|");
                try {
                    if (lineArr[10].equals("N/A")) {
                        listOfModelNames.add(lineArr[1]);
                        listOfColourNames.add(lineArr[4]);
                    }
                } catch (Exception e){
                    JOptionPane.showMessageDialog(null, "Please add cars into the database.");
                }
            }
            removeDuplicates(listOfModelNames, "model");
            removeDuplicates(listOfColourNames, "colour");
        }
        catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Please add cars to the database first.");
        }
    }

    //the list of model names are added to the combo box
    private void addItemsToCarModelCmBox(ArrayList sortedUnsoldList, int x) {
        if(!(sortedUnsoldList.size() == 0)) {
            modelComboBox.addItem("");
            for (int element = 0; element < x; element++) {
                modelComboBox.addItem((String) sortedUnsoldList.get(element));
            }
        } else
            modelComboBox.addItem("Please add cars into the database.");
    }

    //list of colour names are added to the combo box
    private void addItemsToCarColourCmBox(ArrayList sortedUnsoldList, int x) {
        if(!(sortedUnsoldList.size() == 0)) {
        colourComboBox.addItem("");
        for (int element = 0; element < x; element++) {
            colourComboBox.addItem((String) sortedUnsoldList.get(element));
        }
        } else
            addCarsPleaseLabel.setVisible(true);
    }

    //this method assigns what the user has input on the comboBoxes
    private void getValuesInCmBoxes() {
        maxSeats = maxSeatsComboBox.getSelectedItem();
        minSeats = minSeatsComboBox.getSelectedItem();
        model = modelComboBox.getSelectedItem();
        colour = colourComboBox.getSelectedItem();
        transmission = transmissionComboBox.getSelectedItem();
        vanSize = vanSizeComboBox.getSelectedItem();
    }

    //if the model and transmission type has been specified by the user, this function runs
    private void givenModelAndTransmission() {
            ArrayList<Car> carList = (ArrayList<Car>) DatabaseOperations.readCarsFromDatabase
                    (Constants.CARS_DATABASE_FILE);
            for (Car eachCars : carList){
                String carModel = eachCars.getModel();
                String carTransmission = eachCars.getTransmissionType();

                if(carModel.equals(model) && carTransmission.equals(transmission)
                        && eachCars.getLastSoldDate().equals("N/A")){
                    getInfoForCar(eachCars);
                    if(userTypeNew.equals("admin") || userTypeNew.equals("staff"))
                        employeeSearches();
                    else customerSearches();
                }
            }

        }

    //if an employee has logged in then this function runs to display the search result
    private void employeeSearches() {
        shownCars = true;
        modelPic = new ImageIcon("carPhotos\\" + model + regNum + ".jpg");
        JOptionPane.showMessageDialog(null, "Model - " + model + "\n" +
                        "Colour - " + colour + "\n" +
                        "Registration Number - " + regNum + "\n" + "Type - " + carType + "\n" + "Van Size - "
                        + vanSize + "\n" + "Mileage - " + mileage + "\n" + "Accident History - " + accHistory + "\n" +
                        "Price - £" + priceReformatted + "\n" + "Transmission - " +transmission + "\n" + "Arrival Date - "
                        + arrivalDate, "Car",
                JOptionPane.PLAIN_MESSAGE, modelPic);
    }

    //if a customer has logged in then this function runs to display the search result
    private void customerSearches() {
        shownCars = true;
        modelPic = new ImageIcon("carPhotos\\" + model + regNum + ".jpg");
        JOptionPane.showMessageDialog(null, "Model - " + model + "\n" +
                        "Colour - " + colour + "\n" +
                        "Registration Number - " + regNum + "\n" + "Type - " + carType + "\n" + "Van Size - "
                        + vanSize + "\n" + "Mileage - " + mileage + "\n" +
                        "Price - £" + priceReformatted + "\n" + "Transmission - " + transmission + "\n" + "Arrival Date - "
                        + arrivalDate, "Car",
                JOptionPane.PLAIN_MESSAGE, modelPic);
    }

    //when the number of seats is provided by the user
    private List<Car> givenNumberOfSeats() {
        List<Car> allCars = new ArrayList<>();

        int minSeatsInt = parseInt(String.valueOf(minSeats));
        int maxSeatsInt = parseInt(String.valueOf(maxSeats));

        //this maps the number of seats to the type of car
        Map<String, Integer> seatsMap = new HashMap<>();
        seatsMap.put("Coupe", 2);
        seatsMap.put("Van", 3);
        seatsMap.put("Hatchback", 4);
        seatsMap.put("Saloon", 5);
        seatsMap.put("SUV", 5);
        seatsMap.put("MPV", 7);

        if (minSeatsInt <= maxSeatsInt) {
            //this reads the database file and finds the cars that fit the category of the number of seats provided by the user
            //then the cars are added to a list of objects
            allCars = DatabaseOperations.readCarsFromDatabase(Constants.CARS_DATABASE_FILE)
                    .stream()
                    .filter(car -> seatsMap.get(car.getType()) >= minSeatsInt && seatsMap.get(car.getType()) <= maxSeatsInt
                    && car.getLastSoldDate().equals("N/A"))
                    .collect(Collectors.toList());
        } else {
            JOptionPane.showMessageDialog(null, "Maximum number of seats cannot be lower than" +
                    " the minimum.");
        }

        return allCars;
    }

    //method that is used when the user inputs the colour of the car they want
    private void givenColour(){
        ArrayList<Car> carList = (ArrayList<Car>) DatabaseOperations.readCarsFromDatabase
                (Constants.CARS_DATABASE_FILE);
        for (Car eachCars : carList){
            if (eachCars.getColour().equals(colour) && eachCars.getLastSoldDate().equals("N/A")){
                getInfoForCar(eachCars);
                model = eachCars.getModel();
                transmission = eachCars.getTransmissionType();
                correctResult();
            }
        }
    }

    //info for each car is found and stored as a global variable
    private void getInfoForCar(Car eachCars){
        regNum = eachCars.getRegNumber();
        colour = eachCars.getColour();
        carType = eachCars.getType();
        mileage = eachCars.getMileage();
        accHistory = eachCars.getAccidentHistory();
        Double price = eachCars.getPrice();
        DecimalFormat df = new DecimalFormat("#.00");
        priceReformatted = df.format(price);
        arrivalDate = eachCars.getPurchaseDate();
        if(eachCars instanceof Van)
        vanSize = ((Van) eachCars).getVanSize();
        else
            vanSize = "N/A";
    }

    //the user type is determined
    private void getUserType(){
        userTypeNew = Login.getUserType();
    }

    //method used when the van size is specified
    private void givenVanSize(){
        ArrayList<Car> carList = (ArrayList<Car>) DatabaseOperations.readCarsFromDatabase
                (Constants.CARS_DATABASE_FILE);

        for (Car eachCar : carList){
            if (eachCar instanceof Van) {
                if (((Van) eachCar).getVanSize().equals(vanSize) && eachCar.getLastSoldDate().equals("N/A")) {
                    model = eachCar.getModel();
                    transmission = eachCar.getTransmissionType();
                    getInfoForCar(eachCar);
                    correctResult();
                }
            }
        }
    }

    //info for the cars for specific seat is displayed
    private void carSeatSearch(List<Car> cars){
        for(Car car : cars){
            model = car.getModel();
            transmission = car.getTransmissionType();
            getInfoForCar(car);
            correctResult();
        }
    }

    //checks what the user type is and determines which search it should perform
    private void correctResult(){
        if (userTypeNew.equals("admin") || userTypeNew.equals("staff"))
            employeeSearches();
        else customerSearches();
    }

    //when the search button has been pressed, it resets the options chosen.
    private void resetSelectedItems() {
        maxSeatsComboBox.setSelectedItem("");
        minSeatsComboBox.setSelectedItem("");
        colourComboBox.setSelectedItem("");
        modelComboBox.setSelectedItem("");
        transmissionComboBox.setSelectedItem("");
        vanSizeComboBox.setSelectedItem("");
    }
}