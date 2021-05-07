package main.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.model.LoginModel;
import main.BCrypt;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public LoginModel loginModel = new LoginModel();
    @FXML
    private Label isConnected;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private Text registerAccount;



    // Check database connection
    @Override
    public void initialize(URL location, ResourceBundle resources){
        if (loginModel.isDbConnected()){
            isConnected.setText("Connected");
        }else{
            isConnected.setText("Not Connected");
        }

    }
    /* login Action method
       check if user input is the same as database.
     */

    public void changeSceneRegister() throws Exception {
        Parent registerView = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/register.fxml"));
        Stage window = (Stage) registerAccount.getScene().getWindow();
        window.setScene(new Scene(registerView));
        window.show();
    }

    public void Login(ActionEvent event){

        try {
            if (loginModel.isLogin(txtUsername.getText(),txtPassword.getText())){

            }else{
                isConnected.setText("username and password is incorrect");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }






    //11.2.3 big sur



}
