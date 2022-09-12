package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Contacts;
import utilities.TimeHelper;

import java.sql.*;
import java.time.LocalDateTime;

/**This is the AppointmentsDao class.  It contains the static methods to pull data from the SQL database for Appointments.*/
public abstract class AppointmentsDAO {

    /**This is the getAllAppointments method.  It creates a sql query and returns all appointments.
     @return all of the appointments in the database*/
    public static ObservableList<Appointments> getAllAppointments() throws SQLException{
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            Appointments appointment = createAppointment(rs);
            allAppointments.add(appointment);

        }
        return allAppointments;
    }

    /**This is the getAppointmentsBetween method.  It creates a sql query and returns all appointments between two date-time's.
     @param startDateTime is the start date and time being pulled from
     @param endDateTime is the end date and time being pulled from
     @return all of the appointments in the database between the two dates*/
    public static ObservableList<Appointments> getAppointmentsBetweenDates(LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException{
        ObservableList<Appointments> appointmentsBetween = FXCollections.observableArrayList();
        startDateTime = TimeHelper.localToUtc(startDateTime);
        endDateTime = TimeHelper.localToUtc(endDateTime);
        Timestamp startDate = Timestamp.valueOf(startDateTime);
        Timestamp endDate = Timestamp.valueOf(endDateTime);
        String sql = "select * from appointments where Start between ? and ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setTimestamp(1, startDate);
        ps.setTimestamp(2, endDate);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointments appointment = createAppointment(rs);
            appointmentsBetween.add(appointment);

        }
        return appointmentsBetween;
    }

    /**This is the getAppointmentsForUser method.  It creates a sql query and returns all appointments for the selected user.
     @param userID is the userID that is being selected for
     @return all of the appointments in the database for the specific user*/
    public static ObservableList<Appointments> getAppointmentsForUser(int userID) throws SQLException{
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE User_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            Appointments appointment = createAppointment(rs);
            allAppointments.add(appointment);

        }
        return allAppointments;
    }

    /**This is the addNewAppointment method.  It creates a sql insert to add a new appointment.
     @param appointment appointment being added
     */
    public static void addNewAppointment(Appointments appointment) throws SQLException{
        LocalDateTime localStart = appointment.getStartTime();
        LocalDateTime localEnd = appointment.getEndTime();
        localStart = TimeHelper.localToUtc(localStart);
        localEnd = TimeHelper.localToUtc(localEnd);
        Timestamp endDateTime = Timestamp.valueOf(localEnd);
        Timestamp startDateTime = Timestamp.valueOf(localStart);
        String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getTypeOfAppointment());
        ps.setTimestamp(5, startDateTime);
        ps.setTimestamp(6, endDateTime);
        ps.setInt(7, appointment.getCustomerID());
        ps.setInt(8, appointment.getUserID());
        ps.setInt(9, appointment.getContactID());
        ps.executeUpdate();
    }

    /**This is the editAppointment method.  It creates a sql update to edit an existing appointment.
     @param appointment appointment being updated
     */
    public static void editAppointment(Appointments appointment) throws SQLException{
        LocalDateTime localStart = appointment.getStartTime();
        LocalDateTime localEnd = appointment.getEndTime();
        localStart = TimeHelper.localToUtc(localStart);
        localEnd = TimeHelper.localToUtc(localEnd);
        Timestamp endDateTime = Timestamp.valueOf(localEnd);
        Timestamp startDateTime = Timestamp.valueOf(localStart);
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?" +
                " WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getTypeOfAppointment());
        ps.setTimestamp(5, startDateTime);
        ps.setTimestamp(6, endDateTime);
        ps.setInt(7, appointment.getCustomerID());
        ps.setInt(8, appointment.getUserID());
        ps.setInt(9, appointment.getContactID());
        ps.setInt(10, appointment.getAppointmentID());
        ps.executeUpdate();
    }

    /**This is the removeAppointment method.  It creates a sql delete to delete an existing appointment.
     @param appointmentID appointment being removed
     */
    public static void removeAppointment(int appointmentID) throws SQLException{
        String sql = "delete from appointments where Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        ps.executeUpdate();
    }

    /**This is the getAllContacts method.  It creates a sql query and returns all contacts.
     @return an observable list of all contacts in the database*/
    public static ObservableList<Contacts> getAllContacts() throws SQLException{
        ObservableList<Contacts> allContacts = FXCollections.observableArrayList();
        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            Contacts contact = new Contacts(contactID, contactName);
            allContacts.add(contact);
        }
        return allContacts;
    }

    /**This is the getContacts method.  It creates a sql query and returns a specific contact.
     @param contactID the contactID for the contact being pulled
     @return an observable list of all contacts in the database*/
    public static Contacts getContact(int contactID) throws SQLException {
        Contacts contact = new Contacts();
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            contact.setContactID(contactID);
            contact.setContactName(rs.getString("Contact_Name"));
        }
        return contact;
    }


    /**This is the getAppointmentsByType method.  It creates a sql query to retrieve appointments of a specified type.
     @param type the type of appointment being pulled
     @return an observable list of the appointments of that type*/
    public static ObservableList<Appointments> getAppointmentsByType(String type) throws SQLException {
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Type = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Appointments appointment = createAppointment(rs);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**This is the getAppointmentsByContact method.  It creates a sql query to retrieve appointments with a specified contact.
     @param contactID the contact being pulled
     @return an observable list of the appointments for that contact*/
    public static ObservableList<Appointments> getAppointmentsByContact(int contactID) throws SQLException{
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Contact_Id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointments appointment = createAppointment(rs);
            appointments.add(appointment);

        }
        return appointments;
    }

    /**This is the getAppointmentsByUser method.  It creates a sql query to retrieve appointments with a specified user.
     @param userID the user being pulled
     @return an observable list of the appointments for that user*/
    public static ObservableList<Appointments> getAppointmentsByUser(int userID) throws SQLException{
        ObservableList<Appointments> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE User_Id = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, userID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Appointments appointment = createAppointment(rs);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**This is the createAppointment Method.  It is a helper method to reduce the code in the AppointmentsDAO.  It creates Appointment Objects
     @param rs the ResultSet being pulled from
     @return the appointment that the ResultSet had contained*/
    public static Appointments createAppointment(ResultSet rs) throws SQLException{
        int appointmentID = rs.getInt("Appointment_ID");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String type = rs.getString("Type");
        Timestamp startTime = rs.getTimestamp("Start");
        Timestamp endTime = rs.getTimestamp("End");
        LocalDateTime zonedStart = startTime.toLocalDateTime();
        LocalDateTime zonedEnd = endTime.toLocalDateTime();
        int customerID = rs.getInt("Customer_ID");
        int userID = rs.getInt("User_ID");
        int contactID = rs.getInt("Contact_ID");
        Appointments appointment = new Appointments(appointmentID, title, description, location, type, zonedStart, zonedEnd, customerID, userID, contactID);
        return appointment;
    }
}
