package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.model.LoginModel;
import main.model.RegisterModel;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
        public RegisterModel registerModel = new RegisterModel();

        @FXML
        private TextField employeeid;

        @FXML
        private ImageView backButton;

        @FXML
        private Label isConnected;

        @FXML
        private TextField role;

        @FXML
        private TextField firstName;

        @FXML
        private TextField surname;

        @FXML
        private TextField username;

        @FXML
        private PasswordField password;

        @FXML
        private TextField secretQ;

        @FXML
        private TextField secretQAnswer;

        @FXML
        private Button submitButton;

        @FXML
        public void submit(ActionEvent event) throws SQLException {
                System.out.println(registerModel.attempRegister(Integer.parseInt(employeeid.getText()),role.getText(),firstName.getText(),surname.getText(),username.getText(),password.getText(),secretQ.getText(),secretQAnswer.getText()));
        }

        @FXML
        public void goBack(MouseEvent event) throws IOException {
                Parent loginView = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/login.fxml"));
                Stage window = (Stage) backButton.getScene().getWindow();
                window.setScene(new Scene(loginView));
                window.show();
        }

}
