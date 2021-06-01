package main.model.admin;

import main.SQLConnection;
import main.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeactivateUserModel {
    private Singleton singleton = Singleton.getInstance();

    // This retrieves the details for the selected user
    public String[] returnUserDetails(String employeeID) throws SQLException {
        PreparedStatement preparedStatement = null;
        String accountDetails[];
        ResultSet resultSet = null;
        String query = "SELECT user.firstName, user.lastName, user.username, states.stateName FROM user INNER JOIN states ON user.accountState = states.stateID WHERE employeeID = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, employeeID);
            resultSet = preparedStatement.executeQuery();
            // The employee ID was verified in selectUser.fxml, so we don't need to check if the user exists.
            accountDetails = new String[]{resultSet.getString("firstName") + " " + resultSet.getString("lastName"), resultSet.getString("username"), resultSet.getString("stateName")};
        } catch (SQLException error) {
            accountDetails = new String[]{"error"};
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return accountDetails;
    }


    public String deleteUser(String employeeID) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "DELETE FROM user WHERE employeeID = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, employeeID);
            preparedStatement.executeUpdate();
            query = "DELETE FROM userBookings WHERE employeeID = ?";
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, employeeID);
            preparedStatement.executeUpdate();
            return "success";
            // The employee ID was verified in selectUser.fxml, so we don't need to check if the user exists.
        } catch (SQLException error) {
            return "error";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    // This changes the accounts state from either active to deactivated or vice versa.
    public String changeAccountState(String currentState) throws SQLException {
        int newState = 1;
        if (currentState.equals("active"))
            newState = 3;
        else
            newState = 1;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "UPDATE user SET accountState = ? WHERE employeeID = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, newState);
            preparedStatement.setString(2, singleton.getAccountManagementDetails("employeeID"));
            preparedStatement.executeUpdate();
            return "success";
        } catch (SQLException error) {
            return "error";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }
}
