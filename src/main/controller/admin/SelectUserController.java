package main.controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import main.Singleton;
import main.model.admin.SelectUserModel;
import sun.nio.cs.SingleByte;

import java.io.IOException;
import java.sql.SQLException;

public class SelectUserController {
    private SelectUserModel selectUserModel = new SelectUserModel();
    private Singleton singleton = Singleton.getInstance();

    @FXML
    private TextField employeeIDField;

    @FXML
    private Text subHeader;

    @FXML
    private Text errorText;

    @FXML
    private void initialize() {
        if (singleton.getAccountManagementDetails("accountAction").equals("updateAccount"))
            subHeader.setText("Choose account to update");
        else if (singleton.getAccountManagementDetails("accountAction").equals("deleteAccount"))
            subHeader.setText("Choose account to delete");
    }

    @FXML
    private void search(MouseEvent event) throws SQLException, IOException {
        String response = searchUser();
        if (response.equals("found")) {
            singleton.setAccountManagementDetails(String.valueOf(employeeIDField.getText().trim()), "employeeID");
            if (singleton.getAccountManagementDetails("accountAction").equals("updateAccount"))
                singleton.changeScene("main/ui/admin/addUpdateAccount.fxml");
            else if (singleton.getAccountManagementDetails("accountAction").equals("deleteAccount"))
                singleton.changeScene("main/ui/admin/deleteUser.fxml");
        }
        else {
            errorText.setText(response);
            errorText.setVisible(true);
        }

    }


    private String searchUser() throws SQLException{
        int employeeID;
        try {
            employeeID = Integer.parseInt(employeeIDField.getText().trim());
            if (singleton.getAccountManagementDetails("accountType").equals("admin"))
                return  selectUserModel.searchUserID( employeeIDField.getText(), "admin");
            else
                return selectUserModel.searchUserID( employeeIDField.getText(), "employee");
        }
        catch (NumberFormatException error) {
            return "Please make sure you've input a number";
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        singleton.changeScene("main/ui/admin/accountManagement.fxml");
    }
}
