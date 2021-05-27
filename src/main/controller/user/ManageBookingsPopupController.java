package main.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Singleton;
import main.model.user.CreateBookingModel;
import main.model.user.ManageBookingsModel;

import java.io.IOException;
import java.sql.SQLException;

public class ManageBookingsPopupController {

    private ManageBookingsModel manageBookingsModel = new ManageBookingsModel();
    private Singleton singleton = Singleton.getInstance();
    private boolean submittedAlready = false;

    @FXML
    private Button accept;

    @FXML
    private Button cancel;

    @FXML
    private Text homepageNav;

    @FXML
    private Text header;

    @FXML
    private void removeBooking(MouseEvent event) throws IOException, SQLException {
        if (!submittedAlready) {
            submittedAlready = true;
            this.header.setText("Your booking has been deleted");
            this.cancel.setVisible(false);
            this.accept.setVisible(false);
            this.homepageNav.setVisible(true);
            manageBookingsModel.removeBooking(singleton.getUser(), singleton.getDate());
        }

    }

    @FXML
    private void cancelRemove(MouseEvent event) {
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
