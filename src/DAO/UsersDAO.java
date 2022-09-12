package DAO;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This is the UsersDao class.  It contains the static methods to pull data from the SQL database for User objects.*/
public abstract class UsersDAO {

    /**This is the getUserID class.  It gets a userID from a user name.
     @param userName is the userName for the user
     @return is the userID*/
    public static int getUserID(String userName) throws SQLException {
        int userID = 0;
        String sql = "SELECT User_ID FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            userID = rs.getInt("User_ID");
        }
        return userID;
    }

    /**This is the getPassword class.  It gets a password from a user name.
     @param userName is the userName for the user
     @return is the password*/
    public static String getPassword(String userName) throws SQLException {
        String userPassword;
        String sql = "SELECT * FROM users";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            if (rs.getString("User_Name").equals(userName)) {
                return rs.getString("Password");
            }
        }
        return "";
    }
}


