# Further Programming Assignment 2 - Guy Seccull (s3785085)

This is a Java application with a JavaFX GUI made through Scene Builder that allows students of a fake school to make an account through the app, login, then book a seat, with COVID seating restrictions in place. 

## How to run
1. To run this program, start by cloning it into a Java IDE, I've only tested on IntelliJ so use this to prevent 
   unexpected errors.
2. Once loaded up in IntelliJ, right click the root directory folder (assignment-2-GuySeccull) in the file viewer in 
   IntelliJ and select the "Open Module Settings" option. In the "Modules" sub-menu, set the "Language Level" to 8. 
   After this go into the "Project" menu just above "Modules" and set the Project SDK to version 1.8, I've used 
   corretto-1.8 so if all else fails, try using corretto.
4. After changing these settings, exit the settings menu, and set the Main class to 
   assignment-2-GuySeccull/src/main/Main.java, from there it should be able to run. 

The database comes with pre-existing data but will likely be out of date by the time of use, so the only useful data would be the accounts. 
Using IntelliJ or SQLiteBrowser, you can view the database to see what accounts are set up, although my program uses
BCrypt to hash passwords so you won't be able to view them in plaintext, but <b>all the accounts that are set up
use <i>password</i> as the password.</b>

## Design Choices
1. I have used the Singleton pattern to be able to pass data between different scenes.
2. As taught to us in Software Engineering Fundamentals, the MVC architecture works when the view takes input from a 
   user, which is sent to a controller to sanitize or do whatever needs to be done, then it sends that data to the 
   model to retrieve data, which is then sent/displayed directly to the view. In my program I have chosen to use the 
   3-Tier architecture instead of MVC, as I believe it is better that the Model, passes the data back to the controller,
   which then updates the view, as it is likely some modifications will need to be done to the data before being 
   displayed, which is better done in the controller classes, not in the model. I have also chosen to keep the 
   Model-View-Controller naming scheme, just as personal preference.
3. In the assignment specifications sheet, using a whitelisting technique was mentioned to prevent booking the same seat
   twice in a row, I chose not to implement a whitelist into my database, but instead utilise nested SQL queries and 
   joins, along with code, to make this work.
4. As a requirement, an admin must be able to change the seating conditions for a date, but it wasn't specified what 
   happens if that date already has bookings. I chose to prevent an admin from changing seating conditions until all 
   existing bookings have been deleted via the "Cancels Bookings" part of the program.
5. While it is likely important that accepted bookings be kept in the database, even if they were from months ago, I've
   assumed that those bookings that weren't accepted by admins in time, will be deleted, since rejecting a booking 
   deletes it from the database, it makes sense to assume that an admin not accepting a booking can be viewed the same 
   as rejecting it.
   
