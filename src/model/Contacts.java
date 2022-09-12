package model;

/**This is the Contacts Class.  It holds creates Contact objects.*/
public class Contacts {
    int contactID;
    String contactName;

    /**This is the Contacts constructor with all data
     @param contactID is the contacts ID
     @param contactName is the name of the contact
     */
    public Contacts(int contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
    }

    /**This is the Contacts constructor with no data*/
    public Contacts() {

    }

    /**This is the setContactID method.
     @param contactID is the contacts ID*/
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**This is the setContactName method.
     @param contactName is the contacts name*/
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**This is the getContactID method.
     @return the contacts ID*/
    public int getContactID() {
        return contactID;
    }

    /**This is the getContactName method.
     @return  the contacts name*/
    public String getContactName() {
        return contactName;
    }

    /**This is the overridden toString method.  It overides the toString method to return the contactName.
     @return the contacts name
     **/
    @Override
    public String toString() {
        return (contactName);
    }
}
