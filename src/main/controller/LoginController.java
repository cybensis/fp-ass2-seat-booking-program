package main.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import main.Singleton;
import main.model.LoginModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private LoginModel loginModel = new LoginModel();
    private Singleton singleton = Singleton.getInstance();

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

    public void changeSceneRegister(MouseEvent event) throws Exception {
        singleton.changeScene("main/ui/register.fxml");
    }

    public void login(MouseEvent event){

        try {
            //atemptLogin is set to either false, meaning the login failed, or is set to the users account type
            String attemptLogin = loginModel.isLogin(txtUsername.getText().trim(),txtPassword.getText().trim());
            if (!attemptLogin.equals("false")){
                if (attemptLogin.equals("employee"))
                    singleton.changeScene("main/ui/user/userHome.fxml");
                else if (attemptLogin.equals("admin"))
                    singleton.changeScene("main/ui/admin/adminHome.fxml");

            }else{
                isConnected.setText("username and password is incorrect");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetPassword(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/resetPassword.fxml");
    }




    //11.2.3 big sur



}
