package DAO;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Customers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**This is the CustomerDao class.  It contains the static methods to pull data from the SQL database for Customers.*/
public abstract class CustomerDAO {

    /**This is the addCustomer method.  It creates a sql insert to add a new customer.
     @param customerName customer name being added
     @param address the address being added
     @param postalCode the postalCode being added
     @param phoneNumber the Phone number being added
     @param stateProvince the state or province being added
     */
    public static int addCustomer(String customerName, String address, String postalCode, String phoneNumber, String stateProvince) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES (?, ?, ?, ?, (SELECT Division_ID FROM first_level_divisions WHERE Division = ?))";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3,postalCode);
        ps.setString(4,phoneNumber);
        ps.setString(5, stateProvince);
        return ps.executeUpdate();
    }

    /**This is the removeCustomer method.  It creates a sql delete to remove a customer.
     @param customerID is the customerID being removed
     */
    public static void removeCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ps.executeUpdate();
    }

    /**This is the getAllCustomers method.  It creates a sql query and returns all customers.
     @return all of the appointments in the database
     */
    public static ObservableList<Customers> getAllCustomers() throws SQLException{
        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPostalCode = rs.getString("Postal_Code");
            String customerPhoneNumber = rs.getString("Phone");
            int divisionID = rs.getInt("Division_ID");
            Customers customers = new Customers(customerID, customerName, customerPostalCode, customerPhoneNumber, customerAddress,  divisionID);
            allCustomers.add(customers);
        }
        return allCustomers;
    }

    /**This is the updateCustomer method.  It creates a sql update to update an existing customer.
     @param customerName customer name being added
     @param address the address being added
     @param postalCode the postalCode being added
     @param phoneNumber the Phone number being added
     @param divisionID divisionID being used
     */
    public static void updateCustomer(int customerID, String customerName, String address, String postalCode, String phoneNumber, int divisionID) throws SQLException {
        String sql = "update customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? " +
        "WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerName);
        ps.setString(2, address);
        ps.setString(3,postalCode);
        ps.setString(4,phoneNumber);
        ps.setInt(5, divisionID);
        ps.setInt(6, customerID);
        ps.executeUpdate();
    }

    /**This is the customerAppointments method.  It creates a sql select to find appointments for a specific customer.
     @param customerID the customer being searched for
     @return an observable list of appointments that the specific customer has
     */
    public static ObservableList<Appointments> customerAppointments(int customerID) throws SQLException{
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp startTime = rs.getTimestamp("Start");
            Timestamp endTime = rs.getTimestamp("End");
            LocalDateTime zonedStart = startTime.toLocalDateTime();
            LocalDateTime zonedEnd = endTime.toLocalDateTime();
            customerID = rs.getInt("Customer_ID");
            int appointmentUserID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointments appointment = new Appointments(appointmentID, title, description, location, type, zonedStart, zonedEnd, customerID, appointmentUserID, contactID);
            appointments.add(appointment);
        }
        return appointments;
    }


}
