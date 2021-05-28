package main;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Singleton {
    private Connection connection = SQLConnection.connect();

    // Holds the employeeID of the logged in user
    private int currentUser;

    // When a user logs on and visits the manageBookings page, it will delete any bookings that have a date, less than
    // tomorrow (since this is a requirement), and where the state = 'review' (since the admins might want to keep the
    // actual accepted bookings in the database, even if they're old), but I only want this to run once everytime the
    // user logs in, not everytime they visit manageBookings, so I use this to prevent multiple pointless deletes.
    private boolean hasClearedBookings = false;

    // Holds the date selected by the user, used in various parts of the application
    private LocalDate chosenDate;

    // viewBoookings.fxml is used for both viewing existing bookings, and viewing booking requests, this variable
    // defines which one is being used.
    private String viewBookingsType;

    // Since chooseDate.fxml is used in three scenes, this differentiates between them.
    private String adminDateType = "";

    // Holds the id of the desk chosen by the user
    private int chosenDeskID;

    // In the admin panel, there are multiple parts that are reused, such as the addUpdateAccount.fxml. This is used to
    // add not only new employee accounts, but admin accounts, one array index is needed to differentiate between which
    // account type is being modified. On top of this, addUpdateAccount.fxml, can also be used to update the details of
    // an existing account, so now we need an array index for 2 other things, 1. To determine whether
    // addUpdateAccount.fxml is being used to update an account, or create a new one, 2. To determine which user is
    // having their account modified.
    private Map<String ,String> accountManagementDetails = new HashMap<String, String>();

    // Holds the stage from the Main class, this is to help changing scenes.
    private Stage mainStage;

    // Since createBooking.fxml is used in both creating a new booking, and also modifying an existing booking, a
    // boolean variable is used to differentiate between whether the user is modifying a booking or creating a new one.
    private boolean updateBooking;

    // Holds scene titles mapped to its corresponding fxml file, preventing the need to manually go through every scene
    // change event and change the title, if I wanted to change a scenes title.
    private Map<String ,String> sceneTitle = new HashMap<String, String>();
    private final static Singleton SINGLETON = new Singleton();

    private Singleton() {
        sceneTitle.put("main/ui/login.fxml", "Login");
        sceneTitle.put("main/ui/register.fxml", "Register");
        sceneTitle.put("main/ui/user/userHome.fxml", "User panel");
        sceneTitle.put("main/ui/user/manageBookings.fxml", "User panel - Manage bookings");
        sceneTitle.put("main/ui/user/createBooking.fxml", "User panel - Create booking");
        sceneTitle.put("main/ui/user/chooseDate.fxml", "User panel - Create booking");
        sceneTitle.put("main/ui/user/createBookingPopup.fxml", "Book this seat?");
        sceneTitle.put("main/ui/resetPassword.fxml", "Reset password");
        sceneTitle.put("main/ui/admin/adminHome.fxml", "Admin panel");
        sceneTitle.put("main/ui/admin/accountManagement.fxml", "Admin panel - Account management");
        sceneTitle.put("main/ui/admin/addUpdateAccount.fxml", "Admin panel - Account management");
        sceneTitle.put("main/ui/admin/graphicVisualisation.fxml", "Admin panel - Graphic visualisation");
        sceneTitle.put("main/ui/admin/bookingManagement.fxml", "Admin panel - Booking management");
        sceneTitle.put("main/ui/admin/chooseDate.fxml", "Admin panel - Choose date");
        sceneTitle.put("main/ui/admin/deleteUser.fxml", "Admin panel - Account management");
        sceneTitle.put("main/ui/admin/seatingStatus.fxml", "Admin panel - Booking management");
        sceneTitle.put("main/ui/admin/selectUser.fxml", "Admin panel - Account management");
        sceneTitle.put("main/ui/admin/viewBookings.fxml", "Admin panel - Booking management");
        accountManagementDetails.put("employeeID", "");
        accountManagementDetails.put("accountType", "");
        accountManagementDetails.put("accountAction", "");
        updateBooking = false;
    }

    public static Singleton getInstance() {
        return SINGLETON;
    }

    public void setUser(int employeeID) { this.currentUser = employeeID; }
    public int getUser() { return this.currentUser; }

    public void setDate(LocalDate chosenDate) { this.chosenDate = chosenDate;}
    public LocalDate getDate() { return this.chosenDate; }


    public void changeScene(String filePath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(filePath));
        try {
            this.connection.close();
            if (connection == null)
                System.exit(1);
        }
        catch (SQLException error) {}
        this.mainStage.setTitle(sceneTitle.get(filePath));
        this.mainStage.setScene(new Scene(root));
        this.mainStage.show();
        this.connection = SQLConnection.connect();

    }

    public void setChosenDesk(int seatID) { this.chosenDeskID = seatID;}
    public int getChosenDesk() { return this.chosenDeskID; }

    public void setMainStage(Stage mainStage) { this.mainStage = mainStage;}
    public Stage getMainStage() { return this.mainStage; }

    public void setUpdateBooking(boolean isUpdating) {this.updateBooking = isUpdating;};
    public boolean getUpdateBooking() {return this.updateBooking;};


    public void setAccountManagementDetails(String newDetails, String index) { this.accountManagementDetails.put(index, newDetails);};
    public String getAccountManagementDetails(String index) {return this.accountManagementDetails.get(index);};

    public void setViewBookingsType(String viewBookingType) { this.viewBookingsType = viewBookingType; }
    public String getViewBookingsType() { return this.viewBookingsType; }

    public Connection getConnection() {
        return connection;
    }

    public void setAdminDateType(String dateType) {
        this.adminDateType = dateType;
    }
    public String getAdminDateType() {
        return adminDateType;
    }

    public void setHasClearedBookings(boolean hasClearedBookings) { this.hasClearedBookings = hasClearedBookings; }
    public boolean getHasClearedBookings() { return hasClearedBookings; }
}
