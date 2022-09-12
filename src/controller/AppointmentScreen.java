package controller;

import DAO.AppointmentsDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import utilities.CurrentAppointment;
import utilities.StringBuilderInterface;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the AppointmentScreen Class.  It is the controller for the appointment screen.  It contains a table view for all appointments, as well as several buttons
 with functionality.
 */
public class AppointmentScreen implements Initializable {
    public TableView<Appointments> appointmentsTableView;
    public TableColumn appointmentIDTableCol;
    public TableColumn titleTableCol;
    public TableColumn DescriptionTableCol;
    public TableColumn locationTableCol;
    public TableColumn contactTableCol;
    public TableColumn typeTableCol;
    public TableColumn startTableCol;
    public TableColumn endTableCol;
    public TableColumn customerIDTableCol;
    public TableColumn userIDTableCol;
    Stage stage;
    Parent scene;

    /** This is the initialize method.  It initializes the screen with the tableview displayed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            displayTable(AppointmentsDAO.getAllAppointments());
        } catch (SQLException sqlException) {
            System.out.println("SQLException: " + sqlException.getErrorCode());
        }
    }

    /**This is the editAppointmentClicked method.  It takes the selected appointment when the edit button is clicked
     and opens up the edit appointment screen.*/
    public void editAppointmentClicked(ActionEvent actionEvent) throws IOException {
        utilities.CurrentAppointment.currentAppointment = (Appointments) appointmentsTableView.getSelectionModel().getSelectedItem();
        newWindow("/view/EditAppointment.fxml", "Edit Appointment", actionEvent);
    }
    /**This is the addAppointmentClicked method.  It opens up the add appointment screen.*/
    public void addAppointmentClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/AddAppointment.fxml","Add Appointment",actionEvent);
    }

    /**This is the removeAppointmentClicked method.  It takes the selected appointment and removes it from the database.
     It contains a Lambda expression called StringBuilderInterface that builds the strings for the confirmation alert.
     The Lambda was used to simplify the creation of customizable strings
     */
    public void removeAppointmentClicked(ActionEvent actionEvent) throws SQLException{
        boolean wishToRemove;
        int appointmentID = appointmentsTableView.getSelectionModel().getSelectedItem().getAppointmentID();
        String appointmentType = appointmentsTableView.getSelectionModel().getSelectedItem().getTypeOfAppointment();

        StringBuilderInterface headerBuilderInterface = stringPassed -> "Appointment ID: " + stringPassed;
        StringBuilderInterface bodyBuilderInterface = stringPassed -> "Are you sure you wish to cancel your " + stringPassed + " appointment?";
        String confirmationHeader = headerBuilderInterface.stringBuilder(String.valueOf(appointmentID));
        String confirmationMessage = bodyBuilderInterface.stringBuilder(appointmentType);
        wishToRemove = confirmationAlert(confirmationHeader, confirmationMessage);

        if (wishToRemove) {
            AppointmentsDAO.removeAppointment(appointmentID);
            displayTable(AppointmentsDAO.getAllAppointments());
        }
    }

    /** This is the backClicked method.  It takes you back to the home screen when the back button is clicked.
     */
    public void backClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/HomeScreen.fxml", "Home Screen", actionEvent);
    }

/** This is the allAppointmentsSelected method.  It displays the full tableview when all appointments is selected from the radiobuttons */
    public void allAppointmentsSelected(ActionEvent actionEvent) throws SQLException {
        displayTable(AppointmentsDAO.getAllAppointments());
    }

    /** This is the thisMonthSelected method.  It displays the tableview when this month is selected from the radiobuttons */
    public void thisMonthSelected(ActionEvent actionEvent) throws SQLException{
        LocalTime startTime = LocalTime.of(0,0);
        LocalDateTime todaysDate = LocalDateTime.of(LocalDate.now(), startTime); //Starts the month today
        LocalDateTime monthsEnd = todaysDate.plusMonths(1);
        monthsEnd.plusHours(23);
        monthsEnd.plusMinutes(59); //Adds a month and takes it to the end of that day
        ObservableList<Appointments> appointmentsThisMonth = AppointmentsDAO.getAppointmentsBetweenDates(todaysDate, monthsEnd);
        displayTable(appointmentsThisMonth);
    }

    /** This is the thisWeekSelected method.  It displays the tableview when this week is selected from the radiobuttons */
    public void thisWeekSelected(ActionEvent actionEvent) throws SQLException{
        LocalTime startTime = LocalTime.of(0,0);
        LocalDateTime todaysDate = LocalDateTime.of(LocalDate.now(), startTime); //Starts the month today
        LocalDateTime monthsEnd = todaysDate.plusWeeks(1);
        monthsEnd.plusHours(23);
        monthsEnd.plusMinutes(59); //Adds a month and takes it to the end of that day
        ObservableList<Appointments> appointmentsThisWeek = AppointmentsDAO.getAppointmentsBetweenDates(todaysDate, monthsEnd);
        displayTable(appointmentsThisWeek);
    }

    /** This is the displayTable method.  It displays the tableview
     @param appointments takes an observablelist and displays it on the tableview */
    public void displayTable(ObservableList<Appointments> appointments) {
        appointmentsTableView.setItems(appointments);
        appointmentIDTableCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleTableCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        DescriptionTableCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTableCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactTableCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeTableCol.setCellValueFactory(new PropertyValueFactory<>("typeOfAppointment"));
        startTableCol.setCellValueFactory(new PropertyValueFactory<>("localStart"));
        endTableCol.setCellValueFactory(new PropertyValueFactory<>("localEnd"));
        customerIDTableCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDTableCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        appointmentsTableView.getSortOrder().add(startTableCol);
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

    /** This is the confirmationAlert method.  It displays the a confirmation alert.
     @param header is the string for the header of the alert
     @param body is the string for the body of the alert*/
    public boolean confirmationAlert(String header, String body) {
        boolean yesClicked = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(header);
        alert.setContentText(body);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            yesClicked = true;
        }

        return yesClicked;
    }
}
