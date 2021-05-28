package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;
import main.model.admin.SeatingStatusModel;

import java.io.IOException;
import java.sql.SQLException;

public class SeatingStatusController {
    private Singleton singleton = Singleton.getInstance();
    private SeatingStatusModel seatingStatusModel = new SeatingStatusModel();

    @FXML
    private Text subHeader;

    @FXML
    private ImageView backButton;

    @FXML
    private Button proceed;

    @FXML
    private ComboBox<String> dropdownBox;

    @FXML
    private Text responseMessage;


    @FXML
    private void initialize() throws SQLException {
        dropdownBox.getItems().addAll("COVID Conditions", "Lockdown", "Normal");
        String currentStatus = seatingStatusModel.getSeatingStatus();
        subHeader.setText("The current seating status for " + String.valueOf(singleton.getDate()) + ": " + currentStatus);
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/bookingManagement.fxml");
    }

    @FXML
    void proceed(MouseEvent event) throws SQLException {
        String response = "";
        // This prevents an admin from changing the seating status if the date has bookings, unless they are changing it
        // to normal, since no seats are blocked in normal mode.
        if (seatingStatusModel.checkForBookings() && !dropdownBox.getValue().equals("Normal")) {
            responseMessage.setVisible(true);
            responseMessage.setText("This date already has bookings, please delete them before changing the status");
            return;
        }
        else if (dropdownBox.getValue().equals("COVID Conditions") || dropdownBox.getValue().equals("Lockdown") || dropdownBox.getValue().equals("Normal"))
            response = seatingStatusModel.setSeatingStatus(dropdownBox.getValue());

        responseMessage.setVisible(true);
        responseMessage.setText(response);

    }

}
