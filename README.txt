Scheduling Application
The purpose of this application is to provide scheduling software for a company.  It allows multiple users to have logins and be able to schedule, view,
and edit appointments.  It also allows the users to view and edit customers information, and remove customers all together.

Bradley Godfrey, bgodf20@wgu.edu, V1.0, March 25 2022

IntelliJ Community 2021.2.3
JDK 17.0.1
JavaFX-SDK-17.0.1
Mysql Connector 8.0.26 was used for the driver connector.


When you launch the application you are greeted with a login screen.  You will need to enter a valid user name and password.  The first one in the database
is UserName: test Password: test.  Once you have entered that you will be given a notification letting you know if you have an appointment in the next 15
minutes or not.  You then have four options.  Appointments, Customers, Reports and Log Out.  If you click log out you will be taken back to the login screen.

Appointments takes you to the Appointments page, which allows you to view all appointments, appointments in the next month, or appointments in the next week.
From there you can add an appointment by clicking ADD, remove an appointment by selecting once and clicking remove, or edit an appointment by selecting an
appointment and clicking edit.

Clicking back will take you back to the home screen where you can then select Customers.  The customers screen allows you to see all of the customers for
the company.  You can also click ADD to add a new customer, remove to remove an existing customer provided they don't have any appointments, or edit to
edit an existing customer.

Clicking back will take you back to the home screen.  You can then click reports which will provide three reports.  The first is appointments by type.
Appointments by type will give you a report of a selected type and month.  It lets you know how many apppointments exist in a given month for a given type.

Contacts Schedules will give you a report on the schedules different contacts have.  You enter a contact ID and it will list all the appointments they have
coming up.

User schedule is the report I have designated for A.3.f.  It is the schedule for the logged in user.  When selected it displays in chronological order the
appointments that the user has coming.

From there you can click back which takes you to the home page.  You can then click log out and be taken back to the log in page.

