package main.model.admin;

import main.SQLConnection;
import main.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeleteUserModel {
    private Singleton singleton = Singleton.getInstance();
    public String[] returnUserDetails(String employeeID) throws SQLException {
        PreparedStatement preparedStatement = null;
        String accountDetails[];
        ResultSet resultSet=null;
        String query = "SELECT firstName, lastName, username FROM user WHERE employeeID = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, employeeID);
            resultSet = preparedStatement.executeQuery();
            // The employee ID was verified in selectUser.fxml, so we don't need to check if the user exists.
            accountDetails = new String[]{resultSet.getString("firstName") + " " + resultSet.getString("lastName"), resultSet.getString("username")};
        }
        catch (SQLException error)
        {
            accountDetails = new String[]{"error"};
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return accountDetails;
    }

    public String deleteUser(String employeeID) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "DELETE FROM user WHERE employeeID = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, employeeID);
            preparedStatement.executeUpdate();
            return "success";
            // The employee ID was verified in selectUser.fxml, so we don't need to check if the user exists.
        }
        catch (SQLException error)
        {
            return "error";
        }finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }
}
