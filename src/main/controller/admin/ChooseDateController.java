package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;
import main.model.admin.ChooseDateModel;
import main.model.user.ManageBookingsModel;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ChooseDateController {
    private Singleton singleton = Singleton.getInstance();
    private ChooseDateModel chooseDateModel = new ChooseDateModel();


    @FXML
    private Text subHeader;

    @FXML
    private ImageView backButton;

    @FXML
    private DatePicker dateField;

    @FXML
    private Text dateText;

    @FXML
    private Button proceed;

    @FXML
    private Text errorMessage;


    @FXML
    private void goBack(MouseEvent event) throws IOException {
        if (singleton.getAdminDateType().equals("seatingStatus"))
            singleton.changeScene("main/ui/admin/bookingManagement.fxml");
        else if (singleton.getAdminDateType().equals("graphic"))
            singleton.changeScene("main/ui/admin/adminHome.fxml");
        else if (singleton.getAdminDateType().equals("generateReports"))
            singleton.changeScene("main/ui/admin/generateReports.fxml");
        singleton.setAdminDateType("");
    }

    @FXML
    private void initialize() {
        if (singleton.getAdminDateType().equals("graphic")) {
            subHeader.setText("Graphic visualisation");
            dateText.setText("What day would you like to look at?");
        }
        else if (singleton.getAdminDateType().equals("generateReports")) {
            subHeader.setText("Generate reports");
            dateText.setText("What day would you like to generate reports for?");
        }
    }

    @FXML
    private void proceed(MouseEvent event) throws IOException, SQLException {
        if (singleton.getAdminDateType().equals("seatingStatus")) {
            if (ChronoUnit.DAYS.between(LocalDate.now(), dateField.getValue()) < 0) {
                errorMessage.setVisible(true);
                return;
            }
            singleton.setAdminDateType("");
            singleton.setDate(dateField.getValue());
            singleton.changeScene("main/ui/admin/seatingStatus.fxml");
        }
        else if (singleton.getAdminDateType().equals("graphic")){
            singleton.setDate(dateField.getValue());
            singleton.changeScene("main/ui/admin/graphicVisualisation.fxml");
        }
        else if (singleton.getAdminDateType().equals("generateReports")) {
            singleton.setDate(dateField.getValue());
            singleton.changeScene("main/ui/admin/downloadReport.fxml");
        }

    }
}
