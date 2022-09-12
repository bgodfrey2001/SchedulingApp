package controller;

import DAO.AppointmentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Appointments;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** This is the CustomerAppointmentReport Class.  It controls the Appointment Report screen functionality.*/
public class CustomerAppointmentReport implements Initializable {

    public Label numberOfAppointments;
    public ComboBox<String> monthPicker;
    public ComboBox<String> typePicker;
    Stage stage;
    Parent scene;


    /** This is the initialize method.
     It initializes the controller.  It calls methods to set up the combo boxes values.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        monthPicker.setItems(getMonths());
        monthPicker.getSelectionModel().select(0);
        fillTypes();
        numberOfAppointments.setText("");
    }

    /** This is the saveClicked method.
     It generates the report based on the month and type of appointment clicked.
     */
    public void generateReportClicked(ActionEvent actionEvent) throws SQLException{
        String month = monthPicker.getSelectionModel().getSelectedItem();
        int monthInNum = monthToInt(month);
        String type = typePicker.getSelectionModel().getSelectedItem();
        int number = numberOfType(monthInNum, type);
        numberOfAppointments.setText(String.valueOf(number));
    }

    /** This is the backButtonClicked method.
     It returns you to the Customer Screen when back is clicked.
     */
    public void backButtonClicked(ActionEvent actionEvent) throws IOException {
        newWindow("/view/ReportsScreen.fxml", "Reports", actionEvent);
    }

    /** This is the newWindow method.  It takes you to the next window as directed.
     @param viewAddress is the address of the desired view file
     @param newTitle is the title for display on the next screen*/
    public void newWindow(String viewAddress, String newTitle,ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(viewAddress));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.setTitle(newTitle);
        stage.show();
    }

    /** This is the getMonths method.  It provides an observable list with all of the months
     @return an observable list of the names of the months*/
    public ObservableList<String> getMonths() {
        ObservableList<String> months = FXCollections.observableArrayList();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        return months;
    }

    /** This is the monthToInt method.  It provides an integer value of the month being checked.
     @return The integer value of the month passed in.*/
    public int monthToInt(String searchingMonth) {
        int monthInNum = 0;
        int i = 0;
        ObservableList<String> months = getMonths();
        for (String month : months) {
            i++;
            if (month.equals(searchingMonth)){
                monthInNum = i;
                break;
            }
        }
        return monthInNum;
    }
    /** This is the fillTypes method.  It provides the types for the type combo box*/
    public void fillTypes() {
        ObservableList<String> types = Appointments.getAppointmentTypes();
        typePicker.setItems(types);
        typePicker.getSelectionModel().select(0);
    }

    /** This is the numberOfType method.  It calculates the number of times appointments of a certain type are seen in that month
     @param month the integer value of the month in question
     @param type the Type of appointment in question
     @return the number of times that type was found in the month*/
    public int numberOfType(int month, String type) throws SQLException {
        ObservableList<Appointments> appointmentsByType = AppointmentsDAO.getAppointmentsByType(type);
        int counter = 0;
        for (Appointments appointment: appointmentsByType) {
            if(appointment.getLocalStart().toLocalDate().getMonth().getValue() == month) {
                counter++;
            }
        }
        return counter;
    }

}
