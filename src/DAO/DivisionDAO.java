package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This is the DivisionDao class.  It contains the static methods to pull data from the SQL database for Country and first level divisions.*/
public abstract class DivisionDAO {

    /**This is the getAllCountryNames method.  It creates a sql select to get all the country names from the countries database.
     @return an observable list of all the country names
     */
    public static ObservableList<String> getAllCountryNames() throws SQLException {
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        String sql = "SELECT * FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            countryNames.add(rs.getString("Country"));
        }
        return countryNames;
    }

    /**This is the getAllStateNames method.  It creates a sql select to get all the state names for a specified country.
     @param countryCode is the country code being used
     @return an observable list of all the state
     */
    public static ObservableList<String> getAllStateNames(int countryCode) throws SQLException {
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryCode);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            divisionNames.add(rs.getString("Division"));
        }
        return divisionNames;
    }

    /**This is the getCountryID method.  It creates a sql select to get the countryID for a country name.
     @param countryName is the country name being used
     @return an integer value for the country ID
     */
    public static int getCountryID(String countryName) throws SQLException {
        int countryID = 0;
        String sql = "SELECT Country_ID FROM countries WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, countryName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            countryID = rs.getInt("Country_ID");
        }
        return countryID;
    }

    /**This is the getCountryID method.  It creates a sql select to get the countryID for a division.
     @param divisionID is the division being used
     @return an integer value for the countryID
     */
    public static int getCountryID(int divisionID) throws SQLException {
        int countryID = 0;
        String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            countryID = rs.getInt("Country_ID");
        }
        return countryID;
    }

    /**This is the getDivisionID method.  It creates a sql select to get the divisionID for a state.
     @param stateName is the state name being searched from
     @return an integer value for the division ID
     */
    public static int getDivisionID(String stateName) throws SQLException {
        int divisionID = 0;
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, stateName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            divisionID = rs.getInt("Division_ID");
        }
        return divisionID;
    }

    /**This is the getCountryName method.  It creates a sql select to get the name of a country from it's divisionID.
     @param divisionID is the divisionID being looked at
     @return a string with the name of the country
     */
    public static String getCountryName(int divisionID) throws SQLException{
        String countryName = "";
        String sql = "SELECT * FROM countries WHERE Country_ID = (SELECT Country_ID FROM first_level_divisions WHERE Division_ID = ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            countryName = rs.getString("Country");
        }
        return countryName;
    }

    /**This is the getStateName method.  It creates a sql select to get the name of a state from it's divisionID.
     @param divisionID is the divisionID being looked at
     @return a string with the name of the state
     */
    public static String getStateName(int divisionID) throws SQLException{
        String stateName = "";
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            stateName = rs.getString("Division");
        }
        return stateName;
    }
}
