package main.controller.admin;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
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
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/accountManagement.fxml");
    }

    @FXML
    private void manageAdmin(MouseEvent event) {
        this.subHeader.setText("Account Management - Admin");
        updateDotPoints();


    }

    @FXML
    private void manageEmployees(MouseEvent event) {
        this.subHeader.setText("Account Management - Employees");
        updateDotPoints();
    }

    @FXML
    private void modifyBlacklist(MouseEvent event) {

    }

    private void updateDotPoints() {
        this.dotPointOne.setText("Add new account");
        this.dotPointOne.setOnMouseClicked(event -> {addNewAccount();});

        this.dotPointTwo.setText("Update account details");
        this.dotPointTwo.setOnMouseClicked(event -> {updateAcccount();});

        this.dotPointThree.setText("Delete account");
        this.dotPointThree.setOnMouseClicked(event -> {deleteAccount();});



    }

    private void addNewAccount() {

    }

    private void updateAcccount() {

    }

    private void deleteAccount() {

    }

}
