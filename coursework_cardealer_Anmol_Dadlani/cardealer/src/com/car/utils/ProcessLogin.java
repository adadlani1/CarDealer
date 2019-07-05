package com.car.utils;

import com.car.form.*;
import com.car.roles.Admin;
import com.car.roles.Customer;
import com.car.roles.Staff;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

//class that processes the information received by the login class
public class ProcessLogin {

    private String password;
    private String username;
    private static String userType;
    private String tempPassword = " ";

    //user input details are saved in global variables
    public ProcessLogin(String username, String password) {
        this.username = username;
        this.password = password;

    }

    //the input details are checked
    //the user type and username is checked mainly
    // if they match then the password is decrypted in another function
    public void verifyLogin() throws Exception {
        Encryption encryption = new BasicEncryption();

        String tempUsername = " ";

        BufferedReader reader;

        byte[] enc = password.getBytes(StandardCharsets.UTF_8);
        String encPass = new String(encryption.encrypt(enc));

        reader = new BufferedReader(new FileReader(Constants.CARS_USERS_FILE));

        while (true) {
            //the line in the file is read
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            //string is split into a string array
            String[] parts = line.split("\\|");
            for (String part : parts) {

                //if the user and user type match then the values are stored as temporary
                if (part.equals(username) && parts[1].equals(encPass)) {
                    tempUsername = part;
                    userType = parts[2];
                    tempPassword = encPass;
                }
            }
        }

        //if all of the details match then another function is called
        if (tempUsername.equals(username) && tempPassword.equals(encPass)) {
            loginDetailsCorrect();

        }
        //if they do not match then a message pops up telling the user that the login details are wrong
        else {
            JOptionPane.showMessageDialog(null, "Wrong credentials entered. Please try again" +
                    " or contact the administrator.");
        }

        reader.close();

    }

    //if the details are correct then depending on the user type the correct menu loads up
    private void loginDetailsCorrect() throws Exception {
        switch (userType) {
            case "admin":
                JOptionPane.showMessageDialog(null, "You are entering admin mode.");
                Admin admin = new Admin();
                admin.contactForm();
                Login.successfulLogin(true);
                break;
            case "staff":
                JOptionPane.showMessageDialog(null, "You are entering staff mode.");
                Staff staff = new Staff();
                staff.contactForm();
                Login.successfulLogin(true);
                break;
            case "customer":
                JOptionPane.showMessageDialog(null, "You are entering customer mode.");
                Customer customer = new Customer();
                customer.search();
                Login.successfulLogin(true);
                break;
        }
    }

    public static String setUserType(){
        return userType;
    }
}