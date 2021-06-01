package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Singleton;

import java.io.IOException;

public class AddUpdateAccountPopupController {
    private Singleton singleton = Singleton.getInstance();

    @FXML
    private Text mainHeader;

    @FXML
    private Text homepageNav;

    @FXML
    private void initialize() {
        if (singleton.getAccountManagementDetails("accountAction").equals("updateAccount"))
            mainHeader.setText("This account has successfully been updated");
        else if (singleton.getAccountManagementDetails("accountAction").equals("addAccount"))
            mainHeader.setText("This account has successfully been added");
    }

    @FXML
    private void goHome(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/adminHome.fxml");
        Stage stage = (Stage) homepageNav.getScene().getWindow();
        stage.close();
    }

}
