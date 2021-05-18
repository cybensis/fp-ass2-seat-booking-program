package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class AccountManagementController {
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
    private void initialize() {
        if (!this.singleton.getAccountManagementDetails("accountType").equals(""))
            updateDotPoints(this.singleton.getAccountManagementDetails("accountType"));

    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        if (!this.singleton.getAccountManagementDetails("accountType").equals("")) {
            this.singleton.setAccountManagementDetails("", "accountType");
            this.singleton.changeScene("main/ui/admin/accountManagement.fxml");
        }
        else
            this.singleton.changeScene("main/ui/admin/adminHome.fxml");

    }

    @FXML
    private void manageAdmin(MouseEvent event) {
        this.singleton.setAccountManagementDetails("admin", "accountType");
        updateDotPoints("admin");


    }

    @FXML
    private void manageEmployees(MouseEvent event) {
        this.singleton.setAccountManagementDetails("employee", "accountType");
        updateDotPoints("employee");
    }

    @FXML
    private void modifyBlacklist(MouseEvent event) {

    }

    private void updateDotPoints(String accountType) {
        if (accountType.equals("admin"))
            this.subHeader.setText("Account Management - Admin");
        else if (accountType.equals("employee"))
            this.subHeader.setText("Account Management - Employees");

        this.dotPointOne.setText("Add new account");
        this.dotPointOne.setOnMouseClicked(event -> {
            this.singleton.setAccountManagementDetails("addAccount", "updateOrAdd");
            try {
                this.singleton.changeScene("main/ui/admin/addUpdateAccount.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.dotPointTwo.setText("Update account details");
        this.dotPointTwo.setOnMouseClicked(event -> {
            this.singleton.setAccountManagementDetails("updateAccount", "updateOrAdd");
            try {
                this.singleton.changeScene("main/ui/admin/selectUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.dotPointThree.setText("Delete account");
        this.dotPointThree.setOnMouseClicked(event -> {
            this.singleton.setAccountManagementDetails("deleteAccount", "updateOrAdd");
            try {
                this.singleton.changeScene("main/ui/admin/selectUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



    }



    private void deleteAccount() {

    }
}
