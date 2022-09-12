package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**This is the HomeScreen Class.  It controls the home screen of the application.*/
public class HomeScreen implements Initializable {

    Stage stage;
    Parent scene;

    /**This is the initialize method.  It initializes the gui.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    /**This is the appointmentsClicked method.  It opens the appointments screen.*/
    public void appointmentsClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/AppointmentScreen.fxml", "Appointments",actionEvent);
    }

    /**This is the customersClicked method.  It opens the customers screen.*/
    public void customersClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/CustomerScreen.fxml", "Customers",actionEvent);
    }

    /**This is the logoutClicked method.  It closes the app after confirming you wish to exit.*/
    public void logOutClicked(ActionEvent actionEvent) throws IOException{
        boolean wishToQuit = confirmationAlert("Are you sure you want to log out?");
        if(wishToQuit) {
            newWindow("/view/LoginScreen.fxml", "Login", actionEvent);
        }
    }

    /**This is the customersClicked method.  It opens the customers screen.*/
    public void reportsClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/ReportsScreen.fxml", "Reports", actionEvent);
    }


    /**This is the newWindow method.  It handles opening new windows.
     @param viewAddress is the address of the view file
     @param actionEvent is the source of the button push causing the new window to be opened
     */
    public void newWindow(String viewAddress, String newTitle,ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(viewAddress));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.setTitle(newTitle);
        stage.show();
    }

    /**This is the confirmationAlert method.  It handles confirmation type alerts.
     @return false if the cancel button is pressed, true if the ok button is pressed
     @param body is the text that will be displayed
     */
    public boolean confirmationAlert(String body) {
        boolean yesClicked = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(body);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            yesClicked = true;
        }

        return yesClicked;
    }



}
