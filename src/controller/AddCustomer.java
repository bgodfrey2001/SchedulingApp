package controller;

import DAO.CustomerDAO;
import DAO.DivisionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** This is the AddCustomer Class.  It controls the add customer screen functionality.*/
public class AddCustomer implements Initializable {
    public TextField customerNameTextField;
    public TextField postalCodeTextField;
    public TextField phoneNumberTextField;
    public TextArea addressTextArea;
    public ComboBox countryComboBox;
    public ComboBox stateComboBox;

    Stage stage;
    Parent scene;

    /** This is the initialize method.
     It initializes the Add Customer Screen with the combo boxes filled.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        ObservableList<String> countryList = FXCollections.observableArrayList();
        try {
            countryList = DivisionDAO.getAllCountryNames();
        } catch (SQLException sqlException) {
            System.out.println("SQL Exception: " + sqlException);
        }

        countryComboBox.setItems(countryList);
        countryComboBox.getSelectionModel().select(0);
        ObservableList<String> stateNames = FXCollections.observableArrayList();
        try {
            int countryID = (DivisionDAO.getCountryID(String.valueOf(countryComboBox.getSelectionModel().getSelectedItem())));
            stateNames = DivisionDAO.getAllStateNames(countryID);
        } catch (SQLException sqlException) {
            System.out.println("SQLException: " + sqlException.getErrorCode());
        }
        stateComboBox.setItems(stateNames);
        stateComboBox.getSelectionModel().select(0);
    }

    /** This is the saveClicked method.
     It saves the customer when save is clicked if data is provided.
     */
    public void saveClicked(ActionEvent actionEvent) throws IOException{
        boolean saveAble = true;
        String customerName = customerNameTextField.getText();
        String address = addressTextArea.getText();
        String postalCode = postalCodeTextField.getText();
        String phoneNumber = phoneNumberTextField.getText();
        String stateName = String.valueOf(stateComboBox.getSelectionModel().getSelectedItem());
        if(!validInput(customerName)) {
            warningAlert("Invalid Input", "Please enter a customer name.");
            saveAble = false;
        }
        if(!validInput(address)) {
            warningAlert("Invalid Input", "Please enter an address.");
            saveAble = false;
        }
        if(!validInput(postalCode)) {
            warningAlert("Invalid Input", "Please enter a postal code.");
            saveAble = false;
        }

        if(!validInput(phoneNumber)) {
            warningAlert("Invalid Input", "Please enter a phone number.");
            saveAble = false;
        }

        if (saveAble) {
            try {
                CustomerDAO.addCustomer(customerName, address, postalCode, phoneNumber, stateName);
            } catch (SQLException sqlException) {
                System.out.println("SQLException: " + sqlException.getErrorCode());
            }
            newWindow("/view/CustomerScreen.fxml", "Customers", actionEvent);
        }

    }

    /** This is the cancelClicked method.
     It returns you to the Customer Screen when cancelled is clicked.
     */
    public void cancelClicked(ActionEvent actionEvent) throws IOException{
        newWindow("/view/CustomerScreen.fxml", "Customers",actionEvent);
    }

    /** This is the countrySelected method.
     It fills the state box depending on selected country selected.
     */
    public void countrySelected(ActionEvent actionEvent) throws SQLException{
        int countryID = (DivisionDAO.getCountryID(String.valueOf(countryComboBox.getSelectionModel().getSelectedItem())));
        ObservableList<String> stateNames = DivisionDAO.getAllStateNames(countryID);
        stateComboBox.setItems(stateNames);
        stateComboBox.getSelectionModel().select(0);
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

    /**This is the validInput method.  It checks a string to ensure there is something entered.
     @param string is the string to be checked */
    public boolean validInput(String string) {
        if (string.length() > 0) {
            return true;
        }
        return false;
    }

    /**This is the warningAlert method.  It handles warning type alerts.
     @param header is the text that will be displayed in the header
     @param body is the text that will be displayed in the body
     */
    public void warningAlert(String header, String body) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(header);
        alert.setContentText(body);
        alert.showAndWait();
    }
}
