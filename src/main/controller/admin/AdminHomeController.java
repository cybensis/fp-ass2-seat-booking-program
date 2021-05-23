package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class AdminHomeController {
        Singleton singleton = Singleton.getInstance();

        @FXML
        private Label isConnected;

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
            singleton.changeScene("main/ui/admin/accountManagement.fxml");
        }

        @FXML
        private void goBack(MouseEvent event) throws IOException {
            singleton.changeScene("main/ui/login.fxml");

        }

        @FXML
        private void graphicVisualisation(MouseEvent event) throws IOException {
            singleton.changeScene("main/ui/admin/accountManagement.fxml");

        }

        @FXML
        private void bookingManagement(MouseEvent event) throws IOException {
            singleton.changeScene("main/ui/admin/bookingManagement.fxml");

        }

}
