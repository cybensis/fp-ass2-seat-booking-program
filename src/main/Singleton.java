package main;


import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Singleton {
    // Holds the username of the logged in user
    private String currentUser;

    // Holds the date selected by the user, used in various parts of the application
    private LocalDate userChosenDate;

    // viewBoookings.fxml is used for both viewing existing bookings, and viewing booking requests, this variable
    // defines which one is being used.
    private String viewBookingsType;

    // Holds the date selected by an admin, in the admin panel
    private LocalDate adminChosenDate;

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
        sceneTitle.put("main/ui/user/createBooking.fxml", "User panel - Create booking");
        sceneTitle.put("main/ui/user/chooseDate.fxml", "User panel - Create booking");
        sceneTitle.put("main/ui/user/createBookingPopup.fxml", "Book this seat?");
        sceneTitle.put("main/ui/resetPassword.fxml", "Reset password");
        sceneTitle.put("main/ui/admin/adminHome.fxml", "Admin panel");
        sceneTitle.put("main/ui/admin/accountManagement.fxml", "Admin panel - Account management");
        sceneTitle.put("main/ui/admin/addUpdateAccount.fxml", "Admin panel - Account management");
        accountManagementDetails.put("employeeID", "");
        accountManagementDetails.put("accountType", "");
        accountManagementDetails.put("accountAction", "");
        updateBooking = false;
    }

    public static Singleton getInstance() {
        return SINGLETON;
    }

    public void setUser(String username) { this.currentUser = username; }
    public String getUser() { return this.currentUser; }

    public void setDate(LocalDate chosenDate) { this.userChosenDate = chosenDate;}
    public LocalDate getDate() { return this.userChosenDate; }

    public void changeScene(String filePath, MouseEvent event) throws IOException {
        Node source = (Node) event.getSource();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(filePath));
        Stage window = (Stage) source.getScene().getWindow();
        window.setTitle(sceneTitle.get(filePath));
        window.setScene(new Scene(root));
        window.show();
    }

    public void changeScene(String filePath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(filePath));
        this.mainStage.setTitle(sceneTitle.get(filePath));
        this.mainStage.setScene(new Scene(root));
        this.mainStage.show();
    }

    public void setChosenDesk(int seatID) { this.chosenDeskID = seatID;}
    public int getChosenDesk() { return this.chosenDeskID; }

    public void setMainStage(Stage mainStage) { this.mainStage = mainStage;}
    public Stage getMainStage() { return this.mainStage; }

    public void setUpdateBooking(boolean isUpdating) {this.updateBooking = isUpdating;};
    public boolean getUpdateBooking() {return this.updateBooking;};


    public void setAccountManagementDetails(String newDetails, String index) { this.accountManagementDetails.put(index, newDetails);};
    public String getAccountManagementDetails(String index) {return this.accountManagementDetails.get(index);};

    public void setAdminDate(LocalDate chosenDate) { this.adminChosenDate = chosenDate;}
    public LocalDate getAdminDate() { return this.adminChosenDate; }

    public void setViewBookingsType(String viewBookingType) { this.viewBookingsType = viewBookingType; }
    public String getViewBookingsType() { return this.viewBookingsType; }
}
