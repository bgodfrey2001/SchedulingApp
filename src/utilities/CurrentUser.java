package utilities;

/**This is the Current User class.
 It is used to hold the user who is currently logged in.*/
public class CurrentUser {
    private static int userID;
    private static String userName;

    public static void setUserName(String userName) {
        CurrentUser.userName = userName;
    }

    /**This is the setUserID method.
     @param userID  the User ID*/
    public static void setUserID(int userID) {
        CurrentUser.userID = userID;
    }

    /**This is the getUserID method.
     @return  the User ID*/
    public static int getUserID() {
        return userID;
    }

    /**This is the getUserName method.
     @return   the User ID*/
    public static String getUserName() {
        return userName;
    }
}
