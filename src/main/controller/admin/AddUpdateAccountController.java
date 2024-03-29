package main.controller.admin;

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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.BCrypt;
import main.controller.Employee;
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
    private Label responseMessage;

    @FXML
    private void initialize() throws SQLException {
        if (singleton.getAccountManagementDetails("accountType").equals("admin")) {
            subHeader.setText("Register new admin account");
            accountType = 2;
        } else {
            subHeader.setText("Register new employee account");
            accountType = 1;
        }

        if (singleton.getAccountManagementDetails("accountAction").equals("updateAccount")) {
            subHeader.setText("Update " + singleton.getAccountManagementDetails("accountType") + " account");
            updatingAccountDetails = registerModel.retrieveUserDetails(singleton.getAccountManagementDetails("employeeID"), singleton.getAccountManagementDetails("accountType"));
            if (!updatingAccountDetails[0].equals("Error")) {
                // If no error occurred, then the register page fields are filled in with the users details.
                firstName.setText(updatingAccountDetails[0]);
                surname.setText(updatingAccountDetails[1]);
                // Skipping 2 because that holds the password hash, if the password field remains blank, then it will
                // not be changed.
                employeeid.setText(updatingAccountDetails[3]);
                secretQ.setText(updatingAccountDetails[4]);
                secretQAnswer.setText(updatingAccountDetails[5]);
                username.setText(updatingAccountDetails[6]);
                role.setText(updatingAccountDetails[7]);
            } else {
                responseMessage.setText("An unexpected error has occurred");
            }
        } else if (singleton.getAccountManagementDetails("accountAction").equals("addAccount")) {
            subHeader.setText("Add new " + singleton.getAccountManagementDetails("accountType") + " account");
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/accountManagement.fxml");
    }

    @FXML
    private void submit(MouseEvent event) throws SQLException {
        String response = "";
        if (singleton.getAccountManagementDetails("accountAction").equals("addAccount"))
            response = addNewUser();
        else if (singleton.getAccountManagementDetails("accountAction").equals("updateAccount")) {
            response = updateUser();
        }

        if (response.equals("Success")) {
            createPopup(event);
            singleton.setAccountManagementDetails("", "employeeID");
            singleton.setAccountManagementDetails("", "accountType");
            singleton.setAccountManagementDetails("", "accountAction");

        } else {
            responseMessage.setVisible(true);
            responseMessage.setText(response);
        }
    }


    private String updateUser() throws SQLException {
        String passwordHash;
        // The system needs to check if the ID or username was changed to provide more in depth error checking, like
        // preventing duplicate IDs or usernames.
        int originalID = Integer.parseInt(updatingAccountDetails[3]);
        String originalUsername = updatingAccountDetails[6];
        String fieldData[] = new String[]{employeeid.getText(), role.getText(), firstName.getText(), surname.getText(), username.getText(), secretQ.getText(), secretQAnswer.getText()};
        String errorResponse = errorCheck(fieldData);
        if (errorResponse.equals("noErrors")) {
            if (password.getText().trim().equals(""))
                passwordHash = updatingAccountDetails[2];
            else
                passwordHash = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
            Employee newEmployee = new Employee(Integer.parseInt(employeeid.getText()), role.getText(), firstName.getText(), surname.getText(), username.getText(), passwordHash, secretQ.getText(), secretQAnswer.getText(), accountType);
            return registerModel.updateUserDetails(newEmployee, originalID, originalUsername);
        } else {
            return errorResponse;
        }
    }


    private String addNewUser() throws SQLException {
        String response;
        String fieldData[] = new String[]{employeeid.getText(), role.getText(), firstName.getText(), surname.getText(), username.getText(), password.getText(), secretQ.getText(), secretQAnswer.getText()};
        String errorResponse = errorCheck(fieldData);
        if (errorResponse.equals("noErrors")) {
            Employee newEmployee = new Employee(Integer.parseInt(employeeid.getText()), role.getText(), firstName.getText(), surname.getText(), username.getText(), password.getText(), secretQ.getText(), secretQAnswer.getText(), accountType);
            return registerModel.attemptRegister(newEmployee);
        } else
            return errorResponse;

    }


    // This method runs multiple different checks on data to prevent any errors occurring, and returns the appropriate
    // error message.
    public String errorCheck(String fieldData[]) {
        int employeeID;
        try {
            employeeID = Integer.parseInt(employeeid.getText());
        } catch (NumberFormatException error) {
            responseMessage.setText("Please make sure your employee ID is only numbers");
            return null;
        }
        String registerFieldData[] = new String[]{employeeid.getText(), role.getText(), firstName.getText(), surname.getText(), username.getText(), secretQ.getText(), secretQAnswer.getText()};
        for (int i = 0; i < registerFieldData.length; i++) {
            if (registerFieldData[i].length() == 0) {
                responseMessage.setText("Please make sure all fields are filled in");
                return null;
            }
            // This is needed because of the CSV report generator, commas will obviously mess up any CSV readers.
            if (registerFieldData[i].contains(",")) {
                responseMessage.setText("Please check that your entries do not contain any special characters like ','");
                return null;
            }
        }
        if (!password.getText().trim().equals(password.getText()) || !username.getText().trim().equals(username.getText())) {
            responseMessage.setText("Please make sure your password or username have no spaces");
            return null;
        }
        return "noErrors";
    }

    // Once the data has been submitted, this creates a popup in response.
    public void createPopup(MouseEvent event) {
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

}
