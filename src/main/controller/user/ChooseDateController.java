package main.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;
import main.model.user.ManageBookingsModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ChooseDateController {
    private Singleton singleton = Singleton.getInstance();
    private ManageBookingsModel manageBookingsModel = new ManageBookingsModel();


    @FXML
    private Text subHeader;

    @FXML
    private Text dateText;

    @FXML
    private Text newBooking;

    @FXML
    private Text manageBooking;

    @FXML
    private Text errorMessage;

    @FXML
    private DatePicker dateField;

    @FXML
    private ImageView backButton;

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        if (singleton.getUpdateBooking()) {
            singleton.setUpdateBooking(false);
            singleton.changeScene("main/ui/user/manageBookings.fxml");
            singleton.setDate(null);
            singleton.setBookingDate(null);
        } else
            singleton.changeScene("main/ui/user/userHome.fxml");

    }

    @FXML
    private void initialize() {
        if (singleton.getUpdateBooking()) {
            subHeader.setText("Update booking");
            dateText.setText("Choose a new date for you booking");
        }
    }

    @FXML
    private void proceed(MouseEvent event) throws IOException, SQLException {

        if (!singleton.getUpdateBooking() && ChronoUnit.DAYS.between(LocalDate.now(), dateField.getValue()) < 2) {
            errorMessage.setText("Error: Your booking must be at least 2 days from now");
            errorMessage.setVisible(true);
            return;
        }
        String response = manageBookingsModel.alreadyBooked(dateField.getValue());
        if (response.equals("alreadyBooked") && !singleton.getUpdateBooking()) {
            errorMessage.setText("Error: You already have a booking on this day");
            errorMessage.setVisible(true);
            return;
        }
        if (singleton.getUpdateBooking()) {
            if (ChronoUnit.DAYS.between(LocalDate.now(), dateField.getValue()) < 2) {
                errorMessage.setText("Error: New date must be at least 2 days from today");
                errorMessage.setVisible(true);
            }
            else {
                singleton.setDate(dateField.getValue());
                singleton.changeScene("main/ui/user/createBooking.fxml");
            }
        } else {
            singleton.setDate(dateField.getValue());
            singleton.changeScene("main/ui/user/createBooking.fxml");
        }

    }
}
