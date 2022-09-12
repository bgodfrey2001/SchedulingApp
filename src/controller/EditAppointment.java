package controller;

import DAO.AppointmentsDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;
import utilities.CurrentAppointment;
import model.Contacts;
import utilities.TimeHelper;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the EditAppointment Class.  It controls the edit appointment screen functionality.*/
public class EditAppointment implements Initializable {
    public TextField appointmentIDTextField;
    public TextField titleTextField;
    public TextField locationTextField;
    public ComboBox<Contacts> contactComboBox;
    public TextField typeTextField;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public TextField customerIDTextField;
    public TextField userIDTextField;
    public TextArea descriptionTextArea;
    public ComboBox<Integer> startHourComboBox;
    public ComboBox<Integer> startMinuteComboBox;
    public ComboBox<Integer> endHourComboBox;
    public ComboBox<Integer> endMinuteComboBox;
    Stage stage;
    Parent scene;

    /** This is the initialize method.
     It initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIDTextField.setText(String.valueOf(CurrentAppointment.currentAppointment.getAppointmentID()));
        titleTextField.setText(CurrentAppointment.currentAppointment.getTitle());
        locationTextField.setText(CurrentAppointment.currentAppointment.getLocation());
        try {
            setContactComboBox();
        } catch (SQLException sqlException) {
            System.out.println("SQLException " + sqlException.getErrorCode());
        }

        typeTextField.setText(CurrentAppointment.currentAppointment.getTypeOfAppointment());
        startDatePicker.setValue(CurrentAppointment.currentAppointment.getLocalStart().toLocalDate());
        endDatePicker.setValue(CurrentAppointment.currentAppointment.getLocalEnd().toLocalDate());
        setHourBoxes();
        setMinuteBoxes();
        startHourComboBox.getSelectionModel().select(CurrentAppointment.currentAppointment.getLocalStart().toLocalTime().getHour());
        endHourComboBox.getSelectionModel().select(CurrentAppointment.currentAppointment.getLocalEnd().toLocalTime().getHour());
        startMinuteComboBox.getSelectionModel().select(CurrentAppointment.currentAppointment.getLocalStart().toLocalTime().getMinute());
        endMinuteComboBox.getSelectionModel().select(CurrentAppointment.currentAppointment.getLocalEnd().toLocalTime().getMinute());
        customerIDTextField.setText(String.valueOf(CurrentAppointment.currentAppointment.getCustomerID()));
        userIDTextField.setText(String.valueOf(CurrentAppointment.currentAppointment.getUserID()));
        descriptionTextArea.setText(CurrentAppointment.currentAppointment.getDescription());
    }

    /** This is the saveClicked method.
     It saves the appointment when save is clicked if data is provided.
     */
    public void saveClicked(ActionEvent actionEvent) throws IOException {
        boolean saveAble = true;
        String title =  titleTextField.getText();
        String location = locationTextField.getText();
        int contactID = contactComboBox.getSelectionModel().getSelectedItem().getContactID();;
        String type = typeTextField.getText();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        int startHour = startHourComboBox.getSelectionModel().getSelectedItem();
        int startMinute = startMinuteComboBox.getSelectionModel().getSelectedItem();
        int endHour = endHourComboBox.getSelectionModel().getSelectedItem();
        int endMinute = endMinuteComboBox.getSelectionModel().getSelectedItem();

        LocalTime startTime = LocalTime.of(startHour, startMinute);
        LocalTime endTime = LocalTime.of(endHour, endMinute);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        LocalDateTime confirmationStart = TimeHelper.localToEST(startDateTime);
        LocalDateTime confirmationEnd = TimeHelper.localToEST(endDateTime);
        if (!confirmTimeOfficeHours(confirmationStart, "begin")) {
            saveAble = false;
        }
        if (!confirmTimeOfficeHours(confirmationEnd, "end")) {
            saveAble = false;
        }

        int customerID = 0;
        int userID = 0;
        try {
            customerID = Integer.parseInt(customerIDTextField.getText());
            userID = Integer.parseInt(userIDTextField.getText());
        } catch (NumberFormatException numberFormatException){
            saveAble = false;
        }

        String description = descriptionTextArea.getText();

        if (title.length() < 1 || location.length() < 1 || type.length() < 1 || description.length() < 1 || customerID == 0  || userID == 0){
            warningAlert("Enter All Values", "You must select a value for all fields");
            saveAble = false;
        }

        try {
            if(!avoidConflicts(customerID, startDateTime, endDateTime))
            {
                saveAble = false;
            }
        } catch (SQLException sqlException) {
            System.out.println("SQLException: " + sqlException.getErrorCode());
        }

        if(saveAble) {
            try {
                System.out.println("Saved Appointment " + CurrentAppointment.currentAppointment.getAppointmentID());
                Appointments thisAppointment = new Appointments(CurrentAppointment.currentAppointment.getAppointmentID() ,title, description, location, type, startDateTime, endDateTime, customerID, userID, contactID);
                AppointmentsDAO.editAppointment(thisAppointment);
            } catch (SQLException sqlException) {
                System.out.println("SQLException: " + sqlException.getErrorCode());
            }

            newWindow("/view/AppointmentScreen.fxml", "Appointments",actionEvent);
        }
    }

