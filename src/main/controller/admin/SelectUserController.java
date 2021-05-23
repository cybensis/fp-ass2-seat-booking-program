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
        if (this.singleton.getAccountManagementDetails("accountAction").equals("updateAccount"))
            this.subHeader.setText("Choose account to update");
        else if (this.singleton.getAccountManagementDetails("accountAction").equals("deleteAccount"))
            this.subHeader.setText("Choose account to delete");
    }

    @FXML
    private void search(MouseEvent event) throws SQLException, IOException {
        String response = searchUser();
        if (response.equals("found")) {
            this.singleton.setAccountManagementDetails(String.valueOf(this.employeeIDField.getText().trim()), "employeeID");
            if (this.singleton.getAccountManagementDetails("accountAction").equals("updateAccount"))
                this.singleton.changeScene("main/ui/admin/addUpdateAccount.fxml");
            else if (this.singleton.getAccountManagementDetails("accountAction").equals("deleteAccount"))
                this.singleton.changeScene("main/ui/admin/deleteUser.fxml");
        }
        else
            this.errorText.setText(response);

    }


    private String searchUser() throws SQLException{
        int employeeID;
        try {
            employeeID = Integer.parseInt(this.employeeIDField.getText().trim());
            if (this.singleton.getAccountManagementDetails("accountType").equals("admin"))
                return  this.selectUserModel.searchUserID( this.employeeIDField.getText(), "admin");
            else
                return this.selectUserModel.searchUserID( this.employeeIDField.getText(), "employee");
        }
        catch (NumberFormatException error) {
            return "Please make sure you've input a number";
        }
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        this.singleton.changeScene("main/ui/admin/accountManagement.fxml");
    }
}
