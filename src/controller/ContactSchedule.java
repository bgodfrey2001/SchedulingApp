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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** This is the ContactSchedule Class.  It controls the Contacts Schedule Report screen functionality.*/
public class ContactSchedule{
    Stage stage;
    Parent scene;

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

    public Label appointmentsLabel;
    public TextField contactIDTextField;


    /** This is the backButtonClicked method.
     It returns you to the Report Screen when back is clicked.
     */
    public void backClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/ReportsScreen.fxml", "Reports", actionEvent);
    }

    /** This is the generateReportClicked method.
     It displays the contact's schedule in a tableview when clicked.
     */
    public void generateReportClicked(ActionEvent actionEvent) throws SQLException {
        int contactID = Integer.parseInt(contactIDTextField.getText());
        setAppointmentsTableView(AppointmentsDAO.getAppointmentsByContact(contactID));
        String contactName = AppointmentsDAO.getContact(contactID).toString();
        appointmentsLabel.setText("Appointments for " + contactName);
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

    /** This is the setAppointmentsTableView Method.  It fills the customer table view with appointments for the selected contact.*/
    public void setAppointmentsTableView(ObservableList<Appointments> appointments) {
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
}
