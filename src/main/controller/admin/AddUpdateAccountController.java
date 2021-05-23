package main.controller.admin;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.BCrypt;
import main.Singleton;
import main.model.RegisterModel;

import java.io.IOException;
import java.sql.SQLException;

public class AddUpdateAccountController {
    private Singleton singleton = Singleton.getInstance();
    private RegisterModel registerModel = new RegisterModel();
    private String[] updatingAccountDetails;
    private int accountType;

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
    private Text subHeader;

    @FXML
    private Text errorMessage;

    @FXML
    private void initialize() throws SQLException {
        if (this.singleton.getAccountManagementDetails("accountType").equals("admin")) {
            this.subHeader.setText("Register new admin account");
            this.accountType = 2;
        } else {
            this.subHeader.setText("Register new employee account");
            this.accountType = 1;
        }

        if (this.singleton.getAccountManagementDetails("accountAction").equals("updateAccount")) {
            this.subHeader.setText("Update " + this.singleton.getAccountManagementDetails("accountType") + " account");
            this.updatingAccountDetails = this.registerModel.retrieveUserDetails(this.singleton.getAccountManagementDetails("employeeID"), this.singleton.getAccountManagementDetails("accountType"));
            if (!this.updatingAccountDetails[0].equals("Error")) {
                this.firstName.setText(this.updatingAccountDetails[0]);
                this.surname.setText(this.updatingAccountDetails[1]);
                // Skipping 2 because that holds the password hash, if the password field remains blank, then it will
                // not be changed.
                this.employeeid.setText(this.updatingAccountDetails[3]);
                this.secretQ.setText(this.updatingAccountDetails[4]);
                this.secretQAnswer.setText(this.updatingAccountDetails[5]);
                this.username.setText(this.updatingAccountDetails[6]);
                this.role.setText(this.updatingAccountDetails[7]);
            }
        } else if (this.singleton.getAccountManagementDetails("accountAction").equals("addAccount")) {
            this.subHeader.setText("Add new " + this.singleton.getAccountManagementDetails("accountType") + " account");
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/accountManagement.fxml");
    }

    @FXML
    private void submit(MouseEvent event) throws SQLException {
        String response = "";
        if (this.singleton.getAccountManagementDetails("accountAction").equals("addAccount"))
            response = addNewUser();
        else if (this.singleton.getAccountManagementDetails("accountAction").equals("updateAccount")) {
            response = updateUser();
        }

        if (response.equals("Success")) {
            //Do something that says success, popup maybe
            this.singleton.setAccountManagementDetails("", "employeeID");
            this.singleton.setAccountManagementDetails("", "accountType");
            this.singleton.setAccountManagementDetails("", "accountAction");

            // RESET ALL RELATED SINGLETON VARIABLES
            Node source = (Node) event.getSource();
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(source.getScene().getWindow());
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("main/ui/admin/addUpdateAccountPopup.fxml"));
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
                        singleton.changeScene("main/ui/admin/adminHome.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        else {
            this.errorMessage.setVisible(true);
            this.errorMessage.setText(response);

        }
    }

    private String updateUser() throws SQLException {
        int employeeID;
        String password;
        if (this.password.getText().trim().equals(""))
            password = this.updatingAccountDetails[2];
        else
            password = BCrypt.hashpw(this.password.getText(), BCrypt.gensalt());
        try {
            employeeID = Integer.parseInt(this.employeeid.getText().trim());
            return registerModel.updateUserDetails(Integer.parseInt(this.updatingAccountDetails[3]), this.updatingAccountDetails[6], employeeID, role.getText(), firstName.getText(), surname.getText(), username.getText(), password, secretQ.getText(), secretQAnswer.getText(), accountType);
        }
        // Any SQL related errors will be caught by the RegisterModel, so the only error thrown should be a NumberFormatException
        catch (NumberFormatException e) {
            return "Please check only numbers are used for the employee ID";
            // Set error message to Incorrect EmployeeID format, also check for length of all strings
        }
    }

    private String addNewUser() throws SQLException {
        int employeeID;
        String response;
        // ALSO DO A CHECK HERE, FOR IF entry.trim() is different to entry, THEN SAY ERROR, NO SPACES ALLOWED
        String fieldData[] = new String[]{this.employeeid.getText(), role.getText(), firstName.getText(), surname.getText(), username.getText(), password.getText(), secretQ.getText(), secretQAnswer.getText()};
            for (int i = 0; i < fieldData.length; i++) {
                if (fieldData[i].length() == 0)
                    return "Please make sure you fill in all the fields";
            }
            try {
                employeeID = Integer.parseInt(this.employeeid.getText().trim());
                return registerModel.attemptRegister(employeeID, role.getText(), firstName.getText(), surname.getText(), username.getText(), password.getText(), secretQ.getText(), secretQAnswer.getText(), accountType);
            }
            // Any SQL related errors will be caught by the RegisterModel, so the only error thrown should be a NumberFormatException
            catch (NumberFormatException e) {
                return "Please check only numbers are used for the employee ID";
            }
        }
}
