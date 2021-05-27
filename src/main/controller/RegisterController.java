package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import main.Singleton;
import main.model.RegisterModel;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
        private Singleton singleton = Singleton.getInstance();
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
        private Label errorMessage;


        @FXML
        public void submit(ActionEvent event) throws SQLException {
                int employeeID;

                // ALSO DO A CHECK HERE, FOR IF entry.trim() is different to entry, THEN SAY ERROR, NO SPACES ALLOWED
                String registerFieldData[] = new String[] {this.employeeid.getText(), role.getText(), firstName.getText(), surname.getText(), username.getText(), password.getText(), secretQ.getText(), secretQAnswer.getText()};
                for (int i = 0; i < registerFieldData.length; i++) {
                        if (registerFieldData[i].length() == 0)
                                System.out.println("error");
                                // Just have a general error saying, Please make sure you fill in all your fields loser.
                        if (registerFieldData[i].contains(",")){
                                //return error, the way this data is added into CSV reports, this comma would cause problems.
                        }
                }
                try {
                        employeeID = Integer.parseInt(this.employeeid.getText());
                        System.out.println(registerModel.attemptRegister(employeeID,role.getText(),firstName.getText(),surname.getText(),username.getText(),password.getText(),secretQ.getText(),secretQAnswer.getText(), 1));
                }
                catch (NumberFormatException error) {
                        // Set error message to Incorrect EmployeeID format, also check for length of all strings
                }
        }

        @FXML
        public void goBack(MouseEvent event) throws IOException {
                singleton.changeScene("main/ui/login.fxml");
        }

}
