package model;

import DAO.AppointmentsDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.TimeHelper;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**This is the Appointments Class.  It holds creates appointment objects.*/
public class Appointments {
    int appointmentID;
    String title;
    String description;
    String location;
    String typeOfAppointment;
    LocalDateTime startTime;
    LocalDateTime endTime;
    int customerID;
    int userID;
    int contactID;

    /**This is the Appointments constructor with no data*/
    public Appointments() {
    }

    /**This is the Appointments constructor with all data except the appointmentID
     @param title is the title of the appointment
     @param description is the description of the appointment
     @param location is the location of the appointment
     @param typeOfAppointment is the type of the appointment
     @param startTime is the start time of the appointment
     @param endTime is the end time of the appointment
     @param customerID is the customerID of the appointment
     @param contactID is the contactID of the appointment
     @param userID is the userID creating the appointment
     */
    public Appointments(String title, String description, String location, String typeOfAppointment, LocalDateTime startTime, LocalDateTime endTime, int customerID, int userID, int contactID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.typeOfAppointment = typeOfAppointment;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**This is the Appointments constructor with all data
     @param title is the title of the appointment
     @param description is the description of the appointment
     @param location is the location of the appointment
     @param typeOfAppointment is the type of the appointment
     @param startTime is the start time of the appointment
     @param endTime is the end time of the appointment
     @param customerID is the customerID of the appointment
     @param contactID is the contactID of the appointment
     @param userID is the userID creating the appointment
     @param appointmentID is the appointmentID of the appointment
     */
    public Appointments(int appointmentID, String title, String description, String location, String typeOfAppointment, LocalDateTime startTime, LocalDateTime endTime, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.typeOfAppointment = typeOfAppointment;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**This is the getAppointmentID method.
     @return the appointmentID*/
    public int getAppointmentID() {
        return appointmentID;
    }

    /**This is the setAppointmentID method.
     @param appointmentID  the appointmentID*/
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**This is the getTitle method.
     @return the appointment title*/
    public String getTitle() {
        return title;
    }

    /**This is the setTitle method.
     @param title  the appointment title*/
    public void setTitle(String title) {
        this.title = title;
    }

    /**This is the getDescription method.
     @return the appointment description*/
    public String getDescription() {
        return description;
    }

    /**This is the setDescription method.
     @param description  the appointment description*/
    public void setDescription(String description) {
        this.description = description;
    }

    /**This is the getLocation method.
     @return the location title*/
    public String getLocation() {
        return location;
    }

    /**This is the setLocation method.
     @param location the location of the appointment*/
    public void setLocation(String location) {
        this.location = location;
    }

    /**This is the getTypeOfAppointment method.
     @return the type of appointment*/
    public String getTypeOfAppointment() {
        return typeOfAppointment;
    }

    /**This is the setTypeOfAppointment method.
     @param typeOfAppointment  the type of appointment*/
    public void setTypeOfAppointment(String typeOfAppointment) {
        this.typeOfAppointment = typeOfAppointment;
    }

    /**This is the getStartTime method.
     @return the appointment start time*/
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**This is the setStartTime method.
     @param startTime  the appointment start time*/
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**This is the getEndTime method.
     @return the appointment end time*/
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**This is the setEndTime method.
     @param endTime  the appointment end time*/
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**This is the getCustomerID method.
     @return the appointment customer ID*/
    public int getCustomerID() {
        return customerID;
    }

    /**This is the setCustomerID method.
     @param customerID the appointment customer ID*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**This is the getUserID method.
     @return the appointment user ID*/
    public int getUserID() {
        return userID;
    }

    /**This is the setUserID method.
     @param userID  the appointment user ID*/
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**This is the getContactID method.
     @return the appointment Contact ID*/
    public int getContactID() {
        return contactID;
    }

    /**This is the setContactID method.
     @param contactID  the appointment Contact ID*/
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**This is the getContactName method.
     @return the contacts name*/
    public String getContactName() throws SQLException {
        String contactName = AppointmentsDAO.getContact(contactID).toString();
        return contactName;
    }

    /**This is the getLocalStart method.  It gets the utc start and returns it in local time.
     @return the appointment's local start time*/
    public LocalDateTime getLocalStart() {
        LocalDateTime localDateTime = TimeHelper.utcToLocal(startTime);
        return localDateTime;
    }

    /**This is the getLocalEnd method.  It gets the utc end and returns it in local time.
     @return the appointment's local start time*/
    public LocalDateTime getLocalEnd() {
        LocalDateTime localDateTime = TimeHelper.utcToLocal(endTime);
        return localDateTime;
    }

    /**This is the getAppointmentTypes method.  It gets all the types of appointments.
     @return the appointment types*/
    public static ObservableList<String> getAppointmentTypes() {
        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        try {
            allAppointments = AppointmentsDAO.getAllAppointments();
        } catch (SQLException sqlException) {
            System.out.println("SQLException " + sqlException.getErrorCode());
        }

        for (Appointments appointment: allAppointments) {
            if (appointmentTypes.contains(appointment.getTypeOfAppointment())) {
            } else {
                appointmentTypes.add(appointment.getTypeOfAppointment());
            }
        }
        return appointmentTypes;
    }
}
