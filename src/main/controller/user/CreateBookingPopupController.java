package main.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Singleton;
import main.model.user.CreateBookingModel;

import java.io.IOException;
import java.sql.SQLException;

public class CreateBookingPopupController {

    private CreateBookingModel createBookingModel = new CreateBookingModel();
    private Singleton singleton = Singleton.getInstance();
    private boolean submittedAlready = false;

    @FXML
    private Text popupText;

    @FXML
    private Button accept;

    @FXML
    private Button cancel;

    @FXML
    private Text homepageNav;

    @FXML
    private void acceptBooking(MouseEvent event) throws SQLException {
        String response = "";
        if (!submittedAlready) {
            if (singleton.getUpdateBooking()) {
                popupText.setText("Are you sure you want to change this booking?");
                response = createBookingModel.updateBooking();
                singleton.setUpdateBooking(false);
            }
            else
                response = createBookingModel.addBooking();

            accept.setVisible(false);
            cancel.setVisible(false);
            homepageNav.setVisible(true);
            submittedAlready = true;
            singleton.setChosenDesk(-1);
            singleton.setDate(null);
            singleton.setBookingDate(null);
            if (!response.equals("Success"))
                popupText.setText("Sorry, an unexpected error occurred");
            else
                popupText.setText("Your request has been sent to an admin");

        }

    }

    @FXML
    private void cancelBooking(MouseEvent event) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();

    }


    @FXML
    private void goHome(MouseEvent event) throws IOException {
        submittedAlready = false;
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
        singleton.changeScene("main/ui/user/userHome.fxml");

    }


}
