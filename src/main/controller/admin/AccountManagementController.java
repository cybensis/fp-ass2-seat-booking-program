package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;

import java.io.IOException;

public class AccountManagementController {
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
    private Text dotThree;

    @FXML
    private Text dotFour;

    @FXML
    private Text dotPointFour;


    @FXML
    private void initialize() {
        if (!singleton.getAccountManagementDetails("accountType").equals(""))
            updateDotPoints(singleton.getAccountManagementDetails("accountType"));

    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        if (!singleton.getAccountManagementDetails("accountType").equals("")) {
            singleton.setAccountManagementDetails("", "accountType");
            singleton.changeScene("main/ui/admin/accountManagement.fxml");
        }
        else
            singleton.changeScene("main/ui/admin/adminHome.fxml");

    }

    @FXML
    private void manageAdmin(MouseEvent event) {
        singleton.setAccountManagementDetails("admin", "accountType");
        updateDotPoints("admin");


    }

    @FXML
    private void manageEmployees(MouseEvent event) {
        singleton.setAccountManagementDetails("employee", "accountType");
        updateDotPoints("employee");
    }

    private void updateDotPoints(String accountType) {
        dotPointThree.setVisible(true);
        dotThree.setVisible(true);
        if (accountType.equals("admin"))
            subHeader.setText("Account Management - Admin");
        else if (accountType.equals("employee")) {
            subHeader.setText("Account Management - Employees");
            dotPointFour.setVisible(true);
            dotPointFour.setVisible(true);
            dotPointFour.setOnMouseClicked(event -> {
                singleton.setAccountManagementDetails("deactivateAccount", "accountAction");
                try {
                    singleton.changeScene("main/ui/admin/selectUser.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        dotPointOne.setText("Add new account");
        dotPointOne.setOnMouseClicked(event -> {
            singleton.setAccountManagementDetails("addAccount", "accountAction");
            try {
                singleton.changeScene("main/ui/admin/addUpdateAccount.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dotPointTwo.setText("Update account details");
        dotPointTwo.setOnMouseClicked(event -> {
            singleton.setAccountManagementDetails("updateAccount", "accountAction");
            try {
                singleton.changeScene("main/ui/admin/selectUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        dotPointThree.setText("Delete account");
        dotPointThree.setOnMouseClicked(event -> {
            singleton.setAccountManagementDetails("deleteAccount", "accountAction");
            try {
                singleton.changeScene("main/ui/admin/selectUser.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
