package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.Singleton;
import main.model.admin.DeactivateUserModel;

import java.io.IOException;
import java.sql.SQLException;

public class DeactivateUserController {
    private Singleton singleton = Singleton.getInstance();
    private DeactivateUserModel deactivateUserModel = new DeactivateUserModel();
    private String accountDetails[];

    @FXML
    private Button deleteButton;

    @FXML
    private Text idText;

    @FXML
    private Text subHeader;

    @FXML
    private ImageView backButton;

    @FXML
    private Text nameText;

    @FXML
    private Text usernameText;

    @FXML
    private ImageView tickImage;

    @FXML
    private Text homeNavText;

    @FXML
    private Text headerText;

    @FXML
    private Text stateText;

    @FXML
    private Line headerLine;

    @FXML
    private void initialize() throws SQLException {
        accountDetails = deactivateUserModel.returnUserDetails(singleton.getAccountManagementDetails("employeeID"));
        if (accountDetails[0].equals("error")) {
            nameText.setVisible(false);
            usernameText.setVisible(false);
            deleteButton.setVisible(false);
            idText.setText("An unexpected error has occurred");
            return;
        } else {
            nameText.setText("Name: " + accountDetails[0]);
            usernameText.setText("Username: " + accountDetails[1]);
            idText.setText("Employee ID: " + singleton.getAccountManagementDetails("employeeID"));
        }

        if (singleton.getAccountManagementDetails("accountAction").equals("deleteAccount"))
            subHeader.setText("Delete " + singleton.getAccountManagementDetails("accountType") + " account");
        else if (singleton.getAccountManagementDetails("accountAction").equals("deactivateAccount")) {
            subHeader.setText("Deactivate " + singleton.getAccountManagementDetails("accountType") + " account");
            //Index 2 = the accounts current state
            stateText.setVisible(true);
            stateText.setText("Current state: " + accountDetails[2]);
            if (accountDetails[2].equals("active")) {
                subHeader.setText("Deactivate " + singleton.getAccountManagementDetails("accountType") + " account");
                deleteButton.setText("Deactivate");
            } else {
                subHeader.setText("Activate " + singleton.getAccountManagementDetails("accountType") + " account");
                deleteButton.setText("Activate");
            }
        }

    }

    @FXML
    private void modifyAccount(MouseEvent event) throws SQLException {
        String response = "";
        if (singleton.getAccountManagementDetails("accountAction").equals("deleteAccount")) {
            response = deactivateUserModel.deleteUser(singleton.getAccountManagementDetails("employeeID"));
            headerText.setText("Account deleted");
        }
        else if (singleton.getAccountManagementDetails("accountAction").equals("deactivateAccount")) {
            response = deactivateUserModel.changeAccountState(accountDetails[2]);
            headerText.setText("Account state updated");
        }
        if (response.equals("success")) {
            nameText.setVisible(false);
            subHeader.setVisible(false);
            usernameText.setVisible(false);
            deleteButton.setVisible(false);
            idText.setVisible(false);
            headerLine.setVisible(false);
            headerText.setTextAlignment(TextAlignment.CENTER);
            tickImage.setVisible(true);
            homeNavText.setVisible(true);
            stateText.setVisible(false);
        } else {
            headerText.setText("Admin panel");
            nameText.setVisible(false);
            usernameText.setVisible(false);
            deleteButton.setVisible(false);
            idText.setText("An unexpected error has occurred");
        }

    }


    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/AccountManagement.fxml");
    }

    @FXML
    private void goHome(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/adminHome.fxml");
    }

}
