package main.model.admin;

import main.BCrypt;
import main.SQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SelectUserModel {
    Connection connection;

    public SelectUserModel(){

        connection = SQLConnection.connect();
        if (connection == null)
            System.exit(1);

    }
    public String searchUserID(String userID, String accountType) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet=null;
        String query = "SELECT * FROM user INNER JOIN accountTypes ON user.accountType = accountTypes.accountTypeID WHERE user.employeeID = ? AND accountTypes.accountType = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, accountType);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return "found";
            else
                return "notFound";
        }
        catch (Exception e)
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
