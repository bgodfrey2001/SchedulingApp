package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/** This is the CustomerScreen Class.  It controls the edit appointment screen functionality.*/
public class ReportsScreen {
    Stage stage;
    Parent scene;

    /** This is the appointmentsClicked method.
     It takes the user to the Appointments by type and month report.
     */
    public void appointmentsClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/CustomerAppointmentReport.fxml", "Customer Appointments", actionEvent);
    }

    /** This is the contactsClicked method.
     It takes the user to the contact schedule report.
     */
    public void contactsClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/ContactSchedule.fxml", "Contact Schedule", actionEvent);
    }

    /** This is the userScheduleClicked method.
     It takes the user to the user schedule report.
     */
    public void userScheduleClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/UserSchedule.fxml", "User Schedule", actionEvent);
    }

    /** This is the backClicked method.
     It takes the user to the Home Page.
     */
    public void backClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/HomeScreen.fxml", "Home Screen", actionEvent);
    }

    /** This is the newWindow method.  It takes you to the next window as directed.
     @param viewAddress is the address of the desired view file
     @param newTitle is the title for display on the next screen*/
    public void newWindow(String viewAddress, String newTitle, ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(viewAddress));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.setTitle(newTitle);
        stage.show();
    }

}
