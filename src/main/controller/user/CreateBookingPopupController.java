package main.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Singleton;
import main.model.user.CreateBookingModel;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        if (!submittedAlready) {
            if (singleton.getUpdateBooking()) {
                createBookingModel.updateBooking(singleton.getUser(), singleton.getChosenDesk(), singleton.getDate());
                singleton.setUpdateBooking(false);
            }
            else
                createBookingModel.addBooking(singleton.getUser(), singleton.getChosenDesk(), singleton.getDate());

            accept.setVisible(false);
            cancel.setVisible(false);
            popupText.setText("Your request has been sent to an admin");
            homepageNav.setVisible(true);
            submittedAlready = true;
            singleton.setChosenDesk(-1);
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
