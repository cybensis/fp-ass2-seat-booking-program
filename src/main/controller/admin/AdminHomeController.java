package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class AdminHomeController {
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
    private Text dotPointThree;

    @FXML
    private Text dotPointFour;

    @FXML
    private void accountManagement(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/accountManagement.fxml");
    }

    @FXML
    private void generateReports(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/generateReports.fxml");
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/login.fxml");

    }

    @FXML
    private void graphicVisualisation(MouseEvent event) throws IOException {
        singleton.setAdminDateType("graphic");
        singleton.changeScene("main/ui/admin/chooseDate.fxml");

    }

    @FXML
    private void bookingManagement(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/bookingManagement.fxml");

    }

}