    /** This is the cancelClicked method.
     It returns you to the Appointments Screen when cancelled is clicked.
     */
    public void cancelClicked(ActionEvent actionEvent) throws IOException{
        //Scene currentScene;
        newWindow("/view/AppointmentScreen.fxml", "Appointments",actionEvent);
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

    /** This is the setHoursBoxes method.  It sets the two hour boxes with numbers from 0 to 23.
     */
    public void setHourBoxes() {
        ObservableList<Integer> times = FXCollections.observableArrayList();
        for (int i = 0; i < 24; i++) {
            times.add(i);
        }
        startHourComboBox.setItems(times);
        endHourComboBox.setItems(times);
    }

    /** This is the setMinuteBoxes method.  It sets the two minute boxes.
     */
    public void setMinuteBoxes() {
        ObservableList<Integer> times = FXCollections.observableArrayList();

        for (int i = 0; i < 60; i++) {
            times.add(i);
        }
        startMinuteComboBox.setItems(times);
        endMinuteComboBox.setItems(times);
    }


    /** This is the setContactComboBox method.  It sets the contact combobox with all contacts in the database.
     */
    public void setContactComboBox() throws SQLException {
        ObservableList<Contacts> allContacts = AppointmentsDAO.getAllContacts();
        contactComboBox.setItems(allContacts);
        Contacts currentContact = AppointmentsDAO.getContact(CurrentAppointment.currentAppointment.getContactID());
        contactComboBox.getSelectionModel().select(currentContact);
    }

    /** This is the confirmTimeOfficeHours method.  checks to see if the appointment is within office hours, and gives a confirmation alert if it isn't.
     @param localDateTime is the time being checked
     @param beginOrEnd is a string variable for setting up the confirmation alert text
     */
    public boolean confirmTimeOfficeHours(LocalDateTime localDateTime, String beginOrEnd) {
        boolean confirmed = true;
        LocalTime startOfHours = LocalTime.of(8, 0);
        LocalTime endOfHours = LocalTime.of(22, 0);
        LocalTime localTime = localDateTime.toLocalTime();
        if (localTime.isBefore(startOfHours) || localTime.isAfter(endOfHours)) {
            String confirmationString = "Your appointment is scheduled to " + beginOrEnd + " outside of business hours, is this correct?";
            confirmed = confirmationAlert("Confirm Time", confirmationString);
        }
        return confirmed;
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

    /**This is the confirmationAlert method.  It handles confirmation type alerts.
     @param header is the text that will be displayed in the header
     @param body is the text that will be displayed
     */
    public void warningAlert(String header, String body) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    }

    /** This is the avoidConflicts method.  It checks to see if there is a conflicting time that the customer is already booked for an appointment.
     @param appointmentStart is the start time being checked for conflict
     @param appointmentEnd is the end time being checked for conflict
     @param customerID is the customerID being checked for the conflict
     */
    public boolean avoidConflicts(int customerID, LocalDateTime appointmentStart, LocalDateTime appointmentEnd) throws SQLException{
        boolean noConflict = true;
        ObservableList<Appointments> appointments = CustomerDAO.customerAppointments(customerID);

        for (Appointments appt: appointments) {
                if (appointmentStart.isAfter(appt.getEndTime()) || appointmentEnd.isBefore(appt.getStartTime())) {
                    noConflict = true;

                } else if (appt.getAppointmentID() != CurrentAppointment.currentAppointment.getAppointmentID()) {
                    LocalTime start = appt.getStartTime().toLocalTime();
                    LocalTime end = appt.getEndTime().toLocalTime();
                    String body = "There is already an appointment for that customer between " + start + " and " + end + ".";
                    warningAlert("Schedule Conflict", body);
                    noConflict = false;
                    break;
                }

        }
        return noConflict;
    }
}
