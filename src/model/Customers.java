package model;

import DAO.CustomerDAO;
import DAO.DivisionDAO;

import java.sql.SQLException;

/**This is the Customers Class.  It holds creates Customers objects.*/
public class Customers {
    int customerID;
    String customerName;
    String postalCode;
    String phoneNumber;
    String address;
    int divisionID;
    String countryName;
    String stateName;

    /**This is the Customers constructor with all data.
     @param customerName is the name of the customer
     @param postalCode is the customers postal code
     @param phoneNumber is the customers phone number
     @param address is the customers address
     @param divisionID is the customers divisionID
     @param customerID is customers ID
     */
    public Customers(int customerID, String customerName, String postalCode, String phoneNumber, String address, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.divisionID = divisionID;
        try {
            this.countryName = DivisionDAO.getCountryName(divisionID);
            this.stateName = DivisionDAO.getStateName(divisionID);
        } catch (SQLException sqlException) {
            System.out.println("SQLException: " + sqlException.getErrorCode());
        }
    }

    /**This is the getCustomerID method.
     @return the Customer ID*/
    public int getCustomerID() {
        return customerID;
    }

    /**This is the setCustomerID method.
     @param customerID  the Customer ID*/
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**This is the getCustomerName method.
     @return the Customer Name*/
    public String getCustomerName() {
        return customerName;
    }

    /**This is the setCustomerName method.
     @param customerName  the Customer Name*/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**This is the getPostalCode method.
     @return the Customers postal code*/
    public String getPostalCode() {
        return postalCode;
    }

    /**This is the setPostalCode method.
     @param postalCode  the Customers postal code*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**This is the getPhoneNumber method.
     @return the Customers phone number*/
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**This is the setPhoneNumber method.
     @param phoneNumber  the Customers phone number*/
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**This is the getAddress method.
     @return the Customers address*/
    public String getAddress() {
        return address;
    }

    /**This is the setAddress method.
     @param address  the Customers address*/
    public void setAddress(String address) {
        this.address = address;
    }

    /**This is the getDivisionID method.
     @return the Customers Division code*/
    public int getDivisionID() {
        return divisionID;
    }

    /**This is the setDivisionID method.
     @param divisionID the Customers Division code*/
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**This is the getCountryName method.
     @return the name of the country the customer is in*/
    public String getCountryName() {
        return countryName;
    }

    /**This is the getCountryName method.
     @param countryName  the name of the country the customer is in*/
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**This is the getStateName method.
     @return the name of the State the customer is in*/
    public String getStateName() {
        return stateName;
    }

    /**This is the getStateName method.
     @param stateName  the name of the State the customer is in*/
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
