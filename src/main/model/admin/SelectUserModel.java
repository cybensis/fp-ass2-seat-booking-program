package main.model.admin;

import main.BCrypt;
import main.SQLConnection;
import main.Singleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectUserModel {
    private Singleton singleton = Singleton.getInstance();

    // This method takes a userID and an accountType (either admin or basic) to put into the query to check if that
    // user exists.
    public String searchUserID(String userID, String accountType) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM user INNER JOIN accountTypes ON user.accountType = accountTypes.accountTypeID WHERE user.employeeID = ? AND accountTypes.accountType = ?";
        try {
            preparedStatement = singleton.getConnection().prepareStatement(query);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, accountType);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return "found";
            else
                return "No user was found with this ID";
        } catch (Exception e) {
            return "Sorry, an unexpected error occurred";
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }
}
