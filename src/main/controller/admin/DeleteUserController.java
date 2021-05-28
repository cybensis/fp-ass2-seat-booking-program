package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.Singleton;
import main.model.admin.DeleteUserModel;
import sun.nio.cs.SingleByte;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteUserController {
    private Singleton singleton = Singleton.getInstance();
    private DeleteUserModel deleteUserModel = new DeleteUserModel();

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
    private Line headerLine;

    @FXML
    private void initialize() throws SQLException {
            String accountDetails[];
            subHeader.setText("Delete "+singleton.getAccountManagementDetails("accountType")+" account");
            accountDetails = deleteUserModel.returnUserDetails(singleton.getAccountManagementDetails("employeeID"));
            if (accountDetails[0].equals("error")) {
                nameText.setVisible(false);
                usernameText.setVisible(false);
                deleteButton.setVisible(false);
                idText.setText("An unexpected error has occurred");
            }
            else {
                nameText.setText("Name: " + accountDetails[0]);
                usernameText.setText("Username: " + accountDetails[1]);
                idText.setText("Employee ID: " + singleton.getAccountManagementDetails("employeeID"));
            }

    }

    @FXML
    private void deleteUser(MouseEvent event) throws SQLException {
        String response = deleteUserModel.deleteUser(singleton.getAccountManagementDetails("employeeID"));
        if (response.equals("success")) {
            nameText.setVisible(false);
            subHeader.setVisible(false);
            usernameText.setVisible(false);
            deleteButton.setVisible(false);
            idText.setVisible(false);
            headerLine.setVisible(false);
            headerText.setText("Account deleted");
            headerText.setTextAlignment(TextAlignment.CENTER);
            tickImage.setVisible(true);
            homeNavText.setVisible(true);
        }
        else {
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
