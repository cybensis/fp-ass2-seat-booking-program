package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.Singleton;
import main.model.ResetPasswordModel;

import java.io.IOException;
import java.sql.SQLException;

public class ResetPasswordController {
    private ResetPasswordModel resetPasswordModel = new ResetPasswordModel();
    private Singleton singleton = Singleton.getInstance();
    //Since this single resetPassword page holds 3 different scenes in one, enter username, answer question, enter new
    //password, I use this variable to determine which of these states it's at.
    private String currentScene = "enterUsername";
    private String secretQuestionDetails[];
    private String username;

    @FXML
    private TextField textField;

    @FXML
    private Text resetPasswordHeader;

    @FXML
    private Button submit;

    @FXML
    private Text textAboveField;

    @FXML
    private Label isConnected;

    @FXML
    private Text errorText;

    @FXML
    private Text loginRedirectText;

    @FXML
    private Text secretQuestionHeader;

    @FXML
    private Text secretQuestion;

    @FXML
    private Button cancel;

    @FXML
    private ImageView tickIcon;

    @FXML
    private Line lineTop;

    @FXML
    private Line lineBottom;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void submit(MouseEvent event) throws SQLException {
        if (currentScene.equals("enterUsername")) {
            retrieveUserSQ();
        }
        else if (currentScene.equals("secretQuestion")) {
            checkSQAnswer();
        }
        else if (currentScene.equals("newPassword")) {
            changePassword();
        }

    }

    private void changePassword() throws SQLException {
        if (passwordField.getText().trim().isEmpty() || passwordField.getText().trim().length() < 5) {
            errorText.setText("Please make sure your password is greater than 5 characters");
            errorText.setVisible(true);
            return;
        }
        String message = resetPasswordModel.changePassword(passwordField.getText());
        if (!message.equals("Success")) {
            errorText.setText("An error occurred, please try again");
            errorText.setVisible(true);
        }
        else {
            errorText.setVisible(false);
            textField.setVisible(false);
            textAboveField.setVisible(false);
            submit.setVisible(false);
            cancel.setVisible(false);
            lineTop.setVisible(false);
            lineBottom.setVisible(false);
            passwordField.setVisible(false);
            resetPasswordHeader.setText("Your password has successfully been reset");
            resetPasswordHeader.setTextAlignment(TextAlignment.CENTER);
            tickIcon.setVisible(true);
            loginRedirectText.setVisible(true);
        }

    }

    private void checkSQAnswer() {
        String answer;
        if (textField.getText().trim().isEmpty()) {
            errorText.setText("Please answer your secret question");
            errorText.setVisible(true);
            return;
        }
        answer = textField.getText();
        if (secretQuestionDetails[1].toLowerCase().equals(answer.toLowerCase())) {
            secretQuestion.setVisible(false);
            errorText.setVisible(false);
            textAboveField.setVisible(true);
            textAboveField.setText("Please enter your new password");
            textField.setVisible(false);
            passwordField.setVisible(true);
            secretQuestionHeader.setVisible(false);
            currentScene = "newPassword";
        }
        else {
            errorText.setVisible(true);
            errorText.setText("What you wrote does not match your answer");
        }
    }

    private void retrieveUserSQ() throws SQLException {
        String username;
        username = textField.getText().trim();
        if (textField.getText().trim().isEmpty()) {
            errorText.setText("Please enter your username");
            errorText.setVisible(true);
            return;
        }
        secretQuestionDetails = resetPasswordModel.getSecretQuestion(username);
        if (secretQuestionDetails[0].equals("noAccount")) {
            errorText.setText("No account was found with this username");
            errorText.setVisible(true);
        }
        else if (secretQuestionDetails[0].equals("error")) {
            errorText.setText("An error occurred, please try again");
            errorText.setVisible(true);
        }
        else {
            errorText.setVisible(false);
            textAboveField.setVisible(false);
            secretQuestion.setVisible(true);
            secretQuestion.setText(secretQuestionDetails[0]);
            secretQuestionHeader.setVisible(true);
            cancel.setVisible(true);
            textField.setText("");
            currentScene = "secretQuestion";
            this.username = username;
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/login.fxml");
    }

}
