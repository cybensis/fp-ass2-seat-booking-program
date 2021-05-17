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
    private String username;
    private LocalDate chosenDate;
    private int chosenDeskID;
    private Stage mainStage;
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
        updateBooking = false;
    }

    public static Singleton getInstance() {
        return SINGLETON;
    }

    public void setUser(String username) { this.username = username; }
    public String getUser() { return this.username; }

    public void setDate(LocalDate chosenDate) { this.chosenDate = chosenDate;}
    public LocalDate getDate() { return this.chosenDate; }

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

    public String getTitle(String mapID) {
        return sceneTitle.get("mapID");
    }

    public void setChosenDesk(int seatID) { this.chosenDeskID = seatID;}
    public int getChosenDesk() { return this.chosenDeskID; }

    public void setMainStage(Stage mainStage) { this.mainStage = mainStage;}
    public Stage getMainStage() { return this.mainStage; }

    public void setUpdateBooking(boolean isUpdating) {this.updateBooking = isUpdating;};
    public boolean getUpdateBooking() {return this.updateBooking;};
}
