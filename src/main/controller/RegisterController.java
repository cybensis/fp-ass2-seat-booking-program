package main.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.Singleton;
import main.model.RegisterModel;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
        private Singleton singleton = Singleton.getInstance();
        private RegisterModel registerModel = new RegisterModel();

        @FXML
        private TextField employeeid;

        @FXML
        private ImageView backButton;

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
        private Label responseMessage;


        @FXML
        public void submit(ActionEvent event) throws SQLException {
                if (errorCheck() != null) {
                        Employee newEmployee = new Employee(Integer.parseInt(employeeid.getText()), role.getText(), firstName.getText(), surname.getText(), username.getText(), password.getText(), secretQ.getText(), secretQAnswer.getText(), 1);
                        String response = registerModel.attemptRegister(newEmployee);
                        if (response.equals("Success"))
                                createPopup(event);
                        else
                                responseMessage.setText(response);
                }

        }

        public void createPopup(ActionEvent event) {
                Node source = (Node) event.getSource();
                Stage popup = new Stage();
                popup.initModality(Modality.APPLICATION_MODAL);
                popup.initOwner(source.getScene().getWindow());
                Parent root = null;
                try {
                        root = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/registerPopup.fxml"));
                } catch (IOException e) {
                        e.printStackTrace();
                }
                Scene popupScene = new Scene(root, 600, 300);
                popup.setScene(popupScene);
                popup.show();
                popup.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                                Singleton singleton = Singleton.getInstance();
                                try {
                                        singleton.changeScene("main/ui/login.fxml");
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                        }
                });
        }

        public String errorCheck() {
                int employeeID;
                try {
                        employeeID = Integer.parseInt(employeeid.getText());
                }
                catch (NumberFormatException error) {
                        responseMessage.setText("Please make sure your employee ID is only numbers");
                        return null;
                }
                String registerFieldData[] = new String[] {employeeid.getText(), role.getText(), firstName.getText(), surname.getText(), username.getText(), password.getText(), secretQ.getText(), secretQAnswer.getText()};
                for (int i = 0; i < registerFieldData.length; i++) {
                        if (registerFieldData[i].length() == 0) {
                                responseMessage.setText("Please make sure all fields are filled in");
                                return null;
                        }
                        if (registerFieldData[i].contains(",")) {
                                responseMessage.setText("Please check that your entries do not contain any special characters like ','");
                                return null;
                        }
                }
                if (!password.getText().trim().equals(password.getText()) || !username.getText().trim().equals(username.getText())) {
                        responseMessage.setText("Please make sure your password or username have no spaces");
                        return null;
                }
                if (password.getText().trim().length() < 5) {
                        responseMessage.setText("Please make sure your password is greater than 5 characters");
                        return null;
                }
                return "noErrors";
        }


        @FXML
        public void goBack(MouseEvent event) throws IOException {
                singleton.changeScene("main/ui/login.fxml");
        }

        @FXML
        public void goHome(MouseEvent event) throws IOException {
                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
                singleton.changeScene("main/ui/login.fxml");
        }

}
