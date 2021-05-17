package main.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class UserHomeController {
    private Singleton singleton = Singleton.getInstance();

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
        singleton.changeScene("main/ui/login.fxml");
    }

    @FXML
    void manageBookingNav(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/user/chooseDate.fxml");
    }

    @FXML
    void newBookingNav(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/user/chooseDate.fxml");
    }

}
