package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class AddUpdateAccountPopupController {
    Singleton singleton = Singleton.getInstance();

    @FXML
    private Text mainHeader;

    @FXML
    private Text homepageNav;

    @FXML
    private void initialize() {
        if (this.singleton.getAccountManagementDetails("updateOrAdd").equals("updateAccount"))
            this.mainHeader.setText("This account has successfully been updated");
        else if (this.singleton.getAccountManagementDetails("updateOrAdd").equals("addAccount"))
            this.mainHeader.setText("This account has successfully been added");
    }
    @FXML
    private void goHome(MouseEvent event) throws IOException {
        this.singleton.changeScene("main/ui/admin/adminHome.fxml");
    }

}
