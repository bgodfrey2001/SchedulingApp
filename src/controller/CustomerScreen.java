package controller;

import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import utilities.CurrentCustomer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** This is the CustomerScreen Class.  It controls the edit appointment screen functionality.*/
public class CustomerScreen implements Initializable {
    public TableView<Customers> customersTableView;
    public TableColumn customerIDCol;
    public TableColumn customerNameCol;
    public TableColumn customerAddressCol;
    public TableColumn customerPostalCodeCol;
    public TableColumn customerPhoneCol;
    public TableColumn customerCountryCol;
    public TableColumn customerStateCol;

    Stage stage;
    Parent scene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayCustomerTable();
    }

    /**This is the addCustomerClicked method.  It opens up the add customer screen.*/
    public void addCustomerClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/AddCustomer.fxml", "Add Customer",actionEvent);
    }

    /**This is the removeCustomerClicked method.  It takes the selected customer and attempts to remove it from the database.*/
    public void removeCustomerClicked(ActionEvent actionEvent) throws SQLException{
        int customerID = customersTableView.getSelectionModel().getSelectedItem().getCustomerID();
        String customerName = customersTableView.getSelectionModel().getSelectedItem().getCustomerName();
        if(CustomerDAO.customerAppointments(customerID).isEmpty()) {
            String body = customerName + " has been removed from the customer database.";
            warningAlert("Customer Removal", body);
            CustomerDAO.removeCustomer(customerID);
            displayCustomerTable();
        } else {
            warningAlert("Customer has appointments.", "Please remove all appointments before deleting customer.");
        }
    }

    /**This is the editcustomerClicked method.  It takes the selected customer when the edit button is clicked
     and opens up the edit customer screen.*/
    public void editCustomerClicked(ActionEvent actionEvent) throws IOException {
        CurrentCustomer.currentCustomer = (Customers) customersTableView.getSelectionModel().getSelectedItem();

        newWindow("/view/EditCustomer.fxml", "Edit Customer",actionEvent);
    }

    /** This is the backClicked Method.  It takes you back to the home screen if you click back.*/
    public void backClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/HomeScreen.fxml", "Home Screen",actionEvent);
    }

    /** This is the displayCustomerTable Method.  It fills the customer table view with customers.*/
    public void displayCustomerTable() {
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
        try {
            allCustomers = CustomerDAO.getAllCustomers();
        } catch (SQLException sqlException) {
            System.out.println("SQL Exception: " + sqlException.getErrorCode());
        }

        customersTableView.setItems(allCustomers);
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        customerStateCol.setCellValueFactory(new PropertyValueFactory<>("stateName"));
    }

    /** This is the newWindow Method.  It controls opening new screens.
     @param viewAddress is the address of the view file being opened
     @param newTitle is the title of the new page*/
    public void newWindow(String viewAddress, String newTitle,ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(viewAddress));
        stage.setScene(new Scene(scene));
        stage.centerOnScreen();
        stage.setTitle(newTitle);
        stage.show();
    }

    /** This is the warningAlert Method.  It creates warnings.
     @param header contains the header text to be displayed
     @param body contains the body text to be displayed*/
    public void warningAlert(String header, String body) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    }
}
