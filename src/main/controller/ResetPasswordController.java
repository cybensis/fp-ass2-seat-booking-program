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
    ResetPasswordModel resetPasswordModel = new ResetPasswordModel();
    Singleton singleton = Singleton.getInstance();
    //Since this single resetPassword page holds 3 different scenes in one, enter username, answer question, enter new
    //password, I use this variable to determine which of these states it's at.
    String currentScene = "enterUsername";
    String secretQuestionDetails[];
    String username;

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
        //if something isnt set, then hes inputting username, after username success, then submit again, and it will
        // check for
        if (this.currentScene.equals("enterUsername")) {

            retrieveUserSQ();
        }
        else if (this.currentScene.equals("secretQuestion")) {
            checkSQAnswer();
        }
        else if (this.currentScene.equals("newPassword")) {
            changePassword();
        }

    }

    private void changePassword() throws SQLException {
        if (this.textField.getText().trim().isEmpty()) {
            this.errorText.setText("Please enter a new password");
            this.errorText.setVisible(true);
            return;
        }
        String message = resetPasswordModel.changePassword(this.username, this.passwordField.getText());
        if (message.equals("error")) {
            this.errorText.setText("An error has occurred");
            this.errorText.setVisible(true);
        }
        else {
            this.errorText.setVisible(false);
            this.textField.setVisible(false);
            this.textAboveField.setVisible(false);
            this.submit.setVisible(false);
            this.cancel.setVisible(false);
            this.lineTop.setVisible(false);
            this.lineBottom.setVisible(false);
            this.passwordField.setVisible(false);
            this.resetPasswordHeader.setText("Your password has successfully been reset");
            this.resetPasswordHeader.setTextAlignment(TextAlignment.CENTER);
            this.tickIcon.setVisible(true);
            this.loginRedirectText.setVisible(true);
        }

    }

    private void checkSQAnswer() {
        String answer;
        if (this.textField.getText().trim().isEmpty()) {
            this.errorText.setText("Please answer your secret question");
            this.errorText.setVisible(true);
            return;
        }
        answer = this.textField.getText();
        if (this.secretQuestionDetails[1].toLowerCase().equals(answer.toLowerCase())) {
            this.secretQuestion.setVisible(false);
            this.errorText.setVisible(false);
            this.textAboveField.setVisible(true);
            this.textAboveField.setText("Please enter your new password");
            this.textField.setVisible(false);
            this.passwordField.setVisible(true);
            this.secretQuestionHeader.setVisible(false);
            this.currentScene = "newPassword";
        }
        else {
            this.errorText.setVisible(false);
            this.errorText.setText("What you wrote does not match your answer");
        }
    }

    private void retrieveUserSQ() throws SQLException {
        String username;
        username = textField.getText().trim();
        if (this.textField.getText().trim().isEmpty()) {
            this.errorText.setText("Please enter your username");
            this.errorText.setVisible(true);
            return;
        }
        this.secretQuestionDetails = resetPasswordModel.getSecretQuestion(username);
        if (this.secretQuestionDetails[0].equals("noAccount")) {
            this.errorText.setText("No account was found with this username");
            this.errorText.setVisible(true);
        }
        else if (this.secretQuestionDetails[0].equals("error")) {
            this.errorText.setText("An error occurred, please try again");
            this.errorText.setVisible(true);
        }
        else {
            this.errorText.setVisible(false);
            this.textAboveField.setVisible(false);
            this.secretQuestion.setVisible(true);
            this.secretQuestion.setText(this.secretQuestionDetails[0]);
            this.secretQuestionHeader.setVisible(true);
            this.cancel.setVisible(true);
            this.textField.setText("");
            this.currentScene = "secretQuestion";
            this.username = username;
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/login.fxml");
    }

}
