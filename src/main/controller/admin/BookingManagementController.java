package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class BookingManagementController {
    private Singleton singleton = Singleton.getInstance();

    @FXML
    private Label isConnected;

    @FXML
    private Text dotPointOne;

    @FXML
    private Text dotPointTwo;

    @FXML
    private ImageView backButton;

    @FXML
    private Text subHeader;

    @FXML
    private Text dotThree;

    @FXML
    private Text dotPointThree;


    @FXML
    private void goBack(MouseEvent event) {

    }

    @FXML
    private void manageBookings(MouseEvent event) throws IOException {
        this.singleton.setViewBookingsType("existingBookings");
    }

    @FXML
    private void manageRequests(MouseEvent event) throws IOException {
        this.singleton.setViewBookingsType("requestedBookings");
        this.singleton.changeScene("main/ui/admin/viewBookings.fxml");
    }

    @FXML
    private void seatingStatus(MouseEvent event) {

    }

}
