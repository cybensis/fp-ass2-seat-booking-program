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
            this.subHeader.setText("Delete "+this.singleton.getAccountManagementDetails("accountType")+" account");
            accountDetails = this.deleteUserModel.returnUserDetails(this.singleton.getAccountManagementDetails("employeeID"));
            if (accountDetails[0].equals("error")) {
                this.nameText.setVisible(false);
                this.usernameText.setVisible(false);
                this.deleteButton.setVisible(false);
                this.idText.setText("An unexpected error has occurred");
            }
            else {
                this.nameText.setText("Name: " + accountDetails[0]);
                this.usernameText.setText("Username: " + accountDetails[1]);
                this.idText.setText("Employee ID: " + this.singleton.getAccountManagementDetails("employeeID"));
            }

    }

    @FXML
    void deleteUser(MouseEvent event) throws SQLException {
        String response = this.deleteUserModel.deleteUser(this.singleton.getAccountManagementDetails("employeeID"));
        System.out.println(response);
        if (response.equals("success")) {
            this.nameText.setVisible(false);
            this.subHeader.setVisible(false);
            this.usernameText.setVisible(false);
            this.deleteButton.setVisible(false);
            this.idText.setVisible(false);
            this.headerLine.setVisible(false);
            this.headerText.setText("Account deleted");
            this.headerText.setTextAlignment(TextAlignment.CENTER);
            this.tickImage.setVisible(true);
            this.homeNavText.setVisible(true);

        }
    }

    @FXML
    void goBack(MouseEvent event) throws IOException {
        this.singleton.changeScene("main/ui/admin/AccountManagement.fxml");
    }

    @FXML
    private void goHome(MouseEvent event) throws IOException {
        this.singleton.changeScene("main/ui/admin/adminHome.fxml");
    }

}
