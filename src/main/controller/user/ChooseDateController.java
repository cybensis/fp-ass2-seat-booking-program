package main.controller.user;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;
import main.model.user.ChooseDateModel;

import java.io.IOException;
import java.sql.SQLException;

public class ChooseDateController {
    private Singleton singleton = Singleton.getInstance();
    private ChooseDateModel chooseDateModel = new ChooseDateModel();

    @FXML
    private Label isConnected;

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
        singleton.changeScene("main/ui/user/userHome.fxml");
    }


    @FXML
    void proceed(MouseEvent event) throws IOException, SQLException {
        String response = chooseDateModel.alreadyBooked(dateField.getValue(), singleton.getUser());
        if (response.equals("alreadyBooked"))
            errorMessage.setVisible(true);
        else {
            singleton.setDate(dateField.getValue());
            singleton.changeScene("main/ui/user/createBooking.fxml");
        }

    }

}
