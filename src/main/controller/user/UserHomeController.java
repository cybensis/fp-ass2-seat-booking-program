package main.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class UserHomeController {

    @FXML
    private Label isConnected;

    @FXML
    private Text newBooking;

    @FXML
    private Text manageBooking;

    @FXML
    private ImageView backButton;

    @FXML
    public void goBack(MouseEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/login.fxml"));
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(new Scene(loginView));
        window.show();
    }

    @FXML
    void manageBookingNav(MouseEvent event) {

    }

    @FXML
    void newBookingNav(MouseEvent event) throws IOException {
        Parent loginView = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/user/createBooking.fxml"));
        Stage window = (Stage) backButton.getScene().getWindow();
        window.setScene(new Scene(loginView));
        window.show();
    }

}
