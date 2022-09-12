package controller;

import DAO.AppointmentsDAO;
import DAO.UsersDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import utilities.CurrentUser;
import java.io.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;
import java.util.ResourceBundle;

/** This is the LoginScreen Class.  It controls the login screen functionality.*/
public class LoginScreen implements Initializable {

    public TextField passwordTextField;
    public TextField userNameTextField;

    public Button loginButton;
    public Button exitButton;

    public Label userNameLabel;
    public Label passwordLabel;
    public Label timeZoneLabel;

    Stage stage;
    Parent scene;

    /** This is the initialize method.
     It initializes the controller.  It also translates the labels and buttons into french if the user is in a French
     speaking locale*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        timeZoneLabel.setText(ZoneId.systemDefault().getId());
        Locale locale = Locale.getDefault();
        ResourceBundle frenchResource = ResourceBundle.getBundle("utilities/Lang_fr", locale);

        if (locale.getLanguage().equals("fr")) {
            loginButton.setText(frenchResource.getString("Login"));
            exitButton.setText(frenchResource.getString("Exit"));
            passwordLabel.setText(frenchResource.getString("Password"));
            userNameLabel.setText(frenchResource.getString("User") + frenchResource.getString("Name"));
        }


    }

    /** This is the loginClicked Method.  It compares the entered password and userName to the password in the SQL database.
     It also calls for an error message in the event that the username and password don't match what's in the database.
     */
    public void loginClicked(ActionEvent actionEvent) throws IOException {
        FileWriter fileWriter = new FileWriter("login_activity.txt", true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        String userName = userNameTextField.getText();
        String userPassword = passwordTextField.getText();
        String storedPassword = null;
        try {
            storedPassword = UsersDAO.getPassword(userName);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getErrorCode());
        }
        if (userPassword.equals(storedPassword) && storedPassword.length() > 0) {
            try {
                CurrentUser.setUserID(UsersDAO.getUserID(userName));
                CurrentUser.setUserName(userName);
            } catch (SQLException sqlException) {
                System.out.println("SQL Exception: " + sqlException.getErrorCode());
            }
            try {
                appointmentWithinFifteen(CurrentUser.getUserID());
            } catch (SQLException sqlException) {
                System.out.println("SQLException" + sqlException.getErrorCode());
            }

            String loginAttempt = "User " + userName + " successfully logged in at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            printWriter.println(loginAttempt);
            printWriter.close();

            stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/HomeScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.centerOnScreen();
            stage.setTitle("Home Screen");
            stage.show();
        } else {

            String loginAttempt = "User " + userName + " gave invalid logged in information at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            printWriter.println(loginAttempt);
            printWriter.close();
            Locale locale = Locale.getDefault();
            ResourceBundle frenchResource = ResourceBundle.getBundle("utilities/Lang_fr", locale);
            String header = "Try Again";
            String body = "Incorrect User Name or Password";

            if (locale.getLanguage().equals("fr")) {
                header = frenchResource.getString("Try") + " " + frenchResource.getString("Again");
                body = frenchResource.getString("Incorrect") + " " + frenchResource.getString("User") + " " +
                        frenchResource.getString("Name") + " " + frenchResource.getString("or") + " " + " " +
                        frenchResource.getString("Password");
            }

            displayAlert(header, body);
        }

    }

    /**This is the exitClicked method.  It exits the program when the exit button is clicked.*/
    public void exitClicked(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**This is the displayAlert method.  It will display an error type message.
     @param headerName is the text going into the header
     @param body is the body of the error message */
    public void displayAlert(String headerName, String body) {
        Alert errorMessage = new Alert(Alert.AlertType.INFORMATION);
        errorMessage.setHeaderText(headerName);
        errorMessage.setContentText(body);
        errorMessage.showAndWait();
    }

    /**This is the appointmentWithinFifteen method.  It will display a message to tell you whether you have an appointment within 15 minutes based on userID.
     @param userID is the login users ID to look for appointments within 15 minutes
      */
    public void appointmentWithinFifteen(int userID) throws SQLException{
        boolean appointmentComing = false;
        Appointments appointmentFound = new Appointments();
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timePlusFifteen = LocalDateTime.now().plusMinutes(15);
        ObservableList<Appointments> appointments = AppointmentsDAO.getAppointmentsForUser(userID);
        for (Appointments appointment : appointments) {
            if(appointment.getStartTime().isAfter(timeNow) && appointment.getStartTime().isBefore(timePlusFifteen)) {
                appointmentFound = appointment;
                String alertText = "Appointment ID: " + appointment.getAppointmentID() + " at " + appointment.getStartTime().toLocalTime() + " on " + appointment.getStartTime().toLocalDate();
                displayAlert("Appointment within 15 minutes", alertText);
                appointmentComing = true;
            }
        }
        if(!appointmentComing) {
            displayAlert("No upcoming appointments", "You have no appointments within the next 15 minutes");
        }

    }
}
