package com.car.form;

import com.car.utils.BasicEncryption;
import com.car.utils.Constants;
import com.car.utils.Encryption;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

//All of the attributes are implemented in the GUI Form
public class CreateLoginDetails {
    private String encryptedPassword;
    private JPanel panelMain;
    private JLabel titleMain;
    private JTextField createUsernameField;
    private JPasswordField createPasswordField;
    private JButton createDetailsButton;
    private JLabel userTypeLabel;
    private JComboBox createUserTypeComboBox;
    private byte[] createNewPasswordByteArray;
    private char[] createNewPasswordCharArray;
    private Encryption basicEncryption;

    //constructor containing the showResults body of code with Action Listeners and accessing other methods in the class
    //and to initialize the object of a class
    private CreateLoginDetails() {
        basicEncryption = new BasicEncryption();
        createDetailsButton.addActionListener(e -> {

            if(createUsernameField.getText().equals("") || (createPasswordField.getPassword().length == 0)){
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            } else {
                //the values in the fields are assigned to variables
                String createNewUsername = createUsernameField.getText().trim();
                createNewPasswordCharArray = createPasswordField.getPassword();
                String userTypeUpper = (String) createUserTypeComboBox.getSelectedItem();
                assert userTypeUpper != null;
                String userType = userTypeUpper.toLowerCase();

                createNewPasswordByteArray = new String(createNewPasswordCharArray).getBytes(StandardCharsets.UTF_8);

                //the password entered by the admin is encrypted
                encryptPassword();

                boolean usernameAvailable = true;
                try {
                    usernameAvailable = checkIfUsernameTaken(createNewUsername);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //these details are appended to the car-users.txt file
                writeToFile(createNewUsername, userType, usernameAvailable);
            }
        });
    }

    public static void setVisible() {
        JFrame frame2 = new JFrame("Create Details");
        frame2.setContentPane(new CreateLoginDetails().panelMain);
        frame2.setSize(700, 500);
        frame2.setVisible(true);
    }

    //method that calls another class to encrypt the password
    private void encryptPassword() {
        encryptedPassword = new String(basicEncryption.encrypt(createNewPasswordByteArray));
    }

    //checks the username entered taken
    private boolean checkIfUsernameTaken(String usernameInput) throws IOException {
        BufferedReader usernameChecker = new BufferedReader(new FileReader(Constants.CARS_USERS_FILE));
        String line;
        boolean usernameAvailable = true;
        while ((line = usernameChecker.readLine()) != null) {
            String[] username = line.split("\\|");
            String usernameInFile = username[0];
            if (usernameInFile.equals(usernameInput))
                 usernameAvailable = false;
        }
        return usernameAvailable;
    }

    private void writeToFile(String createNewUsername, String userType, boolean usernameAvailable) {
        if (usernameAvailable) {
            String newCustDetails = createNewUsername + "|" + encryptedPassword + "|" + userType + "\n";
            try (FileWriter fileWriter = new FileWriter("car-users.txt", true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
                printWriter.println(newCustDetails);
            } catch (IOException ignored) {
            }
            //message pops up to tell the admin that the credentials have been made
            JOptionPane.showMessageDialog(null, "New Login Credentials have been made");
        } else
            JOptionPane.showMessageDialog(null, "The username is not available. Please enter a new one.");
    }
}