package model;

/**This is the Users Class.  It holds creates User objects.*/
public class Users {
    int userID;
    String userName;
    String userPassword;

    /**This is the Users constructor with all data.
     @param userID is users ID
     @param userName is the user name
     @param userPassword is the users password
     */
    Users (int userID, String userName, String userPassword) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**This is the setUserID method.
     @param userID  the User ID*/
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**This is the setUserName method.
     @param userName  the User ID*/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**This is the setUserPassword method.
     @param userPassword  the User password*/
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    /**This is the getUserID method.
     @return  the User ID*/
    public int getUserID() {
        return userID;
    }

    /**This is the getUserName method.
     @return   the User ID*/
    public String getUserName() {
        return userName;
    }

    /**This is the getUserPassword method.
     @return the User password*/
    public String getUserPassword() {
        return userPassword;
    }
}
