package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class GenerateReportsController {
    private Singleton singleton = Singleton.getInstance();

    @FXML
    private Text dotPointOne;

    @FXML
    private Text dotPointTwo;

    @FXML
    private ImageView backButton;

    @FXML
    private Text subHeader;

    @FXML
    private void generateBooking(MouseEvent event) throws IOException {
        singleton.setAdminDateType("generateReports");
        singleton.changeScene("main/ui/admin/chooseDate.fxml");
    }

    @FXML
    private void generateEmployee(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/downloadReport.fxml");
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/adminHome.fxml");

    }

}
