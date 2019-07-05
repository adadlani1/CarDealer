package com.car.form;

import com.car.utils.ProcessLogin;

import javax.swing.*;
import java.io.IOException;

public class Login {
    private static JFrame frame;
    private JTextField usernameField;
    private JPanel panelMain;
    private JPasswordField passwordField;
    private JButton loginButton;
    protected JLabel usernameLabel;
    protected JLabel passwordLabel;
    protected JLabel loginTitle;
    private static boolean loginSuccessful = false;

    //constructor containing the showResults body of code with Action Listeners and accessing other methods in the class
    //and to initialize the object of a class
    public Login() {
        loginButton.addActionListener(e -> {
            //all of the values in the fields are obtained
            String username = usernameField.getText();
            char [] passwordCharArray = passwordField.getPassword();

            //process login class is accessed and the user details are provided
            ProcessLogin processLogin;
            processLogin = new ProcessLogin(username, new String(passwordCharArray));
            try {
                processLogin.verifyLogin();
                if (loginSuccessful){
                    frame.dispose();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
    }

    //shows the GUI form
    public static void main(String[] args) {
        frame = new JFrame("Login");
        frame.setContentPane(new Login().panelMain);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700,500);
        frame.setVisible(true);
    }

    //userType is obtained and provided to search class
    static String getUserType(){
        String userType = ProcessLogin.setUserType();
        Search search = null;
        try {
            search = new Search();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        assert search != null;
        return userType;
    }

    public static void successfulLogin(boolean login) {
        loginSuccessful = login;
    }
}